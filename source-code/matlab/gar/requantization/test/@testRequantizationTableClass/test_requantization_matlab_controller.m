function self = test_requantization_matlab_controller(self)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function self = test_requantization_matlab_controller(self)
% This test loads stored verified results and also loads the same input
% histograms and compares the generated results with the verified results.
%
%
% If the regression test fails, an error condition occurs.
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%
%  Use a test runner to run the test method:
%  Example: run(text_test_runner, testRequantizationTableClass('test_requantization_matlab_controller'));
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

% Set path to unit test inputs.
initialize_soc_variables;
path = fullfile(socTestDataRoot, 'gar', 'unit-tests', 'requantization');

load(fullfile(path, 'requantizationInputStruct.mat'));

inputFileName = 'inputs-0.bin';
outputFileName = 'outputs-0.bin';

write_RequantInputs(inputFileName, requantizationInputStruct);
requantizationInputStructNew = read_RequantInputs(inputFileName);
delete(inputFileName);



% 2 asserts to check the equality of (1) input structures created versus
% written and read back from file
% (2) output structures received versus stored and read back from the file


status  = compare_structs_to_within_single_precision(requantizationInputStruct, requantizationInputStructNew);

messageOut = 'requantization_matlab_controller - data generated and read back by  read_RequantizationInputs are not identical!';
assert_equals(true, status, messageOut);



% array of strings is a column vector of cell strings
[requantizationResultsStruct] = requantization_matlab_controller(requantizationInputStructNew);

write_RequantOutputs(outputFileName, requantizationResultsStruct);
requantizationResultsStructNew = read_RequantOutputs(outputFileName);
delete(outputFileName);


status  = compare_structs_to_within_single_precision(requantizationResultsStruct, requantizationResultsStructNew);
% requantizationResultsStruct has lots of fields not returned to the java
% side - so remove them before making a comparison


messageOut = 'requantization_matlab_controller - results received and read back by read_RequantizationOutputs are not identical!';
assert_equals(true, status, messageOut);

return