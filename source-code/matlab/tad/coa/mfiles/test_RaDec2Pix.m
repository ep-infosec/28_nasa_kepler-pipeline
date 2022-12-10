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
startTime = datestr2mjd('01-Dec-2009');
duration = 365;
nCadences = 15;
RA = 19;
Dec = 44;
doDVA = 1;

cadenceTimes = linspace(startTime, startTime + duration, nCadences);
tic;
[module, output, aberratedRows, aberratedCols] = ra_dec_2_pix(...
    15*RA, Dec, cadenceTimes, doDVA);
toc;

display(['doDVA = ' num2str(doDVA)]);
display(['# of days ' num2str(max(cadenceTimes) - min(cadenceTimes))]);
display(['range of aberrated rows ' num2str(max(aberratedRows) - min(aberratedRows))]);
display(['range of aberrated columns ' num2str(max(aberratedCols) - min(aberratedCols))]);
figure(10);
scatter(aberratedRows, aberratedCols);

tic;
[modRA, modDec] = pix_2_ra_dec(module(1), output(1), aberratedRows(1), aberratedCols(1), cadenceTimes(1), doDVA);
toc;

display(['original RA = ' num2str(RA) ' hours = ' num2str(15*RA) ' degrees , pix_2_ra_dec says ' num2str(modRA) ' at row ' num2str(aberratedRows(1))]);
display(['original dec = ' num2str(Dec) ', pix_2_ra_dec says ' num2str(modDec) ' at column ' num2str(aberratedCols(1))]);
tic;
[module, output, newRow, newCol] = ra_dec_2_pix(modRA, modDec, cadenceTimes(1), doDVA);
toc;
display(['new ra_dec_2_pix gives row, column = ' num2str([newRow, newCol])]);

