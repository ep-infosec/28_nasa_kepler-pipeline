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

package gov.nasa.kepler.hibernate.dv;

import static gov.nasa.kepler.hibernate.dv.DvAbstractTargetTableDataTest.CCD_MODULE;
import static gov.nasa.kepler.hibernate.dv.DvAbstractTargetTableDataTest.CCD_OUTPUT;
import static gov.nasa.kepler.hibernate.dv.DvAbstractTargetTableDataTest.END_CADENCE;
import static gov.nasa.kepler.hibernate.dv.DvAbstractTargetTableDataTest.QUARTER;
import static gov.nasa.kepler.hibernate.dv.DvAbstractTargetTableDataTest.START_CADENCE;
import static gov.nasa.kepler.hibernate.dv.DvAbstractTargetTableDataTest.TARGET_TABLE_ID;
import static gov.nasa.kepler.hibernate.dv.DvPixelStatisticTest.CCD_COLUMN;
import static gov.nasa.kepler.hibernate.dv.DvPixelStatisticTest.CCD_ROW;
import static gov.nasa.kepler.hibernate.dv.DvStatisticTest.SIGNIFICANCE;
import static gov.nasa.kepler.hibernate.dv.DvStatisticTest.VALUE;
import static gov.nasa.kepler.hibernate.dv.DvTestUtils.createCentroidOffsets;
import static gov.nasa.kepler.hibernate.dv.DvTestUtils.createImageCentroid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@link DvPixelCorrelationResults} class.
 * 
 * @author Forrest Girouard
 */
public class DvPixelCorrelationResultsTest {

    private static final Log log = LogFactory.getLog(DvPixelCorrelationResultsTest.class);

    private static final long ID = 42;
    private static final List<DvPixelStatistic> PIXEL_CORRELATION_STATISTICS = createPixelCorrelationStatistics(
        1, VALUE);

    private DvPixelCorrelationResults pixelCorrelationResults;

    private static List<DvPixelStatistic> createPixelCorrelationStatistics(
        int count, float value) {
        List<DvPixelStatistic> stats = new ArrayList<DvPixelStatistic>();
        for (int i = 0; i < count; i++) {
            stats.add(new DvPixelStatistic(CCD_ROW + i, CCD_COLUMN - i, value,
                SIGNIFICANCE));
        }
        return stats;
    }

    @Before
    public void createExpectedPixelCorrelationResults() {
        pixelCorrelationResults = createPixelCorrelationResults(ID,
            createPixelCorrelationStatistics(1, VALUE));
    }

    private static DvPixelCorrelationResults createPixelCorrelationResults(
        long id, List<DvPixelStatistic> pixelCorrelationStatistics) {

        return new DvPixelCorrelationResults.Builder(TARGET_TABLE_ID).ccdModule(
            CCD_MODULE)
            .ccdOutput(CCD_OUTPUT)
            .quarter(QUARTER)
            .startCadence(START_CADENCE)
            .endCadence(END_CADENCE)
            .id(id)
            .controlCentroidOffsets(createCentroidOffsets(0))
            .controlImageCentroid(createImageCentroid(6))
            .correlationImageCentroid(createImageCentroid(10))
            .kicCentroidOffsets(createCentroidOffsets(14))
            .kicReferenceCentroid(createImageCentroid(20))
            .pixelCorrelationStatistics(pixelCorrelationStatistics)
            .build();
    }

    static DvPixelCorrelationResults createPixelCorrelationResults() {
        return createPixelCorrelationResults(ID, PIXEL_CORRELATION_STATISTICS);
    }

    @Test
    public void testConstructor() {
        // Create simply to get code coverage.
        new DvPixelCorrelationResults();

        testPixelCorrelationResults(pixelCorrelationResults);
    }

    private void testPixelCorrelationResults(
        DvPixelCorrelationResults pixelCorrelationResults) {

        DvAbstractTargetTableDataTest.testTargetTableData(pixelCorrelationResults);

        assertEquals(ID, pixelCorrelationResults.getId());
        assertEquals(PIXEL_CORRELATION_STATISTICS,
            pixelCorrelationResults.getPixelCorrelationStatistics());
    }

    @Test
    public void testEquals() {
        // Include all don't-care fields here.
        DvPixelCorrelationResults pcr = createPixelCorrelationResults(ID + 1,
            PIXEL_CORRELATION_STATISTICS);
        assertEquals("equals", pixelCorrelationResults, pcr);

        pcr = createPixelCorrelationResults(ID + 1,
            createPixelCorrelationStatistics(1, VALUE + 1.0F));
        assertFalse("equals", pixelCorrelationResults.equals(pcr));

        pcr = createPixelCorrelationResults(ID + 1,
            createPixelCorrelationStatistics(2, VALUE));
        assertFalse("equals", pixelCorrelationResults.equals(pcr));
    }

    @Test
    public void testHashCode() {
        // Include all don't-care fields here.
        DvPixelCorrelationResults pcr = createPixelCorrelationResults(ID + 1,
            PIXEL_CORRELATION_STATISTICS);
        assertEquals("hashCode", pixelCorrelationResults.hashCode(),
            pcr.hashCode());

        pcr = createPixelCorrelationResults(ID + 1,
            createPixelCorrelationStatistics(1, VALUE + 1.0F));
        assertFalse("hashCode",
            pixelCorrelationResults.hashCode() == pcr.hashCode());

        pcr = createPixelCorrelationResults(ID + 1,
            createPixelCorrelationStatistics(2, VALUE));
        assertFalse("hashCode",
            pixelCorrelationResults.hashCode() == pcr.hashCode());
    }

    @Test
    public void testToString() {
        // Check log and ensure that output isn't brutally long.
        log.info(pixelCorrelationResults.toString());
    }
}
