function self = test_generate_transit_fit_plots_geometric_model( self ) 
%
% test_generate_transit_fit_plots_geometric_model -- unit test for generate_transit_fit_plots method of transitFitClass with geometric transit model
%
% It tests the following functionality:
%
% ==> That plots are generated with correct names and paths for all valid values of the oddEvenFlag
%
% This test is intended to be executed in the mlunit context.  For standalone execution use the following syntax:
%
%      run(text_test_runner, testTransitFitClass('test_generate_transit_fit_plots_geometric_model'));
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
%    2010-May-07, PT:
%        updated to match current version of the method, which no longer has oddEvenFlag
%        in its signature.
%
%=========================================================================================

  disp(' ');
  disp('... testing transitFitClass master plot generator with geometric transit model ... ');
  disp(' ');
  
  testTransitFitGeometricClass_initialization;
  
% create a folder to put the subfolders into

  testDirName = ['test-', datestr(now,30)];
  mkdir( pwd, testDirName );
  testDir = fullfile(pwd, testDirName);
  
  keplerId = 12345678;
  iPlanet  = 1;
  
% create the necessary subfolders

  planetSearchDirName = ['planet-', num2str(iPlanet, '%02d'), filesep, 'planet-search-and-model-fitting-results'];
  mkdir( testDir, planetSearchDirName );
  planetSearchDir = fullfile(testDir, planetSearchDirName);
  
  allTransitsDirName = 'all-transits-fit';
  mkdir( planetSearchDir, allTransitsDirName );
  allTransitsDir = fullfile( planetSearchDir, allTransitsDirName );
  
  oddTransitsDirName = 'odd-even-transits-fit';
  mkdir( planetSearchDir, oddTransitsDirName );
  oddTransitsDir = fullfile( planetSearchDir, oddTransitsDirName );

% create the figures for the all-transits fit

  load(fullfile(testDataDir,'target-table-data-struct'));
  load(fullfile(testDataDir,'cadence-numbers'));
  generate_transit_fit_plots( transitFitObject1, targetFluxTimeSeries, targetTableDataStruct, cadenceNumbers, testDir, keplerId, iPlanet, true );
  
% check to see that the figures are all present

  figureNameBasis = [num2str(keplerId, '%09d'),'-', num2str(iPlanet, '%02d'),'-all-*.fig'];
  figuresDirResults = dir( fullfile( allTransitsDir, figureNameBasis ) );
  mlunit_assert( length(figuresDirResults) == 8, 'all-transits fit figures not all present in correct directory' );
  
% create the figures for the odd-even transits fit -- this requires making a new transit fit object and performing a new fit

  load(fullfile(testDataDir,'transit-generator-model'));
  transitObject = transitGeneratorCollectionClass( transitModel, 1 );
  transitFitStruct.transitGeneratorObject = transitObject;
  transitFitObject2 = transitFitClass( transitFitStruct, 12 );
  transitFitObject2 = fit_transit( transitFitObject2 );

  generate_transit_fit_plots( transitFitObject2, targetFluxTimeSeries, targetTableDataStruct, cadenceNumbers, testDir, keplerId, iPlanet, true );
  
% check to see that the figures are all present

  figureNameBasis = [num2str(keplerId, '%09d'),'-', num2str(iPlanet, '%02d'),'-odd-even-*.fig'];
  figuresDirResults = dir( fullfile( oddTransitsDir, figureNameBasis ) );
  mlunit_assert( length(figuresDirResults) == 8, 'odd-transits fit figures not all present in correct directory' );
  
% exercise the error statement
  
  gapIndicators = false( size(transitModel.cadenceTimes) );
  filledIndices = [];  
  transitObject = transitGeneratorCollectionClass( transitModel, 2, gapIndicators, filledIndices );
  transitFitStruct.transitGeneratorObject = transitObject;
  transitFitObject3 = transitFitClass( transitFitStruct, 12 );
  errorString = 'generate_transit_fit_plots( transitFitObject3, targetFluxTimeSeries, targetTableDataStruct, cadenceNumbers, testDir, keplerId, iPlanet, true);';
  try_to_catch_error_condition( errorString, 'oddEvenFlagInvalid', 'caller' );
  
% cleanup -- remove the test directory and all subdirs

  rmdir( testDir, 's' );
  pause(1) ;
  disp(' ');
  
return

% and that's it!

%
%
%
