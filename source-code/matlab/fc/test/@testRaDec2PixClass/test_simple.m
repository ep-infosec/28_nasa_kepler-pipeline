function self = test_simple(self)
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
    display('simple');

    ra = 300:0.00001:300.01599; dec = 45.0:0.00001:45.01599; mjd = 55000.5; 

    raDec2PixData = retrieve_ra_dec_2_pix_model(54999, 55001);
    raDec2PixObject = raDec2PixClass(raDec2PixData, 'zero-based');

    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, ra, dec, mjd);
    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, 300, 45, mjd);
    [m11 o11 r11 c11] = ra_dec_2_pix_absolute(raDec2PixObject, 300, 45, mjd, 300, 45, 0);
    [m12 o12 r12 c12] = ra_dec_2_pix_relative(raDec2PixObject, 300, 45, mjd,   .1, .1, .01);
    assert_equals(1, 1);

    [ra1 dec1] =          pix_2_ra_dec(raDec2PixObject, 2, 4, 512, 512, mjd);
    [ra2 dec2] = pix_2_ra_dec_absolute(raDec2PixObject, 2, 4, 512, 512, mjd, 300, 45, 0);
    [ra3 dec3] = pix_2_ra_dec_relative(raDec2PixObject, 2, 4, 512, 512, mjd, .1, .1, .01);
    assert_equals(1, 1);


    raDec2PixData = retrieve_ra_dec_2_pix_model(mjd, mjd);
    raDec2PixObject = raDec2PixClass(raDec2PixData, 'zero-based');

    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, ra, dec, mjd);
    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, 300, 45, mjd);
    [m11 o11 r11 c11] = ra_dec_2_pix_absolute(raDec2PixObject, 300, 45, mjd, 300, 45, 0);
    [m12 o12 r12 c12] = ra_dec_2_pix_relative(raDec2PixObject, 300, 45, mjd,   .1, .1, .01);
    assert_equals(1, 1);

    [ra1 dec1] =          pix_2_ra_dec(raDec2PixObject, 2, 4, 512, 512, mjd);
    [ra2 dec2] = pix_2_ra_dec_absolute(raDec2PixObject, 2, 4, 512, 512, mjd, 300, 45, 0);
    [ra3 dec3] = pix_2_ra_dec_relative(raDec2PixObject, 2, 4, 512, 512, mjd, .1, .1, .01);
    assert_equals(1, 1);

    raDec2PixObject = raDec2PixClass(raDec2PixData, 'one-based');

    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, ra, dec, mjd);
    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, 300, 45, mjd);
    [m11 o11 r11 c11] = ra_dec_2_pix_absolute(raDec2PixObject, 300, 45, mjd, 300, 45, 0);
    [m12 o12 r12 c12] = ra_dec_2_pix_relative(raDec2PixObject, 300, 45, mjd,   .1, .1, .01);
    assert_equals(1, 1);

    [ra1 dec1] =          pix_2_ra_dec(raDec2PixObject, 2, 4, 512, 512, mjd);
    [ra2 dec2] = pix_2_ra_dec_absolute(raDec2PixObject, 2, 4, 512, 512, mjd, 300, 45, 0);
    [ra3 dec3] = pix_2_ra_dec_relative(raDec2PixObject, 2, 4, 512, 512, mjd, .1, .1, .01);
    assert_equals(1, 1);


    raDec2PixData = retrieve_ra_dec_2_pix_model(mjd, mjd);
    raDec2PixObject = raDec2PixClass(raDec2PixData, 'one-based');

    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, ra, dec, mjd);
    [m10 o10 r10 c10] =          ra_dec_2_pix(raDec2PixObject, 300, 45, mjd);
    [m11 o11 r11 c11] = ra_dec_2_pix_absolute(raDec2PixObject, 300, 45, mjd, 300, 45, 0);
    [m12 o12 r12 c12] = ra_dec_2_pix_relative(raDec2PixObject, 300, 45, mjd,   .1, .1, .01);
    assert_equals(1, 1);

    [ra1 dec1] =          pix_2_ra_dec(raDec2PixObject, 2, 4, 512, 512, mjd);
    [ra2 dec2] = pix_2_ra_dec_absolute(raDec2PixObject, 2, 4, 512, 512, mjd, 300, 45, 0);
    [ra3 dec3] = pix_2_ra_dec_relative(raDec2PixObject, 2, 4, 512, 512, mjd, .1, .1, .01);
    assert_equals(1, 1);

return
