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

package gov.nasa.kepler.fs.server.index.blinktree;

import gov.nasa.kepler.fs.api.FsId;
import gov.nasa.kepler.fs.server.index.DiskNodeIO;
import gov.nasa.kepler.fs.server.index.KeyValueIO;
import gov.nasa.kepler.fs.server.index.TreeNodeFactory;
import gov.nasa.kepler.fs.server.index.AbstractDiskNodeIO.CacheNodeKey;
import gov.nasa.kepler.fs.server.index.DiskNodeIO.BtreeFileVersion;
import gov.nasa.kepler.fs.storage.FsIdInfo;
import gov.nasa.kepler.fs.storage.RandomAccessAllocator.RandomAccessKeyValueIo;
import gov.nasa.spiffy.common.collect.LruCache;

import java.io.*;
import java.util.Map;


/**
 * @author Sean McCauliff
 *
 */
public class BLinkTreeInspector {

    private final static int NODE_SIZE = 1024 * 16;
    
    public static void main(String[] argv) throws Exception {
        File indexFile = new File(argv[0]);
        File outputFile = new File(argv[1]);
        
        LruCache<CacheNodeKey, BLinkNode<FsId, FsIdInfo>> destCache = 
            new LruCache<CacheNodeKey, BLinkNode<FsId, FsIdInfo>>(256);
        
        KeyValueIO<FsId, FsIdInfo> keyValueIo = new RandomAccessKeyValueIo();
        NodeLockFactory lockFactory = new NodeLockFactory();
        TreeNodeFactory<FsId, FsIdInfo, BLinkNode<FsId, FsIdInfo>> destNodeFactory = 
            BLinkNode.nodeFactory(lockFactory, FsId.comparator);
        
        DiskNodeIO<FsId, FsIdInfo, BLinkNode<FsId,FsIdInfo>> destIo =
            new DiskNodeIO<FsId, FsIdInfo, BLinkNode<FsId,FsIdInfo>>(
                keyValueIo, indexFile, NODE_SIZE, destCache, destNodeFactory,
                BtreeFileVersion.VERSION_1);
        
        final int leafM = LeafNode.leafM(keyValueIo, NODE_SIZE);
        final int internalM = InternalNode.internalM(keyValueIo, NODE_SIZE);
        
        BLinkTree<FsId, FsIdInfo> srcTree = 
            new BLinkTree<FsId, FsIdInfo>(destIo, leafM,
                internalM, FsId.comparator, lockFactory);
        
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
        
        for (Map.Entry<FsId, FsIdInfo> entry : srcTree) {
            printWriter.println(entry.getKey() + "\n\t" + entry.getValue());
        }
    }
}
