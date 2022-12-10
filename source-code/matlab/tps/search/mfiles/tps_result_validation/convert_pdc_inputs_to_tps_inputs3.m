% convert_pdc_inputs_to_tps_inputs.m
% ans =
%           keplerId: 12200489
%             kepMag: 13.331000328064
%     crowdingMetric: 0.922071993350983
%        validKepMag: 0
%          fluxValue: [476x1 double]
%        uncertainty: [476x1 double]
%         gapIndices: []
%        fillIndices: []
%     outlierIndices: []
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


% inputsStruct.targetDataStruct(1)
% ans =
%                     values: [476x1 double]
%              gapIndicators: [476x1 logical]
%              uncertainties: [476x1 double]
%                   keplerId: 8078685
%                  keplerMag: 10.96399974823
%     fluxFractionInAperture: 0.978408336639404
%             crowdingMetric: 0.990947782993317

nTargets = length(inputsStruct.targetDataStruct);

tpsInputStruct.tpsTargets = [];
tpsInputStruct.cadenceTimes = inputsStruct.cadenceTimes;


% outputsStruct.targetResultsStruct(1)
% ans =
%                    keplerId: 8077555
%     correctedFluxTimeSeries: [1x1 struct]
%                    outliers: [1x1 struct]
% outputsStruct.targetResultsStruct(1).correctedFluxTimeSeries
% ans =
%            values: [476x1 double]
%     uncertainties: [476x1 double]
%     gapIndicators: [476x1 logical]
%     filledIndices: [4x1 double]
% outputsStruct.targetResultsStruct(1).outliers
% ans =
%      values: 54411768.4238112
%     indices: 334
%%
%%
gapIndices = [4065:4075 8415:8425 11471:11482 12102:12157]';

for iTarget = 1:nTargets
    tpsInputStruct.tpsTargets(iTarget).keplerId = inputsStruct.targetDataStruct(iTarget).keplerId;
    tpsInputStruct.tpsTargets(iTarget).kepMag = inputsStruct.targetDataStruct(iTarget).keplerMag;
    tpsInputStruct.tpsTargets(iTarget).validKepMag = false;

    tpsInputStruct.tpsTargets(iTarget).crowdingMetric = inputsStruct.targetDataStruct(iTarget).crowdingMetric;
    tpsInputStruct.tpsTargets(iTarget).fluxValue = inputsStruct.targetDataStruct(iTarget).values;
    tpsInputStruct.tpsTargets(iTarget).uncertainty = inputsStruct.targetDataStruct(iTarget).uncertainties;

    tpsInputStruct.tpsTargets(iTarget).gapIndices = [find(outputsStruct.targetResultsStruct(iTarget).correctedFluxTimeSeries.gapIndicators) gapIndices];
    
    tpsInputStruct.tpsTargets(iTarget).fillIndices = outputsStruct.targetResultsStruct(iTarget).correctedFluxTimeSeries.filledIndices;
    tpsInputStruct.tpsTargets(iTarget).outlierIndices = outputsStruct.targetResultsStruct(iTarget).outliers.indices;
end


save tpsInputStruct.mat tpsInputStruct;
return
%%
nCadences = length(tpsInputStruct.tpsTargets(iTarget).fluxValue);
for j=1:84, 
    gaps = tpsInputStruct.tpsTargets(j).gapIndices; 
    gapIndicators = false(nCadences,1);
    gapIndicators(gaps) = true;
    plot(find(~gapIndicators), tpsInputStruct.tpsTargets(j).fluxValue(~gapIndicators),'.-'), 
    pause, 
end
