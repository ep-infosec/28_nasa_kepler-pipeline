function [prfArray, prfRow, prfColumn] = draw_pixel(prfObject, pixelsToDraw, type, resolution, reverse, offset)
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
if nargin < 3
    type = 'mesh';
end

if nargin < 4
    resolution = 500;
end

if nargin < 5
    reverse = 1;
end

if nargin < 6
    offset = [0 0];
end

[prfArray, prfRow, prfColumn] = make_array_pixel(prfObject, pixelsToDraw, resolution, reverse, offset);
figure('Color', 'white');

switch type
    case 'mesh'
        mesh(prfRow, prfColumn, prfArray, 'EdgeColor', 'b');
        title('nomalized pixel response function');
        xlabel('row pixel');
        ylabel('column pixel');
        axis tight;
        
    case 'contour'
        contour(prfRow, prfColumn, prfArray, 20);
%         axis([1, prfObject.nPrfArrayRows, 1, prfObject.nPrfArrayCols]);
        title('nomalized pixel response function');
        xlabel('row pixel');
        ylabel('column pixel');
        
    case 'image'
        imagesc([min(prfRow) max(prfRow)], [min(prfColumn) max(prfColumn)], prfArray);
%         axis([1, prfObject.nPrfArrayRows, 1, prfObject.nPrfArrayCols]);
        title('nomalized pixel response function');
        xlabel('row pixel');
        ylabel('column pixel');
        
    otherwise
        error('prfClass:draw: not valid type');
end
