function introduce_gaps_in_pdq_pixel_data_same_cadence(s, gapPercentage)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function generate_montecarlo_pixel_data_one_cadence_n_times(turnOffNoiseFlag, useOneModOutFlag)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%-------------------------------------------
% run test data generation
%-------------------------------------------
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

% now extract target pixels, background pixels, collateral pixels
% from this FFI and fill the appropriate input structure fields

% stellar targets
nStellarTargets = length(s.stellarPdqTargets);
nBkgdTargets = length(s.backgroundPdqTargets);
nCollateralTargets = length(s.collateralPdqTargets);

nCadences = length(s.stellarPdqTargets(1).referencePixels(1).gapIndicators);
nGappedCadences = round((gapPercentage/100)*nCadences);
gappedCadences = unidrnd(nCadences, 1, nGappedCadences);
s.cadenceTimes(gappedCadences) = [];

for iTarget = 1:nStellarTargets

    nPixelsForThisTarget = length(s.stellarPdqTargets(iTarget).referencePixels);



    for jPixel = 1:nPixelsForThisTarget

        s.stellarPdqTargets(iTarget).referencePixels(jPixel).gapIndicators(gappedCadences) = [];
        s.stellarPdqTargets(iTarget).referencePixels(jPixel).timeSeries(gappedCadences) = [];

    end
end

for iBkgd = 1:nBkgdTargets

    nBgdPixelsForThisBkgd = length(s.backgroundPdqTargets(iBkgd).referencePixels);
    % not comfortable using 'deal' here
    for jPixel = 1:nBgdPixelsForThisBkgd

        s.backgroundPdqTargets(iBkgd).referencePixels(jPixel).gapIndicators(gappedCadences) = []; % no gaps at all for now
        s.backgroundPdqTargets(iBkgd).referencePixels(jPixel).timeSeries(gappedCadences) = []; % no gaps at all for now
    end
end
for iCollateral = 1:nCollateralTargets

    nCollPixels = length(s.collateralPdqTargets(iCollateral).referencePixels);
    for jPixel = 1:nCollPixels

        s.collateralPdqTargets(iCollateral).referencePixels(jPixel).gapIndicators(gappedCadences) = []; % no gaps at all for now
        s.collateralPdqTargets(iCollateral).referencePixels(jPixel).timeSeries(gappedCadences) = []; % no gaps at all for now
    end
end




sFileName = ['pdq_' num2str(gapPercentage) '_percent_gap_pixel_data.mat'];
save(sFileName,'s');





