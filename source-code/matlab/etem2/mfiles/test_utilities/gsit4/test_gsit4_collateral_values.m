function result = test_gsit4_collateral_values(ffiLocation, fitsLocation)
% script to reconstruct long-cadence pixel values for gsit-4 from FFIs
% must be run from the ETEM2 directory
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

numCcdRows = 1070;
numCcdCols = 1132;
numCoAdds = 270;
maskedSmearRows = [7 14];
virtualSmearRows = [1052 1059];
leadingBlackCols = [3 10];


modules = [2:4, 6:20, 22:24];

% load the quantization table
% load 'configuration_files/requantizationTable.mat';
load '/path/to/ETEM2/configuration_files/requantizationTable.mat';

h = waitbar(0, 'checking background pixel values for mod # 1');
for m=1:length(modules)
    module = modules(m);
    for output = 1:4
        
        modOutNum = (m-1)*4 + output;
        waitbar((modOutNum-1)/84, h, ['checking background pixel values for mod # ' num2str(modOutNum)]);
        
        % load the FFI
%         disp(['loading ' [ffiLocation filesep 'output_' num2str(modOutNum) '.bin']]);
        fid = fopen([ffiLocation filesep 'output_' num2str(modOutNum) '.bin'], 'r', 'ieee-be');
        ffiImage = numCoAdds*fread(fid, [numCcdCols numCcdRows], 'uint16')';
       	fclose(fid);
        
        % first accumulate leading black (see FS-GS ICD fig. 5.3.1-6)
        collateralValues = sum(ffiImage(:, leadingBlackCols(1):leadingBlackCols(2)), 2);
        % accumulate masked smear
        collateralValues = [collateralValues; ...
            sum(ffiImage(maskedSmearRows(1):maskedSmearRows(2), 13:1112), 1)'];
        % accumulate virtual smear
        collateralValues = [collateralValues; ...
            sum(ffiImage(virtualSmearRows(1):virtualSmearRows(2), 13:1112), 1)'];
        
        tooBig = find(collateralValues > 2^23-1);
        for p=1:length(tooBig)
            while collateralValues(tooBig(p)) > 2^23-1
                collateralValues(tooBig(p)) = collateralValues(tooBig(p)) - 2^23-1;
            end
        end
        
        quantizedCollateralValues = uint16(interp1(requantizationTable, ...
            1:length(requantizationTable), collateralValues, 'nearest'))-1;
        reQuantCollateralValues = requantizationTable(quantizedCollateralValues+1);
        
        fitsTables = fitsread(fitsLocation, 'binTable', modOutNum);
        dmcData = fitsTables(1);
        dmcData = dmcData{1,1};
        result(modOutNum) = all(dmcData == reQuantCollateralValues);
        if ~result(modOutNum)
            disp(['disagreement in background pixels, module ' ...
                num2str(module) ' output ' num2str(output)]);
        end
    end
end

close(h);
