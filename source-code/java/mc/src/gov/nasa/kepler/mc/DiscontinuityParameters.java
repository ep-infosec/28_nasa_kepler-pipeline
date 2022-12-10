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

import org.apache.commons.lang.ArrayUtils;

import gov.nasa.spiffy.common.persistable.Persistable;
import gov.nasa.spiffy.common.pi.Parameters;

/**
 * 
 * @author Forrest Girouard
 */
public class DiscontinuityParameters implements Parameters, Persistable {

    private float[] discontinuityModel = ArrayUtils.EMPTY_FLOAT_ARRAY;

    private int medianWindowLength;
    private int savitzkyGolayFilterLength;
    private int savitzkyGolayPolyOrder;
    private float discontinuityThresholdInSigma;
    private float ruleOutTransitRatio;
    private int varianceWindowLengthMultiplier;
    private int maxNumberOfUnexplainedDiscontinuities;
    
    public DiscontinuityParameters() {
    }

    public float[] getDiscontinuityModel() {
        return discontinuityModel;
    }

    public void setDiscontinuityModel(float[] discontinuityModel) {
        this.discontinuityModel = discontinuityModel;
    }

    public int getMedianWindowLength() {
        return medianWindowLength;
    }

    public void setMedianWindowLength(int medianWindowLength) {
        this.medianWindowLength = medianWindowLength;
    }

    public int getSavitzkyGolayFilterLength() {
        return savitzkyGolayFilterLength;
    }

    public void setSavitzkyGolayFilterLength(int savitzkyGolayFilterLength) {
        this.savitzkyGolayFilterLength = savitzkyGolayFilterLength;
    }

    public int getSavitzkyGolayPolyOrder() {
        return savitzkyGolayPolyOrder;
    }

    public void setSavitzkyGolayPolyOrder(int savitzkyGolayPolyOrder) {
        this.savitzkyGolayPolyOrder = savitzkyGolayPolyOrder;
    }

    public float getDiscontinuityThresholdInSigma() {
        return discontinuityThresholdInSigma;
    }

    public void setDiscontinuityThresholdInSigma(float discontinuityThresholdInSigma) {
        this.discontinuityThresholdInSigma = discontinuityThresholdInSigma;
    }

    public float getRuleOutTransitRatio() {
        return ruleOutTransitRatio;
    }

    public void setRuleOutTransitRatio(float ruleOutTransitRatio) {
        this.ruleOutTransitRatio = ruleOutTransitRatio;
    }

    public int getVarianceWindowLengthMultiplier() {
        return varianceWindowLengthMultiplier;
    }

    public void setVarianceWindowLengthMultiplier(int varianceWindowLengthMultiplier) {
        this.varianceWindowLengthMultiplier = varianceWindowLengthMultiplier;
    }

    public int getMaxNumberOfUnexplainedDiscontinuities() {
        return maxNumberOfUnexplainedDiscontinuities;
    }

    public void setMaxNumberOfUnexplainedDiscontinuities(
        int maxNumberOfUnexplainedDiscontinuities) {
        this.maxNumberOfUnexplainedDiscontinuities = maxNumberOfUnexplainedDiscontinuities;
    }

}
