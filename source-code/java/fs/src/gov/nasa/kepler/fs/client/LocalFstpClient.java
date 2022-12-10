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

package gov.nasa.kepler.fs.client;

import static gov.nasa.kepler.fs.FileStoreConstants.FS_FSTP_URL;
import gov.nasa.kepler.fs.FileStoreConstants;
import gov.nasa.kepler.fs.api.FileStoreClient;
import gov.nasa.kepler.fs.server.Server;
import gov.nasa.spiffy.common.lang.BaseInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Starts a file store server in the local process.  This server only listens on
 * the loopback address.
 * 
 * @author Sean McCauliff
 *
 */
public class LocalFstpClient extends BaseInvocationHandler implements InvocationHandler {  
    
    private static final Log log = LogFactory.getLog(LocalFstpClient.class);
    private static int PORTNO = 42236;

    private final FstpClient fstpClient;
    private final int portNo;
    
    //TODO:  remove this dependency on this static variable.
    private static Server fstpServer;
    
    /**
     * @throws Exception 
     * 
     */
    public LocalFstpClient(FstpClient fstpClient, Configuration config)
        throws Exception {
        
        try {
            this.portNo = config.getInt(FileStoreConstants.FS_LISTEN_PORT, PORTNO);
            this.fstpClient = fstpClient;
            if (fstpServer == null) {
                fstpServer = new Server(portNo, true); //localonly
                
                fstpServer.start();
            }
        } catch (Exception e) {
            log.error("LocalFstpClient construction.", e);
            throw e;
        }
        
    }

    
    public static synchronized FileStoreClient newInstance(Configuration config) 
        throws Exception {

        int portNo = config.getInt(FileStoreConstants.FS_LISTEN_PORT, PORTNO);
        String fstpUrl = "fstp://host:" + portNo;
        FstpClient fstpClient = new FstpClient(fstpUrl);
        
        return (FileStoreClient)
            Proxy.newProxyInstance(fstpClient.getClass().getClassLoader(),
              fstpClient.getClass().getInterfaces(), 
              new LocalFstpClient(fstpClient, config));
    }


    @Override
    protected Object invokeOtherMethods(Object proxy, Method method,
            Object[] args) throws Throwable {
        
        Object result;
        try {
            result = method.invoke(fstpClient, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception.", e);
        }
        
        return result;
    }


    @Override
    protected String internalToString(Object proxy, Method method, Object[] args)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        return "LocalFstpClient:" + portNo;
    }


    @Override
    protected int internalHashCode(Object proxy, Method method, Object[] args) {
        return fstpClient.hashCode();
    }


    @Override
    protected boolean invocationHandlersEquals(
            BaseInvocationHandler otherBaseInvocationHandler, Object proxy,
            Method method, Object[] args) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        LocalFstpClient other = (LocalFstpClient) otherBaseInvocationHandler;
        return fstpClient.equals(other.fstpClient);
    }

}
