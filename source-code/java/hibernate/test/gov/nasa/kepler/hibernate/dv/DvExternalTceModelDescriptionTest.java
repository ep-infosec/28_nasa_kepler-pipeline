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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import gov.nasa.kepler.hibernate.pi.PipelineTask;

import org.junit.Before;
import org.junit.Test;

public class DvExternalTceModelDescriptionTest {

    private static final String EXTERNAL_TCE_MODEL_DESCRIPTION = "Test external TCE model description";
    private static final long PIPELINE_TASK_ID = 42;
    private static final PipelineTask PIPELINE_TASK = createPipelineTask(PIPELINE_TASK_ID);

    private DvExternalTceModelDescription externalTceModelDescription;

    private static PipelineTask createPipelineTask(long pipelineTaskId) {
        PipelineTask pipelineTask = new PipelineTask();
        pipelineTask.setId(pipelineTaskId);

        return pipelineTask;
    }

    @Before
    public void createdExpectedTransitModelDescriptions() {
        externalTceModelDescription = createExternalTceModelDescription(
            PIPELINE_TASK, EXTERNAL_TCE_MODEL_DESCRIPTION);
    }

    private static DvExternalTceModelDescription createExternalTceModelDescription(
        PipelineTask pipelineTask, String externalTceModelDescription) {

        return new DvExternalTceModelDescription(pipelineTask,
            externalTceModelDescription);
    }

    @Test
    public void testConstructor() {
        new DvTransitModelDescriptions();

        testExternalTceModelDescription(externalTceModelDescription);
    }

    private void testExternalTceModelDescription(
        DvExternalTceModelDescription externalTceModelDescription) {
        assertEquals(PIPELINE_TASK,
            externalTceModelDescription.getPipelineTask());
        assertEquals(EXTERNAL_TCE_MODEL_DESCRIPTION,
            externalTceModelDescription.getModelDescription());
    }

    @Test
    public void testEquals() {
        DvExternalTceModelDescription description = createExternalTceModelDescription(
            PIPELINE_TASK, EXTERNAL_TCE_MODEL_DESCRIPTION);
        assertEquals(externalTceModelDescription, description);

        description = createExternalTceModelDescription(
            createPipelineTask(PIPELINE_TASK_ID + 1),
            EXTERNAL_TCE_MODEL_DESCRIPTION);
        assertFalse("equals", externalTceModelDescription.equals(description));

        description = createExternalTceModelDescription(PIPELINE_TASK,
            EXTERNAL_TCE_MODEL_DESCRIPTION + " 1");
        assertFalse("equals", externalTceModelDescription.equals(description));
    }

    @Test
    public void testHashCode() {
        DvExternalTceModelDescription description = createExternalTceModelDescription(
            PIPELINE_TASK, EXTERNAL_TCE_MODEL_DESCRIPTION);
        assertEquals(externalTceModelDescription.hashCode(),
            description.hashCode());

        description = createExternalTceModelDescription(
            createPipelineTask(PIPELINE_TASK_ID + 1),
            EXTERNAL_TCE_MODEL_DESCRIPTION);
        assertFalse("hashCode",
            externalTceModelDescription.hashCode() == description.hashCode());

        description = createExternalTceModelDescription(PIPELINE_TASK,
            EXTERNAL_TCE_MODEL_DESCRIPTION + " 1");
        assertFalse("hashCode",
            externalTceModelDescription.hashCode() == description.hashCode());

    }

}
