/*
 * Copyright 2017 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * This file is available under the terms of the NASA Open Source Agreement
 * (NOSA). You should have received a copy of this agreement with the
 * Kepler source code; see the file NASA-OPEN-SOURCE-AGREEMENT.doc.
 * 
 * No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY
 * WARRANTY OF ANY KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY,
 * INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE
 * WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM
 * INFRINGEMENT, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR
 * FREE, OR ANY WARRANTY THAT DOCUMENTATION, IF PROVIDED, WILL CONFORM
 * TO THE SUBJECT SOFTWARE. THIS AGREEMENT DOES NOT, IN ANY MANNER,
 * CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY PRIOR RECIPIENT
 * OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR ANY
 * OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.
 * FURTHER, GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES
 * REGARDING THIRD-PARTY SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE,
 * AND DISTRIBUTES IT "AS IS."
 * 
 * Waiver and Indemnity: RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS
 * AGAINST THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND
 * SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT. IF RECIPIENT'S USE OF
 * THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES, DEMANDS, DAMAGES,
 * EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES FROM
 * PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT
 * SOFTWARE, RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED
 * STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY
 * PRIOR RECIPIENT, TO THE EXTENT PERMITTED BY LAW. RECIPIENT'S SOLE
 * REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE, UNILATERAL
 * TERMINATION OF THIS AGREEMENT.
 */

package gov.nasa.kepler.fc.undershoot;

