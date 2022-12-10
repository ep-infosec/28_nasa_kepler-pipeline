function fieldsAndBounds = get_config_struct_fields_and_bounds()
%**************************************************************************  
% function fieldsAndBounds = get_config_struct_fields_and_bounds()
%**************************************************************************  
% Facilitates existing pipeline input validation method. See
% apertureModelClass for up-to-date descriptions of parameters.
%
% INPUTS
%     (none)
%
% OUTPUTS
%     fieldsAndBounds : A cell array suitable for passing to the standard 
%                       SOC validation function: validate_structure().
%**************************************************************************  
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
    fieldsAndBounds = cell(14,4);
    fieldsAndBounds(1,:)  = { 'excludeSnrThreshold'; ['>=  0']; ['< Inf'];  []};
    fieldsAndBounds(2,:)  = { 'lockSnrThreshold';    ['>=  0']; ['< Inf'];  []};
    fieldsAndBounds(3,:)  = { 'amplitudeFitMethod';  []; []; {'bbnnls'; ...
                                                              'lsqnonneg'; ...
                                                              'unconstrained'}};
    fieldsAndBounds(4,:)  = { 'raDecFittingEnabled'; []; []; [true; false]};
    fieldsAndBounds(5,:)  = { 'raDecFitMethod';      []; []; {'nlinfit'; ...
                                                              'lsqnonlin'}};
    fieldsAndBounds(6,:)  = { 'raDecMaxDeltaPixels'; ['>= 0']; ['< Inf']; []};
    fieldsAndBounds(7,:)  = { 'raDecRestoringCoef';  ['>= 0']; ['<  Inf']; []};
    fieldsAndBounds(8,:)  = { 'raDecRepulsiveCoef';  ['>= 0']; ['<  Inf']; []};
    fieldsAndBounds(9,:)  = { 'raDecMaxIter';        ['>= 1']; ['<  Inf']; []};
    fieldsAndBounds(10,:) = { 'raDecTolFun';         ['>  0']; ['< Inf']; []};
    fieldsAndBounds(11,:) = { 'raDecTolX';           ['>  0']; ['< Inf']; []};
    fieldsAndBounds(12,:) = { 'maxDeltaMagnitude';   ['>= 0']; ['< Inf']; []};
    fieldsAndBounds(13,:) = { 'maxNumStars';         ['>= 0']; ['< Inf']; []};
    fieldsAndBounds(14,:) = { 'ukirtMagnitudeThreshold';['>= 0']; ['< Inf']; []};
end

%********************************** EOF ***********************************