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

package gov.nasa.kepler.hibernate.gar;

import gov.nasa.kepler.hibernate.gar.ExportTable.State;

import java.util.Date;

/**
 * A descriptor for {@link HuffmanTable}. This object saves one from downloading
 * entire {@link HuffmanTable}s when listing them in a table. Used in
 * projections in {@link CompressionCrud}.
 * 
 * @author Bill Wohler
 */
public class HuffmanTableDescriptor extends CompressionTableDescriptor {

    private float effectiveCompressionRate;
    private float theoreticalCompressionRate;

    /**
     * Creates a {@link HuffmanTableDescriptor} from the given
     * {@link HuffmanTable}.
     */
    public HuffmanTableDescriptor(HuffmanTable huffmanTable) {
        this(
            huffmanTable.getId(),
            huffmanTable.getPipelineTask() != null
                && huffmanTable.getPipelineTask()
                    .getPipelineInstance() != null ? huffmanTable.getPipelineTask()
                .getPipelineInstance()
                .getId()
                : INVALID_ID,
            huffmanTable.getPipelineTask() != null ? huffmanTable.getPipelineTask()
                .getId()
                : INVALID_ID,
            huffmanTable.getPlannedStartTime(), huffmanTable.getState(),
            huffmanTable.getExternalId(),
            huffmanTable.getEffectiveCompressionRate(),
            huffmanTable.getTheoreticalCompressionRate());
    }

    /**
     * Creates a {@link HuffmanTableDescriptor} with the given arguments.
     */
    public HuffmanTableDescriptor(long id, long pipelineInstanceId,
        long pipelineTaskId, Date plannedStartTime, State state,
        int externalId, float effectiveCompressionRate,
        float theoreticalCompressionRate) {

        super(id, pipelineInstanceId, pipelineTaskId, plannedStartTime, state,
            externalId);
        this.effectiveCompressionRate = effectiveCompressionRate;
        this.theoreticalCompressionRate = theoreticalCompressionRate;
    }

    public float getEffectiveCompressionRate() {
        return effectiveCompressionRate;
    }

    public float getTheoreticalCompressionRate() {
        return theoreticalCompressionRate;
    }
}