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

package gov.nasa.kepler.ui.proxy;

import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
import gov.nasa.kepler.hibernate.tad.MaskTable;
import gov.nasa.kepler.hibernate.tad.TargetTable;
import gov.nasa.kepler.tad.xml.TargetExporter;

import java.io.File;
import java.util.List;

/**
 * Provides a transactional version of {@link TargetExporter}.
 * 
 * @author Bill Wohler
 */
public class TargetExporterProxy {

    private DatabaseService databaseService;
    private TargetExporter targetExporter = new TargetExporter();

    /**
     * Creates a new {@link TargetExporterProxy} object.
     */
    public TargetExporterProxy() {
        this(DatabaseServiceFactory.getInstance());
    }

    /**
     * Creates a new {@link TargetExporterProxy} object with the specified
     * database service.
     * 
     * @param databaseService the {@link DatabaseService} to use for the
     * operations
     */
    public TargetExporterProxy(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * See
     * {@link TargetExporter#export(gov.nasa.kepler.hibernate.tad.TargetTable, gov.nasa.kepler.hibernate.tad.TargetTable, gov.nasa.kepler.hibernate.tad.MaskTable, gov.nasa.kepler.hibernate.tad.MaskTable, gov.nasa.kepler.hibernate.tad.TargetTable, gov.nasa.kepler.hibernate.tad.TargetTable, gov.nasa.kepler.hibernate.tad.TargetTable, gov.nasa.kepler.hibernate.tad.TargetTable, String)}
     * .
     */
    public List<File> export(TargetTable lcTargetTable,
        TargetTable bgTargetTable, MaskTable targetMaskTable,
        MaskTable bgMaskTable, TargetTable rpTargetTable,
        TargetTable sc1TargetTable, TargetTable sc2TargetTable,
        TargetTable sc3TargetTable, String path) {

        databaseService.beginTransaction();
        List<File> files = targetExporter.export(lcTargetTable, bgTargetTable,
            targetMaskTable, bgMaskTable, rpTargetTable, sc1TargetTable,
            sc2TargetTable, sc3TargetTable, path);
        databaseService.flush();
        databaseService.commitTransaction();

        return files;
    }

    /**
     * Used only for testing.
     */
    void setTargetExporter(TargetExporter targetExporter) {
        this.targetExporter = targetExporter;
    }
}
