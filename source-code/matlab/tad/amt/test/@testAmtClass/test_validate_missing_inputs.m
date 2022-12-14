function self = test_validate_missing_inputs(self)
%test_validate_missing_inputs checks whether the class
% constructor catches the missing field and throws an error
%
%
%  Example
%  =======
%  Use a test runner to run the test method:
%         Example: run(text_test_runner, test_sin('test_null'));
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

% get a valid amt input structure
amtParameterStruct = setup_valid_amt_test_struct();

% first we test the high level field names
test_missing_fields(amtParameterStruct, []);
test_missing_fields(amtParameterStruct, 'amtConfigurationStruct');

function test_missing_fields(amtParameterStruct, subStruct);
% get a list of its fields
if isempty(subStruct)
    names = fieldnames(amtParameterStruct);
else
    names = fieldnames(amtParameterStruct.(subStruct));
end
nNames = length(names);
for j=1:nNames

    % create structure with one missing input each time or create the structure
    % whole;
    amtData = amtParameterStruct;

    % now delete jth field and test whether the constructor catches the error
    if isempty(subStruct)
        amtData = rmfield(amtData, names(j));
    else
        amtData.(subStruct) = rmfield(amtData.(subStruct), names(j));
    end

    caughtErrorFlag = 0;
    try
        amtObject = amtClass(amtData);
    catch
        % test passed, input validation failed
        err = lasterror;
        % check to see whether the correct exception/error was
        % thrown
        % parse the string and look for 'missingField' and the name
        % of the field
        if(isempty(findstr(err.identifier, 'missingField')))
            assert_equals('missingField',err.identifier,'Wrong type of error thrown!');
        else
            if(isempty(findstr(err.identifier,char(names(j)))))
                assert_equals(names(j),err.identifier,'Wrong field identified as a missing field' );
            end;
        end;
        caughtErrorFlag = 1;
%         print_error(err, 'amt missing field test');    
    end;
    if(~caughtErrorFlag)
        % optional message is printed only when assert fails
        % test failed, input validation did not catch the error
        assert_equals(1,0, 'Validation failed to catch the error..');
    end;
    clear amtData
end;
