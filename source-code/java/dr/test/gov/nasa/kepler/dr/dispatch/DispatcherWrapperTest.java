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

package gov.nasa.kepler.dr.dispatch;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.dr.importer.ModelMetadataImporter;
import gov.nasa.kepler.fs.api.FileStoreClient;
import gov.nasa.kepler.fs.api.FsId;
import gov.nasa.kepler.hibernate.dr.DispatchLog;
import gov.nasa.kepler.hibernate.dr.DispatchLog.DispatcherType;
import gov.nasa.kepler.hibernate.dr.DispatchLogFactory;
import gov.nasa.kepler.hibernate.dr.FileLog;
import gov.nasa.kepler.hibernate.dr.LogCrud;
import gov.nasa.kepler.hibernate.dr.ReceiveLog;
import gov.nasa.kepler.mc.fs.DrFsIdFactory;
import gov.nasa.spiffy.common.jmock.JMockTest;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * @author Miles Cote
 * 
 */
public class DispatcherWrapperTest extends JMockTest {

    private String sourceDirectory = "sourceDirectory";
    private String filename = "filename";
    private Set<String> filenames = newHashSet(filename);
    private File file = new File(sourceDirectory, filename);
    private String messageFileName = "messageFileName";

    private DispatcherType dispatcherType = DispatcherType.LONG_CADENCE_PIXEL;
    private FsId fsId = DrFsIdFactory.getFile(dispatcherType, filename);

    private FileLog fileLog = mock(FileLog.class);

    private Dispatcher dispatcher = mock(Dispatcher.class);
    private NotificationMessageHandler notificationMessageHandler = mock(NotificationMessageHandler.class);
    private ReceiveLog receiveLog = mock(ReceiveLog.class);
    private LogCrud logCrud = mock(LogCrud.class);
    private ModelMetadataImporter modelMetadataImporter = mock(ModelMetadataImporter.class);
    private DispatchLog dispatchLog = mock(DispatchLog.class);
    private List<DispatchLog> dispatchLogs = newArrayList(dispatchLog);
    private DispatchLogFactory dispatchLogFactory = mock(DispatchLogFactory.class);
    private FileStoreClient fileStoreClient = mock(FileStoreClient.class);

    private DispatcherWrapper dispatcherWrapper = new DispatcherWrapper(
        dispatcher, dispatcherType, sourceDirectory,
        notificationMessageHandler, logCrud, modelMetadataImporter,
        dispatchLogFactory, fileStoreClient);

    @Test
    public void testAddFileNameAndDispatchAndStoreFile() {
        setAllowances();

        oneOf(logCrud).createDispatchLog(dispatchLog);

        oneOf(dispatchLog).start();

        oneOf(dispatchLog).setTotalFileCount(filenames.size());

        oneOf(dispatcher).dispatch(filenames, sourceDirectory, dispatchLog,
            dispatcherWrapper);

        oneOf(dispatchLog).end();

        oneOf(modelMetadataImporter).importModelMetadata(receiveLog,
            dispatcherType);

        oneOf(fileStoreClient).writeBlob(fsId,
            DispatcherWrapper.DATA_RECEIPT_ORIGIN_ID, file);

        dispatcherWrapper.addFileName(filename);
        dispatcherWrapper.dispatch();
        FileLog actualFileLog = dispatcherWrapper.storeFile(filename);

        assertEquals(fileLog, actualFileLog);
    }

    @Test(expected = DispatchException.class)
    public void testThrowExceptionForFile() {
        dispatcherWrapper.throwExceptionForFile(filename, new Exception());
    }

    @Test
    public void testGetName() {
        String actualName = dispatcherWrapper.getName();

        assertEquals(dispatcher.getClass()
            .getSimpleName(), actualName);
    }

    private void setAllowances() {
        allowing(notificationMessageHandler).getReceiveLog();
        will(returnValue(receiveLog));

        allowing(dispatchLogFactory).create(receiveLog, dispatcherType);
        will(returnValue(dispatchLog));

        allowing(notificationMessageHandler).getDispatchLogs();
        will(returnValue(dispatchLogs));

        allowing(receiveLog).getMessageFileName();
        will(returnValue(messageFileName));

        allowing(logCrud).createFileLog(dispatchLog, filename);
        will(returnValue(fileLog));
    }

}
