function [cotrendedFluxTimeSeries, fittedFluxTimeSeries, ...
saturationSegmentsStruct, shortTimeScalePowerRatio] = ...
correct_systematic_error_for_target_table(targetTableId, ...
conditionedAncillaryDataArray, targetDataStruct, ...
ancillaryDesignMatrixConfigurationStruct, pdcModuleParameters, ...
saturationSegmentConfigurationStruct, gapFillParametersStruct, ...
restoreMeanFlag, dataAnomalyIndicators)
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
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function [cotrendedFluxTimeSeries, fittedFluxTimeSeries, ...
% saturationSegmentsStruct, shortTimeScalePowerRatio] = ...
% correct_systematic_error_for_target_table(targetTableId, ...
% conditionedAncillaryDataArray, targetDataStruct, ...
% ancillaryDesignMatrixConfigurationStruct, pdcModuleParameters, ...
% saturationSegmentConfigurationStruct, gapFillParametersStruct, ...
% restoreMeanFlag, dataAnomalyIndicators)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% This function corrects systematic errors in pixel, centroid and flux
% time series for the given target table by cotrending with conditioned
% ancillary data (gap filled and resampled). The cotrending is performed by
% either robust fit or singular value decomposition and least squares
% projection. The cotrended flux time series (from which the systematic
% trend has been removed), the fitted flux time series (representing the
% nonlinear trend due to systematic errors) and the uncertainties in the
% cotrended flux time series are returned in the output structures of this
% function. The uncertainties are obtained through standard propagation of
% error analysis.
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


% Get the number of targets and cadences, and initialize the output time
% series. Return empty structs/struct arrays if there are no targets.
nTargets = length(targetDataStruct);

if nTargets == 0
    cotrendedFluxTimeSeries = [];
    fittedFluxTimeSeries = [];
    saturationSegmentsStruct = [];
    shortTimeScalePowerRatio = [];
    return
end % if

nCadences = length(targetDataStruct(1).values);

timeSeries = repmat(struct( ...
    'values', zeros([nCadences, 1]), ...
    'uncertainties', zeros([nCadences, 1]), ...
    'gapIndicators', true([nCadences, 1])), [1, nTargets]);

cotrendedFluxTimeSeries = timeSeries;
fittedFluxTimeSeries = timeSeries;

for iTarget = 1 : nTargets
    cotrendedFluxTimeSeries(iTarget).values = ...
        targetDataStruct(iTarget).values;
    cotrendedFluxTimeSeries(iTarget).uncertainties = ...
        targetDataStruct(iTarget).uncertainties;
    cotrendedFluxTimeSeries(iTarget).gapIndicators = ...
        targetDataStruct(iTarget).gapIndicators;
    fittedFluxTimeSeries(iTarget).gapIndicators = ...
        targetDataStruct(iTarget).gapIndicators;
end % for iTarget

% Get the conditioned ancillary data for the given target table, and select
% the range of data anomaly indicators.
targetTableIds = [conditionedAncillaryDataArray.targetTableId];
iTable = find(targetTableId == targetTableIds);

conditionedAncillaryDataStruct = ...
    conditionedAncillaryDataArray(iTable).conditionedAncillaryDataStruct;

startCadenceRelative = ...
    conditionedAncillaryDataArray(iTable).startCadenceRelative;
endCadenceRelative = ...
    conditionedAncillaryDataArray(iTable).endCadenceRelative;
cadenceRangeForTimeSeries = startCadenceRelative : endCadenceRelative;

dataAnomalyIndicators.attitudeTweakIndicators = ...
    dataAnomalyIndicators.attitudeTweakIndicators(cadenceRangeForTimeSeries);
dataAnomalyIndicators.safeModeIndicators = ...
    dataAnomalyIndicators.safeModeIndicators(cadenceRangeForTimeSeries);
dataAnomalyIndicators.earthPointIndicators = ...
    dataAnomalyIndicators.earthPointIndicators(cadenceRangeForTimeSeries);
dataAnomalyIndicators.coarsePointIndicators = ...
    dataAnomalyIndicators.coarsePointIndicators(cadenceRangeForTimeSeries);
dataAnomalyIndicators.argabrighteningIndicators = ...
    dataAnomalyIndicators.argabrighteningIndicators(cadenceRangeForTimeSeries);
dataAnomalyIndicators.excludeIndicators = ...
    dataAnomalyIndicators.excludeIndicators(cadenceRangeForTimeSeries);
dataAnomalyIndicators.planetSearchExcludeIndicators = ...
    dataAnomalyIndicators.planetSearchExcludeIndicators(cadenceRangeForTimeSeries);

% Ensure that the target data is gapped for invalid science cadences at the
% start or end of the target table. This will not otherwise be the case if
% the target data struct contains filled flux. There is no conditioned
% ancillary data for such cadences.
firstScienceCadenceIndex = ...
    conditionedAncillaryDataArray(iTable).firstScienceCadenceIndex;
lastScienceCadenceIndex = ...
    conditionedAncillaryDataArray(iTable).lastScienceCadenceIndex;
cadenceRangeForTable = firstScienceCadenceIndex : lastScienceCadenceIndex;

isValidScienceCadence = false([1, nCadences]);
isValidScienceCadence(cadenceRangeForTable) = true;

for iTarget = 1 : nTargets
    targetDataStruct(iTarget).values(~isValidScienceCadence) = 0;
    targetDataStruct(iTarget).uncertainties(~isValidScienceCadence) = 0;
    targetDataStruct(iTarget).gapIndicators(~isValidScienceCadence) = true;
end % for iTarget

% Correct the systematic errors and update the output variables in the
% valid science cadence range.
[cotrendedFluxForTargetTable, fittedFluxForTargetTable, ...
    saturationSegmentsStruct, shortTimeScalePowerRatio] = ...
    correct_systematic_error(conditionedAncillaryDataStruct, targetDataStruct, ...
    ancillaryDesignMatrixConfigurationStruct, pdcModuleParameters, ...
    saturationSegmentConfigurationStruct, gapFillParametersStruct, ...
    restoreMeanFlag, dataAnomalyIndicators);

[cotrendedFluxTimeSeries] = ...
        merge_segment_for_target_table(cotrendedFluxForTargetTable, ...
        cotrendedFluxTimeSeries, cadenceRangeForTable, cadenceRangeForTable);
[fittedFluxTimeSeries] = ...
        merge_segment_for_target_table(fittedFluxForTargetTable, ...
        fittedFluxTimeSeries, cadenceRangeForTable, cadenceRangeForTable);
    
% Return.
return
