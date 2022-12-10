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

package gov.nasa.kepler.pi.worker;

import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.pi.PipelineInstance;
import gov.nasa.kepler.services.messaging.MessageContext;
import gov.nasa.kepler.services.messaging.MessagingDestinations;
import gov.nasa.kepler.services.messaging.MessagingService;
import gov.nasa.kepler.services.messaging.MessagingServiceFactory;
import gov.nasa.spiffy.common.pi.PipelineException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Drains task messages from all worker task queues
 * 
 * @author tklaus
 *
 */
public class WorkerQueueDrainer {
    private static final Log log = LogFactory.getLog(WorkerQueueDrainer.class);

    public WorkerQueueDrainer() {
    }

    public void drain(){
        try{
            log.info("Initializing DatabaseService...");
            DatabaseServiceFactory.getInstance();
            
            log.info("Initializing MessagingService...");
            MessagingServiceFactory.getInstance().initialize();
            
            for (int priority = PipelineInstance.HIGHEST_PRIORITY; priority <= PipelineInstance.LOWEST_PRIORITY; priority++) {
                String queueName = MessagingDestinations.WORKER_TASK_REQUEST_QUEUE_NAMES[priority];
                log.info("draining queueName = " + queueName);
                
                drainQueue(queueName);
            }

        }catch( PipelineException e ){
            log.fatal("Initialization failed!", e);
        }
        
        log.info("Done draining all messages");
        
        System.exit(1); // should be messagingService.shutdown()
    }
    
    private void drainQueue(String name) {
        MessagingService messagingService = MessagingServiceFactory.getInstance();
        MessageContext msgContext = null;

        do{
            msgContext = messagingService.receiveNoWait(name);
            
            if(msgContext != null){
                log.info("Got a message from queueName = " + name);
            }
            messagingService.commitTransaction();
        }while(msgContext != null);
    }

    public static void main(String[] args) {
        WorkerQueueDrainer drainer = new WorkerQueueDrainer();
        drainer.drain();
    }

}
