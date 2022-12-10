function utc = mjd_to_utc( mjdvec, format )
%
% mjd_to_utc -- convert a vector of Modified Julian Dates to strings of UTC times.
%
% utc = mjd_to_utc( mjdvec ) converts a vector of MJDs into a vector of Matlab UTC
%    strings. The format of the resulting strings will be 'yyyy-mmm-dd HH:MM:SS Z'.  
%
% utc = mjd_to_utc( mjdvec, format ) uses a Matlab format conversion specifier to produce
%    the output strings.  Supported format values and their corresponding formats are as
%    follows:
%
%    Number           String                     Example
%    ===========================================================================
%       0             'dd-mmm-yyyy HH:MM:SS Z'   01-Mar-2000 15:45:17 Z
%      13             'HH:MM:SS Z'               15:45:17 Z    
%      15             'HH:MM Z'                  15:45 Z       
%      21             'mmm.dd,yyyy HH:MM:SS Z'   Mar.01,2000 15:45:17 Z
%      30 (ISO 8601)  'yyyymmddTHHMMSSZ'         20000301T154517Z 
%      31             'yyyy-mm-dd HH:MM:SS Z'    2000-03-01 15:45:17 Z
%
% The format argument can also be a string, such as 'yyyymmdd'.  See MATLAB help for
%    DATESTR for description of the use of free-form date format strings.  In this case, a
%    Z will be postpended with no space if the format string does not contain whitespace
%    characters; if the format string contains whitespace characters, the Z will be
%    postpended with a space.
%
% Version date:  2010-November-03.
%
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

% Modification History:
%
%    2010-November-03, PT:
%        accept free form date format string for format argument.
%
%=========================================================================================

% check that the format is supported

  if ( nargin == 1)
      format = 0 ;
  end
  supportedFormats = [0 13 15 21 30 31] ;
  if ~ischar(format) && ~ismember(format,supportedFormats)
      error('common:mjdToUtc:unsupportedFormat' , ...
          'mjd_to_utc:  unsupported format requested' ) ;
  end

% use julian2datestr to do the main conversion

  utcLong = julian2datestr(mjdvec + 2400000.5) ;
  
% convert to a date vector

  utcVector = date_string_to_vector_long_seconds( utcLong ) ;
  
% convert the vectors to the desired format and return

  utc = datestr(utcVector,format) ;
  
% append the Z

  utc = append_z_to_date_string( utc, format ) ;
  
% and that's it!

%
%
%

