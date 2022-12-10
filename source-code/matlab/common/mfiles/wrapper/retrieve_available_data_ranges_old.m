function availableData = retrieve_available_data_ranges(isShortCadence)
% function availableData = retrieve_available_data_ranges(isShortCadence)
% 
%   Returns a struct array with one element for each target table for which
%   cadence data have been ingested.  
%
%   By default, long cadence target tables are returned.  Set
%   isShortCadence=1 to retrieve data ranges for short cadence.
%
%   Inputs
%     isShortCadence        Optional.  If 1, short cadence target table 
%                           data ranges are returned.  If 0, or if this 
%                           argument is not specified, long cadence target 
%                           table data ranges are returned.
%   Outputs
%     availableData         1xN struct array, one entry per target table
%       .cadenceType        'LONG' or 'SHORT'
%       .tableId            The target table ID
%       .startCadence       The first cadence number for which data were
%                           collected on the spacecraft for this target
%                           table
%       .endCadence         The last cadence number for which data were
%                           collected on the spacecraft for this target
%                           table.
%       .startMjd           The startMjd timestamp for the first cadence 
%                           for which data were collected on the spacecraft 
%                           for this target table
%       .endMjd             The endMjd timestamp for the last cadence 
%                           for which data were collected on the spacecraft 
%                           for this target table
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

if (nargin==0)
    shortCadence = 0;
elseif (nargin==1)
    shortCadence = isShortCadence;   
else
    error('MATLAB:SBT:wrapper:retrieve_available_data:wrongNumberOfInputs', 'MATLAB:SBT:wrapper:retrieve_available_data: must be called with 0 or 1 input arguments.');
end;

if (shortCadence)
    cadenceType = 'SHORT';
else
    cadenceType = 'LONG';
end;

pixelLogs = retrieve_cadence_logs(~shortCadence);

if (shortCadence)
    [tableIds tableStartIndices] = unique([pixelLogs.scTargetTableId],'first');
    [tableIds tableEndIndices] = unique([pixelLogs.scTargetTableId],'last');
else
    [tableIds tableStartIndices] = unique([pixelLogs.lcTargetTableId],'first');
    [tableIds tableEndIndices] = unique([pixelLogs.lcTargetTableId],'last');
end;

tableStarts = pixelLogs(tableStartIndices);
tableEnds = pixelLogs(tableEndIndices);

availableData = repmat(struct('cadenceType',[],'tableId',[],'startCadence',[],...
    'endCadence',[],'startMjd',[],'endMjd',[]), 1, length(tableStarts));

for tableIndex = 1:length(tableStarts)
    availableData(tableIndex).cadenceType = cadenceType;
    availableData(tableIndex).tableId = tableIds(tableIndex);
    availableData(tableIndex).startCadence = tableStarts(tableIndex).cadenceNumber;
    availableData(tableIndex).endCadence = tableEnds(tableIndex).cadenceNumber;
    availableData(tableIndex).startMjd = tableStarts(tableIndex).mjdStartTime;
    availableData(tableIndex).endMjd = tableEnds(tableIndex).mjdEndTime;
    
    availableData(tableIndex).startLocal = mjd_to_local_time(availableData(tableIndex).startMjd);
    availableData(tableIndex).endLocal = mjd_to_local_time(availableData(tableIndex).endMjd);
    availableData(tableIndex).startOpsFormat = date_in_ops_format(availableData(tableIndex).startMjd);
    availableData(tableIndex).endOpsFormat = date_in_ops_format(availableData(tableIndex).endMjd);
end;

return

function opsFormatDate = date_in_ops_format(mjd)
    yearStr = mjd_to_local_time(mjd, 'yyyy');
    doy = fix(datestr2doy(mjd_to_local_time(mjd)));
    timeString = mjd_to_local_time(mjd, 'HHMMSS');
    opsFormatDate = sprintf('%s%03d%s', yearStr, doy, timeString);
return 




