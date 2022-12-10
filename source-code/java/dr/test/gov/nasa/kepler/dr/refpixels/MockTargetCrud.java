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

package gov.nasa.kepler.dr.refpixels;

import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.tad.TargetDefinition;
import gov.nasa.kepler.hibernate.tad.TargetTable;
import gov.nasa.kepler.hibernate.tad.TargetTable.TargetType;
import gov.nasa.spiffy.common.pi.PipelineException;

import java.util.List;

public class MockTargetCrud extends GuardTargetCrud {

    private RefPixelTargetModel refPixelTargetModel;

    private int requestedExternalId = -1;
    private TargetType requestedTargetTableType = null;
    private int retrieveOrderedTargetDefinitionsCallCount = 0;
    private int returnedPixelCount = 0;

    /**
     * @param dbs
     * @throws PipelineException
     */
    public MockTargetCrud(DatabaseService dbs) {
        super(dbs);
        refPixelTargetModel = new RefPixelTargetModel();
    }

    @Override
    public TargetTable retrieveUplinkedTargetTable(int externalId,
        TargetType type) {
        TargetTable result = new TargetTable(type);
        result.setExternalId(externalId);
        requestedExternalId = externalId;
        requestedTargetTableType = type;
        return result;
    }

    @Override
    public List<TargetDefinition> retrieveTargetDefinitions(
        TargetTable targetTable, int ccdModule, int ccdOutput) {

        List<TargetDefinition> targetDefs = refPixelTargetModel.generateTargetDefsForRefPixelTest();
        retrieveOrderedTargetDefinitionsCallCount++;
        returnedPixelCount += refPixelTargetModel.getReturnedPixelCount();

        return targetDefs;
    }

    public int getRequestedExternalId() {
        return requestedExternalId;
    }

    public TargetType getRequestedTargetTableType() {
        return requestedTargetTableType;
    }

    public int getRetrieveOrderedTargetDefinitionsCallCount() {
        return retrieveOrderedTargetDefinitionsCallCount;
    }

    public int getReturnedPixelCount() {
        return returnedPixelCount;
    }
}
