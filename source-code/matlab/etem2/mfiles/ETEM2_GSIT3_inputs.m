function gloabalConfigurationStruct = ETEM2_single_plane_inputs()

% Example ETEM2 input file which
% set up all ETEM2 input variables and saves them in a mat file named
% ETEM2_inputs_run###.mat
%
% 
% Copyright 2017 United States Government as represented by the
% Administrator of the National Aeronautics and Space Administration.
% All Rights Reserved.
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

% get set of defined plugin classes
pluginList = defined_plugin_classes();
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% global specification data for this simulation run
runParamsData.simulationData.numberOfTargetsRequested=2000; % Number of target stars before downselect

runParamsData.simulationData.runStartDate = '24-June-2010 17:29:36.8448'; % start date of current run
runParamsData.simulationData.runDuration = 1392; % length of run in the units defined in the next field
runParamsData.simulationData.runDurationUnits = 'cadences'; % units of run length paramter: 'days' or 'cadences'
runParamsData.simulationData.initialScienceRun = -1; % if positive, indicates a previous run to use as base
runParamsData.simulationData.firstExposureStartDate = '1-Mar-2009 17:29:36.8448'; % Date of a roll near launch.

runParamsData.simulationData.moduleNumber = 14; % which CCD module, ouput and season, legal values: 2-4, 6-20, 22-24
runParamsData.simulationData.outputNumber = 1; % legal values: 1-4
runParamsData.simulationData.observingSeason = 1; % 0-3 0-summer,1-fall,2-winter,3-spring

runParamsData.simulationData.cadenceType = 'long'; % cadence types, <long> or <short>

runParamsData.keplerData.exposuresPerShortCadence = 9; % # of exposures in a short cadence, 9 for a ~1-minute cadence
runParamsData.keplerData.shortsPerLongCadence = 30; % # of short cadences in a long cadence

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% data about the current run
runParamsData.etemInformation.className = 'runParamsClass';
% Root location of the ETEM 2 code
runParamsData.etemInformation.etem2Location = '.';
runParamsData.etemInformation.etem2OutputLocation = [runParamsData.etemInformation.etem2Location filesep 'output']; 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% global data about the spacecraft
runParamsData.keplerData.keplerInitialOrbitFilename = 'keplerInitialOrbit.mat';
runParamsData.keplerData.keplerInitialOrbitFileLocation = 'configuration_files';

% set ccd parameters
runParamsData.keplerData.pixelWidth = 27; % pixel width in microns
runParamsData.keplerData.pixelAngle = 3.98; % pixel width in seconds of arc
runParamsData.keplerData.boresiteDec = 44.5; % declination of boresite in degrees

runParamsData.keplerData.intrapixWavelength = 800; % wavelength in nm of intra pixel variability
                                  % must be 500 or 800 nm
runParamsData.keplerData.numVisibleRows = 1024; % should eventually get these from fc_constants
runParamsData.keplerData.numVisibleCols = 1100;
runParamsData.keplerData.numLeadingBlack = 12;
runParamsData.keplerData.numTrailingBlack = 20;
runParamsData.keplerData.numVirtualSmear = 26;
runParamsData.keplerData.numMaskedSmear = 20;
% specification of which collateral pixels to use relative to the pixel set
runParamsData.keplerData.maskedSmearRows = 4:8;
runParamsData.keplerData.virtualSmearRows = 4:8;
runParamsData.keplerData.leadingBlackCols = 4:8;

runParamsData.keplerData.nSubPixelLocations = 10; % # of sub-pixel locations on a side of  a pixel
runParamsData.keplerData.prfDesignRangeBuffer = 1.25; % amount to expand prf design rangef

runParamsData.keplerData.integrationTime = 6.12361; % seconds from mail 4-June-2007.  Was 5.70845
runParamsData.keplerData.transferTime = 0.51895; % seconds from CDPP spreadsheed
runParamsData.keplerData.wellCapacity = 1.30E+06; % electrons assuming 1.3 milliion e- full well
runParamsData.keplerData.readNoise = 30; % e-/pixel/second (should be 100, use 30 to get more pixels for test)
runParamsData.keplerData.numAtoDBits = 14; % number of bits in A/D converter
runParamsData.keplerData.parallelCTE = 0.9996; % parallel charge transfer efficiency
runParamsData.keplerData.serialCTE = 0.9996; % serial charge transfer efficiency
runParamsData.keplerData.electronsPerADU = 116; % match current value
runParamsData.keplerData.saturationSpillUpFraction = 0.50; % fraction of spilled saturation goes up
runParamsData.keplerData.adcGuardBandFractionLow = 0.05; % 5% guard band on low end of A/D converter
runParamsData.keplerData.adcGuardBandFractionHigh = 0.05; % 5% guard band on high end of A/D converter
runParamsData.keplerData.fluxOfMag12Star = 2.34E+05; % flux of a 12th magnitude start in e-/sec

runParamsData.keplerData.simulationFramesPerExposure = 5; % # of simulation frames per integration
                        % not sure about that: ETEM comments say
                        % = ceil(integrationTime/.5 + 1) but that's not
                        % consistent with ETEM values
runParamsData.keplerData.numChains = 5; % not entirely sure what this is - fixed by hardware?

