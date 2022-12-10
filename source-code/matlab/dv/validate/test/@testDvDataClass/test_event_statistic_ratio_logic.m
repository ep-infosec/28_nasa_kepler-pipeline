function self = test_event_statistic_ratio_logic( self )
% 
% test_event_statistic_logic -- test the logic related to the ratio of multiple event
% statistic to single event statistic in the planet search method of dvDataClass
%
% This unit test exercises the following functionality:
%
% ==> When MES / SES is less than the specified ratio, the fitter is not called and
%     appropriate alerts are issued and flags set
% ==> The MES / SES test calls TPS and obtains fresh values of MES and SES, ignoring the
%     values in the TCE sent in the DV inputs.
%
% This test is intended to be executed in the mlunit context.  For standalone execution
% use the following syntax:
%
%      run(text_test_runner, testDvDataClass('test_event_statistic_ratio_logic'));
%
% Version date:  2009-December-18.
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

  disp('... testing MES / SES ratio logic ... ')
  
  testDvDataClass_fitter_initialization ;

% set the ratio to be absurdly high, so that the test will fail

  dvDataStruct.planetFitConfigurationStruct.minEventStatisticRatio = 100 ;
  dvDataObject = dvDataClass(dvDataStruct) ;
  dvResultsStruct = perform_dv_planet_search_and_model_fitting( dvDataObject, ...
      dvResultsStructBeforeFit ) ;
  
% The statisticRatioBelowThreshold flag should be set, and the transit fit structs should
% not be changed from their values at entry to the method

  planetResultsStruct = dvResultsStruct.targetResultsStruct.planetResultsStruct ;
  planetResultsStructOld = dvResultsStructBeforeFit.targetResultsStruct.planetResultsStruct ;
  
  mlunit_assert( planetResultsStruct.planetCandidate.statisticRatioBelowThreshold, ...
      'Flag statisticRatioBelowThreshold not set!' ) ;
  assert_equals( planetResultsStruct.allTransitsFit, ...
      planetResultsStructOld.allTransitsFit, ...
      'Struct allTransitsFit changed when it should not be!' ) ;
  assert_equals( planetResultsStruct.oddTransitsFit, ...
      planetResultsStructOld.oddTransitsFit, ...
      'Struct oddTransitsFit changed when it should not be!' ) ;
  assert_equals( planetResultsStruct.evenTransitsFit, ...
      planetResultsStructOld.evenTransitsFit, ...
      'Struct evenTransitsFit changed when it should not be!' ) ;
  
% the results struct should contain 1 alert, which mentions the threshold and the event
% statistic ratio

  alerts = dvResultsStruct.alerts ;
  mlunit_assert( length( alerts ) == 1, ...
      'Alerts struct has wrong length!' ) ;
  mlunit_assert( ~isempty( strfind( alerts.message, 'Event statistic ratio' ) ) && ...
      ~isempty( strfind( alerts.message, 'threshold' ) ), ...
      'Expected alert not found!' ) ;

% change the dvDataStruct TCE to conform to the ratio above, and see that the ratio test
% is still failed (because the method calls TPS itself to get MES and SES)

  dvDataStruct.targetStruct.thresholdCrossingEvent.maxSingleEventSigma = ...
      dvDataStruct.targetStruct.thresholdCrossingEvent.maxMultipleEventSigma / 101 ;
  dvDataObject = dvDataClass( dvDataStruct ) ;
  dvResultsStruct = perform_dv_planet_search_and_model_fitting( dvDataObject, ...
      dvResultsStructBeforeFit ) ;
  planetResultsStruct = dvResultsStruct.targetResultsStruct.planetResultsStruct ;
  mlunit_assert( planetResultsStruct.planetCandidate.statisticRatioBelowThreshold, ...
      'Flag statisticRatioBelowThreshold not set for altered MES / SES in dvDataObject!' ) ;

return

% and that's it!

%
%
%
