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

package gov.nasa.kepler.mc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import gov.nasa.spiffy.common.pi.Parameters;

/**
 * Parameters needed by the TargetListChunkUnitOfWork.
 * 
 * @author Sean McCauliff
 *
 */
public class TargetListParameters implements Parameters {

    private String[] targetListNames = ArrayUtils.EMPTY_STRING_ARRAY;
    private String[] excludeTargetListNames = ArrayUtils.EMPTY_STRING_ARRAY;
    private int chunkSize;
    
    public TargetListParameters() {
        
    }
    
    public List<String> targetListNames() {
        if (targetListNames != null) {
            return Arrays.asList(targetListNames);
        }
        return Collections.emptyList();
    }
    
    public List<String> excludeTargetListNames() {
        if (excludeTargetListNames != null) {
            return Arrays.asList(excludeTargetListNames);
        }
        return Collections.emptyList();
    }
    
    public TargetListParameters(int chunkSize, String[] targetListNames, String[] excludeTargetListNames) {
        this.targetListNames = targetListNames;
        this.chunkSize = chunkSize;
        this.excludeTargetListNames = excludeTargetListNames;
    }

    public String[] getTargetListNames() {
        return targetListNames;
    }

    public void setTargetListNames(String[] targetListNames) {
        this.targetListNames = targetListNames;
    }

    
    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    
    public String[] getExcludeTargetListNames() {
        return excludeTargetListNames;
    }

    public void setExcludeTargetListNames(String[] excludeTargetListNames) {
        this.excludeTargetListNames = excludeTargetListNames;
    }
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + chunkSize;
        result = prime * result + Arrays.hashCode(excludeTargetListNames);
        result = prime * result + Arrays.hashCode(targetListNames);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TargetListParameters other = (TargetListParameters) obj;
        if (chunkSize != other.chunkSize)
            return false;
        if (!Arrays
                .equals(excludeTargetListNames, other.excludeTargetListNames))
            return false;
        if (!Arrays.equals(targetListNames, other.targetListNames))
            return false;
        return true;
    }

    
}
