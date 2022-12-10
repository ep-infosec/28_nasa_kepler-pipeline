% make_K2_thruster_firing_flags.m
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

% Driver script to make thruster firing flags for all the K2 runs to date

% Note: I use cadenceTimes from PA or PDC, but evidently cadenceTimes is
% also provided in CAL, so in future, we can make flags as long as CAL has
% been run.

% Loop over existing campaigns, create the flags and archive in a file to
% be delivered to SO
campaignIdRange = {'c0_part2_lc','c1_part1_lc','c1_part2_lc','c1_part1_sc','c1_part2_sc','c2_lc','c2_sc'};
for iCampaign0 = campaignIdRange
    
    % Convert cell to string
    iCampaign = iCampaign0{:};
    
    % Make the .mat output files
    skip=true;
    if(~skip)
        
        % Process the campaign, create the thruster firing flags
        fprintf('Making thruster flags for campaign %s\n',iCampaign)
        [thrusterFiringDataStruct, thrusterFiringEvents, cadenceTimes] = process_K2_thruster_firing_data_TFR(iCampaign);
        
        % Save the output file
        fprintf('Saving thruster flags for campaign %s\n',iCampaign)
        archiveFileName = strcat('thruster_firing_flags_',iCampaign);
        save(archiveFileName,'thrusterFiringEvents','cadenceTimes');
        
    end
    
    % Load the .mat output files and make the .csv output files
    archiveFileName = strcat('thruster_firing_flags_',iCampaign,'.mat');
    load(archiveFileName)
    
    % Make the filename for the csv output file
    csvFileName = strcat('thruster_firing_flags_',iCampaign,'.csv');
    outputData = [cadenceTimes.cadenceNumbers, thrusterFiringEvents.definiteThrusterActivityIndicators, thrusterFiringEvents.possibleThrusterActivityIndicators];
    dlmwrite(csvFileName,outputData,'precision',9)
    
    
    clear thrusterFiringDataStruct
    clear thrusterFiringEvents
    clear cadenceTimes
    
end