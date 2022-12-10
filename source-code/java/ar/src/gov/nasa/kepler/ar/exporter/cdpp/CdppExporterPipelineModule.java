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

package gov.nasa.kepler.ar.exporter.cdpp;

import gov.nasa.kepler.common.Cadence;
import gov.nasa.kepler.common.pi.TpsType;
import gov.nasa.kepler.common.pi.TpsTypeParameters;
import gov.nasa.kepler.fs.api.FileStoreClient;
import gov.nasa.kepler.fs.client.FileStoreClientFactory;
import gov.nasa.kepler.hibernate.pi.ModelMetadataRetrieverPipelineInstance;
import gov.nasa.kepler.hibernate.pi.PipelineInstance;
import gov.nasa.kepler.hibernate.pi.PipelineModule;
import gov.nasa.kepler.hibernate.pi.PipelineTask;
import gov.nasa.kepler.hibernate.pi.UnitOfWorkTask;
import gov.nasa.kepler.hibernate.tps.AbstractTpsDbResult;
import gov.nasa.kepler.hibernate.tps.TpsCrud;
import gov.nasa.kepler.mc.dr.MjdToCadence;
import gov.nasa.spiffy.common.io.FileUtil;
import gov.nasa.spiffy.common.pi.ModuleFatalProcessingException;
import gov.nasa.spiffy.common.pi.Parameters;
import gov.nasa.spiffy.common.pi.PipelineException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import nom.tam.fits.FitsException;

/**
 * Exports CDPP data to files. See SAS to SOC ICD.
 * 
 * @author Sean McCauliff
 * 
 */
public class CdppExporterPipelineModule extends PipelineModule {

    public static final String MODULE_NAME = "cdppx";

    private MjdToCadence mjdToCadence;
    private TpsCrud tpsCrud;
    private FileStoreClient fsClient;

    private long tpsPipelineInstanceId;

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Class<? extends UnitOfWorkTask> unitOfWorkTaskType() {
        return TpsResultUowTask.class;
    }

    @Override
    public List<Class<? extends Parameters>> requiredParameters() {
        List<Class<? extends Parameters>> rv = new ArrayList<Class<? extends Parameters>>();
        rv.add(TpsResultUowParameters.class);
        rv.add(CdppExporterModuleParameters.class);
        rv.add(TpsTypeParameters.class);
        return rv;
    }

    @Override
    public void processTask(PipelineInstance pipelineInstance,
        PipelineTask pipelineTask) throws PipelineException {

        TpsResultUowTask uow = pipelineTask.uowTaskInstance();

        CdppExporterModuleParameters params = pipelineTask.getParameters(CdppExporterModuleParameters.class);
        TpsType tpsType = pipelineTask.getParameters(TpsTypeParameters.class).toTpsTypeEnumValue();

        tpsPipelineInstanceId = uow.getPipelineInstanceId();

        List<? extends AbstractTpsDbResult> tpsResults;
        switch (tpsType) {
            case TPS_FULL:
                tpsResults = getTpsCrud().retrieveTpsResultByPipelineInstanceId(uow.getStartKeplerId(), uow.getEndKeplerId(), tpsPipelineInstanceId);
            break;
            case TPS_LITE:
                tpsResults = getTpsCrud().retrieveTpsLiteResultByPipelineInstanceId(uow.getStartKeplerId(), uow.getEndKeplerId(), tpsPipelineInstanceId);
            break;
            default:
                throw new IllegalStateException("Bad tps type \"" + tpsType + "\".");
        }

        CdppExporter cdppExporter = new CdppExporter(getFsClient(),
            getMjdToCadence());
        File outputDir = new File(params.getExportDirectory());
        try {
            FileUtil.mkdirs(outputDir);
            if (!outputDir.canWrite()) {
                throw new ModuleFatalProcessingException("Directory \""
                    + outputDir + "\" is not writable.");
            }

            cdppExporter.export(tpsResults, outputDir, tpsType);
        } catch (IOException e) {
            throw new ModuleFatalProcessingException("Writing to directory \""
                + outputDir + "\".", e);
        } catch (FitsException e) {
            throw new ModuleFatalProcessingException("Writing to directory \""
                + outputDir + "\".", e);
        }
    }

    private MjdToCadence getMjdToCadence() {
        if (mjdToCadence == null) {
            this.mjdToCadence = new MjdToCadence(
                Cadence.CadenceType.LONG,
                new ModelMetadataRetrieverPipelineInstance(tpsPipelineInstanceId));
        }
        return mjdToCadence;
    }

    void setMjdToCadence(MjdToCadence mjdToCadence) {
        this.mjdToCadence = mjdToCadence;
    }

    private TpsCrud getTpsCrud() {
        if (tpsCrud == null) {
            tpsCrud = new TpsCrud();
        }
        return tpsCrud;
    }

    void setTpsCrud(TpsCrud tpsCrud) {
        this.tpsCrud = tpsCrud;
    }

    private FileStoreClient getFsClient() {
        if (fsClient == null) {
            return FileStoreClientFactory.getInstance();
        }
        return fsClient;
    }

    void setFsClient(FileStoreClient fsClient) {
        this.fsClient = fsClient;
    }

}
