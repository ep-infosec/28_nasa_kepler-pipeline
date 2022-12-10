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

package gov.nasa.kepler.hibernate.mc;

import gov.nasa.kepler.common.FcConstants;
import gov.nasa.kepler.common.Cadence.CadenceType;

import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Extends the {@code AbstractCadenceBlob} which implements the
 * {@code CadenceBlob} interface by adding the module output.
 * 
 * @author Forrest Girouard
 * 
 */
@Entity
public abstract class AbstractModOutCadenceBlob extends AbstractCadenceBlob {

    /**
     * CCD Module covered by this blob.
     */
    private int ccdModule;

    /**
     * CCD Output covered by this blob.
     */
    private int ccdOutput;

    protected AbstractModOutCadenceBlob() {
    }

    protected AbstractModOutCadenceBlob(long pipelineTaskId, int startCadence,
        int endCadence, CadenceType cadenceType, int ccdModule, int ccdOutput) {

        super(pipelineTaskId, startCadence, endCadence, cadenceType);
        setModuleOutput(ccdModule, ccdOutput);
    }

    protected AbstractModOutCadenceBlob(long pipelineTaskId, int startCadence,
        int endCadence, CadenceType cadenceType, int ccdModule, int ccdOutput,
        String fileExtension) {

        super(pipelineTaskId, startCadence, endCadence, cadenceType,
            fileExtension);
        setModuleOutput(ccdModule, ccdOutput);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ccdModule;
        result = PRIME * result + ccdOutput;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AbstractModOutCadenceBlob other = (AbstractModOutCadenceBlob) obj;
        if (ccdModule != other.ccdModule)
            return false;
        if (ccdOutput != other.ccdOutput)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
            .append("ccdModule", getCcdModule())
            .append("ccdOutput", getCcdOutput())
            .toString();
    }

    public int getCcdModule() {
        return ccdModule;
    }

    public int getCcdOutput() {
        return ccdOutput;
    }

    private void setModuleOutput(int ccdModule, int ccdOutput) {

        if (!FcConstants.validCcdModule(ccdModule)) {
            throw new IllegalArgumentException("invalid ccdModule: "
                + ccdModule);
        }
        if (!FcConstants.validCcdOutput(ccdOutput)) {
            throw new IllegalArgumentException("invalid ccdOutput: "
                + ccdOutput);
        }

        this.ccdModule = ccdModule;
        this.ccdOutput = ccdOutput;
    }
}
