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

package gov.nasa.kepler.ar.exporter.dv;

import static gov.nasa.kepler.ar.exporter.MockUtils.baseBinaryTableExpectations;
import static gov.nasa.kepler.ar.exporter.MockUtils.baseTargetBinaryTableExpectations;
import static gov.nasa.kepler.common.FitsConstants.CHECKSUM_DEFAULT;
import static gov.nasa.spiffy.common.lang.StringUtils.breakAt80Characters;
import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.ar.exporter.binarytable.BaseTargetBinaryTableHeaderSource;
import gov.nasa.spiffy.common.io.FileUtil;
import gov.nasa.spiffy.common.io.Filenames;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import nom.tam.fits.Header;
import nom.tam.util.BufferedDataOutputStream;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit test for class DvTargetTceHeaderFormatter.
 * 
 * @author lbrownst
 */
// Specify the org.junit.runner.Runner
@RunWith(JMock.class)
public class DvTceHeaderFormatterTest {

    /** The file to which the TCE Header is exported. */
    private final File outputDir = new File(Filenames.BUILD_TEST,
        DvTceHeaderFormatterTest.class.getSimpleName());

    /** The test context, created afresh before each Test is run. */
    private Mockery mockery;

    /** Execute before each Test */
    @Before
    public void setUp() throws Exception {
        // Re-create the directory
        FileUtil.cleanDir(outputDir);
        FileUtil.mkdirs(outputDir);
        // Re-create and initialize the test context
        mockery = new Mockery();
        mockery.setImposteriser(ClassImposteriser.INSTANCE); // Bypass
                                                             // constructor
    }

    /**
     * Test method that mocks a DvTceMetadata, calls
     * DvTargetTceHeaderFormatter.formatHeader() with that mocked object,
     * and then calls FitsDiff.diff() to compare the resulting FITS file with a
     * reference file.
     * When the standard file changes, copy the exported file to the standard:
     * cp /path/to/java/ar/build/test/DvTargetTceHeaderFormatterTest/dv-target-tce-header.fits
     * /path/to/java/ar/testdata
     * @throws Exception if the output file or reference file can't be opened,
     * or if the code being tested throws an Exception
     */
    @Test
    public void dvTargetTceHeaderFormatterTest() throws Exception {
        // The directory in which a standard exported TCE Header is saved
        final String testFileDirectory = "testdata";
        // The name of the exported file
        final String fitsFileName = "dv-tce-header.fits";
        // The value of the "generatedAt" property
        final Date generatedAt = new Date(88888999);

        // Mock the argument to the formatter
        final DvTceMetadata mockedDvTceMetadata = mockery.mock(DvTceMetadata.class);
        mockery.checking(new Expectations() {
            {
                // Proper
                allowing(mockedDvTceMetadata).index();
                will(returnValue(1));
                atLeast(1).of(mockedDvTceMetadata).period();
                will(returnValue(54.40));
                atLeast(1).of(mockedDvTceMetadata).epoch();
                will(returnValue(40.54));
                atLeast(1).of(mockedDvTceMetadata).transitDepth();
                will(returnValue(2.71828182846));
                atLeast(1).of(mockedDvTceMetadata).transitSignalToNoiseRatio();
                will(returnValue(3.14159265359f));
                atLeast(1).of(mockedDvTceMetadata).transitDurationHours();
                will(returnValue(1.41421));
                atLeast(1).of(mockedDvTceMetadata).ingressDurationHours();
                will(returnValue(1.77245385));
                atLeast(1).of(mockedDvTceMetadata).impact();
                will(returnValue(911.2001));
                atLeast(1).of(mockedDvTceMetadata).inclinationDegrees();
                will(returnValue(94.301));
                atLeast(1).of(mockedDvTceMetadata).planetDistanceStarRadiusRatio();
                will(returnValue(9.4301));
                atLeast(1).of(mockedDvTceMetadata).planetRadiusWrtEarth();
                will(returnValue(4.8219));
                atLeast(1).of(mockedDvTceMetadata).maxMes();
                will(returnValue(1.5213f));
                atLeast(1).of(mockedDvTceMetadata).maxSes();
                will(returnValue(650.3240778f));
                atLeast(1).of(mockedDvTceMetadata).transitCount();
                will(returnValue(66));
                atLeast(1).of(mockedDvTceMetadata).convergence();
                will(returnValue(true));
                atLeast(1).of(mockedDvTceMetadata).medianDetrendWindowHours();
                will(returnValue(9.28f));
                atLeast(1).of(mockedDvTceMetadata).planetRadiusStarRadiusRatio();
                will(returnValue(0.001));
                
            }
        });
        
        final BaseTargetBinaryTableHeaderSource source = mockery.mock(BaseTargetBinaryTableHeaderSource.class);
        baseBinaryTableExpectations(mockery, generatedAt, source);
        baseTargetBinaryTableExpectations(mockery, source);
        mockery.checking(new Expectations() {{
            atLeast(1).of(source).isSingleQuarter();
            will(returnValue(true));
        }});
        
        // Export the TCE Header
        File outputFile = new File(outputDir, fitsFileName);
        DvTceHeaderFormatter formatter = new DvTceHeaderFormatter();
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedDataOutputStream bdos = new BufferedDataOutputStream(fos);
        Header tceHeader =
            formatter.formatHeader(source, CHECKSUM_DEFAULT,  mockedDvTceMetadata);
        tceHeader.write(bdos);
        bdos.close();

        // The standard of comparison
        File expectedFile = new File(testFileDirectory, fitsFileName);
        String actualStr = breakAt80Characters(FileUtils.readFileToString(outputFile));
        
        String expectedStr = breakAt80Characters(FileUtils.readFileToString(expectedFile));
        
        assertEquals(actualStr, expectedStr);
    }

}
