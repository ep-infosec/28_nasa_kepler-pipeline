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

package gov.nasa.kepler.ppa;

import gov.nasa.kepler.fs.api.FloatTimeSeries;
import gov.nasa.kepler.fs.api.FsId;
import gov.nasa.kepler.mc.CompoundTimeSeries;
import gov.nasa.spiffy.common.CompoundFloatTimeSeries;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A time series associated with a particular target.
 * 
 * @author Bill Wohler
 */
public class PpaTargetTimeSeries extends CompoundFloatTimeSeries {

    private int keplerId;

    /**
     * Creates a {@link PpaTargetTimeSeries} object.
     * 
     * @param keplerId the Kepler ID of the target
     * @param valuesFsId the {@link FsId} for the values
     * @param uncertaintiesFsId the {@link FsId} for the uncertainties
     * @param timeSeriesByFsId a map of {@link FsId}s to {@link FloatTimeSeries}
     * @return a {@link PpaTargetTimeSeries}, or {@code null} if either
     * {@code valuesFsId} or {@code uncertaintiesFsId} were not found in the map
     * or referenced an empty time series
     * @throws NullPointerException if any of the arguments are {@code null}
     */
    public static PpaTargetTimeSeries createTargetTimeSeries(int keplerId,
        FsId valuesFsId, FsId uncertaintiesFsId,
        Map<FsId, FloatTimeSeries> timeSeriesByFsId) {

        CompoundFloatTimeSeries compoundFloatTimeSeries = CompoundTimeSeries.getFloatInstance(
            valuesFsId, uncertaintiesFsId, timeSeriesByFsId);

        PpaTargetTimeSeries targetTimeSeries = new PpaTargetTimeSeries();
        targetTimeSeries.setKeplerId(keplerId);
        targetTimeSeries.setValues(compoundFloatTimeSeries.getValues());
        targetTimeSeries.setUncertainties(compoundFloatTimeSeries.getUncertainties());
        targetTimeSeries.setGapIndicators(compoundFloatTimeSeries.getGapIndicators());

        return targetTimeSeries;
    }

    public int getKeplerId() {
        return keplerId;
    }

    public void setKeplerId(int keplerId) {
        this.keplerId = keplerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + keplerId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof PpaTargetTimeSeries)) {
            return false;
        }
        final PpaTargetTimeSeries other = (PpaTargetTimeSeries) obj;
        if (keplerId != other.keplerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
            .append("keplerId", keplerId)
            .toString();
    }
}
