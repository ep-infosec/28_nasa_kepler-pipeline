%% classdef mapInputClass
%
%   Collects the data needed for input to MAP and stores in one convenient object for easy reference. This
%   data is public but most cannot be changed once the object is constructed. The only exception being the
%   debug object.
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

classdef mapInputClass < handle
    
    properties(GetAccess = 'public', SetAccess = 'immutable')
        taskInfoStruct  = []; % contains ccdModule, ccdOutput, quarter, month
        mapParams = [];
        mapBlobStruct = [];
        cbvBlobStruct = [];
        randomSeed; % Seed used to initialize randomstreams
        randomStream;
        pdcModuleParameters;
        targetDataStruct; % The unnormalized data
        cadenceTimes;
        cadenceTimestamps; % the time stamps to use, currently set to midTimestamps
        targetsForBasisVectorsAndPriors; % [logical(nTargets)] Logical indicators of targets to use to generate Priors
        goodnessMetricConfigurationStruct;
        motionPolyStruct; % motion polynomial data from pdcInputObject
        multiChannelMotionPolyStruct; % motion polynomial data from pdcInputObject for a multi-channel run
    end
    properties(GetAccess = 'public', SetAccess = 'public')
        debug;
    end

    methods
        % Construct mapInputClass with all data needed from higher up in PDC.
        function obj = mapInputClass(mapParams, pdcModuleParameters, targetDataStruct, ...
                        cadenceTimes, targetsForBasisVectorsAndPriors, mapBlobStruct, cbvBlobStruct, ...
                        goodnessMetricConfigurationStruct, motionPolyStruct, multiChannelMotionPolyStruct, taskInfoStruct)

            % usePriorsFromPixels is an experiemental parameter, if doesn't exists create and set to false
            if (~isfield(mapParams, 'usePriorsFromPixels'))
                mapParams.usePriorsFromPixels = false;
            end
            
            obj.mapParams = mapParams;
            obj.mapBlobStruct = mapBlobStruct;
            obj.cbvBlobStruct = cbvBlobStruct;
            if (obj.mapParams.randomStreamSeed == 0)
                % Initiate the random seed with system clock
                obj.randomSeed = int32(rem(now,1)*1e9);
                obj.randomStream = RandStream('mt19937ar', 'Seed', obj.randomSeed);
            else
                % Use the specified integer
                obj.randomSeed = obj.mapParams.randomStreamSeed;
                obj.randomStream = RandStream('mt19937ar', 'Seed', obj.randomSeed);
            end
            obj.pdcModuleParameters = pdcModuleParameters;
            obj.targetDataStruct = targetDataStruct;
            obj.cadenceTimes = cadenceTimes;
            obj.cadenceTimestamps = cadenceTimes.midTimestamps;
            obj.targetsForBasisVectorsAndPriors = targetsForBasisVectorsAndPriors;
            obj.debug = mapDebugClass(mapParams.randomStreamSeed);
            obj.goodnessMetricConfigurationStruct = goodnessMetricConfigurationStruct;
            obj.motionPolyStruct = motionPolyStruct;
            obj.multiChannelMotionPolyStruct = multiChannelMotionPolyStruct;
            obj.taskInfoStruct = taskInfoStruct;

            % Number of random targets to plot cannot be greater than the total number of targets
            obj.debug.nRandomTargetsToAnalyze = ...
                    min(obj.debug.nRandomTargetsToAnalyze, length(targetDataStruct));
        end
    end

end

