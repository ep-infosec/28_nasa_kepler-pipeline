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

package gov.nasa.kepler.mc.ukirt;

import static gov.nasa.kepler.services.alert.AlertService.Severity.ERROR;
import gov.nasa.kepler.hibernate.cm.TargetSelectionCrud;
import gov.nasa.kepler.hibernate.pi.ModelMetadataRetrieverPipelineInstance;
import gov.nasa.kepler.hibernate.pi.PipelineInstance;
import gov.nasa.kepler.hibernate.pi.PipelineTask;
import gov.nasa.kepler.hibernate.pi.UnitOfWorkTask;
import gov.nasa.kepler.mc.CustomTargetParameters;
import gov.nasa.kepler.mc.TargetListParameters;
import gov.nasa.kepler.mc.cm.CelestialObjectOperations;
import gov.nasa.kepler.mc.uow.TargetListChunkUowTask;
import gov.nasa.kepler.pi.module.ExternalProcessPipelineModule;
import gov.nasa.kepler.services.alert.AlertServiceFactory;
import gov.nasa.spiffy.common.pi.Parameters;
import gov.nasa.spiffy.common.pi.PipelineException;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UkirtImageRetrieverPipelineModule extends
    ExternalProcessPipelineModule {

    private static final Log log = LogFactory.getLog(UkirtImageRetrieverPipelineModule.class);

    private static final String MODULE_NAME = "UkirtImageRetriever";

    private static final int EXTERNAL_PROCESS_TIMEOUT = 120 * 60; // in seconds

    private static final String SHELL_COMMAND = "/bin/sh";

    private TargetSelectionCrud targetSelectionCrud = null;
    private CelestialObjectOperations celestialObjectOperations = null;

    @Override
    public void processTask(PipelineInstance pipelineInstance,
        PipelineTask pipelineTask) throws PipelineException {

        UkirtImageRetrieverOptions options = retrieveOptions(pipelineTask);

        DateFormat dateFormatter = new SimpleDateFormat(
            UkirtImageRetriever.DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (int skyGroupId : options.getSkyGroupIds()) {
            String formattedDate = dateFormatter.format(new Date());

            try {
                List<String> fitsFiles = new UkirtImageRetriever(
                    getTargetSelectionCrud(), getCelestialObjectOperations(
                        pipelineTask, pipelineInstance), options).retrieve(
                    formattedDate, skyGroupId);

                if (fitsFiles.size() > 0) {
                    String[] commandArray = UkirtImageRetriever.buildConversionCommand(
                        options, null, fitsFiles, formattedDate);

                    log.info(String.format("Executing \"%s %s\"",
                        SHELL_COMMAND, commandArray[0]));
                    executeExternalProcess(new File(SHELL_COMMAND),
                        Arrays.asList(commandArray), EXTERNAL_PROCESS_TIMEOUT,
                        options.getWorkingDir());
                }

            } catch (Exception e) {
                log.error(e.toString(), e);
                AlertServiceFactory.getInstance()
                    .generateAlert(MODULE_NAME, pipelineTask.getId(), ERROR,
                        e.toString());
            }
        }
    }

    private UkirtImageRetrieverOptions retrieveOptions(PipelineTask pipelineTask) {

        UkirtImageRetrieverOptions options = new UkirtImageRetrieverOptions();

        TargetListChunkUowTask task = pipelineTask.uowTaskInstance();
        TargetListParameters targetListParameters = pipelineTask.getParameters(TargetListParameters.class);
        UkirtImageRetrieverParameters ukirtImageRetrieverParameters = pipelineTask.getParameters(UkirtImageRetrieverParameters.class);
        CustomTargetParameters customTargetParameters = pipelineTask.getParameters(CustomTargetParameters.class);

        options.setCustomTargetProcessingEnabled(customTargetParameters.isProcessingEnabled());
        options.setDs9CommandLineArgs(ukirtImageRetrieverParameters.getDs9CommandLineArgs());
        options.setDs9Executable(ukirtImageRetrieverParameters.getDs9Executable());
        options.setEndKeplerId(task.getEndKeplerId());
        options.setImageSize(ukirtImageRetrieverParameters.getImageSize());
        options.setOutputDir(ukirtImageRetrieverParameters.getOutputDir());
        options.setSkyGroupIds(Arrays.asList(task.getSkyGroupId()));
        options.setStartKeplerId(task.getStartKeplerId());
        options.setTargetListNames(targetListParameters.getTargetListNames());
        options.setWorkingDir(allocateWorkingDir(pipelineTask));

        return options;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Class<? extends UnitOfWorkTask> unitOfWorkTaskType() {
        return TargetListChunkUowTask.class;
    }

    @Override
    public List<Class<? extends Parameters>> requiredParameters() {
        ArrayList<Class<? extends Parameters>> parameters = new ArrayList<Class<? extends Parameters>>();

        parameters.add(CustomTargetParameters.class);
        parameters.add(TargetListParameters.class);
        parameters.add(UkirtImageRetrieverParameters.class);

        return parameters;
    }

    private TargetSelectionCrud getTargetSelectionCrud() {
        if (targetSelectionCrud == null) {
            targetSelectionCrud = new TargetSelectionCrud();
        }

        return targetSelectionCrud;
    }

    void setTargetSelectionCrud(TargetSelectionCrud targetSelectionCrud) {
        this.targetSelectionCrud = targetSelectionCrud;
    }

    private CelestialObjectOperations getCelestialObjectOperations(
        PipelineTask pipelineTask, PipelineInstance pipelineInstance) {
        if (celestialObjectOperations == null) {
            celestialObjectOperations = new CelestialObjectOperations(
                new ModelMetadataRetrieverPipelineInstance(pipelineInstance),
                !pipelineTask.getParameters(CustomTargetParameters.class)
                    .isProcessingEnabled());
        }

        return celestialObjectOperations;
    }

    void setCelestialObjectOperations(
        CelestialObjectOperations celestialObjectOperations) {
        this.celestialObjectOperations = celestialObjectOperations;
    }
}
