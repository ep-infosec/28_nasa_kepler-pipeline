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

package gov.nasa.kepler.fs.server.xfiles;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Allows access to lock state information for debugging purposes.
 * @author Sean McCauliff
 *
 */
public class DebugReentrantLock extends ReentrantLock{

    /**
     * 
     */
    private static final long serialVersionUID = -7390778401888102918L;


    public DebugReentrantLock() {
        super();
    }

    public DebugReentrantLock(boolean fair) {
        super(fair);
    }

    /** This information may be completely out of date.
     * 
     */
    public String getOwningThreadsStack() {
        Thread owningThread = super.getOwner();
        if (owningThread == null) {
            return "Lock not held.";
        }
        StringBuilder bldr = new StringBuilder();
        threadStackTrace(bldr, owningThread);
        return bldr.toString();
    }

    private void threadStackTrace(StringBuilder bldr, Thread owningThread) {
        StackTraceElement[] stackTraceElements = owningThread.getStackTrace();
        bldr.append(owningThread.getName());
        for (StackTraceElement st : stackTraceElements) {
            bldr.append("\t").append(st.toString()).append('\n');
        }
    }
    
    
    /**
     * Description of waiting threads.  This may not be available if the lock is
     * not fair.
     */
    public String waitingThreadsDescription() {
        StringBuilder bldr = new StringBuilder();
        for (Thread thread : getQueuedThreads()) {
            threadStackTrace(bldr, thread);
        }
        return bldr.toString();
    }
}
