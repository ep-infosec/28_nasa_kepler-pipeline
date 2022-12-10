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

package gov.nasa.kepler.aft.gar;

import gov.nasa.kepler.aft.AbstractTestDataGenerator;
import gov.nasa.kepler.common.SocEnvVars;
import gov.nasa.kepler.gar.huffman.HuffmanPipelineModule;
import gov.nasa.kepler.hibernate.dbservice.TransactionService;
import gov.nasa.kepler.hibernate.dbservice.TransactionServiceFactory;
import gov.nasa.kepler.hibernate.gar.CompressionCrud;
import gov.nasa.kepler.hibernate.gar.ExportTable.State;
import gov.nasa.kepler.hibernate.gar.HuffmanTable;
import gov.nasa.kepler.pi.configuration.PipelineConfigurationOperations;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates an HSQLDB database with one {@code GAR_HUFFMAN_TABLE} and 2^17-1
 * {@code GAR_HUFFMAN_TABLE_ENTRIES} ({@literal 131071}) seeded by running the
 * relevant pipeline modules.
 * <p>
 * See the comment that starts with "uncomment" to shorten the run-time during
 * development.
 * 
 * @author Forrest Girouard
 * @author Bill Wohler
 * @author Sean McCauliff
 */
public class HuffmanTestDataGenerator extends AbstractTestDataGenerator {

    private static final Log log = LogFactory.getLog(HuffmanTestDataGenerator.class);

    public static final String GENERATOR_NAME = HuffmanPipelineModule.MODULE_NAME;

    private static final String HUFFMAN_TRIGGER_NAME = "HUFFMAN";

    private static final long MILLIS_PER_MONTH = 1000L * 60 * 60 * 24 * 30;

    public HuffmanTestDataGenerator() {
        super(GENERATOR_NAME);
        setImportFcModels(false);
    }

    @Override
    protected void createDatabaseContents() throws Exception {

        log.info(getLogName() + ": Importing pipeline configuration");
        new PipelineConfigurationOperations().importPipelineConfiguration(new File(
            SocEnvVars.getLocalDataDir(), AFT_PIPELINE_CONFIGURATION_ROOT
                + GENERATOR_NAME + ".xml"));

        // Uncomment the following line during development only.
        // HuffmanNominalTest.setHistogramLength(
        // huffmanModuleParameters.getHistogramPipelineInstanceId(),
        // huffmanModuleParameters.getBaselineInterval(), 1024);
    }

    @Override
    protected void process() throws Exception {
        runPipeline(HUFFMAN_TRIGGER_NAME);

        TransactionService transactionService = TransactionServiceFactory.getInstance();
        transactionService.beginTransaction();
        log.info(getLogName() + ": Modifying database objects");
        readyHuffmanTableForExport();
        transactionService.commitTransaction();
    }

    protected void readyHuffmanTableForExport() {
        CompressionCrud compressionCrud = new CompressionCrud();

        List<HuffmanTable> huffmanTables = compressionCrud.retrieveAllHuffmanTables();
        HuffmanTable huffmanTable = huffmanTables.get(0);

        // Update ExportTable fields.
        huffmanTable.setExternalId(retrievePlannedPhotometerConfigParameters().getCompressionExternalId());
        huffmanTable.setPlannedStartTime(new Date());
        huffmanTable.setPlannedEndTime(new Date(
            huffmanTable.getPlannedStartTime()
                .getTime() + MILLIS_PER_MONTH));
        huffmanTable.setState(State.UPLINKED);
        huffmanTable.setPipelineTask(null);
    }

    // See also HuffmanTestDataGenerateTest harness.
    public static void main(String[] args) {
        try {
            new HuffmanTestDataGenerator().generate();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }
}
