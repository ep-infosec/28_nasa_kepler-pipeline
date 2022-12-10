function attitudeSolutionStruct = generate_attitude_solution_data_100s_of_cadences(nModOuts, modOutsProcessed, numCadences, nStars)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function attitudeSolutionStruct = generate_attitude_solution_data(nModOuts, cadenceIndex, nStars)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% This function loads 84 module output results stored in 84 different
% pdqTempStruct.mat files one at a time and extracts the relevant data
% structures and assembles them into a compact data structure for computing
% attitude solution. This compact data structure contains centroid rows,
% centroid columns, ra, dec, covariance matrices of centroid row, column
% uncertainties
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

minusOnesArray = -1*ones(nStars,1);
zeroMatrix = zeros(nStars,nStars);


attitudeSolutionStruct = repmat(struct('raStars', minusOnesArray, 'decStars', minusOnesArray,'keplerMags', minusOnesArray,'keplerIds', minusOnesArray, ...
    'centroidRows', minusOnesArray, 'centroidColumns', minusOnesArray, 'CcentroidRow', zeroMatrix, ...
    'CcentroidColumn', zeroMatrix, 'ccdModule', minusOnesArray, 'ccdOutput', minusOnesArray, 'starIndexNotUsedForAttitudeSolution', [], ...
    'starIndexUsedForAttitudeSolution', []), numCadences,1);

% for cadence 1
clear zeroMatrix minusOnesArray;

starIndexNotUsedForAttitudeSolutionStruct = repmat(struct('indices', []), numCadences,1);

startIndex = 1;

fprintf('PDQ:extracting centroid rows, columns from %d pdqTempStruct for attitude solution...\n', nModOuts);

for currentModOut = 1 : nModOuts

    if(~modOutsProcessed(currentModOut))
        continue;
    end


    sFileName = ['attitudeTempStruct_' num2str(currentModOut) '.mat'];

    % check to see the existence of the .mat file

    if(~exist(sFileName, 'file'))
        continue;
    end


    load(sFileName, 'attitudeTempStruct');
   
    
    for jCadence = 1:numCadences


        attitudeSolutionStruct(jCadence).nominalPointing =  attitudeTempStruct.nominalPointing(jCadence,:);

        centroidRows = attitudeTempStruct.centroidRows(:,jCadence);
        centroidColumns = attitudeTempStruct.centroidColumns(:,jCadence);

        CcentroidRow = attitudeTempStruct.centroidUncertaintyStruct(jCadence).CcentroidRow;
        CcentroidColumn = attitudeTempStruct.centroidUncertaintyStruct(jCadence).CcentroidColumn;

        invalidRowCentroids = find(centroidRows == -1);
        invalidColCentroids = find(centroidColumns == -1);

        invalidTargetStars = unique([invalidRowCentroids;invalidColCentroids]);

        raStars = attitudeTempStruct.raStars;
        decStars = attitudeTempStruct.decStars;
        keplerMags = attitudeTempStruct.keplerMags;
        keplerIds = attitudeTempStruct.keplerIds;


        nStarsInModule = length(raStars);
        endIndex = startIndex + nStarsInModule - 1;


        if(~isempty(invalidTargetStars))
            
            % Get the indices of the invalidTargetStars
            starIndexNotUsedForAttitudeSolution = (startIndex + invalidTargetStars -1);
            starIndexNotUsedForAttitudeSolutionStruct(jCadence).indices = [starIndexNotUsedForAttitudeSolutionStruct(jCadence).indices; starIndexNotUsedForAttitudeSolution];

            if(nStarsInModule == length(invalidTargetStars)) % all targets gapped for this cadence
                continue;
            else
                centroidRows(invalidTargetStars) = [];
                centroidColumns(invalidTargetStars) = [];
                raStars(invalidTargetStars) = [];
                decStars(invalidTargetStars) = [];
                keplerMags(invalidTargetStars) = [];
                keplerIds(invalidTargetStars) = [];

                % eliminate the row and column from the uncertainty matrices as well

                CcentroidRow(invalidTargetStars,:) = [];
                CcentroidRow(:, invalidTargetStars) = [];

                CcentroidColumn(invalidTargetStars,:) = [];
                CcentroidColumn(:, invalidTargetStars) = [];

                endIndex = startIndex + nStarsInModule - length(invalidTargetStars) - 1;
            end

        end



        attitudeSolutionStruct(jCadence).raStars(startIndex:endIndex) = raStars;
        attitudeSolutionStruct(jCadence).decStars(startIndex:endIndex) = decStars;

        attitudeSolutionStruct(jCadence).keplerMags(startIndex:endIndex) = keplerMags;
        attitudeSolutionStruct(jCadence).keplerIds(startIndex:endIndex) = keplerIds;

        attitudeSolutionStruct(jCadence).centroidRows(startIndex:endIndex) = centroidRows;
        attitudeSolutionStruct(jCadence).centroidColumns(startIndex:endIndex) = centroidColumns;
        attitudeSolutionStruct(jCadence).ccdModule(startIndex:endIndex) = attitudeTempStruct.ccdModule;
        attitudeSolutionStruct(jCadence).ccdOutput(startIndex:endIndex) = attitudeTempStruct.ccdOutput;

        attitudeSolutionStruct(jCadence).CcentroidRow(startIndex:endIndex, startIndex:endIndex) = CcentroidRow;
        attitudeSolutionStruct(jCadence).CcentroidColumn(startIndex:endIndex, startIndex:endIndex) = CcentroidColumn;
        attitudeSolutionStruct(jCadence).cadenceTime = attitudeTempStruct.cadenceTimes(jCadence);

        % make sure all the vectors in the structure are column vectors

        attitudeSolutionStruct(jCadence).raStars  = attitudeSolutionStruct(jCadence).raStars(:);
        attitudeSolutionStruct(jCadence).decStars = attitudeSolutionStruct(jCadence).decStars(:);
        attitudeSolutionStruct(jCadence).keplerMags = attitudeSolutionStruct(jCadence).keplerMags(:);
        attitudeSolutionStruct(jCadence).keplerIds = attitudeSolutionStruct(jCadence).keplerIds(:);

        attitudeSolutionStruct(jCadence).centroidRows = attitudeSolutionStruct(jCadence).centroidRows(:);
        attitudeSolutionStruct(jCadence).centroidColumns = attitudeSolutionStruct(jCadence).centroidColumns(:);
        attitudeSolutionStruct(jCadence).ccdModule = attitudeSolutionStruct(jCadence).ccdModule(:);
        attitudeSolutionStruct(jCadence).ccdOutput = attitudeSolutionStruct(jCadence).ccdOutput(:);


    end


    startIndex = endIndex+1;

end

% Populate starIndexUsedForAttitudeSolution and starIndexNotUsedForAttitudeSolution
for jCadence = 1:numCadences
    attitudeSolutionStruct(jCadence).starIndexNotUsedForAttitudeSolution = starIndexNotUsedForAttitudeSolutionStruct(jCadence).indices(:);
    attitudeSolutionStruct(jCadence).starIndexUsedForAttitudeSolution = setdiff( (1:nStars)', starIndexNotUsedForAttitudeSolutionStruct(jCadence).indices);
end


return
