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
import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.common.DefaultProperties;
import gov.nasa.kepler.hibernate.cm.CharacteristicCrud;
import gov.nasa.kepler.hibernate.cm.CharacteristicType;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.TestUtils;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Miles Cote
 * 
 */
public class TadProductCharTypeCreatorTest {

    private DatabaseService databaseService;

    @Before
    public void setUp() throws Exception {
        DefaultProperties.setPropsForUnitTest();
        databaseService = DatabaseServiceFactory.getInstance();
        TestUtils.setUpDatabase(databaseService);
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.tearDownDatabase(databaseService);
    }

    @Test
    public void testCreate() {
        databaseService = DatabaseServiceFactory.getInstance();
        CharacteristicCrud charCrud = new CharacteristicCrud();

        databaseService.closeCurrentSession();
        databaseService.beginTransaction();
        TadProductCharTypeCreator creator = new TadProductCharTypeCreator();
        creator.run();
        databaseService.commitTransaction();

        databaseService.closeCurrentSession();
        Collection<CharacteristicType> charTypes = charCrud.retrieveAllCharacteristicTypes();

        Collection<CharacteristicType> expectedCharTypes = newArrayList();
        expectedCharTypes.add(new CharacteristicType("SIGNAL_TO_NOISE_RATIO_SEASON_0",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SIGNAL_TO_NOISE_RATIO_SEASON_1",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SIGNAL_TO_NOISE_RATIO_SEASON_2",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SIGNAL_TO_NOISE_RATIO_SEASON_3",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("CROWDING_SEASON_0",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("CROWDING_SEASON_1",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("CROWDING_SEASON_2",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("CROWDING_SEASON_3",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "FLUX_FRACTION_IN_APERTURE_SEASON_0",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "FLUX_FRACTION_IN_APERTURE_SEASON_1",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "FLUX_FRACTION_IN_APERTURE_SEASON_2",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "FLUX_FRACTION_IN_APERTURE_SEASON_3",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "DISTANCE_FROM_EDGE_SEASON_0", TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "DISTANCE_FROM_EDGE_SEASON_1", TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "DISTANCE_FROM_EDGE_SEASON_2", TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType(
            "DISTANCE_FROM_EDGE_SEASON_3", TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SKY_CROWDING_SEASON_0",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SKY_CROWDING_SEASON_1",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SKY_CROWDING_SEASON_2",
            TadProductCharTypeCreator.FORMAT));
        expectedCharTypes.add(new CharacteristicType("SKY_CROWDING_SEASON_3",
            TadProductCharTypeCreator.FORMAT));

        assertEquals(expectedCharTypes, charTypes);
    }

    @Test
    public void testDontCreateExistingTypes() {
        // Create types...
        testCreate();

        // then, immediately try to create them again. This call should do
        // nothing.
        testCreate();
    }

}
