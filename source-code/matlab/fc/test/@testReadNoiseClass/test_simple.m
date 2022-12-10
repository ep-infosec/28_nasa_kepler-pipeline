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
    display('test simple');

    readNoiseModel = retrieve_read_noise_model();
    readNoiseObject = readNoiseClass(readNoiseModel);

    readNoise = get_read_noise(readNoiseObject, 55000);
    assert_equals(size(readNoise, 1), 1);
    assert_equals(size(readNoise, 2), 84);

    readNoise = get_read_noise(readNoiseObject, 55000:56000);
    assert_equals(size(readNoise, 1), 1001);
    assert_equals(size(readNoise, 2), 84);

    rnObj = readNoiseClass(retrieve_read_noise_model());
    rn = get_read_noise(rnObj, 55000);
    assert_equals(size(rn, 1), 1);
    assert_equals(size(rn, 2), 84);

    rn = get_read_noise(rnObj, 55000:55009);
    assert_equals(size(rn, 1), 10);
    assert_equals(size(rn, 2), 84);

    rn = get_read_noise(rnObj, 55000:55009, 2, 1);
    assert_equals(size(rn, 1), 10);
    assert_equals(size(rn, 2),  1);

    rn = get_read_noise(rnObj, 55000:55009, [2 3 4 7], [1 1 1 1]);
    assert_equals(size(rn, 1), 10);
    assert_equals(size(rn, 2),  4);
    
    rn = get_read_noise(rnObj);
    assert_equals(size(rn, 1), 1);
    assert_equals(size(rn, 2), 84);
return
