function amaResultStruct = ama_matlab_controller_1_base(amaParameterStruct)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function amaResultStruct = ama_matlab_controller_1_base(amaParameterStruct)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% 
% master control function for the assignment of apretures to masks.
% Assumes 1-base indexing on inputs
% Input:
%   amaParameterStruct - see amaClass.m for description
%
% Output amaResultStruct contains the following fields:
%   .targetDefinitions a # of targets by 1 structure containing the fields
%       .keplerId kepler ID of the target
%       .maskIndex index (into the input maskDefinitions array) of the 
%           mask assigned to this target
%       .referenceRow, .referenceColumn reference row and column of the
%           mask assigned to this target
%       .excessPixels the number of pixels in the assigned mask that are
%           not in the requested aperture
%       .status status indicating successful mask assignment: 
%           status = -1: no mask assigned
%           status = 0: mask assigned, no problems
%           status = -2: mask assigned but has pixels off the CCD
%   .usedMasks a # of masks x 1 logical array parallel to the input maskDefinitions
%       array indicating whether a mask is used (0 = not used, 1 = used)
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


debugFlag = amaParameterStruct.debugFlag;
durationList = [];

% set values from the focal plane constants
fcConstants = amaParameterStruct.fcConstants;
 
amaParameterStruct.moduleDescriptionStruct.nRowPix = fcConstants.nRowsImaging;
amaParameterStruct.moduleDescriptionStruct.nColPix = fcConstants.nColsImaging;
amaParameterStruct.moduleDescriptionStruct.leadingBlack = fcConstants.nLeadingBlack;
amaParameterStruct.moduleDescriptionStruct.trailingBlack = fcConstants.nTrailingBlack;
amaParameterStruct.moduleDescriptionStruct.virtualSmear = fcConstants.nVirtualSmear;
amaParameterStruct.moduleDescriptionStruct.maskedSmear = fcConstants.nMaskedSmear;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% create amaClass
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
tic;
    amaObject = amaClass(amaParameterStruct);
duration = toc;

durationElement = length(durationList);
durationList(durationElement + 1).time = duration;
durationList(durationElement + 1).label = 'amaClass';

if (debugFlag) 
    display(['amaClass duration: ' num2str(duration) ...
        ' seconds = '  num2str(duration/60) ' minutes']);
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% assign masks to apertures
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
tic;
amaObject = assign_masks(amaObject);
duration = toc;

durationElement = length(durationList);
durationList(durationElement + 1).time = duration;
durationList(durationElement + 1).label = 'assign_masks';

if (debugFlag) 
    display(['assign_masks duration: ' num2str(duration) ...
        ' seconds = '  num2str(duration/60) ' minutes']);
end

amaResultStruct = set_result_struct(amaObject);
amaResultStruct.durationList = durationList;
