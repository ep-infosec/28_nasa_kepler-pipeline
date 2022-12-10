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

package gov.nasa.kepler.hibernate.tip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for TipBlobMetadata.
 * 
 * @author Forrest Girouard
 * 
 */
public class TipBlobMetadataTest {

    private static final long PIPELINE_TASK_ID = 356;
    private static final int SKY_GROUP_ID = 42;
    private static final String FILE_EXT = ".txt";
    private static final String BLOB_STRING_VALUE = "[pipelineTaskId="
        + PIPELINE_TASK_ID + "," + "skyGroupId=" + SKY_GROUP_ID + ","
        + "fileExtension=" + FILE_EXT + "]";

    private TipBlobMetadata tbm;

    @Before
    public void createTipBlobMetadata() {
        tbm = new TipBlobMetadata(PIPELINE_TASK_ID, SKY_GROUP_ID, FILE_EXT);
    }

    @Test
    public void testConstructor() {
        assertEquals(tbm.getCreationTime(), PIPELINE_TASK_ID);
        assertEquals(tbm.getSkyGroupId(), SKY_GROUP_ID);
    }

    @Test
    public void testEqualsObject() {
        TipBlobMetadata tbm1 = new TipBlobMetadata(PIPELINE_TASK_ID,
            SKY_GROUP_ID, FILE_EXT);
        TipBlobMetadata tbm2 = new TipBlobMetadata(PIPELINE_TASK_ID + 1,
            SKY_GROUP_ID + 1, FILE_EXT);

        assertEquals(tbm, tbm1);
        assertFalse(tbm1.equals(tbm2));
    }

    @Test
    public void testHashCode() {
        TipBlobMetadata tbm1 = new TipBlobMetadata(PIPELINE_TASK_ID,
            SKY_GROUP_ID, FILE_EXT);
        TipBlobMetadata tbm2 = new TipBlobMetadata(PIPELINE_TASK_ID + 1,
            SKY_GROUP_ID + 1, FILE_EXT);

        assertEquals(tbm.hashCode(), tbm1.hashCode());
        assertTrue(tbm1.hashCode() != tbm2.hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(tbm.toString()
            .indexOf(BLOB_STRING_VALUE) != -1);
    }

}
