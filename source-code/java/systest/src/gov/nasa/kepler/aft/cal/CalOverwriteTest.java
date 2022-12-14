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

package gov.nasa.kepler.aft.cal;

import gov.nasa.kepler.cal.CalPipelineModule;
import gov.nasa.kepler.cal.io.CalModuleParameters;
import gov.nasa.kepler.fs.FileStoreConstants;
import gov.nasa.kepler.hibernate.dbservice.ConfigurationServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.TransactionService;
import gov.nasa.kepler.hibernate.dbservice.TransactionServiceFactory;
import gov.nasa.kepler.hibernate.pi.ParameterSet;
import gov.nasa.kepler.mc.PouModuleParameters;
import gov.nasa.kepler.pi.pipeline.PipelineOperations;
import gov.nasa.spiffy.common.io.FileUtil;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Sean McCauliff
 * @author Forrest Girouard
 */
public class CalOverwriteTest extends AbstractCalFeatureTest {

    private static final Log log = LogFactory.getLog(CalOverwriteTest.class);

    private static final String TEST_NAME = "Overwrite";

    private static final String POU_PARAMETER_SET_NAME = "pou (cal quarterly)";

    public CalOverwriteTest() {
        super(TEST_NAME);
    }

    @Override
    protected void process() throws Exception {

        log.info(getLogName() + ": Running initial pipeline");
        runPipeline(CAL_LC_TRIGGER_NAME);
        log.info(getLogName() + ": Initial cal run complete");

        Configuration config = ConfigurationServiceFactory.getInstance();
        File fsDataDir = new File(config.getString(
            FileStoreConstants.FS_DATA_DIR_PROPERTY,
            FileStoreConstants.FS_DATA_DIR_DEFAULT));
        File intermediateFs = new File(fsDataDir.getParent(), "intermediate");
        log.info(getLogName() + ": Saving initial state to " + intermediateFs);
        FileUtil.copyFiles(fsDataDir, intermediateFs);

        log.info(getLogName() + ": Overwriting with new cal run");
        TransactionService transactionService = TransactionServiceFactory.getInstance();
        transactionService.beginTransaction(true, false, true);

        PipelineOperations pipelineOps = new PipelineOperations();

        ParameterSet parameterSet = pipelineOps.retrieveLatestParameterSet(CalPipelineModule.MODULE_NAME);
        CalModuleParameters moduleParameters = new CalModuleParameters();
        moduleParameters.setCrCorrectionEnabled(false);
        moduleParameters.setFlatFieldCorrectionEnabled(false);
        moduleParameters.setLinearityCorrectionEnabled(false);
        moduleParameters.setUndershootEnabled(false);
        pipelineOps.updateParameterSet(parameterSet, moduleParameters, false);

        parameterSet = pipelineOps.retrieveLatestParameterSet(POU_PARAMETER_SET_NAME);
        PouModuleParameters pouParameters = new PouModuleParameters();
        pouParameters.setPouEnabled(false);
        pipelineOps.updateParameterSet(parameterSet, pouParameters, false);
        transactionService.commitTransaction();

        log.info(getLogName() + ": Running overwrite pipeline");
        runPipeline(CAL_LC_TRIGGER_NAME);
    }
}
