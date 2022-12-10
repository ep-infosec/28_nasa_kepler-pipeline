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

package gov.nasa.kepler.fs.api.gen;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Encapsulates information about method parameters.
 * 
 * @author Sean McCauliff
 *
 */
class ParameterData {
    private final Class<?> parameterClass;
    private final int id;
    private final String signature;
 
    /**
     * 
     * @param parameterClass
     * @param count The class plus the id constitutes a unique identifier for
     * this parameter within the method.
     */
    ParameterData(Class<?> parameterClass, Type genericType, int id) {
        this.parameterClass = parameterClass;
        this.id = id;
        
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pzType = (ParameterizedType) genericType;
            this.signature = pzType.toString();
        } else {
            this.signature = parameterClass.getSimpleName();
        }
    }
    
    String parameterName() {
        if (parameterClass.isArray()) {
            String prefix = parameterClass.getSimpleName().toLowerCase().replaceAll("\\[\\]", "_a");
            return prefix + id;
        }
        return parameterClass.getSimpleName().toLowerCase() + id;
    }
    
    /** This is useful for creating a final variable that can be used with 
     * inner classes.
     * @return
     */
    String finalVariableName() {
        return "final"+parameterName();
    }
    
    Class<?> getParameterClass() {
        return parameterClass;
    }
    
    String signature() {
        return signature;
    }
    
    int id() {
        return id;
    }
}
