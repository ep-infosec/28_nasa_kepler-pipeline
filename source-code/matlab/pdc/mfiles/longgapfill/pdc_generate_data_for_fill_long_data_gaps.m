function pdcLongGapFillInputs = pdc_generate_data_for_fill_long_data_gaps(runNumber, dirString, nStarsPerCategory, gapScenario, gapConstantsStruct)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function pdcLongGapFillInputs = pdc_generate_data_for_fill_long_data_gaps(runNumber, dirString, nFewerStars, debugLevel)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% dirString = 'C:\path\to\Retrofit_Pipeline\';
% dirString = 'path/to/etem/quarter/1/';
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

% run specific parameters
runString = ['run' num2str(runNumber)];

runparamsLoadString = ['load ' dirString  runString '/run_params_' runString ];
injectScienceLoadString = ['load ' dirString  runString '/inject_science_' runString ];

fluxWithGcrFile = [dirString  runString '/raw_flux_gcr_' runString '.dat' ];


eval(runparamsLoadString);

nStars = run_params.num_targets;

% read flux time series into an array
[flux] = read_rawflux_data( fluxWithGcrFile, nStars);


nCadences = length(flux(:,1));


eval(injectScienceLoadString);


indexE = cat(1,Transiting_Earths.index);
indexE = indexE(1:min(nStarsPerCategory, length(indexE)));
indexJ = cat(1,Transiting_Jupiters.index);
indexJ = indexJ(1:min(nStarsPerCategory, length(indexJ)));
indexS = cat(1,Transiting_Stars.index);
indexS = indexS(1:min(nStarsPerCategory, length(indexS)));
indexR = cat(1, ReflectedLightSignatures.index);
indexR = indexR(1:min(nStarsPerCategory, length(indexR)));
indexSaturated  =  (1:nStarsPerCategory)';

indexTested = unique([indexE; indexJ; indexS; indexR; indexSaturated]);



%indexTested = unique([indexE; indexJ; indexS; indexSaturated]);




nSafeModes = 3;
igap = create_safemode_gaps(nSafeModes,(1:nCadences)', gapConstantsStruct);

pdcLongGapFillInputs.flux = flux(:, indexTested);



nTimeSeries = length(indexTested);
pdcLongGapFillInputs.fluxWithGaps = (flux(:, indexTested).*repmat(~igap,1,nTimeSeries));


[tempVal, indexToWhichFlux] = intersect(indexTested, indexE);
pdcLongGapFillInputs.indexEarths = indexToWhichFlux;

[tempVal, indexToWhichFlux] = intersect(indexTested, indexJ);
pdcLongGapFillInputs.indexJupiters = indexToWhichFlux;

[tempVal, indexToWhichFlux] = intersect(indexTested, indexR);
pdcLongGapFillInputs.indexRefLight = indexToWhichFlux;

[tempVal, indexToWhichFlux] = intersect(indexTested, indexS);
pdcLongGapFillInputs.indexBgStars = indexToWhichFlux;

[tempVal, indexToWhichFlux] = intersect(indexTested, indexSaturated);
pdcLongGapFillInputs.indexSaturated = indexToWhichFlux;


pdcLongGapFillInputs.dataGapIndicators = igap;



%--------------------------------------------------------------------------
function [flux] = read_rawflux_data(fluxWithGcrFile, nStars)
%--------------------------------------------------------------------------
fid = fopen(fluxWithGcrFile,'r','ieee-le');

flux = fread(fid,[nStars,inf],'float32');
fclose(fid);
flux = flux';
return;

%--------------------------------------------------------------------------
function gapIndicator = create_safemode_gaps(nSafeModes,cadenceNumbers, constantsStruct)
%--------------------------------------------------------------------------

nCadences =   length(cadenceNumbers);
gapIndicator = false(nCadences,1);

nCadencePerDay = constantsStruct.nCadencePerDay; % 48 samples per day
daysInAMonth = constantsStruct.daysInAMonth; % 30 days in a month
safeModeDuration = constantsStruct.safeModeDuration; % safe mode lasting 8 days

% place one safemode gap per month in a random manner
contactedMonths = unique(fix(cadenceNumbers/(daysInAMonth*nCadencePerDay)));
nContacts = length(contactedMonths) - 1;
%nContacts = length(contactedMonths) ;

monthsChosenForSafemode = unidrnd(nContacts, nSafeModes, 1);% may return duplicates
monthsChosenForSafemode = sort(monthsChosenForSafemode - 1);

while(length(unique(monthsChosenForSafemode) ) ~= nSafeModes)
    monthsChosenForSafemode = unidrnd(nContacts, nSafeModes, 1); % safemode can occur during the first month
    monthsChosenForSafemode = sort(monthsChosenForSafemode - 1);
end
% locate one safemode anywhere inside a month
for j = 1:nSafeModes

    firstCadenceOfMonth = monthsChosenForSafemode(j)*daysInAMonth*nCadencePerDay+1;

    safeModeGapBegin = firstCadenceOfMonth + nCadencePerDay*5  + unidrnd((daysInAMonth-safeModeDuration)*nCadencePerDay,1,1);
    safeModeGapEnd = safeModeGapBegin + safeModeDuration*nCadencePerDay -1;
    safeModeGapEnd = min(safeModeGapEnd, nCadences);
    gapIndicator(safeModeGapBegin:safeModeGapEnd) = true;
end;


return;



