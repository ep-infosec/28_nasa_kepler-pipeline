function self = test_perform_quarter_stitching( self )
%
% test_perform_quarter_stitching -- unit test of quarterStitchingClass method
% perform_quarter_stitching
%
% This unit test exercises the perform_quarter_stitching method, which in turn is just a
% list of method calls.  Thus, the test exercises only the most basic functionality, which
% is that execution occurs without errors.  More substantive tests are performed by the
% unit tests of the lower-level methods.
%
% This test is performed in the mlunit context.  For standalone operation, use the
% following syntax:
%
%      run(text_test_runner, testQuarterStitchingClass('test_perform_quarter_stitching'));
%
% Version date:  2010-September-21.
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

% Modification History:
%
%=========================================================================================

  disp(' ... testing main quarter-stitching method ... ') ;
  
% set the test data path and retrieve the input struct 

  tpsDataFile = 'tps-multi-quarter-struct' ;
  tpsDataStructName = 'tpsInputs' ;
  tps_testing_initialization ;
  load( fullfile( testDataPath, 'quarterStitchingClass-struct' ) ) ;
  
% validate the input and update the quarterStitchingStruct with anything
% new that it might need
  
  dummyKeplerId = 0 ;
  nTargets = length(quarterStitchingStruct.timeSeriesStruct) ;
  tpsInputs.tpsTargets = tpsInputs.tpsTargets(1) ;
  tpsInputs.tpsTargets.keplerId = dummyKeplerId ;
  tpsInputs.tpsTargets(1:nTargets) = tpsInputs.tpsTargets;
  tpsInputs = validate_tps_input_structure( tpsInputs ) ;
  
  for i=1:nTargets
      quarterStitchingStruct.timeSeriesStruct(i).keplerId = 0;
  end
  quarterStitchingStruct.gapFillParametersStruct = tpsInputs.gapFillParameters ;
  quarterStitchingStruct.harmonicsIdentificationParametersStruct = tpsInputs.harmonicsIdentificationParameters ;
  quarterStitchingStruct.randStreams = tpsInputs.randStreams ;    
  
% limit execution to the first 10 targets so that it doesn't take forever to run this test

  quarterStitchingStruct.timeSeriesStruct = quarterStitchingStruct.timeSeriesStruct(1:10) ;
  
  quarterStitchingObject = quarterStitchingClass( quarterStitchingStruct ) ;
  quarterStitchingObject = perform_quarter_stitching( quarterStitchingObject ) ;
  
return

% and that's it!

%
%
%

