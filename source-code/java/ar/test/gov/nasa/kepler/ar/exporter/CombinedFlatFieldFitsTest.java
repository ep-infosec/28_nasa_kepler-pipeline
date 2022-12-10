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

package gov.nasa.kepler.ar.exporter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nasa.kepler.ar.FitsVerify;
import gov.nasa.kepler.ar.FitsVerify.FitsVerifyResults;
import gov.nasa.kepler.common.FcConstants;
import gov.nasa.spiffy.common.io.FileUtil;
import gov.nasa.spiffy.common.io.Filenames;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CombinedFlatFieldFitsTest {

    private File testRoot =
        new File(Filenames.BUILD_TEST, "CombinedFlatFieldFits.test");
    private final FitsVerify fitsVerify = new FitsVerify();
    
    @Before
    public void setUp() throws Exception {
        FileUtils.forceMkdir(testRoot);
    }

    @After
    public void tearDown() throws Exception {
        FileUtil.removeAll(testRoot);
    }
    
    @Test
    public void readWriteCombinedFlatFieldFits() throws Exception {
        CombinedFlatFieldFits cFlatFieldFits = new CombinedFlatFieldFits("testfname_blah.fits", 1.0);
        for (int module : FcConstants.modulesList) {
            for (int output : FcConstants.outputsList) {
                int s = module * output;
                float[][] imageData = 
                    new float[][] { {1.0f * s , 2.0f * s}, { 3.0f * s, 4.0f * s}};
                cFlatFieldFits.addModuleOutput(imageData, module, output);
            }
        }
        
        File outFits = new File(testRoot, "testfname.fits");
        cFlatFieldFits.write(outFits);
        
        
        CombinedFlatFieldFits readFits = new CombinedFlatFieldFits(outFits);
        
        //ReflectionEquals refEquals = new ReflectionEquals();
       // refEquals.assertEquals(cFlatFieldFits, readFits);
        
        for (int module : FcConstants.modulesList) {
            for (int output : FcConstants.outputsList) {
                int s = module * output;
                float[][] imageData = 
                    new float[][] { {1.0f * s , 2.0f * s}, { 3.0f * s, 4.0f * s}};
                
                float[][]  readImage = readFits.imageFor(module, output);
                assertTrue(Arrays.deepEquals(readImage, imageData));
            }
        }
        
        
        assertEquals(1.0, readFits.validTime(), 0);
        
        FitsVerifyResults verification = fitsVerify.verify(outFits);
        assertEquals(verification.output, 0, verification.returnCode);
    }

}