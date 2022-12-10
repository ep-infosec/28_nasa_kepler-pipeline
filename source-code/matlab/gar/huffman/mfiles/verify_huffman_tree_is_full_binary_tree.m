function verify_huffman_tree_is_full_binary_tree(huffmanOutputStruct,huffmanTableLength )
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function verify_huffman_tree_is_full_binary_tree(huffmanOutputStruct,
% huffmanTableLength )
% This function verifies that the generated Huffman code tree is a full
% binary tree 
% This is to verify that
%   1. Every non-leaf node has exactly two children - one on the right,
%   another on the left
%   2. Those children do not appear in any other node as children (no
%   duplication of nodes)
%   3. There are exactly nSymbol leaves
%   4. Their depth matches what is in symbolDepths field
%   5. All the nodes have parent nodes except the root node
%   6. All nodes have one sibling (either a leaf or a node) on the left or
%   right.
% 
% If any of the checks fail, an error condition occurs.
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

% huffmanOutputStruct =
%                  levelStruct: [1x9 struct]
%                 symbolDepths: [9x1 double]
%            binaryNodesStruct: [1x8 struct]
%                    sortOrder: [9x1 double]
%           huffmanCodeStrings: {9x1 cell}
%           huffmanCodeLengths: [9x1 double]
%     theoreticalCompressionRate: 2.6911
%        effectiveCompressionRate: 2.7207

% huffmanOutputStruct.binaryNodesStruct =
%     nodeValue
%     nodeDepth
%     leftChildProbability
%     leftChildType
%     leftChildDepth
%     rightChildProbability
%     rightChildType
%     rightChildDepth
%     leftChildSymbolNumber
%     rightChildSymbolNumber
%     leftChildNodeNumber
%     rightChildNodeNumber
%     parentNodeNumber
%--------------------------------------------------------------------------
% Verify: Tree for an optimal prefix free code has exactly 'nSymbols'
% leaves and (nSymbols-1) nodes
% Step 1
% This verification proceeds in two steps - first verify that the number of
% nodes = (huffmanTableLength  -1)
% Step 2
% In the second step, count the number of leaves in the terminal nodes and
% verify that it is  = huffmanTableLength ;
%--------------------------------------------------------------------------
nSymbols = huffmanTableLength ;
nNodes = length(huffmanOutputStruct.binaryNodesStruct);
% you can only check whether the number of unique codewords match the table
% length ( = number of symbols)

if(nNodes ~= (nSymbols-1))
    % need an error message here
    error('GAR:verifyHuffmanTree:IncorrectNumberOfNodes', ...
        'Number of nodes in the Huffman tree does not equal the number of symbols -1');
end;


leftChildSymbolNumber = cat(1, huffmanOutputStruct.binaryNodesStruct.leftChildSymbolNumber);
rightChildSymbolNumber = cat(1, huffmanOutputStruct.binaryNodesStruct.rightChildSymbolNumber);
leafChildren = [leftChildSymbolNumber rightChildSymbolNumber];

nChildren = length(leafChildren(leafChildren > 0));

if(nChildren ~= nSymbols)
    % need an error message here
    error('GAR:verifyHuffmanTree:IncorrectNumberOfLeaves', ...
        'Number of leaves in the Huffman tree does not equal the number of symbols ');
end;

%--------------------------------------------------------------------------
% This is to verify that the huffman code tree is a full binary tree
%   1. Every non-leaf node has exactly two children - one on the right,
%   another on the left
%   6. All nodes have one sibling (either a leaf or a node) on the left or
%   right.
%   The above are true by code design.
% Step 3
%   2. Those two children do not appear in any other node as children (no
%   duplication of nodes)
%   The above is verified in two steps - first that there are no duplicate
%   children (both leaves and nodes) assigned to nodes
% Step 4
%   second, that the children pair assigned to any node is unique
%--------------------------------------------------------------------------

leftChildNodeNumber = cat(1, huffmanOutputStruct.binaryNodesStruct.leftChildNodeNumber);
rightChildNodeNumber = cat(1, huffmanOutputStruct.binaryNodesStruct.rightChildNodeNumber);


nodeChildren = [leftChildNodeNumber rightChildNodeNumber];

% both node children and leaf children have numbers running from 1 through
% (nSymbols-1), nSymbols respectively. To distinguish between the two classes
% of children, node children numbers are multiplied by 1e6 ( > 2^17)

nodeChildren(nodeChildren > 0) = nodeChildren(nodeChildren > 0)*1e6;
%incorporate leaf children
nodeChildren(nodeChildren < 0) = leafChildren(leafChildren > 0);
nUniqueChildren = length(unique(nodeChildren));

if(nUniqueChildren ~= 2*(nSymbols-1))
    % need an error message here
    error('GAR:verifyHuffmanTree:DuplicateChildren', ...
        'Duplicate children detected ');
end;

nUniqueChildSet = length(unique(nodeChildren,'rows'));
if(nUniqueChildSet ~= (nSymbols-1))
    % need an error message here
    error('GAR:verifyHuffmanTree:DuplicateNodes', ...
        'Duplicate nodes detected ');
end;

%--------------------------------------------------------------------------
% Step 5: This test checks to see whether all the nodes have parent nodes except the root node
%--------------------------------------------------------------------------
parentNodes = cat(1, huffmanOutputStruct.binaryNodesStruct.parentNodeNumber);

% check for invalid parent nodes

% you can only check whether the number of unique codewords match the table
% length ( = number of symbols)
rootNode = find(parentNodes == -1);

if(length(rootNode) ~= 1)
    % need an error message here
    error('GAR:verifyHuffmanTree:MoreThanOneRoot', ...
        'More than one root node detected!');
end;


%--------------------------------------------------------------------------
% Step 6: This test checks to see whether the symbol depth matches what is
% in symbolDepths field
% for each symbol (leaf), trace its path to the root
%--------------------------------------------------------------------------
leafDepths = zeros(nSymbols,1);
for j = 1:nSymbols
    leafDepths(j) = 0;
    nodeId = find(leftChildSymbolNumber ==j); % find leftChildSymbolNumber == j
    if(isempty(nodeId))
        nodeId = find(rightChildSymbolNumber== j);
    end;
    parentNumber = parentNodes(nodeId);
    leafDepths(j) = leafDepths(j) + 1;
    while (parentNumber ~= -1)
        parentNumber = parentNodes(parentNumber);
        leafDepths(j) = leafDepths(j) + 1;
    end;
end;

if(~isequal(leafDepths, huffmanOutputStruct.symbolDepths))
    % need an error message here
    error('GAR:verifyHuffmanTree:IncorrectLeafDepths', ...
        'Symbol depths are not equal to leaf depths!');
end;

return;