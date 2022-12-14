function plot_dawg_centroid_metrics( S )

% set plot position on screen
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
P1 = [ 175   145   830   950];


%         S(i).ccdModule = nanmedian(C(i).ccdModule);
%         S(i).ccdOutput = nanmedian(C(i).ccdOutput);
% 
%         S(i).rowStdFw       = nanmedian(C(i).rowStdFw);
%         S(i).colStdFw       = nanmedian(C(i).colStdFw);
%         S(i).rowUncRmsFw    = nanmedian(C(i).rowUncRmsFw);
%         S(i).colUncRmsFw    = nanmedian(C(i).colUncRmsFw);
%         S(i).rowStdPrf      = nanmedian(C(i).rowStdPrf);
%         S(i).colStdPrf      = nanmedian(C(i).colStdPrf);
%         S(i).rowUncRmsPrf   = nanmedian(C(i).rowUncRmsPrf);
%         S(i).colUncRmsPrf   = nanmedian(C(i).colUncRmsPrf); 


% generate summary plots
summaryChannel = convert_from_module_output([S.ccdModule],[S.ccdOutput]);
stdFw = sqrt([S.rowStdFw].^2 + [S.colStdFw].^2);
uncFw = sqrt([S.rowUncRmsFw].^2 + [S.colUncRmsFw].^2);
stdPrf = sqrt([S.rowStdPrf].^2 + [S.colStdPrf].^2);
uncPrf = sqrt([S.rowUncRmsPrf].^2 + [S.colUncRmsPrf].^2);


% flux weighted standard deviation
figure;
subplot(4,1,1);
plot(summaryChannel,stdFw,'o');
grid;
ylabel('\bf\fontsize{12}flux weighted SD');
title('\bf\fontsize{12}Median Centroid SD and RMS Uncertainty (pixels)');

% flux weighted rms uncertainty
subplot(4,1,2);
plot(summaryChannel,uncFw,'o');
grid;
ylabel('\bf\fontsize{12}flux weighted unc');

% prf standard deviation
subplot(4,1,3);
plot(summaryChannel,stdPrf,'o');
grid;
ylabel('\bf\fontsize{12}prf SD');

% prf rms uncertainty
subplot(4,1,4);
plot(summaryChannel,uncPrf,'o');
grid;
ylabel('\bf\fontsize{12}prf unc');
xlabel('\bf\fontsize{12}channel');
set(get(gcf,'Children'),'FontWeight','bold');
set(get(gcf,'Children'),'FontSize',12);
set(gcf,'Position',P1);
