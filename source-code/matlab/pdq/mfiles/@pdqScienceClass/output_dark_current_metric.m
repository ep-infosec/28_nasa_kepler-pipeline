function [pdqTempStruct, pdqOutputStruct]  = output_dark_current_metric(pdqScienceObject, pdqTempStruct, pdqOutputStruct, currentModOut)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function [pdqTempStruct, pdqOutputStruct]  =
% output_dark_current_metric(pdqScienceObject, pdqTempStruct,
% pdqOutputStruct, currentModOut)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% This script appends to the dark currents level metric time series (metric
% consisting of median dark currents level for each cadence and associated
% uncertainty)the median dark currents values and their uncertainties
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

% NOTE: median dark current level values are computed in background_correction

% Retrieve the existing dark currents structure if any (will be empty if it does
% not exist
darkCurrents    = pdqScienceObject.inputPdqTsData.pdqModuleOutputTsData(currentModOut).darkCurrents;
nCadences = length(pdqTempStruct.cadenceTimes);


if (isempty(darkCurrents.values))
    
    darkCurrents.values = pdqTempStruct.medianDarkCurrentLevels(:);
    darkCurrents.uncertainties = pdqTempStruct.medianDarkCurrentLevelsUncertainties(:);
    darkCurrents.gapIndicators = false(nCadences,1);

    % set the gap indicators to true wherever the metric = -1;
    metricGapIndex = find(pdqTempStruct.medianDarkCurrentLevels(:) == -1);

    if(~isempty(metricGapIndex))
        darkCurrents.gapIndicators(metricGapIndex) = true;
    end

else
    
    darkCurrents.values = [darkCurrents.values(:); pdqTempStruct.medianDarkCurrentLevels(:)];
    darkCurrents.uncertainties = [darkCurrents.uncertainties(:); pdqTempStruct.medianDarkCurrentLevelsUncertainties(:)];

    gapIndicators = false(nCadences,1);

    % set the gap indicators to true wherever the metric = -1;
    metricGapIndex = find(pdqTempStruct.medianDarkCurrentLevels(:) == -1);

    if(~isempty(metricGapIndex))
        gapIndicators(metricGapIndex) = true;
    end

    darkCurrents.gapIndicators = [darkCurrents.gapIndicators(:); gapIndicators(:)];

    % Sort time series using the time stamps as a guide
    [allTimes sortedTimeSeriesIndices] = ...
        sort([pdqScienceObject.inputPdqTsData.cadenceTimes(:); ...
        pdqScienceObject.cadenceTimes(:)]);

    darkCurrents.values = darkCurrents.values(sortedTimeSeriesIndices);
    darkCurrents.uncertainties = darkCurrents.uncertainties(sortedTimeSeriesIndices);
    darkCurrents.gapIndicators = darkCurrents.gapIndicators(sortedTimeSeriesIndices);

end
%--------------------------------------------------------------------------
% Save results in pdqOutputStruct
% This is a time series for tracking and trending
%--------------------------------------------------------------------------
pdqOutputStruct.outputPdqTsData.pdqModuleOutputTsData(currentModOut).darkCurrents = darkCurrents;



return

