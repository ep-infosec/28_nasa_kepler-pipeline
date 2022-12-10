function inputStruct = ...
    assemble_cosmic_ray_input_struct( calObject, calIntermediateStruct )
%**************************************************************************
% function inputStruct = assemble_cosmic_ray_input_struct( calObject )
%**************************************************************************
% Convert a calClass object to a valid cosmicRayInputStruct.     
%**************************************************************************    
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
    if exist('calIntermediateStruct', 'var')
        pixelArray  = ...
            build_masked_smear_pix_array_from_intermediate_struct( ...
                calIntermediateStruct);
    else
        pixelArray  = ...
            build_masked_smear_pix_array(calObject.maskedSmearPixels);
    end
    
    inputStruct = ...
        calCosmicRayCleanerClass.initialize_cosmic_ray_input_struct( ...
            calObject );
    inputStruct.targetArray ...
        = calCosmicRayCleanerClass.assemble_collateral_targets(...
            pixelArray, calMSmearCosmicRayCleanerClass.NEIGHBORHOOD);
end


%**************************************************************************
% pixelArray = build_masked_smear_pix_array(maskedSmearPixels)
%**************************************************************************
% Build a pixel array from the black pixel array contained in a calClass
% object. Assumes pixel values and gap indicators are stored as *column*
% vectors.  
%
% INPUTS
%     maskedSmearPixels 
%     |-.column [1 x 1]  
%     |-.values [C x 1]        
%      -.gapIndicators [C x 1]
%
% OUTPUTS
%     pixelArray
%     |-.ccdRow        : (contains a class-defined default value)
%     |-.ccdColumn     : The CCD column position of the collateral pixel.
%     |-.values        : Column vector of flux values at each cadence.
%     |-.gapIndicators : Column vector of gap indicators at each cadence.
%      -.uncertainties : Column vector of 1's. This is just a placeholder,
%                        since uncertainties are unavailable for CAL
%                        collateral pixels).
%**************************************************************************
function pixelArray = build_masked_smear_pix_array(maskedSmearPixels)
    maskedSmearRow  = calMSmearCosmicRayCleanerClass.MASKED_ROW;
    
    pixelStruct = cosmicRayCleanerClass.create_empty_pixel_struct();
    pixelArray  = repmat(pixelStruct, size(maskedSmearPixels));
    
    if ~isempty(maskedSmearPixels)
        [pixelArray.ccdRow]        = deal(maskedSmearRow);
        [pixelArray.ccdColumn]     = deal(maskedSmearPixels.column);
        [pixelArray.values]        = deal(maskedSmearPixels.values);
        [pixelArray.gapIndicators] = deal(maskedSmearPixels.gapIndicators);
        
        [pixelArray.uncertainties] = deal(ones(size(pixelArray(1).values)));

%         % Estimate uncertainies as the square roots of flux values. This
%         % assumes that photon noise is the dominant noise source in smear
%         % pixels. 
%         valuesMat                  = [pixelArray.values]; % nCadences x nPixels
%         uncertaintiesCellArray     = num2cell(sqrt(valuesMat), 1);
%         [pixelArray.uncertainties] = deal(uncertaintiesCellArray{:});
    end

end


%**************************************************************************
% pixelArray = build_masked_smear_pix_array_from_intermediate_struct( ...
%                                                    calIntermediateStruct)
%**************************************************************************
% Build a pixel array from a calIntermediateStruct. Assumes pixel values
% and gap indicators are stored as *row* vectors. 
%
% INPUTS
%     calIntermediateStruct : A structure assumed to have the fields
%     |                       listed.
%     |-.mSmearColumns      : nColumns x 1 array containing zeros where no
%     |                       valid pixel data exists.
%     |-.mSmearGaps         : nColumns x nCadences
%      -.mSmearPixels       : nColumns x nCadences
%
%     where nColumns is the total number of columns (typically 1132).
%
% OUTPUTS
%     pixelArray
%     |-.ccdRow        : (contains a class-defined default value)
%     |-.ccdColumn     : The CCD column position of the collateral pixel.
%     |-.values        : Column vector of flux values at each cadence.
%     |-.gapIndicators : Column vector of gap indicators at each cadence.
%      -.uncertainties : Column vector of 1's. This is just a placeholder,
%                        since uncertainties are unavailable for CAL
%                        collateral pixels).
%**************************************************************************
function pixelArray = ...
    build_masked_smear_pix_array_from_intermediate_struct( ...
        calIntermediateStruct)

    maskedSmearRow  = calMSmearCosmicRayCleanerClass.MASKED_ROW;
    mSmearColumns   = calIntermediateStruct.mSmearColumns;
    validColumnInd  = find(mSmearColumns);
    nValid          = length(validColumnInd);
    
    if nValid > 0
        
        % Eliminate non-valid pixels and transpose to column vectors.
        mSmearColumns   = mSmearColumns(validColumnInd);
        mSmearGapMat    = calIntermediateStruct.mSmearGaps(validColumnInd, :)';
        mSmearValueMat  = calIntermediateStruct.mSmearPixels(validColumnInd, :)';

        % Initialize output pixel array.
        pixelStruct = cosmicRayCleanerClass.create_empty_pixel_struct();
        pixelArray  = repmat(pixelStruct, [nValid, 1]);
    
        % Distribute array contents to pixel structs.
        [pixelArray.ccdRow]        = deal(maskedSmearRow);
        
        c                          = num2cell(mSmearColumns);
        [pixelArray.ccdColumn]     = deal(c{:});
        
        c                          = num2cell(mSmearValueMat, 1);
        [pixelArray.values]        = deal(c{:});
        
        c                          = num2cell(mSmearGapMat, 1);
        [pixelArray.gapIndicators] = deal(c{:});
        
        [pixelArray.uncertainties] = deal(ones(size(pixelArray(1).values)));

%         % Estimate uncertainies as the square roots of flux values. This
%         % assumes that photon noise is the dominant noise source in smear
%         % pixels. 
%         valuesMat                  = [pixelArray.values]; % nCadences x nPixels
%         uncertaintiesCellArray     = num2cell(sqrt(valuesMat), 1);
%         [pixelArray.uncertainties] = deal(uncertaintiesCellArray{:});
    end

end

%********************************** EOF ***********************************