function [orbitR, orbitV] = compute_keplerian_orbit(transitingOrbitObject, intermediateStruct, timestamps)
% function [orbitR, orbitV] = compute_keplerian_orbit(transitingOrbitObject, intermediateStruct, timestamps)
%
% computes the keplerian orbit for the system described in
% transitingOrbitObject for the times in the array timestamps
%
% adapted from kepler.m from utilities/astronomy_astrodynamics
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

debugFlag = transitingOrbitObject.debugFlag; %#ok<NASGU>
tol       = 1.e-7;

primaryMassKg       = intermediateStruct.primaryMassKg;
semiMajorAxisMeters = intermediateStruct.semiMajorAxisMeters;
orbitalPeriodSec    = intermediateStruct.orbitalPeriodSec;
periCenterTimeSec   = intermediateStruct.periCenterTimeSec;

rPericenterMeters   = intermediateStruct.rPericenterMeters;
vPericenterMks      = intermediateStruct.vPericenterMks;


numTimestamps = length(timestamps);

G = get_physical_constants_mks('gravitationalConstant');
sqmu_1      = 1/sqrt(G*primaryMassKg);

r0  = [rPericenterMeters, 0, 0];
v0  = [vPericenterMks,    0];

r0dotv0     = udotv(r0,v0);
r0mag       = magvec(r0);

% solve universal time-of-flight equation for x using Newton iteration.
t_t0  = rem(timestamps - periCenterTimeSec, orbitalPeriodSec);

x     = sqrt(G*primaryMassKg) * t_t0 * (1/semiMajorAxisMeters);% as a first guess, for elliptical orbits
z     = x.^2 * (1/semiMajorAxisMeters);
C     = c_of_z_function(z);
S     = s_of_z_function(z);

t_try = sqmu_1 * (r0dotv0 * sqmu_1 * x.^2 .* C + (1 - r0mag * (1/semiMajorAxisMeters)) * x.^3 .* S + r0mag * x);

tic;
while any(abs(t_t0 - t_try) > tol)

    dtdx  = sqmu_1 * (x.^2 .* C + r0dotv0 * sqmu_1 * x .* (1 - z .* S) + r0mag * (1 - z .* C));
    x     = x + (t_t0-t_try)./dtdx;
    z     = x .^ 2 * (1/semiMajorAxisMeters);
    C     = c_of_z_function(z);
    S     = s_of_z_function(z);
    t_try = sqmu_1 * (r0dotv0 * sqmu_1 * x.^2 .* C + (1 - r0mag * (1/semiMajorAxisMeters)) * x.^3 .* S + r0mag * x);

    if toc > 30*60 % are we stuck?

        save bad_orbit.mat
        error('compute_kepler_orbit: orbit is not converging');
    end
end

% Evaluate f and g, then compute r and rmag
f     = 1 - x.^2 / r0mag .* C;
g     = t_t0 - x.^3 * sqmu_1 .* S;

r0mat = [r0(1)*ones(numTimestamps, 1) r0(2)*ones(numTimestamps, 1) r0(3)*ones(numTimestamps, 1)];
v0mat = [v0(1)*ones(numTimestamps, 1) v0(2)*ones(numTimestamps, 1) v0(3)*ones(numTimestamps, 1)];

orbitR = scalev(f, r0mat) + scalev(g, v0mat);

rmag = magvec(orbitR);

% Evaluate fdot and gdot, then compute v
fdot = sqrt(G*primaryMassKg) / r0mag ./ rmag .* x .* (z .* S - 1);
gdot = 1 - x.^2 ./ rmag .* C;

orbitV = scalev(fdot,r0mat) + scalev(gdot,v0mat);

return




%==========================================================================
function Cz = c_of_z_function(z)
%function Cz = c_of_z_function(z)
%
% Supporting code for computer solutions of the Kepler problem
% Returns the cosine integral fucntion of an orbit (used in the time-of-flight
%   equation for 2-body orbital problems).
%
% Reference: Fundamentals of Astrodynamics, Bate, Mueller, & White
% Dover 0-486-60061-0
% Pages 208-209

% Allocate space for the results
n     = length(z);
Cz    = zeros(n,1);

% Positive z
igt0  = find(z > 2*eps);

% negative z
ilt0  = find(z < -2*eps);

% z approximately equal to zero
i0    = find(z <= 2*eps & z >= -2*eps);

% Equation 4.4-10 page 208
if ~isempty(igt0)
    Cz(igt0) = (1 - cos(sqrt(z(igt0)))) ./ z(igt0);
end

% Equation 4.5-12 page 208
if ~isempty(ilt0)
    Cz(ilt0) = (1 - cosh(sqrt(-z(ilt0)))) ./ z(ilt0);
end

% Equation 4.5-14 evaluated at z=0, S is about 1/2
if ~isempty(i0)
    Cz(i0) = 1/2 + Cz(i0);
    k      = 1;
    term   = -z(i0) / factorial(4);

    % Keep computing the series expansion of C(z) according to
    % equation 4.5-14 until the relative size of C(z) is not changing
    % with respect to the size of "term" (i.e. the series converged)
    while abs(Cz(i0) + term - Cz(i0)) > 0
        Cz(i0) = Cz(i0) + term;
        term   = term * (-z(i0)) / (2*k+1) / (2*k+2);
    end

end

return

function S = s_of_z_function(z)
%function S = s_of_z_function(z)
%
% Supporting code for computer solutions of the Kepler problem
% Returns the sine integral fucntion of an orbit (used in the time-of-flight
%   equation for 2-body orbital problems).
%
% Reference: Fundamentals of Astrodynamics, Bate, Mueller, & White
% Dover 0-486-60061-0
% Pages 208-209

% Allocate space for the results
S    = zeros(size(z));

% Positive z
igt0 = find(z > eps);

% z = 0
i0   = find(z <= eps & z >= -eps);

% negative z
ilt0 = find(z < -eps);

% Equation 4.4-11 page 208
if ~isempty(igt0)
    rootz   = sqrt(z(igt0));
    S(igt0) = (rootz - sin(rootz)) ./ rootz.^3;
end

% Equation 4.5-15 evaluated at z=0, S is 1/6
if ~isempty(i0)
    S(i0) = 1/6 + S(i0);
end

% Equation 4.5-13 page 208
if ~isempty(ilt0)
    rootz   = sqrt(-z(ilt0));
    S(ilt0) = (sinh(rootz) - rootz) ./ rootz.^(3);
end

return
