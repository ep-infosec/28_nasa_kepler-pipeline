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

package gov.nasa.kepler.systest.sbt;

import gov.nasa.kepler.common.TicToc;
import gov.nasa.kepler.common.persistable.SdfPersistableOutputStream;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Superclass for Sandbox tools (SBTs) that use the .sdf file
 * format for transferring data between Java and MATLAB
 * 
 * @author Todd Klaus todd.klaus@nasa.gov
 *
 */
public class AbstractSbt {
    static final Log log = LogFactory.getLog(AbstractSbt.class);
    public boolean requiresDatabase;
    public boolean requiresFilestore;

    public AbstractSbt() {
        this.requiresDatabase = true;
        this.requiresFilestore = true;
    }
    
    public AbstractSbt(boolean requiresDatabase, boolean requiresFilestore) {
        this.requiresDatabase = requiresDatabase;
        this.requiresFilestore = requiresFilestore;
    }

    protected boolean validateDatastores() {
        PingDataStores pinger = new PingDataStores();
        return pinger.validateDatastore(requiresDatabase, requiresFilestore);
    }

    /**
     * @param data
     * @return  The absolute path of the created SDF file as a string.
     * @throws FileNotFoundException
     * @throws Exception
     * @throws IOException
     */
    protected String makeSdf(Object data, String sdfPath) throws FileNotFoundException, Exception, IOException {
        File sdfFile = new File(sdfPath);
        return makeSdf(data, sdfFile);
    }
    
    /**
     * 
     * @param data
     * @param sdfFile
     * @return The absolute path of the created SDF file as a string.
     * @throws FileNotFoundException
     * @throws Exception
     * @throws IOException
     */
    protected String makeSdf(Object data, File sdfFile) throws FileNotFoundException, Exception, IOException {
        sdfFile = sdfFile.getCanonicalFile();
        TicToc.tic("Generating .sdf file \"" + sdfFile + "\".");
        
       
        
        FileOutputStream fos = new FileOutputStream(sdfFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DataOutputStream dos = new DataOutputStream(bos);

        SdfPersistableOutputStream spos = new SdfPersistableOutputStream(dos);
               
        spos.save(data);
        dos.close();
        
        TicToc.toc();
        
        return sdfFile.getCanonicalPath();
    }
}
