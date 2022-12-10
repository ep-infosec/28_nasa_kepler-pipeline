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

package gov.nasa.kepler.systest;

import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.hibernate.pi.PipelineInstance;
import gov.nasa.kepler.hibernate.pi.PipelineTask;
import gov.nasa.kepler.hibernate.pi.PipelineTaskCrud;
import gov.nasa.spiffy.common.io.FileUtil;
import gov.nasa.spiffy.common.io.Filenames;
import gov.nasa.spiffy.common.jmock.JMockTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * @author Miles Cote
 * 
 */
public class TaskCopyExpectationPipelineTaskDirsExistTest extends JMockTest {

    private static final long PIPELINE_INSTANCE_ID = 1;
    private static final long PIPELINE_TASK_ID = 2;

    private PipelineTask pipelineTask = mock(PipelineTask.class, "pipelineTask");
    private List<PipelineTask> pipelineTasks = ImmutableList.of(pipelineTask);

    private PipelineTask pipelineTaskFiltered = mock(PipelineTask.class,
        "pipelineTaskFiltered");
    private List<PipelineTask> pipelineTasksFiltered = ImmutableList.of(pipelineTaskFiltered);

    private PipelineInstance pipelineInstance = mock(PipelineInstance.class);
    private PipelineTaskCrud pipelineTaskCrud = mock(PipelineTaskCrud.class);
    private TaskCopyValidatorPipelineTaskFilter taskCopyValidatorPipelineTaskFilter = mock(TaskCopyValidatorPipelineTaskFilter.class);
    private File taskCopyDir = new File(Filenames.BUILD_TMP,
        "taskCopyDir");
    private File taskDir = new File(taskCopyDir, PIPELINE_INSTANCE_ID
        + TaskCopyExpectationPipelineTaskDirsExist.TASK_DIR_NAME_PART_SEPARATOR
        + PIPELINE_TASK_ID);

    private TaskCopyExpectationPipelineTaskDirsExist taskCopyExpectationPipelineTaskDirsExist = new TaskCopyExpectationPipelineTaskDirsExist(
        taskCopyDir, pipelineInstance, pipelineTaskCrud,
        taskCopyValidatorPipelineTaskFilter);

    @Before
    public void setUp() throws IOException {
        FileUtil.cleanDir(taskCopyDir);
        FileUtil.cleanDir(taskDir);

        allowing(pipelineTaskCrud).retrieveAll(pipelineInstance);
        will(returnValue(pipelineTasks));

        allowing(pipelineTask).getId();
        will(returnValue(PIPELINE_TASK_ID));

        allowing(pipelineTaskFiltered).getId();
        will(returnValue(PIPELINE_TASK_ID));

        allowing(pipelineInstance).getId();
        will(returnValue(PIPELINE_INSTANCE_ID));

        allowing(taskCopyValidatorPipelineTaskFilter).filter(pipelineTasks);
        will(returnValue(pipelineTasksFiltered));
    }

    @Test
    public void testIsMet() {
        boolean actualMet = taskCopyExpectationPipelineTaskDirsExist.isMet();

        assertEquals(true, actualMet);
    }

    @Test
    public void testIsMetWithNoTaskDir() {
        taskDir.delete();

        boolean actualMet = taskCopyExpectationPipelineTaskDirsExist.isMet();

        assertEquals(false, actualMet);
    }

}
