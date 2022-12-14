function self = test_time_series_return_methods( self )
%
% test_time_series_return_methods -- test methods which return harmonic time series and
% harmonic-removed time series methods of the harmonicCorrectionClass
%
% This unit test exercises the following functionality of the harmonicCorrectionClass
%
% ==> When no harmonic correction has been performed, the harmonic time series is a zero
%     vector and the harmonic-removed time series is the original time series
% ==> After successful removal, the RMS of the harmonic-removed time series is smaller
%     than for the original, and the RMS of the harmonic time series is nonzero
% ==> The sum of the harmonic and harmonic-removed time series is the original time series
% ==> When no time series has been set, both methods error out.
%
% This test is intended to be executed in the context of the mlunit framework.  To run the
% individual test, use the following syntax:
%
%      run(text_test_runner, harmonicCorrectionTestClass('test_time_series_return_methods'));
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

%=========================================================================================

  harmonic_correction_test_initialization ;
  obj = harmonicCorrectionClass( harmonicIdentificationParametersStruct ) ;
  
% first test:  both methods should fail when exercised in this state

  testString = 'v=obj.get_harmonic_free_time_series ;' ;
  try_to_catch_error_condition( testString, 'originalFluxTimeSeriesNotSet', 'caller' ) ;
  testString = 'v=obj.get_harmonic_time_series ;' ;
  try_to_catch_error_condition( testString, 'originalFluxTimeSeriesNotSet', 'caller' ) ;
  
% second test:  harmonics are set but no removal has been performed

  obj.set_time_series( fluxValues, sampleIntervalSeconds, gapOrFillIndicators ) ;
  timeSeries = obj.get_harmonic_free_time_series ;
  assert_equals( timeSeries, fluxValues, ...
      'Harmonic free time series not as expected in no-subtraction case!' ) ;
  timeSeries = obj.get_harmonic_time_series ;
  assert_equals( timeSeries, zeros( size( fluxValues ) ), ...
      'Harmonic time series not as expected in no-subtraction case!' ) ;
  
% Third test:  after harmonic removal

  obj.add_harmonics ;
  timeSeries = obj.get_harmonic_free_time_series ;
  mlunit_assert( std( timeSeries ) < std( fluxValues ), ...
      'Harmonic removal does not reduce RMS!' ) ;
  timeSeries = obj.get_harmonic_time_series ;
  mlunit_assert( std( timeSeries ) > 0, ...
      'Harmonic time series has zero RMS!' ) ;
  timeSeries = obj.get_harmonic_free_time_series + obj.get_harmonic_time_series ;
  mlunit_assert( max(abs(timeSeries-fluxValues)) < 1e-19, ...
      'Sum of time series not correct in harmonic removal case!' ) ;
  
return

