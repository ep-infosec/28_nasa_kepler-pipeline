function verify_sc_127pdc1(flightDataDirString)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function verify_sc_127pdc1
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%
% SOC127
%   The SOC shall optimize SNR by reducing systematic errors to the limit of
%   stochastic noise for errors with time-scales less than three days.
%
% 127.PDC.1
%   PDC shall perform standard propagation of errors to generate uncertainties
%   in Corrected Flux time series.
%
%
% Plot uncertainties in the corrected flux time series for all targets in
% the TC04 (DV, JT on / SV, AP off). Then produce a scatter plot showing
% the standard deviation in the corrected flux vs the RMS uncertainty in
% the corrected flux for all targets. Repeat for the TC05 (everything on)
% data set.
%
%
% flightDataDirString
%
% ex. /path/to/flight/q2/i956/pdc-matlab-956-22686
%     /path/to/flight/q2/i956/pdc-matlab-956-22692
%     /path/to/flight/q2/i956/pdc-matlab-956-22703
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

SCRATCHDIR = '/path/to/matlab/pdc/test/';
cd(SCRATCHDIR);

invocation = 0;
fileName = ['pdc-outputs-', num2str(invocation), '.mat'];

if nargin==1
    cd(flightDataDirString);
    load(fileName);
    cd(SCRATCHDIR);
else
    load(fileName);
end

pdcResultsStruct = outputsStruct;
clear outputsStruct

correctedFluxTimeSeries = ...
    [pdcResultsStruct.targetResultsStruct.correctedFluxTimeSeries];
correctedFluxValues = [correctedFluxTimeSeries.values];
correctedFluxUncertainties = [correctedFluxTimeSeries.uncertainties];
correctedGapArray = [correctedFluxTimeSeries.gapIndicators];
clear pdcResultsStruct


if ~isempty(correctedFluxUncertainties)
    display('Field exists:  pdcResultsStruct.targetResultsStruct(i).correctedFluxTimeSeries.uncertainties')
    display(' ')
else
    display('Field does not exist!: pdcResultsStruct.targetResultsStruct(i).correctedFluxTimeSeries.uncertainties')
    display(' ')
end

correctedFluxUncertainties(correctedGapArray) = NaN;

figure
plot(correctedFluxUncertainties);
title('[PDC] Corrected Flux Uncertainties');
xlabel('Cadence');
ylabel('Uncertainty (e-)');


% create figures
printJpgFlag = false;
paperOrientationFlag = false;
includeTimeFlag      = false;
fileNameStr = 'sc_req_127pdc1_figure1';
plot_to_file(fileNameStr, paperOrientationFlag, includeTimeFlag, printJpgFlag);


nTargets = length(correctedFluxTimeSeries);
sigmaCorrectedFlux = zeros([nTargets, 1]);
rmsUncertainties = zeros([nTargets, 1]);
for iTarget = 1 : nTargets
    targetFlux = correctedFluxValues( : , iTarget);
    targetUncertainties = correctedFluxUncertainties( : , iTarget);
    targetGaps = correctedGapArray( : , iTarget);
    sigmaCorrectedFlux(iTarget) = std(targetFlux(~targetGaps));
    rmsUncertainties(iTarget) = ...
        sqrt(mean(targetUncertainties(~targetGaps) .^ 2));
end

isValid = sum(~correctedGapArray, 1)' > 1;
sigmaCorrectedFlux(~isValid) = 0;
rmsUncertainties(~isValid) = 0;

figure;
loglog(rmsUncertainties(isValid), sigmaCorrectedFlux(isValid), '.');
hold on
x = axis;
maxAxis = max(x);
minAxis = min(x);
loglog([minAxis; maxAxis], [minAxis; maxAxis], 'k');
hold off
title('[PDC] RMS Flux Uncertainties & Flux Sigmas')
xlabel('RMS Uncertainty (e-)');
ylabel('Flux Sigma (e-)');
grid

fileNameStr = 'sc_req_127pdc1_figure2';
plot_to_file(fileNameStr, paperOrientationFlag, includeTimeFlag, printJpgFlag);


return
