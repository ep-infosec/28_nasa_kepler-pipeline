function self = test_for_illegal_inputs(self)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function self = test_for_illegal_inputs(self)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% This method tests the received file extractor for illegal inputs.
%
%  Example
%  =======
%  Use a test runner to run the test method:
%      run(text_test_runner, testReceivedFileClass('test_for_illegal_inputs'));
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%
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

display('Test received file extractor for illegal inputs');

messageOut = 'Test failed - Error was not caught for illegal inputs!';

% Test MATLAB wrapper for illegal inputs

for iTest=1:8

    try
        % Ignore the returned value
        switch (iTest)
            case 1                              % startMjd is a vector
                dispatcherType = 'CONFIG_MAP';
                startMjd = [54590.0 54591.0];
                endMjd   = 54595.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 2                              % endMjd is a vector
                dispatcherType = 'CONFIG_MAP';
                startMjd = 54590.0;
                endMjd   = [54595.0 54596.0];
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 3                             % startMjd is negative
                dispatcherType = 'CONFIG_MAP';
                startMjd = -54590.0;
                endMjd   =  54595.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 4                             % endMjd is negative
                dispatcherType = 'CONFIG_MAP';
                startMjd =  54590.0;
                endMjd   = -54595.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 5                             % startMjd is too large
                dispatcherType = 'CONFIG_MAP';
                startMjd = 64590.0;
                endMjd   = 64595.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 6                             % endMjd is too large
                dispatcherType = 'CONFIG_MAP';
                startMjd = 54590.0;
                endMjd   = 64595.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 7                             % startMjd is greater than endMjd
                dispatcherType = 'CONFIG_MAP';
                startMjd = 54595.0;
                endMjd   = 54590.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
            case 8                             % illegal dispatcherType
                dispatcherType = 'INVALID_DISPATCHER_TYPE';
                startMjd = 54590.0;
                endMjd   = 54595.0;
                retrievedReceivedFiles = retrieve_received_file(dispatcherType, startMjd, endMjd);
        end
    catch
        % Test passed, input validation failed
        err = lasterror;
        if( isempty(findstr(err.identifier, 'invalidInput')) )
            assert_equals(1, 0, messageOut);
        end
    end

end
   
% Test receivedFileClass constructor for illegal inputs

dispatcherType = 'CONFIG_MAP';
receivedFiles = retrieve_received_file(dispatcherType);
receivedFile  = receivedFiles(1);

fieldsAndBounds = cell(2,4);
fieldsAndBounds( 1,:) = { 'mjdSocIngestTime';       '>= 54000'; '<= 64000'; []};
fieldsAndBounds( 2,:) = { 'dispatcherType';         [];         [];         {'LONG_CADENCE_PIXEL', 'SHORT_CADENCE_PIXEL', 'GAP_REPORT', 'CONFIG_MAP', 'REF_PIXEL', ...
                                                                             'LONG_CADENCE_TARGET_PMRF', 'SHORT_CADENCE_TARGET_PMRF', 'BACKGROUND_PRMF', ...
                                                                             'LONG_CADENCE_COLLATERAL_PMRF', 'SHORT_CADENCE_COLLATERAL_PMRF', 'HISTOGRAM', ...
                                                                             'ANCILLARY', 'EPHEMERIS', 'SCLK', 'CRCT', 'FFI', 'HISTORY'}};
assign_illegal_value_and_test_for_failure(receivedFile, 'receivedFile', receivedFile, 'receivedFile', 'receivedFileClass', fieldsAndBounds);

return
