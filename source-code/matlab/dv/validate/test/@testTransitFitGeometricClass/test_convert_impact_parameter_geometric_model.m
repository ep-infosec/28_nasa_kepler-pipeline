function self = test_convert_impact_parameter_geometric_model( self )
%
% test_convert_impact_parameter_geometric_model -- unit test for convert_impact_parameter method of transitFitClass with geometric transit model
%
% This is a unit test for the convert_impact_parameter private method of the transitFitClass.  The following functionality is tested:
%
% ==> Conversion from bounded impact parameter to unbounded
% ==> conversion from unbounded impact parameter to bounded
% ==> computation of derivatives
% ==> Proper execution of the convert_impact_parameter error statement
%
% This test is intended to be executed in the mlunit context.  For standalone execution use the following syntax:
%
%      run(text_test_runner, testTransitFitClass('test_convert_impact_parameter_geometric_model'));
%
% Version date:  2011-April-20.
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
%
%=========================================================================================

  disp(' ');  
  disp('... testing impact parameter conversion method with geometric transit model ... ');
  disp(' ');
  
% Thanks to the brain-deadness of Matlab R2007a object-oriented programming, the tests of the private transitFitClass methods actually have to be performed
% by a public transitFitClass method.  So we can do that now

  doFit = false;
  testTransitFitGeometricClass_initialization;
  
  testResultsStruct = test_private_methods_with_geometric_model( transitFitObject1, 'convert_impact_parameter' );
  
  mlunit_assert( testResultsStruct.direction1Ok, 'Impact parameter bounded-to-unbounded conversion failed' );
  mlunit_assert( testResultsStruct.direction2Ok, 'Impact parameter unbounded-to-bounded conversion failed' );
  assert_equals( testResultsStruct.errorMessage, 'dv:transitFitClass:convertImpactParameter:invalidDirection', 'Impact parameter converter error thrown incorrectly' );
  
  disp(' ');
  
return

% and that's it!

%
%
%

