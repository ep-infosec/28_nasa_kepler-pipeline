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

package gov.nasa.kepler.tad.peer.ama;

import static com.google.common.collect.Lists.newArrayList;
import gov.nasa.kepler.common.FcConstants;
import gov.nasa.kepler.tad.peer.AmaModuleParameters;
import gov.nasa.kepler.tad.peer.ApertureStruct;
import gov.nasa.kepler.tad.peer.MaskDefinition;
import gov.nasa.kepler.tad.peer.MaskTableParameters;
import gov.nasa.spiffy.common.persistable.Persistable;

import java.util.List;

/**
 * Contains the data that is required by AMA MATLAB.
 * 
 * @author Miles Cote
 */
public class AmaInputs implements Persistable {

    private List<MaskDefinition> maskDefinitions = newArrayList();

    private List<ApertureStruct> apertureStructs = newArrayList();

    private FcConstants fcConstants = new FcConstants();

    private AmaModuleParameters amaConfigurationStruct;
    private MaskTableParameters maskTableParametersStruct;
    private int debugFlag;

    public AmaInputs() {
    }

    public AmaModuleParameters getAmaConfigurationStruct() {
        return amaConfigurationStruct;
    }

    public void setAmaConfigurationStruct(
        AmaModuleParameters amaConfigurationStruct) {
        this.amaConfigurationStruct = amaConfigurationStruct;
    }

    public MaskTableParameters getMaskTableParametersStruct() {
        return maskTableParametersStruct;
    }

    public void setMaskTableParametersStruct(
        MaskTableParameters maskTableParametersStruct) {
        this.maskTableParametersStruct = maskTableParametersStruct;
    }

    public List<ApertureStruct> getApertureStructs() {
        return apertureStructs;
    }

    public void setApertureStructs(List<ApertureStruct> apertureStructs) {
        this.apertureStructs = apertureStructs;
    }

    public int getDebugFlag() {
        return debugFlag;
    }

    public void setDebugFlag(int debugFlag) {
        this.debugFlag = debugFlag;
    }

    public List<MaskDefinition> getMaskDefinitions() {
        return maskDefinitions;
    }

    public void setMaskDefinitions(List<MaskDefinition> maskDefinitions) {
        this.maskDefinitions = maskDefinitions;
    }

    public FcConstants getFcConstants() {
        return fcConstants;
    }

    public void setFcConstants(FcConstants fcConstants) {
        this.fcConstants = fcConstants;
    }

}
