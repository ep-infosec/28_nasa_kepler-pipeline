function make_pa_data_from_etem(locationStr, run, nCadences, nStars, iStarIndex)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function make_pa_data_from_etem(locationStr, run, nCadences)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% locationStr = '\path\to\etem\quarter\1\';
% runStr = '20400'
% make_pa_data_from_etem('\path\to\etem\quarter\1\', 20400,-1,100)%
% usage example: 
% build_time_series('/path/to/ETEM/Results/', 100);
% make_pa_data_from_etem('\path\to\etem\quarter\1', 20400)
% builds various pa input data structures and saves them to a mat file
%
% inputs:
%   locationStr: string with trailing / giving the fully qualified location of the
%       directory containing the runxxx directory with the desired data
%   run: ID # of the desired ETEM run
%   etemFileType: body of the name of the ETEM output .dat file containing
%       the desired data type, e.g. 'long_cadence_q_black_gcr_'
%   nCadences: optional argument: # of cadences to use for this data set.
%       If this is missing the number of cadences in the ETEM run is used.
%   nStars: # of stars containing injected science (optional, set to empty if not used)
%   iStarIndex: indices of desired stars (optional, if both nStars and
%   iStarIndex are set, then iStarIndex takes precedence)
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


if(~exist('nCadences', 'var'))
    nCadences = -1;
end;    
    
if(~exist('nStars', 'var'))
    nStars = -1;
end;    

if(~exist('iStarIndex', 'var'))
    iStarIndex = [];
end;    




runStr = ['run' num2str(run)];

%--------------------------------------------------------------------------
% compute the time series
%--------------------------------------------------------------------------
[targetStarStruct, backgroundStruct, smearStruct, leadingBlackStruct] = ...
    build_time_series(locationStr, ...
    run, 'long_cadence_q_black_gcr_', nCadences,nStars, iStarIndex);



%--------------------------------------------------------------------------
% insert data gaps into the targetStarStruct and backgroundStruct
%--------------------------------------------------------------------------
completeness = 1; % percentage of targets not gapped

[targetStarStruct, backgroundStruct] = ...
    insert_data_gaps(targetStarStruct, backgroundStruct, completeness);

cosmicRayConfigurationStruct = build_cr_configuration_struct();

%--------------------------------------------------------------------------
% simulate results from BPP
%--------------------------------------------------------------------------
backgroundConfigurationStruct = build_background_configuration_struct();

% replace gap values with zero
nPixels = length(backgroundStruct);
for pixel = 1:nPixels 
    gapList = backgroundStruct(pixel).gapList;
    backgroundStruct(pixel).timeSeries(gapList) = 0;    
end

% clean the background
backgroundStruct = clean_cosmic_ray_from_background( ...
    backgroundStruct, cosmicRayConfigurationStruct);

% fit the background
backgroundCoeffStruct = fit_background_by_time_series(backgroundStruct, ...
    backgroundConfigurationStruct);

%--------------------------------------------------------------------------
% simulate results from diareg
%--------------------------------------------------------------------------
% don't bother to clean background and cosmic rays
moduleOutputMotionPolyStruct = module_output_motion(targetStarStruct, 5);


%--------------------------------------------------------------------------
% replace target pixels gap values with zero
%--------------------------------------------------------------------------
nTargets = length(targetStarStruct);
for target = 1:nTargets
    nPixels = length(targetStarStruct(target).pixelTimeSeriesStruct);
   
    for pixel = 1:nPixels 
        gapList = targetStarStruct(target).pixelTimeSeriesStruct(pixel).gapList;
        targetStarStruct(target).pixelTimeSeriesStruct(pixel).timeSeries(gapList) = 0;    
    end
end

nCadences = length(moduleOutputMotionPolyStruct);

filename = ['paOAPTestData_' runStr '_' num2str(nCadences) '_nogaps.mat'];
save(filename, 'targetStarStruct', 'backgroundStruct', 'smearStruct', ...
    'leadingBlackStruct', 'moduleOutputMotionPolyStruct', 'backgroundCoeffStruct');



%--------------------------------------------------------------------------
% add other structures to tppInputStruct
%--------------------------------------------------------------------------
backgroundConfigurationStruct = build_background_configuration_struct();
cosmicRayConfigurationStruct = build_cr_configuration_struct();

tppInputStruct.backgroundCoeffStruct = backgroundCoeffStruct;
tppInputStruct.backgroundStruct = backgroundStruct;
tppInputStruct.targetStarStruct = targetStarStruct;
tppInputStruct.smearStruct = smearStruct;
tppInputStruct.backgroundConfigurationStruct = backgroundConfigurationStruct;
tppInputStruct.cosmicRayConfigurationStruct = cosmicRayConfigurationStruct;
tppInputStruct.motionPolyStruct = moduleOutputMotionPolyStruct;

nCadences = length(tppInputStruct.motionPolyStruct);
tppConfigurationStruct.startCadence = 1;
tppConfigurationStruct.endCadence = nCadences;
tppConfigurationStruct.cadenceType = 1;
tppConfigurationStruct.ccdModule = 2;
tppConfigurationStruct.ccdOutput = 4;
tppConfigurationStruct.cleanCosmicRays = 1;

tppInputStruct.debugFlag = 0;
tppInputStruct.tppConfigurationStruct = tppConfigurationStruct;


gapFillParametersStruct.MAD_X_FACTOR = 10;

gapFillParametersStruct.MAX_GIANT_TRANSIT_DURATION_IN_HOURS = 72;
gapFillParametersStruct.MAX_DETREND_POLY_ORDER = 25;
gapFillParametersStruct.MAX_AR_ORDER_LIMIT = 25;
gapFillParametersStruct.MAX_CORRELATION_WINDOW_X_FACTOR = 5;
gapFillParametersStruct.CADENCE_DURATION_IN_MINUTES = 30;
gapFillParametersStruct.GAP_FILL_MODE_IS_ADD_BACK_PREDICTION_ERROR = true;


tppInputStruct.gapFillParametersStruct = gapFillParametersStruct;


%--------------------------------------------------------------------------
% add ancillary data structure to tppInputStruct
%--------------------------------------------------------------------------
locationStr = '\path\to\etem\quarter\1\';
run = '20400';

runStr =  ['run' num2str(run)];

tnow = datenum(now);
startCadenceTimes = (tnow:(1/48):(tnow + (nCadences-1)/48))'; % turn into a column vector
endCadenceTimes = startCadenceTimes+(1/48);

% (http://www.mathworks.com/support/solutions/data/1-19T9M.html?solution=1-% 19T9M)
% The Julian day number, JD, and MATLAB's datenum, T, are measuring the
% same thing -- number of days (and fractions of days) since some arbitrary
% ancient origin. One is simply an offset of the other. The Julian day
% number starts at noon, while the MATLAB datenum starts at midnight, so
% the offset involves half a day.
%
% Here is the formula, applied to the time when you want to write this:
% JD = T + 1721058.5

% ancillary data
endCadenceTimes = endCadenceTimes + 1721058.5 - 2400000.5;
startCadenceTimes = startCadenceTimes + 1721058.5 - 2400000.5;
ancillaryBeginTime = startCadenceTimes(1) - (1/48)*.5;

ancillaryDataStruct = get_ancillary_data_struct(runStr, locationStr,nCadences, ancillaryBeginTime, startCadenceTimes);

tppInputStruct.ancillaryDataStruct = ancillaryDataStruct;


%--------------------------------------------------------------------------
% add other fields to tppInputStruct
%--------------------------------------------------------------------------

tppInputStruct.motionGaps = [];
tppInputStruct.backgroundGaps = [];
tppInputStruct.startCadenceTimes = startCadenceTimes;
tppInputStruct.endCadenceTimes = endCadenceTimes;
tppInputStruct.gridSize = 3;


save inputs.mat tppInputStruct;

return









