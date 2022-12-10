% To run this test:
%     run(text_test_runner, testInvertMotionPolynomialClass('test_covariance_matrix_ra_dec'));
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

function self  = test_invert_motion_polynomial(self)

initialize_soc_variables;
addpath(fullfile(socTestDataRoot, 'common', 'unit-tests', 'motionPolynomial', 'invertMotionPolynomial'));

load fcConstants.mat;

load blobMotionPolynomials.mat;
motionPolyStruct = inputStruct(1);
clear inputStruct

display(' ');
display('Test Common:motion_polynomial:invert_motion_polynomial');

messageOut = 'Test failed - The difference between the output data and the expecte value is too big!';

rowColUncertainty = 50e-6;           % Uncertainty of input row/column: 50 microPixels
covRowCol         = (rowColUncertainty)^2*eye(2,2);

for i=1:100

    fprintf('i = %d\n', i);
    rowStar   = fcConstants.CCD_ROWS*rand(1);
    colStar   = fcConstants.CCD_COLUMNS*rand(1);

    [ra, dec, covRaDecIgnored, errIgnored] = invert_motion_polynomial(rowStar, colStar, motionPolyStruct, covRowCol, fcConstants);

    if errIgnored~=-1
        rowCalc = weighted_polyval2d(ra, dec, motionPolyStruct.rowPoly);
        colCalc = weighted_polyval2d(ra, dec, motionPolyStruct.colPoly);
        err2D   = sqrt( (rowCalc-rowStar)^2 + (colCalc-colStar)^2 );
        fprintf('err2D = %e\n', err2D);

        if ( err2D>1e-10 )
            assert_equals(1, 0, messageOut);
        end
    end

end

return