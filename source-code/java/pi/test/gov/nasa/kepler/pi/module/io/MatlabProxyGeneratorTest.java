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

package gov.nasa.kepler.pi.module.io;

import gov.nasa.kepler.pi.module.io.matlab.MatlabProxyGenerator;
import gov.nasa.spiffy.common.persistable.BinaryPersistableOutputStream;
import gov.nasa.spiffy.common.persistable.TestPersistable;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Todd Klaus tklaus@arc.nasa.gov
 * 
 */
public class MatlabProxyGeneratorTest {

    private static final String OUTPUT_DIR = "testdata/MatlabProxyGenerator/";

    @Before
    public void setup() throws Exception {
        File outputDir = new File(OUTPUT_DIR);
        outputDir.mkdirs();
        FileUtils.cleanDirectory(outputDir);
    }

    @Test
    public void testMatlabProxyGenerator() throws Exception {
        generateBinFile();
    }

    @Test
    public void testGenBinFile() throws Exception {
        generateBinFile();

        String testFileName = OUTPUT_DIR + "testClass2.bin";

        // save

        FileOutputStream fos = new FileOutputStream(testFileName);
        DataOutputStream dos = new DataOutputStream(fos);
        BinaryPersistableOutputStream bpos = new BinaryPersistableOutputStream(
            dos);

        TestPersistable testPersistable = new TestPersistable(1);
        bpos.save(testPersistable);
        dos.flush();
        fos.close();
    }

    private void generateBinFile() throws Exception {
        MatlabProxyGenerator matlabProxyGenerator = new MatlabProxyGenerator(
            "foo", OUTPUT_DIR, OUTPUT_DIR,
            TestPersistable.class.getName());

        matlabProxyGenerator.generate();
    }
}
