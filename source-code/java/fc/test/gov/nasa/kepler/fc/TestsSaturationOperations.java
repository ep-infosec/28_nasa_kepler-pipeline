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

package gov.nasa.kepler.fc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nasa.kepler.common.SocEnvVars;
import gov.nasa.kepler.fc.importer.ImporterRollTime;
import gov.nasa.kepler.fc.importer.ImporterSaturation;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestsSaturationOperations {
    private static DatabaseService databaseService;

    private final static int CHANNEL = 84;
    private final static int CCD_MODULE = 24;
    private final static int CCD_OUTPUT = 4;
    private final static int SEASON = 3;
    private final static double MJD = 55672.0;

    @Before
    public void setUp() throws Exception {
        databaseService = DatabaseServiceFactory.getInstance();
        TestUtils.setUpDatabase(databaseService);
        try {
            databaseService.beginTransaction();

            new ImporterSaturation().run(new String[] { "rewriteHistory",
                SocEnvVars.getLocalTestDataDir() + "/fc/saturation/v2",
                "importing saturation from TestsSaturationOperations.setup" });
            new ImporterRollTime().run(new String[] { "rewriteHistory",
                "importing rolltime from TestsSaturationOperations.setup" });

            databaseService.commitTransaction();
        } finally {
            databaseService.rollbackTransactionIfActive();
            databaseService.closeCurrentSession();
        }
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.tearDownDatabase(databaseService);
    }

    @Test
    public void testRetrieveModelSeasonChannel() {
        SaturationOperations ops = new SaturationOperations();
        SaturationModel model = ops.retrieveSaturationModel(SEASON, CHANNEL);

        assertEquals(model.getSeason(), SEASON);
        SaturatedStar[] stars = model.getStars();
        assertTrue(stars.length > 0);
        assertTrue(stars[0].getSaturatedColumns().length > 0);
    }

    @Test
    public void testRetrieveModelMjdChannel() {
        SaturationOperations ops = new SaturationOperations();
        SaturationModel model = ops.retrieveSaturationModel(MJD, CHANNEL);

        assertEquals(model.getSeason(), SEASON);
        SaturatedStar[] stars = model.getStars();
        assertTrue(stars.length > 0);
        assertTrue(stars[0].getSaturatedColumns().length > 0);
    }

    @Test
    public void testRetrieveModelSeasonModOut() {
        SaturationOperations ops = new SaturationOperations();
        SaturationModel model = ops.retrieveSaturationModel(SEASON, CCD_MODULE,
            CCD_OUTPUT);

        assertEquals(model.getSeason(), SEASON);
        SaturatedStar[] stars = model.getStars();
        assertTrue(stars.length > 0);
        assertTrue(stars[0].getSaturatedColumns().length > 0);
    }

    @Test
    public void testRetrieveModelMjdModOut() {
        SaturationOperations ops = new SaturationOperations();
        SaturationModel model = ops.retrieveSaturationModel(MJD, CCD_MODULE,
            CCD_OUTPUT);

        assertEquals(model.getSeason(), SEASON);
        SaturatedStar[] stars = model.getStars();
        assertTrue(stars.length > 0);
        assertTrue(stars[0].getSaturatedColumns().length > 0);
    }
}
