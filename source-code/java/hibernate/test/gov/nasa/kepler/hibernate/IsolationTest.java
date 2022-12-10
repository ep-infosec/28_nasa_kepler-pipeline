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

package gov.nasa.kepler.hibernate;

import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.hibernate.cm.CharacteristicCrud;
import gov.nasa.kepler.hibernate.cm.CharacteristicType;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

/**
 * @author smccauliff
 * 
 */
public class IsolationTest {

    @Before
    public void setup() {
        DatabaseServiceFactory.getInstance()
            .getDdlInitializer()
            .initDB();
    }

    @Test
    public void isolationTest() throws Exception {
        final int nThreads = 2;

        final CountDownLatch otherdone = new CountDownLatch(nThreads);
        final AtomicInteger typeSequence = new AtomicInteger(1);
        final AtomicInteger nFound = new AtomicInteger(0);
        final AtomicReference<Exception> error = new AtomicReference<Exception>();
        final CountDownLatch done = new CountDownLatch(nThreads);

        Runnable inserter = new Runnable() {

            @Override
            public void run() {
                try {
                    DatabaseService dbService = DatabaseServiceFactory.getInstance();
                    dbService.beginTransaction();
                    CharacteristicCrud charCrud = new CharacteristicCrud();
                    CharacteristicType charType = new CharacteristicType(
                        "type-" + typeSequence.getAndIncrement(), "%f");
                    charCrud.create(charType);
                    otherdone.countDown();
                    otherdone.await();
                    nFound.addAndGet(charCrud.retrieveAllCharacteristicTypes()
                        .size());
                } catch (Exception e) {
                    error.compareAndSet(null, e);
                } finally {
                    done.countDown();
                }

            }

        };

        for (int i = 0; i < nThreads; i++) {
            new Thread(inserter).start();
        }

        done.await();

        if (error.get() != null) {
            error.get()
                .printStackTrace();
        }
        assertEquals(null, error.get());
        assertEquals(2, nFound.get());

    }

}