import gov.nasa.kepler.common.FcConstants;
import gov.nasa.kepler.common.ModifiedJulianDate;
import gov.nasa.kepler.fc.FcModelFactory;
import gov.nasa.kepler.fc.FocalPlaneException;
import gov.nasa.kepler.fc.UndershootModel;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.fc.FcCrud;
import gov.nasa.kepler.hibernate.fc.History;
import gov.nasa.kepler.hibernate.fc.HistoryModelName;
import gov.nasa.kepler.hibernate.fc.Undershoot;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UndershootOperations {
    private static HistoryModelName HISTORY_NAME = HistoryModelName.UNDERSHOOT;
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(UndershootOperations.class);

    @SuppressWarnings("unused")
	private DatabaseService dbService;
    private FcCrud fcCrud;
    private History history;

    public UndershootOperations() {
        this(DatabaseServiceFactory.getInstance());
    }

    public UndershootOperations(DatabaseService dbService) {
        this.dbService = dbService;
        fcCrud = new FcCrud(dbService);
        history = null;
    }
    
    public void create(Undershoot undershoot) {
        fcCrud.create(undershoot);
    }

    public Undershoot retrieveUndershoot(int ccdModule, int ccdOutput,
        double startMjd) {
        Undershoot undershoot = fcCrud.retrieveUndershoot(ccdModule, ccdOutput,
            startMjd, getHistory());
        if (undershoot == null) {
            throw new FocalPlaneException("no undershoot object in db");
        }
        return undershoot;
    }

    public List<Undershoot> retrieveUndershoots(int ccdModule, int ccdOutput,
        double startTime, double endTime) {
        List<Undershoot> undershoots = fcCrud.retrieveUndershoots(ccdModule,
            ccdOutput, startTime, endTime, getHistory());
        if (undershoots.size() == 0) {
            throw new FocalPlaneException("no undershoot object in db");
        }
        return undershoots;
    }

    public Undershoot retrieveUndershootExact(Undershoot undershoot) {
        Undershoot out = fcCrud.retrieveUndershootExact(undershoot, getHistory());
        return out;
    }

    /**
     * 
     * Return the unique Undershoot models that are valid for the range of
     * specified MJD times. The UndershootModel will contain only the distinct
     * models that are valid for that time range, without duplicates.
     * 
     * @param mjdStart
     * @param mjdEnd
     * @return
     */
    public UndershootModel retrieveUndershootModel(double mjdStart,
        double mjdEnd) {
        int numUndershoot = fcCrud.getUndershootCount(2, 1, mjdStart, mjdEnd,
            getHistory());

        double[] mjds = new double[numUndershoot];
        double[][][] constants = new double[numUndershoot][FcConstants.nModules * FcConstants.nOutputsPerModule][];
        double[][][] uncertainty = new double[numUndershoot][FcConstants.nModules * FcConstants.nOutputsPerModule][];

        for (int module : FcConstants.modulesList) {
            for (int output : FcConstants.outputsList) {
                List<Undershoot> undershoots = fcCrud.retrieveUndershoots(
                    module, output, mjdStart, mjdEnd, getHistory());

                for (int iTime = 0; iTime < undershoots.size(); ++iTime) {
                    mjds[iTime] = undershoots.get(iTime).getStartMjd();

                    int channel = FcConstants.getChannelNumber(module, output);
                    constants[iTime][channel - 1] = undershoots.get(iTime).getCoefficients();
                    uncertainty[iTime][channel - 1] = undershoots.get(iTime).getUncertainties();
                }

            }
        }

        return FcModelFactory.undershootModel(mjds, constants, uncertainty);
    }

    /**
     * 
     * Return every persisted Undershoot model.
     * 
     * @param mjdStart
     * @param mjdEnd
     * @return
     */
    public UndershootModel retrieveUndershootModelAll() {
        int numUndershoot = fcCrud.getUndershootCount(2, 1, getHistory());

        double[] mjds = new double[numUndershoot];
        double[][][] constants = new double[numUndershoot][FcConstants.nModules* FcConstants.nOutputsPerModule][];
        double[][][] uncertainty = new double[numUndershoot][FcConstants.nModules* FcConstants.nOutputsPerModule][];
        
        for (int module : FcConstants.modulesList) {
            for (int output : FcConstants.outputsList) {
                List<Undershoot> undershoots = fcCrud.retrieveUndershoots(
                    module, output, getHistory());

                for (int iTime = 0; iTime < undershoots.size(); ++iTime) {
                    mjds[iTime] = undershoots.get(iTime).getStartMjd();

                    int channel = FcConstants.getChannelNumber(module, output);
                    constants[iTime][channel - 1] = undershoots.get(iTime).getCoefficients();
                    uncertainty[0][channel - 1] = undershoots.get(0).getUncertainties();
                }

            }
        }

        return FcModelFactory.undershootModel(mjds, constants, uncertainty);
    }

    public UndershootModel retrieveMostRecentUndershootModel() {
        int numUndershoot = fcCrud.getUndershootCount(2, 1, getHistory());

        double[] mjds = new double[numUndershoot];
        double[][][] constants = new double[numUndershoot][FcConstants.nModules
            * FcConstants.nOutputsPerModule][];
        double[][][] uncertainty = new double[numUndershoot][FcConstants.nModules
            * FcConstants.nOutputsPerModule][];

        for (int module : FcConstants.modulesList) {
            for (int output : FcConstants.outputsList) {
                List<Undershoot> undershoots = fcCrud.retrieveUndershoots(
                    module, output, getHistory());
                // fcCrud.retrieveUndershoots is ordered by MJD desc, so the
                // first entry is the latest:
                //
                Double mostRecentMjd = undershoots.get(0).getStartMjd();
                mjds[0] = mostRecentMjd;

                int channel = FcConstants.getChannelNumber(module, output);
                constants[0][channel - 1] = undershoots.get(0).getCoefficients();
                uncertainty[0][channel - 1] = undershoots.get(0).getUncertainties();
            }
        }

        return FcModelFactory.undershootModel(mjds, constants, uncertainty);
    }

    public History getHistory() {
        if (history == null) {
            history = fcCrud.retrieveHistory(HISTORY_NAME);
        }
        if (history == null) {
            Date now = new Date();
            double mjdNow = ModifiedJulianDate.dateToMjd(now);
            history = new History(
                mjdNow,
                HISTORY_NAME,
                "created by UndershootOperations because history table was empty",
                1);
            fcCrud.create(history);
        }
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
