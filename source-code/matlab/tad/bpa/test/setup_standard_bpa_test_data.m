function bpaParameterStruct = setup_standard_bpa_test_data()
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function bpaParameterStruct = setup_standard_bpa_test_data()
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% 
% script that creates the standard input struct for regression test of bpa
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

load /path/to/matlab/tad/bpa/bpa_standard_test_data.mat;
% description of background apertures
bpaParameterStruct.moduleOutputImage = completeOutputImage; % the full image for this module output
bpaParameterStruct.bpaConfigurationStruct.lineStartRow = minRow; % will be set by other parts of TAD
bpaParameterStruct.bpaConfigurationStruct.lineEndRow = maxRow;
bpaParameterStruct.bpaConfigurationStruct.lineStartCol = minCol;
bpaParameterStruct.bpaConfigurationStruct.lineEndCol = maxCol;
bpaParameterStruct.bpaConfigurationStruct.nLinesRow = 31;
bpaParameterStruct.bpaConfigurationStruct.nLinesCol = 36; % nLinesRow*nLinesCol should match numBackgroundApertures
bpaParameterStruct.bpaConfigurationStruct.nEdge = 6; % # of point in edge region: 2*nEdge + ncenter = nlines
bpaParameterStruct.bpaConfigurationStruct.edgeFraction = 1/10; % fractional size of hi-res edge
bpaParameterStruct.bpaConfigurationStruct.histBinSize = 100; % 

bpaParameterStruct.debugFlag = 0;

bpaParameterStruct.moduleDescriptionStruct.nRowPix = 1024;
bpaParameterStruct.moduleDescriptionStruct.nColPix = 1100;
bpaParameterStruct.moduleDescriptionStruct.leadingBlack = 12;
bpaParameterStruct.moduleDescriptionStruct.trailingBlack = 20;
bpaParameterStruct.moduleDescriptionStruct.virtualSmear = 26;
bpaParameterStruct.moduleDescriptionStruct.maskedSmear = 20;

