function self = test_get_transit_object_with_new_star_radius( self ) 
%
% test_get_transit_object_with_new_star_radius -- unit test of
% transitGeneratorCollectionClass method get_transit_object_with_new_star_radius
%
% This unit test exercises the following functionality of the
% transitGeneratorCollectionClass method get_transit_object_with_new_star_radius:
%
% ==> Correct execution for a new radius > the old radius
% ==> Error exit for a new radius < the old radius
% 
% This test is intended to be executed in the mlunit context.  For standalone execution
% use the following syntax:
%
%      run(text_test_runner, testTransitGeneratorCollectionClass('test_get_transit_object_with_new_star_radius'));
%
% Version date:  2010-April-27.
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

  disp('... testing set_star_radius_via_kepler_3 method ... ')
  
% initialize with the correct transit model

  testTransitGeneratorCollectionClass_initialization ;
  transitObject = transitGeneratorCollectionClass( transitModel, 2 ) ;
  
% get the initial star radius

  starRadiusSolarRadii = get( transitObject, 'starRadiusSolarRadii' ) ;
  
% set a new radius into the object

  newRadius = starRadiusSolarRadii(1) * 1.01 ;
  newTransitObject = get_transit_object_with_new_star_radius( transitObject, ...
      newRadius ) ;
  newRadiusVector = get( newTransitObject, 'starRadiusSolarRadii' ) ;
  
% the new radius should agree with the user-set one to the bit

  mlunit_assert( all( newRadiusVector == newRadius ), ...
      'star radii do not agree' ) ;
  
% if we set a smaller radius, then an error should occur in the transitGeneratorClass
% method which underlies the transitGeneratorCollectionClass one
 
  newRadius = starRadiusSolarRadii(1) * 0.99 ;
  try_to_catch_error_condition( ...
      'newTransitObject=get_transit_object_with_new_star_radius(transitObject,newRadius);', ...
      'impactParameterInvalid', 'caller' ) ;
  
  
  disp(' ') ;
  
return

% and that's it!

%
%
%
