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

package gov.nasa.spiffy.common.intervals;

import gov.nasa.spiffy.common.persistable.Persistable;

import java.util.List;

/**
 * A [start, end] range of values where start <= end.  And start()
 * and end() return non-negative integers.  Implementers of this class
 * should make it immutable.
 * 
 * @author Sean McCauliff
 *
 */
public interface Interval extends Persistable {
    /**
     * @return the start of this interval
     */
    public long start();
    
    /**
     * @return the end of this interval
     */
    public long end();
    
    /**
     * Merges two intervals into each other.
     * 
     * @param other  Some other potentially overlapping interval.  If the
     * intervals are bordering each other by one, that is 
     * this.start == other.end + 1 OR this.end == other.start + 1 and
     * the two intervals are compitable then this should merge them into one
     * Interval.
     * @return One or more non-overlapping intervals in ascending order.
     */
    public MergeResult merge(Interval other);
    
    /**
     * Copies all the information that is not-start,end into a new interval.
     */
    public Interval clone(long start, long end);
    
    /**
     * The intervals that are produced as an act of running merge().
     * 
     * @author smccauliff
     *
     */
    public static interface  MergeResult {
        /**
         * 
         * @return One or more Interval objects.
         */
        public List<Interval> mergedIntervals();
        
        /** Which index into the list of merged intervals contains the 
         * other interval which was merged into this one.
         * @return A non-negative integer that will be a valid index into
         * the list returned by mergedintervals()
         */
        public int otherIndex();
    }
}
