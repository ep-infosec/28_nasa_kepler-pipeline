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

import gov.nasa.kepler.hibernate.AbstractCrud;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.tad.MaskTable;
import gov.nasa.kepler.hibernate.tad.MaskTable.MaskType;
import gov.nasa.kepler.hibernate.tad.TargetCrud;
import gov.nasa.kepler.hibernate.tad.TargetTable;
import gov.nasa.kepler.hibernate.tad.TargetTable.TargetType;

import java.util.Set;

/**
 * Provides a transactional version of {@link TargetCrud}.
 * 
 * @author Bill Wohler
 */
public class TargetCrudProxy extends AbstractCrud {

    private TargetCrud targetCrud;

    /**
     * Creates a new {@link TargetCrudProxy} object.
     */
    public TargetCrudProxy() {
        this(null);
    }

    /**
     * Creates a new {@link TargetCrudProxy} object with the specified database
     * service.
     * 
     * @param databaseService the {@link DatabaseService} to use for the
     * operations
     */
    public TargetCrudProxy(DatabaseService databaseService) {
        super(databaseService);
        targetCrud = new TargetCrud(databaseService);
    }

    public void createTargetTable(TargetTable targetTable) {
        getDatabaseService().beginTransaction();
        targetCrud.createTargetTable(targetTable);
        getDatabaseService().flush();
        getDatabaseService().commitTransaction();
    }

    public Set<Integer> retrieveUplinkedExternalIds(TargetType type) {
        getDatabaseService().beginTransaction();
        Set<Integer> ids = targetCrud.retrieveUplinkedExternalIds(type);
        getDatabaseService().flush();
        getDatabaseService().commitTransaction();

        return ids;
    }

    public Set<Integer> retrieveExternalIdsInUse(TargetType type) {
        getDatabaseService().beginTransaction();
        Set<Integer> ids = targetCrud.retrieveExternalIdsInUse(type);
        getDatabaseService().flush();
        getDatabaseService().commitTransaction();

        return ids;
    }

    public Set<Integer> retrieveUplinkedExternalIds(MaskType type) {
        getDatabaseService().beginTransaction();
        Set<Integer> ids = targetCrud.retrieveUplinkedExternalIds(type);
        getDatabaseService().flush();
        getDatabaseService().commitTransaction();

        return ids;
    }

    public Set<Integer> retrieveExternalIdsInUse(MaskType type) {
        getDatabaseService().beginTransaction();
        Set<Integer> ids = targetCrud.retrieveExternalIdsInUse(type);
        getDatabaseService().flush();
        getDatabaseService().commitTransaction();

        return ids;
    }

    public void createMaskTable(MaskTable maskTable) {
        getDatabaseService().beginTransaction();
        targetCrud.createMaskTable(maskTable);
        getDatabaseService().flush();
        getDatabaseService().commitTransaction();
    }

    /**
     * Used only for testing.
     */
    void setTargetCrud(TargetCrud targetCrud) {
        this.targetCrud = targetCrud;
    }
}