runParamsData.keplerData.motionPolyOrder = 6; % order of the motion polynomial
runParamsData.keplerData.dvaMeshOrder = 3; % order of the dva motion interpolation polynomial
runParamsData.keplerData.motionGridResolution = 5; % number of points on a side for motion grid
runParamsData.keplerData.numCadencesPerChunk = 100; % number of cadences to work with for memory management
runParamsData.keplerData.targetImageSize = 11; % size on a side of a target image in pixels.  Must be odd.

runParamsData.keplerData.badFitTolerance = 4; % allowed error before a pixel is considered badly fit
runParamsData.keplerData.saturationBoxSize = 7; % box to put around saturated pixels
runParamsData.keplerData.supressAllMotion = 0; % diagnostic which if true turns all object motion off
runParamsData.keplerData.transitTimeBuffer = 4; % cadences to add to each side of a transit

runParamsData.keplerData.rowCorrection = 0; % offset to add to tad target definitions.
runParamsData.keplerData.colCorrection = 0; % offset to add to tad target definitions.

runParamsData.keplerData.refPixCadenceInterval = 48; % how often to save reference pixels (in cadences)
runParamsData.keplerData.refPixCadenceOffset = 0; % offset to add to reference pixel cadence interval.

% global pointing offset data
runParamsData.keplerData.raOffset = 0; % in longitudinal degrees
runParamsData.keplerData.decOffset = 0; % degrees
runParamsData.keplerData.phiOffset = 0; % degrees

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% set the raDec2pix object
runParamsData.raDec2PixData = pluginList.productionRaDec2PixData;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% set the catalog reader object
tadInputData = pluginList.runTadData; 
tadInputData.targetSelectorData = pluginList.selectTargetByPropertyData;
tadInputData.usePointingOffsets = 0;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% set the catalog reader object
catalogReaderData = pluginList.kicCatalogReaderData; 
% catalogReaderData = pluginList.etem1CatalogReaderData; 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% define ccd plane objects

ccdPlaneCount = 1;
% define the psf for this ccd plane
ccdPlaneData(ccdPlaneCount).psfObjectData = pluginList.specificPsfData;
ccdPlaneData(ccdPlaneCount).psfObjectData.psfFilename = 'psf_focus_4_z1f1.mat';
% define motions that are global to this ccd plane
ccdPlaneData(ccdPlaneCount).motionDataList = [];
% define the selection algorithm which determines which stars are on this
% plane
ccdPlaneData(ccdPlaneCount).starSelectorData = pluginList.randomStarSelectorData;
ccdPlaneData(ccdPlaneCount).starSelectorData.probability = 1; %pick out all the stars
ccdPlaneData(ccdPlaneCount).diagnosticDisplay = 1;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% define ccd object

% define motions that are global to the ccd
% first define the special dva motion object
% this object cannot be empty.  To turn jitter off use
% ccdData.dvaMotionData = pluginList.dvaNoMotionData;
ccdData.dvaMotionData = pluginList.dvaMotionData;

% define the special jitter motion object
% this object cannot be empty.  To turn jitter off use
% ccdData.jitterMotionData = pluginList.jitterNoMotionData;
ccdData.jitterMotionData = pluginList.jitterMotionData;

% define the other motion objects in the motion list
% example:
% ccdMotionCount = 1;
% ccdData.motionDataList(ccdMotionCount) = {pluginList.jitterMotionData};
ccdData.motionDataList = [];

% define the visible background objects
% these background signals appear on the visible pixels only
counter = 1;
ccdData.visibleBackgroundDataList(counter) ...
    = {pluginList.scalarZodiLightData};
counter = counter+1;
ccdData.visibleBackgroundDataList(counter) ...
    = {pluginList.stellarBackgroundData};

% define the pixel background objects
% these background signals appear on all physical pixels
counter = 1;
ccdData.pixelBackgroundDataList(counter) ...
    = {pluginList.darkCurrentData};

% define the flat field component objects
counter = 1;
ccdData.flatFieldDataList(counter) ...
    = {pluginList.interPixelVariabilityData};
counter = counter+1;
ccdData.flatFieldDataList(counter) ...
    = {pluginList.vignettingData};

% define the pixel effect objects
counter = 1;
ccdData.pixelEffectDataList(counter) ...
    = {pluginList.cteData};

% define the pixel noise objects
counter = 1;
ccdData.pixelNoiseDataList(counter) ...
    = {pluginList.shotNoiseData};
counter = counter+1;
ccdData.pixelNoiseDataList(counter) ...
    = {pluginList.readNoiseData};

% define the list of ccdPlanes
ccdData.ccdPlaneDataList = ccdPlaneData;

% define the cosmic ray object
% ccdData.cosmicRayData = [];
ccdData.cosmicRayData = pluginList.cosmicRayData;

ccdData.targetScienceManagerData = [];

% clear everything we don't want saved
clear ccdPlaneData ccdPlaneCount ccdMotionCount etem2FileLocations

% set the output struct
gloabalConfigurationStruct.runParamsData = runParamsData;
gloabalConfigurationStruct.ccdData = ccdData;
gloabalConfigurationStruct.tadInputData = tadInputData;
gloabalConfigurationStruct.catalogReaderData = catalogReaderData;
