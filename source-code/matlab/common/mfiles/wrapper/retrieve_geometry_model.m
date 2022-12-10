function geometryData = retrieve_geometry_model(mjdStart, mjdEnd)
% function geometryData = retrieve_geometry_model(mjdStart, mjdEnd)
% or
% function geometryData = retrieve_geometry_model()
% 
% Returns a matlab GeometryData object that contains data from the various
% models necessary to run ra_dec_2_pix for any of the times between mjdStart and
% mjdEnd.
%
% The geometry model constains the angle-shift constants used by the raDec2PixClass to
% perform the sky-focal plane transformation.
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
import gov.nasa.kepler.systest.sbt.SandboxTools;
SandboxTools.displayDatabaseConfig;

    import gov.nasa.kepler.fc.GeometryModel;
    import gov.nasa.kepler.fc.geometry.GeometryOperations;

    ops = GeometryOperations();
    if 2 == nargin
        geometryModel = ops.retrieveGeometryModel(mjdStart, mjdEnd);
    elseif 0 == nargin
        geometryModel = ops.retrieveGeometryModelAll();
    else
        error('bad argument list length in retrieve_geometry_model');
    end

    geometryData = struct(...
        'mjds',      [], ...
        'constants', [], ...
        'uncertainty', []);

    geometryData.mjds = geometryModel.getMjds();

    constants = geometryModel.getConstants();
    uncertainty = geometryModel.getUncertainty();
    for ii = 1:(size(constants, 1))
        geometryData.constants(ii).array = constants(ii,:);
        geometryData.uncertainty(ii).array = uncertainty(ii,:);
    end

    import gov.nasa.kepler.hibernate.fc.HistoryModelName
    geometryData.fcModelMetadata = get_model_metadata(geometryModel.getFcModelMetadata);

    import gov.nasa.kepler.hibernate.dbservice.DatabaseServiceFactory;
    dbInstance = DatabaseServiceFactory.getInstance();
    dbInstance.clear();
SandboxTools.close;
return
