function obj = cbdCollateralClass(varargin)

% Constructor for a cbdCollateralClass class object.
% You must always pass one argument if you want to create a new object.
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

if nargin == 0 % Used when objects are loaded from disk
  obj = init_fields;
  obj = class(obj, 'cbdCollateralClass');
  return;
end

firstArg = varargin{1};
if isa(firstArg, 'cbdCollateralClass') %  used when objects are passed as arguments
  obj = firstArg;
  return;
end

% We must always construct the fields in the same order,
% whether the object is new or loaded from disk.
% Hence we call init_fields to do this.
obj = init_fields; 

% attach class name tag, so we can call member functions to do initial setup
obj = class(obj, 'cbdCollateralClass'); 

% Now the real initialization begins


%%%%%%%%% 

function obj = init_fields()
% Initialize all fields to default values 

constants;	% load constants

% pre-allocate memory for input data structure

% coefficients of the column/row fits
obj.trailingBlackPolyFit = [];
obj.leadingBlackPolyFit = [];
obj.maskedSmearPolyFit   = [];
obj.virtualSmearPolyFit  = [];

% residuals of the fits
obj.residualTrailingBlack = single([]);
obj.residualLeadingBlack = single([]);
obj.residualMaskedSmear = single([]);
obj.residualVirtualSmear = single([]);

% significance of the residuals being zero mean norm distribution
obj.differenceSignificanceTrailingBlack    = single(0);
obj.differenceSignificanceLeadingBlack    = single(0);
obj.differenceSignificanceMaskedSmear   = single(0);
obj.differenceSignificanceVirtualSmear  = single(0);

% locations of the collateral columns or rows
obj.trailingBlackCols 	= uint16( TRAILING_BLACK_COLS);
obj.leadingBlackCols 	= uint16(LEADING_BLACK_COLS);
obj.maskedSmearRows     = uint16(MASKED_SMEAR_ROWS);
obj.virtualSmearRows    = uint16(VIRTUAL_SMEAR_ROWS);

obj.polyFitOrder        = POLY_FIT_ORDER;

% debug option
obj.debugStatus         = false;
