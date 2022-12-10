function self = test_whitener_fitter_iter_limit_geometric_model( self )
%
% test_whitener_fitter_fit_iter_limit_geometric_model -- test perform_iterative_whitening_and_model_fitting method of dvDataClass for cases in which
% the iteration limit is reached
%
% This unit test exercises the following functionality of the dvDataClass method which performs the iterative whitening and fitting of the flux time series:
%
% ==> Exits with an error when the iteration limit is exceeded.
%
% This test is intended to be executed in the mlunit context.  For standalone execution use the following syntax:
%
%      run(text_test_runner, testDvDataGeometricClass('test_whitener_fitter_iter_limit_geometric_model'));
%
% Version date:  2011-May-05.
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
%    2011-May-05, JL:
%        update in support of DV 7.0.
%
%=========================================================================================

  disp(' ');
  disp('... testing iterative whitener-fitter method with geometric transit model, iteration limit reached ... ');
  disp(' ');
  
  testDvDataGeometricClass_fitter_initialization;
  
  % retune the parameters so that the fitter will not converge, and make sure that the correct error is thrown

  dvDataStruct.planetFitConfigurationStruct.convergenceTolerance        = 0.0001;
  dvDataStruct.planetFitConfigurationStruct.whitenerFitterMaxIterations = 2;
  dvDataObject = dvDataClass( dvDataStruct );

% construct folders to contain the plots when they are generated

  dvResultsStruct = create_directories_for_dv_figures( dvDataObject, dvResultsStructBeforeFit );
  
  refTime = clock;  
  try_to_catch_error_condition( ...
      '[dvResultsStruct, converged, secondaryConverged, alertMessageStruct] = perform_iterative_whitening_and_model_fitting(dvDataObject, dvResultsStructBeforeFit, dvDataStruct.targetStruct.thresholdCrossingEvent, 1, 1, 0, inf, refTime);', ...
      'iterationLimitExhausted', 'caller' );

  disp(' ');
  
return

% and that's it!

%
%
%
