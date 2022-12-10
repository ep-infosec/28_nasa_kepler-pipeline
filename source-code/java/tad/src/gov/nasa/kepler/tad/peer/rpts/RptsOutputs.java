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

package gov.nasa.kepler.tad.peer.rpts;

import static com.google.common.collect.Lists.newArrayList;
import gov.nasa.kepler.tad.peer.MaskDefinition;
import gov.nasa.kepler.tad.peer.TargetDefinitionStruct;
import gov.nasa.spiffy.common.persistable.Persistable;

import java.util.List;

/**
 * Contains the data that is returned by RPTS MATLAB.
 * 
 * @author Miles Cote
 */
public class RptsOutputs implements Persistable {

    private List<TargetDefinitionStruct> stellarTargetDefinitions = newArrayList();

    private List<TargetDefinitionStruct> dynamicRangeTargetDefinitions = newArrayList();

    private TargetDefinitionStruct backgroundTargetDefinition;
    private List<TargetDefinitionStruct> blackTargetDefinitions = newArrayList();
    private List<TargetDefinitionStruct> smearTargetDefinitions = newArrayList();

    private MaskDefinition backgroundMaskDefinition;
    private MaskDefinition blackMaskDefinition;
    private MaskDefinition smearMaskDefinition;

    public RptsOutputs() {
    }

    public MaskDefinition getBackgroundMaskDefinition() {
        return backgroundMaskDefinition;
    }

    public void setBackgroundMaskDefinition(
        MaskDefinition backgroundMaskDefinition) {
        this.backgroundMaskDefinition = backgroundMaskDefinition;
    }

    public TargetDefinitionStruct getBackgroundTargetDefinition() {
        return backgroundTargetDefinition;
    }

    public void setBackgroundTargetDefinition(
        TargetDefinitionStruct backgroundTargetDefinition) {
        this.backgroundTargetDefinition = backgroundTargetDefinition;
    }

    public MaskDefinition getBlackMaskDefinition() {
        return blackMaskDefinition;
    }

    public void setBlackMaskDefinition(MaskDefinition blackMaskDefinition) {
        this.blackMaskDefinition = blackMaskDefinition;
    }

    public List<TargetDefinitionStruct> getBlackTargetDefinitions() {
        return blackTargetDefinitions;
    }

    public void setBlackTargetDefinitions(
        List<TargetDefinitionStruct> blackTargetDefinitions) {
        this.blackTargetDefinitions = blackTargetDefinitions;
    }

    public List<TargetDefinitionStruct> getDynamicRangeTargetDefinitions() {
        return dynamicRangeTargetDefinitions;
    }

    public void setDynamicRangeTargetDefinitions(
        List<TargetDefinitionStruct> dynamicRangeTargetDefinitions) {
        this.dynamicRangeTargetDefinitions = dynamicRangeTargetDefinitions;
    }

    public MaskDefinition getSmearMaskDefinition() {
        return smearMaskDefinition;
    }

    public void setSmearMaskDefinition(MaskDefinition smearMaskDefinition) {
        this.smearMaskDefinition = smearMaskDefinition;
    }

    public List<TargetDefinitionStruct> getSmearTargetDefinitions() {
        return smearTargetDefinitions;
    }

    public void setSmearTargetDefinitions(
        List<TargetDefinitionStruct> smearTargetDefinitions) {
        this.smearTargetDefinitions = smearTargetDefinitions;
    }

    public List<TargetDefinitionStruct> getStellarTargetDefinitions() {
        return stellarTargetDefinitions;
    }

    public void setStellarTargetDefinitions(
        List<TargetDefinitionStruct> stellarTargetDefinitions) {
        this.stellarTargetDefinitions = stellarTargetDefinitions;
    }

}
