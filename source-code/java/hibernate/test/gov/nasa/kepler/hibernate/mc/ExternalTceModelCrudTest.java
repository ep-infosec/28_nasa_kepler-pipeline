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

package gov.nasa.kepler.hibernate.mc;

import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.TestUtils;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class ExternalTceModelCrudTest {

    @Before
    public void setUp() throws Exception {
        TestUtils.setUpDatabase(DatabaseServiceFactory.getInstance());
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.tearDownDatabase(DatabaseServiceFactory.getInstance());
    }

    @Test
    public void testCreateRetrieveDelete() {
        int revision = 1;
        int keplerId = 2;
        int planetNumber = 1;
        float transitDurationHours = 2.0F;
        double epochMjd = 3.0;
        float orbitalPeriodDays = 4.0F;
        float maxSingleEventSigma = 5.0F;
        float maxMultipleEventSigma = 6.0F;

        List<ExternalTce> externalTces = ImmutableList.of(new ExternalTce(
            keplerId, planetNumber, transitDurationHours, epochMjd,
            orbitalPeriodDays, maxSingleEventSigma, maxMultipleEventSigma));

        ExternalTceModel externalTceModel = new ExternalTceModel(revision,
            externalTces);

        ExternalTceModelCrud externalTceModelCrud = new ExternalTceModelCrud();

        DatabaseServiceFactory.getInstance()
            .beginTransaction();
        externalTceModelCrud.create(externalTceModel);
        DatabaseServiceFactory.getInstance()
            .commitTransaction();
        DatabaseServiceFactory.getInstance()
            .closeCurrentSession();

        DatabaseServiceFactory.getInstance()
            .beginTransaction();
        externalTceModelCrud.delete(externalTceModelCrud.retrieve(revision));
        DatabaseServiceFactory.getInstance()
            .commitTransaction();
        DatabaseServiceFactory.getInstance()
            .closeCurrentSession();

        DatabaseServiceFactory.getInstance()
            .beginTransaction();
        externalTceModelCrud.create(externalTceModel);
        DatabaseServiceFactory.getInstance()
            .commitTransaction();
        DatabaseServiceFactory.getInstance()
            .closeCurrentSession();

        ExternalTceModel actualExternalTceModel = externalTceModelCrud.retrieve(revision);

        assertEquals(externalTceModel, actualExternalTceModel);
    }
}
