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

package gov.nasa.kepler.ui.mon.master;

import gov.nasa.kepler.pi.worker.WorkerStatusMessage;
import gov.nasa.kepler.services.process.ProcessInfo;
import gov.nasa.kepler.services.process.StatusMessage;
import gov.nasa.spiffy.common.lang.StringUtils;

/**
 * Displays realtime status of all worker task threads by listening for
 * {@link WorkerStatusMessage}s
 * 
 * @author tklaus
 *
 */
public class WorkersIndicatorPanel extends AgeBasedIndicatorPanel {
    private static final long serialVersionUID = 4810649149031661010L;

    public WorkersIndicatorPanel(Indicator parentIndicator) {
        super(parentIndicator);
    }

    public WorkersIndicatorPanel(Indicator parentIndicator, int numRows, boolean hasTitleButtonBar) {
        super(parentIndicator, numRows, hasTitleButtonBar);
    }

    @Override
    public void update(StatusMessage statusMessage) {
        WorkerStatusMessage workerStatusMessage = (WorkerStatusMessage) statusMessage;
        ProcessInfo sourceProcess = statusMessage.getSourceProcess();
//        String displayName = sourceProcess.getHost() + ":" + sourceProcess.getPid() + "(" + sourceProcess.getJvmid() + "):" + workerStatusMessage.getThreadNumber();
        String displayName = sourceProcess.getHost() + ":" + workerStatusMessage.getThreadNumber();
        
        WorkerIndicator indicator = (WorkerIndicator) currentIndicators.get(displayName);
        
        if(indicator == null){
            // new worker thread
            indicator = new WorkerIndicator(this, displayName);
            indicator.setId(displayName);
            currentIndicators.put(displayName, indicator);
            add(indicator);
        }
        
        indicator.setModule(workerStatusMessage.getModule());
        indicator.setModuleUow(workerStatusMessage.getModuleUow());
        indicator.setState(workerStatusMessage.getState());
        
//        indicator.setModuleTime(new Date(workerStatusMessage.getProcessingStartTime()).toString());
        indicator.setModuleTime(StringUtils.elapsedTime(workerStatusMessage.getProcessingStartTime(), System.currentTimeMillis()));

        indicator.setLastUpdatedNow();
    }
}
