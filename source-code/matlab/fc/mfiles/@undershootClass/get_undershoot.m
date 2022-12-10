function [result covarianceMatrix] = get_undershoot(undershootObject, mjds, module, output)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function [result covarianceMatrix] = get_undershoot(undershootObject, mjds, module, output)
% or
% function [result covarianceMatrix] = get_undershoot(undershootObject, mjds)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% Get the coefficients of the undershoot filter for a given vector of 
% (module, output) at a given vector of mjd for this undershootObject.
%
% The return undershoot data is MxNxJ, where M is the number of MJDs given as
% an argument, N is the number of module/outputs requested (84 if no
% module/output args were given), and J is the number of constants in the
% undershoot model. The return covariance matrix is MxNxJxJ.
%
% The input mjds argument need not be sorted.
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
    isModOutSpecified = 4 == nargin;

    if nargin ~= 4 && nargin ~= 2
        error('Matlab:FC:undershootClass:get_undershoot', 'get_undershoot needs 2 or 4 args');
    end
    
    if isModOutSpecified && length(module) ~= length(output)
        error('Matlab:FC:undershootClass:get_undershoot', 'get_undershoot needs equal-length module and output arguments');
    end

    % If mod/outs are specified, return only those.  The
    % convert_from_module_output routine validates the module/output values.
    %
    if ~isModOutSpecified
        channel = 1:84;
    else
        channel = convert_from_module_output(module, output);
    end
    
    
    indexBefore = find(mjds <  min(undershootObject.mjds));
    indexAfter  = find(mjds >  max(undershootObject.mjds));
    indexIn     = find(mjds >= min(undershootObject.mjds) & mjds <= max(undershootObject.mjds));

    %preallocate memory for result and covariance matrix

    % The iArray loops are unavoidable b/c of the constants(i).array(j) 
    % structure of the constants structure.
    %
    if ~isempty(indexBefore)
        for iChannel = 1:84
            for iArray = 1:length(undershootObject.constants(1).array(iChannel).array)
                result(          indexBefore, iChannel, iArray)         = undershootObject.constants(1).array(iChannel).array(iArray);
                covarianceMatrix(indexBefore, iChannel, iArray, iArray) = undershootObject.uncertainty(1).array(iChannel).array(iArray);
            end
        end
    end

    if ~isempty(indexAfter)
        for iChannel = 1:84
            for iArray = 1:length(undershootObject.constants(end).array(iChannel).array)
                result(          indexAfter, iChannel, iArray)         = undershootObject.constants(end).array(iChannel).array(iArray);
                covarianceMatrix(indexAfter, iChannel, iArray, iArray) = undershootObject.uncertainty(end).array(iChannel).array(iArray);
            end
        end
    end

    if ~isempty(indexIn)
         for iIndex = 1:length(indexIn)
                currentIndex = indexIn(iIndex);
                
                for iChannel = 1:84
                    for iArray = 1:length(undershootObject.constants(1).array(iChannel).array)
                        result(currentIndex, iChannel, iArray)                   = undershootObject.constants(currentIndex).array(iChannel).array(iArray);
                        covarianceMatrix(currentIndex, iChannel, iArray, iArray) = undershootObject.uncertainty(currentIndex).array(iChannel).array(iArray);
                    end
                end
        end
    end

    % Sanity check on data:
    %
    fc_nonimage_data_check(result);
    fc_nonimage_data_check(covarianceMatrix);

    % add check for negativity on covar matrix
    result = result(:, channel, :);
    covarianceMatrix = covarianceMatrix(:, channel, :, :);

    result = squeeze(result);
    covarianceMatrix = squeeze(covarianceMatrix);
    
    % 8/4/09 - JIRA KSOC-393
    % squeeze returns nx1 when given 1x1xn 
    % result must be row vector when returning single cadence, single channel
    % coefficient array dimension s/b nCadences x nCoeffs or nChannels x nCoeffs
    if( ndims(result)==2 && any(size(result)==1) )
        result = result(:)';
    end

    switch length(size(covarianceMatrix)) % how many dimensions does covarianceMatrix have?
        case 4
            for ii = 1:size(covarianceMatrix, 1)
                for jj = 1:size(covarianceMatrix, 2)
                    tmp_covar = squeeze(covarianceMatrix(ii,jj,:,:));
                    [Trow,errFlagRow] = factor_covariance_matrix(tmp_covar);
                    if errFlagRow < 0
                        % not a valid covariance matrix.
                        error('FC:undershootClass:get_undershoot', 'Covariance matrix must be positive definite or positive semidefinite.');
                    end
                end
            end
        case 3
            for ii = 1:size(covarianceMatrix, 1)
                tmp_covar = squeeze(covarianceMatrix(ii,:,:));
                [Trow,errFlagRow] = factor_covariance_matrix(tmp_covar);
                if errFlagRow < 0
                    % not a valid covariance matrix.
                    error('FC:undershootClass:get_undershoot', 'Covariance matrix must be positive definite or positive semidefinite.');
                end
            end
        case 2
            [Trow,errFlagRow] = factor_covariance_matrix(covarianceMatrix);
            if errFlagRow < 0
                % not a valid covariance matrix.
                error('FC:undershootClass:get_undershoot', 'Covariance matrix must be positive definite or positive semidefinite.');
            end
        otherwise
    end

return
