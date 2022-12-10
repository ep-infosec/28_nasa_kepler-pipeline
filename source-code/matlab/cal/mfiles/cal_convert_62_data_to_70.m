function [ inputsStruct ] = cal_convert_62_data_to_70( inputsStruct )
% function [ inputsStruct ] = cal_convert_62_data_to_70( inputsStruct )
%
% This function converts a SOC 6.2 CAL inputsStruct to one used in the SOC
% 7.0 build by adding the required module parameters hard coded to default
% values as well as the season and oneDBlackBlobs fields. In the case of
% 'short cadence', the oneDBlackBlobs field is populated with the default
% .mat file prooduced in a long cadence cal run.
% 
% Copyright 2017 United States Government as represented by the
% Administrator of the National Aeronautics and Space Administration.
% All Rights Reserved.
% 
% NASA acknowledges the SETI Institute's primary role in authoring and
% producing the Kepler Data Processing Pipeline under Cooperative
% Agreement Nos. NNA04CC63A, NNX07AD96A, NNX07AD98A, NNX11AI13A,
% NNX11AI14A, NNX13AD01A & NNX13AD16A.
% 
% This file is available under the terms of the NASA Open Source Agreement
% (NOSA). You should have received a copy of this agreement with the
% Kepler source code; see the file NASA-OPEN-SOURCE-AGREEMENT.doc.
% 
% No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY
% WARRANTY OF ANY KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY,
% INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE
% WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
% MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM
% INFRINGEMENT, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR
% FREE, OR ANY WARRANTY THAT DOCUMENTATION, IF PROVIDED, WILL CONFORM
% TO THE SUBJECT SOFTWARE. THIS AGREEMENT DOES NOT, IN ANY MANNER,
% CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY PRIOR RECIPIENT
% OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR ANY
% OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.
% FURTHER, GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES
% REGARDING THIRD-PARTY SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE,
% AND DISTRIBUTES IT "AS IS."
% 
% Waiver and Indemnity: RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS
% AGAINST THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND
% SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT. IF RECIPIENT'S USE OF
% THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES, DEMANDS, DAMAGES,
% EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES FROM
% PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT
% SOFTWARE, RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED
% STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY
% PRIOR RECIPIENT, TO THE EXTENT PERMITTED BY LAW. RECIPIENT'S SOLE
% REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE, UNILATERAL
% TERMINATION OF THIS AGREEMENT.
%


DEFAULT_DARK_CURRENT                    = 1.0555;          % e-/s
DEFAULT_MIN_COMPRESSION_CADENCES        = 5;
DEFAULT_N_SIGMA_FFI_OUTLIER_REJECTION   = 2.5;
DEFAULT_SEASON                          = 0;


if( ~isfield(inputsStruct,'season') )

    cadenceMidtimes = inputsStruct.cadenceTimes.midTimestamps;
    cadenceGapIndicators = inputsStruct.cadenceTimes.gapIndicators;
    
    if( ~all(cadenceGapIndicators) )
        s = retrieve_sky_group( inputsStruct.ccdModule, inputsStruct.ccdOutput, cadenceMidtimes(find(~cadenceGapIndicators,1)));
        inputsStruct.season = s.observingSeason;
    else
        inputsStruct.season = DEFAULT_SEASON;
    end
end

if( ~isfield(inputsStruct.moduleParametersStruct,'performExpLc1DblackFit') )
    inputsStruct.moduleParametersStruct.performExpLc1DblackFit            = true;
end
if( ~isfield(inputsStruct.moduleParametersStruct,'performExpSc1DblackFit') )
    inputsStruct.moduleParametersStruct.performExpSc1DblackFit            = true;
end
if( ~isfield(inputsStruct.moduleParametersStruct,'defaultDarkCurrentElectronsPerSec') )
    inputsStruct.moduleParametersStruct.defaultDarkCurrentElectronsPerSec = DEFAULT_DARK_CURRENT;
end
if( ~isfield(inputsStruct.moduleParametersStruct,'minCadencesForCompression') )
    inputsStruct.moduleParametersStruct.minCadencesForCompression         = DEFAULT_MIN_COMPRESSION_CADENCES;
end
if( ~isfield(inputsStruct.moduleParametersStruct,'nSigmaForFfiOutlierRejection') )
    inputsStruct.moduleParametersStruct.nSigmaForFfiOutlierRejection      = DEFAULT_N_SIGMA_FFI_OUTLIER_REJECTION;
end
if( ~isfield(inputsStruct.moduleParametersStruct,'errorOnCoarsePointFfi') )
    inputsStruct.moduleParametersStruct.errorOnCoarsePointFfi             = false;
end

if( strcmpi(inputsStruct.cadenceType,'short') )
    if( ~isfield(inputsStruct,'oneDBlackBlobs') )
        
        cadenceNumbers = inputsStruct.cadenceTimes.cadenceNumbers;
        oneDBlackBlobs.blobIndices      = zeros(size(cadenceNumbers));
        oneDBlackBlobs.gapIndicators    = false(size(cadenceNumbers));
        oneDBlackBlobs.blobFilenames    = {'cal_lc_1D_black_fit.mat'};
        oneDBlackBlobs.startCadence     = cadenceNumbers(1);
        oneDBlackBlobs.endCadence       = cadenceNumbers(end);
        inputsStruct.oneDBlackBlobs     = oneDBlackBlobs;
    end
else
    if( ~isfield(inputsStruct,'oneDBlackBlobs') )
        inputsStruct.oneDBlackBlobs     = [];
    end
end


