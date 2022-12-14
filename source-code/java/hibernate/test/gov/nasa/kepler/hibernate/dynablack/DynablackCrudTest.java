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

package gov.nasa.kepler.hibernate.dynablack;

import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.TestUtils;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * @author Miles Cote
 * 
 */
public class DynablackCrudTest {

    private static final int CCD_MODULE = 2;
    private static final int CCD_OUTPUT = 1;
    private static final int START_CADENCE = 3;
    private static final int END_CADENCE = 4;
    private static final long PIPELINE_TASK_ID = 5;
    private static final String FILE_EXTENSION = "FILE_EXTENSION";

    private DynamicTwoDBlackBlobMetadata dynamicTwoDBlackBlobMetadata = new DynamicTwoDBlackBlobMetadata(
        PIPELINE_TASK_ID, CCD_MODULE, CCD_OUTPUT, START_CADENCE, END_CADENCE,
        FILE_EXTENSION);
    private List<DynamicTwoDBlackBlobMetadata> dynamicTwoDBlackBlobMetadataList = ImmutableList.of(dynamicTwoDBlackBlobMetadata);

    private DatabaseService databaseService = DatabaseServiceFactory.getInstance();

    private DynablackCrud dynablackCrud = new DynablackCrud();

    @Before
    public void setUp() throws Exception {
        TestUtils.setUpDatabase(databaseService);
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.tearDownDatabase(databaseService);
    }

    @Test
    public void testCreateRetrieveDynamicTwoDBlackBlobMetadata() {
        databaseService.beginTransaction();
        dynablackCrud.createDynamicTwoDBlackBlobMetadata(dynamicTwoDBlackBlobMetadata);
        databaseService.commitTransaction();
        databaseService.closeCurrentSession();

        List<DynamicTwoDBlackBlobMetadata> actualDynamicTwoDBlackBlobMetadataList = dynablackCrud.retrieveDynamicTwoDBlackBlobMetadata(
            CCD_MODULE, CCD_OUTPUT, START_CADENCE, END_CADENCE);

        assertEquals(dynamicTwoDBlackBlobMetadataList,
            actualDynamicTwoDBlackBlobMetadataList);
    }

}
