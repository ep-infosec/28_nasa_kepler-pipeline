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

package gov.nasa.kepler.common.file;

import gov.nasa.spiffy.common.io.DirectoryWalker;
import gov.nasa.spiffy.common.io.FileVisitor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * In order traversal of directories and the directories contained in them. Kind
 * of like the UNIX command "find". Note that Java does not know anything about
 * symlinks and so it will traverse all links that are directories potentially
 * creating problems for visitors that intend to remove everything.
 * 
 * @author Forrest Girouard
 * @author Sean McCauliff
 * 
 */
public class DirectoryCopyWalker extends DirectoryWalker {

    private static final FileFilter DIRECTORY_FILTER = new FileFilter() {

        public boolean accept(File file) {
            return file.isDirectory();
        }
    };

    public DirectoryCopyWalker(File rootDir) {

        super(rootDir);
    }

    @Override
    public void traverse(FileVisitor v) throws IOException {

        LinkedList<File> unvisited = new LinkedList<File>();
        LinkedList<File> currentPath = new LinkedList<File>();

        unvisited.add(getRootDir());

        while (unvisited.size() != 0) {
            File nextDir = unvisited.removeLast();
            // Are we exiting a directory, never to see it again?
            if (currentPath.size() != 0 && !currentPath.getLast()
                .equals(nextDir.getParentFile())) {
                v.exitDirectory(currentPath.removeLast());
            }
            currentPath.add(nextDir);

            v.enterDirectory(nextDir);
            if (v.prune()) {
                continue;
            }

            File[] directories = nextDir.listFiles(DIRECTORY_FILTER);
            if (directories != null) {
                for (File directory : directories) {
                    unvisited.add(directory);
                }
            }
        }

        // There are no more files to visit, but we still need to exit from
        // directories, this will happen when we get to the last unopend
        // directory.
        while (currentPath.size() != 0) {
            v.exitDirectory(currentPath.removeLast());
        }
    }

}
