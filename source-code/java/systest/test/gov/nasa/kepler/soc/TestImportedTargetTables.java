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

package gov.nasa.kepler.soc;

import gov.nasa.kepler.hibernate.gar.ExportTable.State;
import gov.nasa.kepler.hibernate.tad.ModOut;
import gov.nasa.kepler.hibernate.tad.TargetDefinition;
import gov.nasa.kepler.hibernate.tad.TargetTable;
import gov.nasa.kepler.hibernate.tad.TargetTable.TargetType;
import gov.nasa.kepler.tad.xml.ImportedTargetTable;

import java.util.Date;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * @author Miles Cote
 * 
 */
public class TestImportedTargetTables {

    public static final ImportedTargetTable createImportedTargetTable() {
        List<TargetDefinition> targetDefinitions = createTargetDefinitions();

        return new ImportedTargetTable(targetDefinitions.get(0)
            .getTargetTable(), targetDefinitions);
    }

    public static final List<TargetDefinition> createTargetDefinitions() {
        return ImmutableList.of(createTargetDefinition());
    }

    public static final TargetDefinition createTargetDefinition() {
        TargetDefinition targetDefinition = new TargetDefinition();
        targetDefinition.setModOut(ModOut.of(7, 8));
        targetDefinition.setIndexInModuleOutput(10);
        targetDefinition.setKeplerId(11);
        targetDefinition.setMask(TestImportedMaskTables.createMask());
        targetDefinition.setReferenceColumn(14);
        targetDefinition.setReferenceRow(15);
        targetDefinition.setTargetTable(createTargetTable());

        return targetDefinition;
    }

    public static final TargetTable createTargetTable() {
        TargetTable targetTable = new TargetTable(TargetType.LONG_CADENCE);
        targetTable.setState(State.UPLINKED);
        targetTable.setExternalId(3);
        targetTable.setMaskTable(TestImportedMaskTables.createMaskTable());
        targetTable.setPlannedStartTime(new Date(4000));
        targetTable.setPlannedEndTime(new Date(5000));

        return targetTable;
    }

}
