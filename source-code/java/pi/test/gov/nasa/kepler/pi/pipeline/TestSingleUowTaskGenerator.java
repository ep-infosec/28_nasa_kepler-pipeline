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

package gov.nasa.kepler.pi.pipeline;

import gov.nasa.kepler.hibernate.pi.UnitOfWorkTask;
import gov.nasa.kepler.hibernate.pi.UnitOfWorkTaskGenerator;
import gov.nasa.spiffy.common.pi.Parameters;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A simple task generator that generates a single task
 * to support the PipelineExecutorFeatureTest
 * 
 * @author Todd Klaus tklaus@arc.nasa.gov
 *
 */
public class TestSingleUowTaskGenerator implements UnitOfWorkTaskGenerator {

    private boolean lastTask = false;
    
    public TestSingleUowTaskGenerator() {
    }

    public TestSingleUowTaskGenerator(boolean lastTask) {
        this.lastTask = lastTask;
    }

    @Override
    public Class<? extends UnitOfWorkTask> unitOfWorkTaskType() {
        return TestSingleUowTask.class;
    }

    @Override
    public List<Class<? extends Parameters>> requiredParameterClasses() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends UnitOfWorkTask> generateTasks(Map<Class<? extends Parameters>, Parameters> parameters) {

        List<TestSingleUowTask> tasks = new LinkedList<TestSingleUowTask>();
        TestSingleUowTask prototypeTask = new TestSingleUowTask(lastTask);
        tasks.add(prototypeTask);

        return tasks;
    }

    /**
     * used by the PIG to display the name of the task generator
     */
    @Override
    public String toString(){
        return "TestSingle";
    }

    /**
     * @return the lastTask
     */
    public boolean isLastTask() {
        return lastTask;
    }

    /**
     * @param lastTask the lastTask to set
     */
    public void setLastTask(boolean lastTask) {
        this.lastTask = lastTask;
    }
}
