function self = test_model_function_geometric_model( self )
%
% test_model_function_geometric_model -- test the model_function method of the transitFitClass with geometric transit model
%
% This unit test exercises the functionality of the transitFitClass model_function method.
% In particular, it tests the following functionality:
%
% ==> For fitTypes 12 and 14, the method takes a correctly-formed vector of parameters and does something sensible with them
% ==> The method does the correct things with "internal" and "external" configurations of the minImpactParameter.
%
% This test is intended to be executed in the mlunit context.  For standalone execution use the following syntax:
%
%      run(text_test_runner, testTransitFitClass('test_model_function_geometric_model'));
%
% Version date:  2011-April-20-20.
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
%    2011-April-20, JL:
%        update to support DV 7.0
%    2010-May-02, PT:
%        test case in which cadencesUsed argument is supplied.
%    2010-January-10, PT:
%        test error which is thrown when fit timeout datenum is exceeded.
%
%=========================================================================================

  disp(' ');
  disp('... testing model function method with geometric transit model ... ');
  disp(' ');

% perform fit-free initialization

  doFit = false ;
  testTransitFitGeometricClass_initialization;
  
% construct a parameter array

  planetModel    = get( get( transitFitObject1, 'transitGeneratorObject' ), 'planetModel' );
  parameterArray = [planetModel.transitEpochBkjd; ...
                    planetModel.ratioPlanetRadiusToStarRadius; ...
                    planetModel.ratioSemiMajorAxisToStarRadius; ...
                    planetModel.minImpactParameter; ...
                    planetModel.orbitalPeriodDays];
                
% get the model values, setting the external/internal flag to both possible values, and see that there is no impact on the model values when a fitType 12 object is used

  modelValues1 = model_function( transitFitObject1, parameterArray, true,  true );
  modelValues2 = model_function( transitFitObject1, parameterArray, false, true );
  assert_not_equals( modelValues1, modelValues2, 'Model values not changed under internal/external parameter flag for fitType 12' );
  
% test the method for fitType 2

  transitFitObject2 = transitFitClass( transitFitStruct, 14 );
  modelValues1 = model_function( transitFitObject2, parameterArray, true,  true );
  modelValues2 = model_function( transitFitObject2, parameterArray, false, true );
  assert_not_equals( modelValues1, modelValues2, 'Model values not changed under internal/external parameter flag for fitType 14' );
  
% For external parameters, the impact parameter can exceed 1

  parameterArray(4) = 2;
  modelValues3 = model_function( transitFitObject1, parameterArray, false );
  
% user can specify which cadences they want the model function to return

  cadencesUsed = true( size( modelValues3 ) );
  cadencesUsed(1:500) = false;
  cadencesUsed(250)   = true;
  modelValues4 = model_function( transitFitObject1, parameterArray, false, true, cadencesUsed );
  
  assert_equals( modelValues4, modelValues3(cadencesUsed), 'Model values not correct when cadence list supplied' );
  
% when the timeout is exceeded, model function throws an error -- check that now

  transitFitStruct3 = get( transitFitObject1, '*' );
  transitFitStruct3.fitTimeoutDatenum = datenum(now) - 1;
  transitFitObject3 = transitFitClass( transitFitStruct3 );
  try_to_catch_error_condition( 'modelValues4=model_function(transitFitObject3,parameterArray,false, false)', 'fitTimeLimitExceeded', 'caller' );
  
  disp(' ');
  
return

% and that's it!

%
%
%
