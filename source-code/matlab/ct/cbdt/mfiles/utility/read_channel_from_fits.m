%% read all the data from a single channel
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

ffiFolder = 'C:\path\to\CBD_Data';
dataFile = { ...
    'ffi_200809030929_set_001.fits', ...
    'ffi_200809030929_set_002.fits', ...
    'ffi_200809030929_set_003.fits', ...
    'ffi_200809030930_set_001.fits', ...
    'ffi_200809030930_set_002.fits', ...
    'ffi_200809030930_set_003.fits', ...
    'ffi_200809030931_set_001.fits', ...
    'ffi_200809030931_set_002.fits', ...
    'ffi_200809030931_set_003.fits', ...
    'ffi_200809031347_set_001.fits', ...
    'ffi_200809031347_set_002.fits', ...
    'ffi_200809031347_set_003.fits', ...
    'ffi_200809031508_set_001.fits', ...
    'ffi_200809031508_set_002.fits', ...
    'ffi_200809031508_set_003.fits', ...
    'ffi_200809031618_set_001.fits', ...
    'ffi_200809031618_set_002.fits', ...
    'ffi_200809031618_set_003.fits', ...
    'ffi_200809031856_set_001.fits', ...
    'ffi_200809031856_set_002.fits', ...
    'ffi_200809031856_set_003.fits', ...
    'ffi_200809032124_set_001.fits', ...
    'ffi_200809032124_set_002.fits', ...
    'ffi_200809032124_set_003.fits', ...
    };

twoDBlackModelFile = 'TwoDBlackFGS_20080903.fits';

nFFIs = length( dataFile );
nRows = 1070;
nCols = 1132;
nAddOn = 270;
channel = 1;
blackFFIs = zeros(nRows, nCols, nFFIs);

modout = 16;
twoDBlackModel = fitsread( fullfile(ffiFolder, twoDBlackModelFile), 'Image', modout);

% problematic channels: [45,46,47,48]
for channel=modout %1:84
    disp(['*** Channel ' num2str(channel) ' ***']);
    close all;
    % read all FFI into memory
    for k=1:1:nFFIs
        %disp( num2str(k) );
        fileFITS = fullfile(ffiFolder, dataFile{k} );
        blackFFIs(:, :, k) = fitsread(fileFITS, 'Image', channel) / nAddOn;
    end

    continue;
    
    % show difference between model and each FFI
    for k=1:1:nFFIs
        figure(1), imagesc( blackFFIs(:, :, k), [704,712] ); colorbar; title( num2str(k) );
        % verify the difference between input raw and the smoothed 2D black
        diff = twoDBlackModel - blackFFIs(:, :, k);
        figure(2), imagesc( diff, [-1, 1] ); colorbar; title('difference');
        
        % verify the FGS mask locations
%         fgs_diff = blackFFIs(:, :, k) - ( single(xtalkImage < 4) ) * 712 ;
%         figure(3), imagesc(fgs_diff, [712,715]); colorbar;
        
        pause(2);
    end
    pause;
end
