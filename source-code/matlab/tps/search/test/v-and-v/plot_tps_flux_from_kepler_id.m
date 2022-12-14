%=========================================================================================
% function plot_tps_flux_from_kepler_id( tceStruct, keplerIdList, koiPeriods, tpsPeriods, ...
%    medianCorrectionFlag, iKeplerId )
%
% plot_tps_flux_from_kepler_id( tceStruct, keplerIdList, koiPeriods, tpsPeriods, 
%     medianCorrectionFlag, iKeplerId) -- drill down into the TPS task files and plot the
%     input flux and detrended stitched flux for the target which has Kepler ID ==
%     keplerIdList( iKeplerId ).  If iKeplerId is missing or empty, use keplerIdList(1).
%     If koiPeriods and tpsPeriods are provided, include the relevant periods in the plot
%     title.  If medianCorrectionFlag is present, use it to decide whether to perform
%     quarter-by-quarter median correction (default is no correction).
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
function plot_tps_flux_from_kepler_id( tceStruct, keplerIdList, koiPeriods, tpsPeriods, ...
    medianCorrectionFlag, iKeplerId )

% manage optional arguments

  if ~exist('iKeplerId','var') || isempty( iKeplerId )
      iKeplerId = 1 ;
  end
  keplerId = keplerIdList( iKeplerId ) ;
  if exist('koiPeriods','var') && ~isempty( koiPeriods )
      koiPeriodText = [', KOI Period = ', num2str(koiPeriods(iKeplerId))] ;
  else
      koiPeriodText = '' ;
  end
  if exist('tpsPeriods','var') && ~isempty( tpsPeriods )
      tpsPeriodText = [', TPS Period = ', num2str(tpsPeriods(iKeplerId))] ;
  else
      tpsPeriodText = '' ;
  end
  if ~exist('medianCorrectionFlag', 'var') || isempty( medianCorrectionFlag )
      medianCorrectionFlag = false ;
  end
  
% get the TPS input struct

  tpsTaskFile = get_tps_struct_by_kepid_from_task_dir_tree( tceStruct, keplerId, ...
      'input', false ) ;
  
% compute the timestamps

  timeStampsKjd = get_midTimestamps_filling_gaps( tpsTaskFile.cadenceTimes ) - ...
      kjd_offset_from_mjd ;
  
% determine the filled and normal cadences (we ignore the gapped ones)

  [gapIndicators, fillIndicators, normalIndicators] = get_indicators( ...
      tpsTaskFile ) ;
  [normalChunks, fillChunks] = get_normal_and_fill_chunks( ...
      tpsTaskFile.tpsTargets.gapIndices, tpsTaskFile.tpsTargets.fillIndices, ...
      length( timeStampsKjd ), false ) ;
  fluxValue = compute_median_corrected_flux_values( tpsTaskFile, normalIndicators, ...
      fillIndicators, medianCorrectionFlag ) ;
  
% plot the normal flux

  subplot(2,1,1) ;
  plot_data_and_fill( timeStampsKjd, fluxValue, normalChunks, fillChunks ) ;
  hold off
  
% construct the title

  title(['Kepler ID ', num2str( keplerId ), ' Input Flux ', ...
      koiPeriodText, tpsPeriodText] ) ;
  if medianCorrectionFlag
      ylabel('Relative Flux') ;
  else
      ylabel('Absolute Flux [Photoelectrons]') ;
  end
  
% now for the detrended, stitched flux time series  

  tpsDiagnosticStruct = get_tps_struct_by_kepid_from_task_dir_tree( tceStruct, ...
      keplerId, 'diagnostic', false ) ;
  fillIndicators( gapIndicators ) = true ;
  fillChunks   = identify_contiguous_integer_values( find( fillIndicators ) ) ;
 
  subplot(2,1,2) ;
  plot_data_and_fill( timeStampsKjd, tpsDiagnosticStruct(1).detrendedFluxTimeSeries, ...
      normalChunks, fillChunks ) ;
  hold off
  title(['Kepler ID ', num2str( keplerId ), ' Detrended Stitched Flux']) ;
  ylabel('Relative Flux') ;
  xlabel('Cadence Time [KJD]') ;
  
% adjust the x range of the top plot to match the bottom plot

  subplot(2,1,2) ;
  xLimits = get( gca, 'xlim' ) ;
  subplot(2,1,1) ;
  set( gca, 'xlim', xLimits ) ;
  
return

%=========================================================================================

% subfunction which returns vectors of gap, fill, and normal indicators

function [gapIndicators, fillIndicators, normalIndicators] = get_indicators( taskFile )
    
% start with the cadence times gap indicators and a default set of fill indicators

  gapIndicators    = taskFile.cadenceTimes.gapIndicators ;
  fillIndicators   = false( size( gapIndicators ) ) ;
  normalIndicators = true( size( gapIndicators ) ) ;
  
% get the gap and fill out of the tpsTargets, converting to 1-based indexing

  gapIndicators(  taskFile.tpsTargets.gapIndices+1  ) = true ;
  fillIndicators( taskFile.tpsTargets.fillIndices+1 ) = true ;
  
% any gapped or filled cadence is not normal

  normalIndicators( gapIndicators )  = false ;
  normalIndicators( fillIndicators ) = false ;
  
return

%=========================================================================================

% subfunction which returns the flux values to plot, handling any median correction which
% is needed

function fluxValue = compute_median_corrected_flux_values( tpsTaskFile, normalIndicators, ...
      fillIndicators, medianCorrectionFlag )
  
% set the fluxValue to be equal to the uncorrected values

  fluxValue = tpsTaskFile.tpsTargets.fluxValue ;
  
% if the correction is required, perform it now

  if medianCorrectionFlag
      
      dataIndicators = normalIndicators | fillIndicators ;
      dataSegments = identify_contiguous_integer_values( find( dataIndicators ) ) ;
      
      for iSegment = 1:length(dataSegments)
          thisSegment = dataSegments{iSegment} ;
          fluxValue(thisSegment) = fluxValue(thisSegment) / ...
              median( fluxValue(thisSegment) ) - 1 ;
      end
      
  end

return
  
