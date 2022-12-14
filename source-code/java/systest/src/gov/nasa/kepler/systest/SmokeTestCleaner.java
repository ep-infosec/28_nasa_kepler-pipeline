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

package gov.nasa.kepler.systest;

import gov.nasa.kepler.common.DefaultProperties;
import gov.nasa.kepler.fs.FileStoreConstants;
import gov.nasa.kepler.hibernate.dbservice.ConfigurationServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.dbservice.HibernateConstants;
import gov.nasa.kepler.hibernate.dbservice.KeplerSchemaExport;
import gov.nasa.spiffy.common.io.FileUtil;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SmokeTestCleaner {

    private static final Log log = LogFactory.getLog(SmokeTestCleaner.class);

    private void cleanSmokeTest() throws Exception {
        verifyIsValidDestDatastore();

        String databaseUrl = ConfigurationServiceFactory.getInstance()
            .getString(HibernateConstants.HIBERNATE_URL_PROP);

        String databaseVendor;
        if (databaseUrl.contains("oracle")) {
            databaseVendor = "oracle";
        } else if (databaseUrl.contains("hsqldb")) {
            databaseVendor = "hsqldb";
        } else {
            throw new IllegalArgumentException("Unexpected databaseUrl: "
                + databaseUrl);
        }

        cleanDatabase(databaseVendor);

        cleanFsDataDir();
    }

    public static void verifyIsValidDestDatastore() {
        Configuration configService = ConfigurationServiceFactory.getInstance();

        String databaseUrl = configService.getString(HibernateConstants.HIBERNATE_URL_PROP);
        String databaseUsername = configService.getString(HibernateConstants.HIBERNATE_USERNAME_PROP);
        String filestoreUrl = configService.getString(FileStoreConstants.FS_FSTP_URL);

        if (!databaseUrl.contains("localhost")
            && !databaseUsername.contains("user1")
            && !databaseUsername.contains("user2")) {
            throw new IllegalArgumentException(
                "Either the databaseUrl must contain the phrase 'localhost' or the databaseUsername "
                    + "must contain the phrase 'user1' or 'user2'.\n  databaseUrl: "
                    + databaseUrl + "\n  databaseUsername: " + databaseUsername);
        }

        if (!filestoreUrl.contains("localhost")
            && !filestoreUrl.contains("filestore")) {
            throw new IllegalArgumentException(
                "The filestoreUrl must contain the phrase 'localhost' or 'filestore'.\n  filestoreUrl: "
                    + filestoreUrl);
        }
    }

    private void cleanDatabase(String databaseVendor) throws Exception {
        verifyIsValidDestDatastore();

        Configuration configService = ConfigurationServiceFactory.getInstance();
        String databaseUsername = configService.getString(HibernateConstants.HIBERNATE_USERNAME_PROP);

        // Set schema dir prop.
        String testSchemaDir = ConfigurationServiceFactory.getInstance()
            .getString(DefaultProperties.TEST_SCHEMA_DIR_PROP);
        System.setProperty(DefaultProperties.TEST_SCHEMA_DIR_PROP,
            testSchemaDir);

        // Create new create script.
        String[] createArgs = {
            "--create",
            "--output=" + DefaultProperties.getTestSchemaDir() + "/ddl."
                + databaseVendor + "-create.sql" };
        KeplerSchemaExport.main(createArgs);

        // Execute old drop script.
        DatabaseServiceFactory.getInstance()
            .getDdlInitializer()
            .cleanDB();

        // Execute new create script.
        DatabaseServiceFactory.getInstance()
            .getDdlInitializer()
            .initDB();

        // Create new drop script.
        String[] dropArgs = {
            "--drop",
            "--output=" + DefaultProperties.getUnitTestDataDir("hibernate")
                + "/schema/" + databaseUsername + "/ddl." + databaseVendor
                + "-drop.sql" };
        KeplerSchemaExport.main(dropArgs);
    }

    private void cleanFsDataDir() throws IOException {
        verifyIsValidDestDatastore();

        String fsDataDirPropName = "fs.data.dir";
        String fsDataDir = ConfigurationServiceFactory.getInstance()
            .getString(fsDataDirPropName);
        if (fsDataDir == null) {
            throw new IllegalArgumentException(fsDataDirPropName
                + " must not be null.");
        }

        log.info("Cleaning fsDataDir: " + fsDataDir);
        FileUtil.cleanDir(fsDataDir);
    }

    public static void main(String[] args) throws Exception {
        SmokeTestCleaner cleaner = new SmokeTestCleaner();
        cleaner.cleanSmokeTest();
    }

}
