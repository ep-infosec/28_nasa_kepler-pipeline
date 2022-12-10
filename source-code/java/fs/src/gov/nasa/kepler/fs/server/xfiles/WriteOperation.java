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

import gov.nasa.spiffy.common.intervals.IntervalSet;
import gov.nasa.spiffy.common.intervals.SimpleInterval;
import gov.nasa.spiffy.common.intervals.TaggedInterval;
import gov.nasa.spiffy.common.intervals.SimpleInterval.Factory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Sean McCauliff
 *
 */
class WriteOperation extends Operation {

    static {
        addReader(new WriteOperationReader());
    }
    
    private final IntervalSet<SimpleInterval, Factory> valid;
    private final IntervalSet<TaggedInterval, TaggedInterval.Factory> originators;
    //TODO:  remove redundant journal offset.
    private final long journalOffset;

    
    protected WriteOperation(long start, long end,
        IntervalSet<SimpleInterval, Factory> valid,
        IntervalSet<TaggedInterval, TaggedInterval.Factory> originators, 
        long journalOffset) {
        
        super(start, end);
        this.valid = valid;
        this.originators = originators;
        this.journalOffset = journalOffset;

    }

    /**
     * Where the data portion is stored.
     * @return
     */
    long journalOffset() { return journalOffset; }

    IntervalSet<SimpleInterval, Factory> valid() { return valid; }
    
    IntervalSet<TaggedInterval, TaggedInterval.Factory> originators() { return originators; }


    @Override
    void merge(IntervalSet<SimpleInterval, Factory> iset) {
        iset.mergeSet(valid);
    }

    @Override
    void writeTo(DataOutput dout) throws IOException {
        dout.writeUTF(OpType.OP_TYPE_WRITE.name());
        dout.writeLong(start);
        dout.writeLong(end);
        //TODO: don't really need this
        dout.writeLong(journalOffset);
        valid.writeTo(dout);
        originators.writeTo(dout);
    }
    
    @Override
    void mergeOriginators(
        IntervalSet<TaggedInterval, gov.nasa.spiffy.common.intervals.TaggedInterval.Factory> iset) {

        iset.mergeSet(originators);
    }

    
    private static class WriteOperationReader implements OperationReader {

        static final TaggedInterval.Factory taggedFactory = new TaggedInterval.Factory();
        static final SimpleInterval.Factory simpleFactory = new SimpleInterval.Factory();
        
        @Override
        public OpType opType() {
            return OpType.OP_TYPE_WRITE;
        }

        @Override
        public Operation readFrom(DataInput din) throws IOException {
            long start = din.readLong();
            long end = din.readLong();
            //TODO: remove redundant journalOffset.
            long journalOffset = din.readLong();
            
            IntervalSet<SimpleInterval, SimpleInterval.Factory> valid =
                new IntervalSet<SimpleInterval, SimpleInterval.Factory>(simpleFactory);
            valid.readFrom(din);
            
            IntervalSet<TaggedInterval, TaggedInterval.Factory> origin =
                new IntervalSet<TaggedInterval, TaggedInterval.Factory>(taggedFactory);
            origin.readFrom(din);

            return new WriteOperation(start, end, valid, origin, journalOffset);
        }
        
    }

}
