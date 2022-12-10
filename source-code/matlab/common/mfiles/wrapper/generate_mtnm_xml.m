function generate_mtnm_xml(mtXmlFileNames, mtnmXmlFileName)
% function generate_mtnm_xml(mtXmlFileNames, mtnmXmlFileName)
%
% mtXmlFileNames is an array of strings (these are the names of the mask table xml files; nominally, just one file).
% mtnmXmlFileName is the output file name of the mtnm.xml file that will be generated (this file name must end with the string '_mtnm.xml').
%
% Example: 
% >> generate_mtnm_xml({'mask-table-my-favorite.xml', }, 'kplr2008310114036_mtnm.xml')
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

fid = fopen(mtnmXmlFileName, 'w');
fprintf(fid, '<?xml version="1.0" encoding="UTF-8"?>\n');
fprintf(fid, '<nm:data_product_message xmlns:nm="http://kepler.nasa.gov/nm">\n');
fprintf(fid, '  <message_type>MTNM</message_type>\n');
fprintf(fid, '  <identifier>%s</identifier>\n', mtnmXmlFileName);
fprintf(fid, '  <file_list>\n');

for i=1:length(mtXmlFileNames)
    mtXmlFileName = mtXmlFileNames{i};

    fprintf(fid, '    <file>\n');
    fprintf(fid, '      <filename>%s</filename>\n', mtXmlFileName);
    fprintf(fid, '      <size>0</size>\n');
    fprintf(fid, '      <checksum>skipped</checksum>\n');
    fprintf(fid, '    </file>\n');
end

fprintf(fid, '  </file_list>\n');
fprintf(fid, '</nm:data_product_message>\n');

fclose(fid);
