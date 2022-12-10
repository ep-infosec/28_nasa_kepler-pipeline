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

package gov.nasa.kepler.tad.peer.chartable;

import static com.google.common.collect.Lists.newArrayList;
import gov.nasa.kepler.hibernate.cm.Characteristic;
import gov.nasa.kepler.hibernate.pi.PipelineInstance;
import gov.nasa.kepler.hibernate.pi.PipelineModule;
import gov.nasa.kepler.hibernate.pi.PipelineTask;
import gov.nasa.kepler.hibernate.pi.UnitOfWorkTask;
import gov.nasa.kepler.hibernate.tad.ModOut;
import gov.nasa.kepler.mc.tad.TadParameters;
import gov.nasa.kepler.mc.uow.ModOutUowTask;
import gov.nasa.spiffy.common.pi.Parameters;

import java.util.List;

/**
 * This class retrieves tad products and stores them in the
 * {@link Characteristic}s table.
 * 
 * @author Miles Cote
 */
public class TadProductsToCharTablePipelineModule extends PipelineModule {

    public static final String MODULE_NAME = "tadProductsToCharTable";

    private final TadProductCharManager tadProductCharManager;

    static enum TadProductType {
        SIGNAL_TO_NOISE_RATIO,
        CROWDING,
        FLUX_FRACTION_IN_APERTURE,
        DISTANCE_FROM_EDGE,
        SKY_CROWDING;

        String getCharTypeName(int season) {
            if (season < 0 || season > 3) {
                throw new IllegalArgumentException("Illegal season: " + season);
            }

            return toString() + "_SEASON_" + season;
        }
    }

    public TadProductsToCharTablePipelineModule() {
        this(new TadProductCharManager());
    }

    TadProductsToCharTablePipelineModule(
        TadProductCharManager tadProductCharManager) {
        this.tadProductCharManager = tadProductCharManager;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Class<? extends UnitOfWorkTask> unitOfWorkTaskType() {
        return ModOutUowTask.class;
    }

    @Override
    public List<Class<? extends Parameters>> requiredParameters() {
        List<Class<? extends Parameters>> requiredParams = newArrayList();
        requiredParams.add(TadParameters.class);
        return requiredParams;
    }

    @Override
    public void processTask(PipelineInstance pipelineInstance,
        PipelineTask pipelineTask) {
        TadParameters params = pipelineTask.getParameters(TadParameters.class);
        String targetListSetName = params.getTargetListSetName();

        ModOutUowTask task = pipelineTask.uowTaskInstance();
        int ccdModule = task.getCcdModule();
        int ccdOutput = task.getCcdOutput();

        tadProductCharManager.manage(targetListSetName,
            ModOut.of(ccdModule, ccdOutput));
    }

}
