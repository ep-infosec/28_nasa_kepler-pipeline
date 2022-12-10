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

package gov.nasa.kepler.ui.proxy;

import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.TransactionService;
import gov.nasa.kepler.hibernate.dbservice.TransactionServiceFactory;
import gov.nasa.kepler.hibernate.pi.PipelineTask;
import gov.nasa.kepler.hibernate.services.Privilege;
import gov.nasa.kepler.pi.pipeline.PipelineExecutor;
import gov.nasa.kepler.services.messaging.MessagingServiceFactory;
import gov.nasa.kepler.ui.PipelineConsole;
import gov.nasa.kepler.ui.models.DatabaseModelRegistry;
import gov.nasa.spiffy.common.pi.PipelineException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Todd Klaus tklaus@arc.nasa.gov
 * 
 */
public class PipelineExecutorProxy extends CrudProxy {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(PipelineExecutorProxy.class);

    /**
     * @param databaseService
     */
    public PipelineExecutorProxy() {
    }

    /**
     * Wrapper for the PipelineExecutor.reRunTask() method that also handles
     * messaging and database service transactions.
     * 
     * @param trigger
     * @throws Exception
     */
    public void reRunTask(final PipelineTask task, final boolean doTransitionOnly) throws Exception {
        verifyPrivileges(Privilege.PIPELINE_OPERATIONS);
        List<PipelineTask> taskList = new ArrayList<PipelineTask>();
        taskList.add(task);
        
        reRunTasks(taskList, false);
    }

    /**
     * Wrapper for the PipelineExecutor.reRunTask() method that also handles
     * messaging and database service transactions.
     * 
     * @param trigger
     * @throws Exception
     */
    public void reRunTasks(final List<PipelineTask> failedTasks, final boolean doTransitionOnly) throws Exception {
        verifyPrivileges(Privilege.PIPELINE_OPERATIONS);
        PipelineConsole.crudProxyExecutor.executeSynchronous(new Callable<Object>() {
            public Object call() throws PipelineException {

                MessagingServiceFactory.setUseXa(false);
                DatabaseServiceFactory.setUseXa(false);

                TransactionService transactionService = TransactionServiceFactory.getInstance(false);
                transactionService.beginTransaction(true, true, false);

                try {
                    PipelineExecutor pipelineExecutor = new PipelineExecutor();
                    for (PipelineTask failedTask : failedTasks) {
                        pipelineExecutor.reRunFailedTask(failedTask, false);
                    }

                    // need a flush here since we have auto-flush turned off
                    DatabaseService databaseService = DatabaseServiceFactory.getInstance();
                    databaseService.flush();
                    
                    transactionService.commitTransaction();
                } catch (Exception e) {
                    transactionService.rollbackTransactionIfActive();
                    throw new PipelineException("failed to launch task", e);
                }

                return null;
            }
        });
        // invalidate the models since rerunning a task changes other states.
        DatabaseModelRegistry.invalidateModels();
    }
}
