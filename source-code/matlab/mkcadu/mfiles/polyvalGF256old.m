function y = polyvalGF256(p,x,RSstruct)
% y = polyvalGF256(p,x,RSstruct)
% Evaluates a polynomial p overGF256 at x.
% Vectorized so that p can be an array of polynomials
% each of which is a row vector.
% Vectorized so that x is treated as a row vector and the output
% is nPolys x nPoints
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
[nPoly,m] = size(p);

x = x(:)'; % force x to be a row vector
nPoints = length(x);

x = repmat(x,nPoly,1); % repmat x out to size of output

% y = zeros(nPoly,nPoints,'uint8');
y = repmat(p(:,1),1, nPoints);

for i = 2:m
  yMultX = multiplyGF256(y, x, RSstruct);
  y = bitxor(yMultX, repmat(p(:,i),1,nPoints));
end

return

function c = multiplyGF256(a,b,RSstruct)
% c = multiplyGF256(a,b,RSstruct)
% multiplies a by b in GF256 defined in structure RSstruct

%a = a(:)';
%b = b(:)';

%if length(a)~=length(b)&&length(a)~=1&&length(b)~=1
%     error('lengths of a and b are not compatible')
%end

%if length(a)==1
%  a = repmat(a,size(b));
%end

%if length(b)==1
%  b = repmat(b,size(a));
%end

iiNot0 = find(a~=0&b~=0);

c = zeros(size(a),'uint8');

logAlphaA = RSstruct.logAlphaOfWord( a(iiNot0) );
logAlphaB = RSstruct.logAlphaOfWord( b(iiNot0) );

logAplusLogB = mod(logAlphaA+logAlphaB,255);

c(iiNot0) = RSstruct.alphaToTheN(logAplusLogB + 1);

return

