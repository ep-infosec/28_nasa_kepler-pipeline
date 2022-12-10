% script that tests centroid routines using a Gaussian function
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
clear;

nCadences = 10;

arraySize = 10;
[c, r] = meshgrid(-arraySize:arraySize, -arraySize:arraySize);
[X, Y] = meshgrid(-arraySize:arraySize, -arraySize:arraySize);
width = 4;

uncertainties = 1e-3;

deltaRow = [0; 0.3; -0.6; 0.1; 0.4];
deltaCol = [0; -0.2; 0.6; 0.5; 0.2];
baseRow = [10; 600; 19; 39; 800];
baseCol = [15; 75; 436; 20; 942];

for i=1:length(deltaRow)
    X0 = X - deltaCol(i);
    Y0 = Y - deltaRow(i);
    v = exp(-(X0.*X0 + Y0.*Y0)/width);
%     figure;
%     mesh(v)
    starDataStruct(i).row = baseRow(i) + r(:);
    starDataStruct(i).column = baseCol(i) + c(:);
    starDataStruct(i).values = repmat(v(:), 1, nCadences) + uncertainties*randn(length(v(:)), nCadences);
    starDataStruct(i).uncertainties = uncertainties*ones(size(starDataStruct(1).values));
    starDataStruct(i).gapIndicators = zeros(size(starDataStruct(1).values));
    starDataStruct(i).inOptimalAperture = ones(size(starDataStruct(1).row));
	starDataStruct(i).seedRow = [];
	starDataStruct(i).seedColumn = [];
end

% set an example of optimal aperture containing pixels with some value
starDataStruct(2).inOptimalAperture = starDataStruct(2).values(:,1) > 1e-3;
starDataStruct(4).inOptimalAperture = starDataStruct(2).values(:,1) > 1e-3;
% introduce random gaps
starDataStruct(3).gapIndicators = ...
    rand(size(starDataStruct(3).gapIndicators)) < 0.1;
starDataStruct(4).gapIndicators = ...
    rand(size(starDataStruct(4).gapIndicators)) < 0.5;
% zero out a couple cadences
starDataStruct(5).values(:, [3, 6]) = 0;


% [centroidRow, centroidColumn, status, centroidRowUncertainty, centroidColumnUncertainty] ...
%     = compute_starDataStruct_centroid(starDataStruct, [], [], '2D-gaussian');
% [centroidRow, centroidColumn, status, centroidRowUncertainty, centroidColumnUncertainty] ...
%     = compute_starDataStruct_centroid(starDataStruct, [], [], 'gaussian-marginal');
[centroidRow, centroidColumn, status, centroidCovariance, transformationStruct] ...
    = compute_starDataStruct_centroid(starDataStruct, [], [], 'flux-weighted');

disp('status');
disp(status);
disp('row centroid results: 1st column is exact answer');
disp([baseRow + deltaRow centroidRow]);
disp('mean row error and row error standard deviation');
disp([mean((centroidRow - repmat(baseRow + deltaRow, 1, nCadences)).*~status, 2) std((centroidRow - repmat(baseRow + deltaRow, 1, nCadences)).*~status, 0, 2)]);
disp('column centroid results: 1st column is exact answer');
disp([baseCol + deltaCol centroidColumn]);
disp('mean column error and column error standard deviation:');
disp([mean((centroidColumn - repmat(baseCol + deltaCol, 1, nCadences)).*~status, 2) std((centroidColumn - repmat(baseCol + deltaCol, 1, nCadences)).*~status, 0, 2)]);
disp('row uncertainties');
disp(sqrt(squeeze(centroidCovariance(:,1,1,:)).*~status));
disp('column uncertainties');
disp(sqrt(squeeze(centroidCovariance(:,2,2,:)).*~status));

% test a call without uncertainties
[centroidRow, centroidColumn, status] ...
    = compute_pixel_centroid(starDataStruct(1).row, starDataStruct(1).column, ...
		starDataStruct(1).values, starDataStruct(1).uncertainties, [], [], 'best');
disp('single call without uncertainties');
disp('status');
disp(status);
disp('row centroid results: 1st column is exact answer');
disp([baseRow(1) + deltaRow(1) centroidRow]);
disp('row error standard deviation');
disp(std((centroidRow - repmat(baseRow(1) + deltaRow(1), 1, nCadences)).*~status, 0, 2));
disp('column centroid results: 1st column is exact answer');
disp([baseCol(1) + deltaCol(1) centroidColumn]);
disp('column error standard deviation:');
disp(std((centroidColumn - repmat(baseCol(1) + deltaCol(1), 1, nCadences)).*~status, 0, 2));

