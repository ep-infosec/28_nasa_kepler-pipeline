function inputsStruct = tip_convert_92_data_to_93(inputsStruct)
%
% function inputsStruct = tip_convert_92_data_to_93(inputsStruct)
%
% This function converts a SOC 9.2 TIP inputsStruct to one used in the SOC
% 9.3 build.
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

DEFAULT_TIP_INPUT_FILENAME = '';    % no parameter input file

DEFAULT_GENERATING_PARAM_SET = 'sesDurationParamSet';
DEFAULT_RANDOM_PARAM_GEN = true;

PLANET_RADIUS_UPPER = 10;           % Rearth
PLANET_RADIUS_LOWER = 0.5;          % Rearth
ORBITAL_PERIOD_UPPER = 400;         % days
ORBITAL_PERIOD_LOWER = 10;          % days


% update simulation config struct
if isfield(inputsStruct, 'simulatedTransitsConfigurationStruct')
    
    simulatedTransitsConfigurationStruct = inputsStruct.simulatedTransitsConfigurationStruct;
    
    % remove obscolete field
    if isfield(simulatedTransitsConfigurationStruct, 'useExistingPolynomials')        
        simulatedTransitsConfigurationStruct = rmfield(simulatedTransitsConfigurationStruct,'useExistingPolynomials');
    end
    
    % add parameter input filename field
    if ~isfield(simulatedTransitsConfigurationStruct,'parameterInputFilename')
        simulatedTransitsConfigurationStruct.parameterInputFilename = DEFAULT_TIP_INPUT_FILENAME;
    end
    
    % add planet radius limits
    if ~isfield(simulatedTransitsConfigurationStruct, 'inputPlanetRadiusUpperLimit')
        simulatedTransitsConfigurationStruct.inputPlanetRadiusUpperLimit = PLANET_RADIUS_UPPER;
    end
    if ~isfield(simulatedTransitsConfigurationStruct, 'inputPlanetRadiusLowerLimit')
        simulatedTransitsConfigurationStruct.inputPlanetRadiusLowerLimit = PLANET_RADIUS_LOWER;
    end
    
    % add orbital period limits
    if ~isfield(simulatedTransitsConfigurationStruct, 'inputOrbitalPeriodUpperLimit')
        simulatedTransitsConfigurationStruct.inputOrbitalPeriodUpperLimit = ORBITAL_PERIOD_UPPER;
    end
    if ~isfield(simulatedTransitsConfigurationStruct, 'inputOrbitalPeriodLowerLimit')
        simulatedTransitsConfigurationStruct.inputOrbitalPeriodLowerLimit = ORBITAL_PERIOD_LOWER;
    end
    
    % add generating set name
    if ~isfield(simulatedTransitsConfigurationStruct, 'generatingParamSetName')
        simulatedTransitsConfigurationStruct.generatingParamSetName = DEFAULT_GENERATING_PARAM_SET;
    end
    
    % add boolen for random param generation
    if ~isfield(simulatedTransitsConfigurationStruct, 'enableRandomParamGen')
        simulatedTransitsConfigurationStruct.enableRandomParamGen =DEFAULT_RANDOM_PARAM_GEN;
    end  
    
    inputsStruct.simulatedTransitsConfigurationStruct = simulatedTransitsConfigurationStruct;
end
