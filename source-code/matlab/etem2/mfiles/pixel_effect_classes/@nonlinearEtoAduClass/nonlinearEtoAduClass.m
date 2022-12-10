function nonlinearEtoAObject = nonlinearEtoAduClass(nonlinearEtoAData, runParamsObject)
%
% Copyright 2017 United States Government as represented by the
% Administrator of the National Aeronautics and Space Administration.
% All Rights Reserved.
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

module = get(runParamsObject, 'moduleNumber');
output = get(runParamsObject, 'outputNumber');
runStartMjd = datestr2mjd(get(runParamsObject, 'runStartDate'));
runEndMjd = runStartMjd + get(runParamsObject, 'runDurationDays');
exposuresPerCadence = get(runParamsObject, 'exposuresPerCadence');
numCcdRows = get(runParamsObject, 'numCcdRows');
numCcdCols = get(runParamsObject, 'numCcdCols');

if isempty(nonlinearEtoAData.spatialQeVariation)
    nonlinearEtoAData.spatialQeVariation = 1;
end

if ~isempty(nonlinearEtoAData.timeQeModulationData)
    classString = ...
        ['nonlinearEtoAData.timeQeModulationObject = ' ...
        nonlinearEtoAData.timeQeModulationData.className ...
        '(nonlinearEtoAData.timeQeModulationData, runParamsObject);'];
    classString
    eval(classString);
    clear classString;
else
    nonlinearEtoAData.timeQeModulationObject = [];
end


gainObject = gainClass(retrieve_gain_model(runStartMjd, runEndMjd));
gain = get_gain(gainObject, runStartMjd, module, output);
nonlinearEtoAData.gain = gain;

linearityObject = linearityClass(retrieve_linearity_model(runStartMjd, runEndMjd, module, output));

polyStruct = get_weighted_polyval_struct(linearityObject, runStartMjd, module, output);
nonlinearEtoAData.polyStruct = polyStruct;
nonlinearEtoAData.maxDnPerExposure = double(get_max_domain(linearityObject, runStartMjd, module, output));

% we construct a function giving electrons as a function of DN/exposure
% given by
%
%   v_e- = v_DN * P(v_DN/exposure) * gain
%

valueDnPerExposure = (0:10:nonlinearEtoAData.maxDnPerExposure)';
nonlinearEtoAData.valueDn = valueDnPerExposure * exposuresPerCadence;
nonlinearEtoAData.valueElectrons = nonlinearEtoAData.valueDn .* gain ...
    .* weighted_polyval(valueDnPerExposure, polyStruct);

% compute the full well in electrons from the same formula
maxDnPerExposure = nonlinearEtoAData.maxDnPerExposure;
maxElectronsPerExposureNonLinear = maxDnPerExposure .* gain ...
    .* weighted_polyval(maxDnPerExposure, polyStruct);
maxElectronsPerExposureLinear = maxDnPerExposure .* gain;

nonlinearEtoAData.maxElectronsPerExposure = min([maxElectronsPerExposureNonLinear, ...
    maxElectronsPerExposureLinear]);
disp(['maxElectronsPerExposure = ' num2str(nonlinearEtoAData.maxElectronsPerExposure)]);

% instantiate the class
nonlinearEtoAObject = class(nonlinearEtoAData, 'nonlinearEtoAduClass', runParamsObject);

