/*
 * Copyright 2017 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * This file is available under the terms of the NASA Open Source Agreement
 * (NOSA). You should have received a copy of this agreement with the
 * Kepler source code; see the file NASA-OPEN-SOURCE-AGREEMENT.doc.
 * 
 * No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY
 * WARRANTY OF ANY KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY,
 * INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE
 * WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM
 * INFRINGEMENT, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR
 * FREE, OR ANY WARRANTY THAT DOCUMENTATION, IF PROVIDED, WILL CONFORM
 * TO THE SUBJECT SOFTWARE. THIS AGREEMENT DOES NOT, IN ANY MANNER,
 * CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY PRIOR RECIPIENT
 * OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR ANY
 * OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.
 * FURTHER, GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES
 * REGARDING THIRD-PARTY SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE,
 * AND DISTRIBUTES IT "AS IS."
 * 
 * Waiver and Indemnity: RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS
 * AGAINST THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND
 * SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT. IF RECIPIENT'S USE OF
 * THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES, DEMANDS, DAMAGES,
 * EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES FROM
 * PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT
 * SOFTWARE, RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED
 * STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY
 * PRIOR RECIPIENT, TO THE EXTENT PERMITTED BY LAW. RECIPIENT'S SOLE
 * REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE, UNILATERAL
 * TERMINATION OF THIS AGREEMENT.
 */

package gov.nasa.kepler.ui.config.pipeline;

import gov.nasa.kepler.hibernate.pi.PipelineDefinition;
import gov.nasa.kepler.hibernate.pi.PipelineDefinitionNode;
import gov.nasa.kepler.ui.PigSecurityException;
import gov.nasa.kepler.ui.PipelineUIException;
import gov.nasa.kepler.ui.models.AbstractDatabaseModel;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author tklaus
 *
 */
@SuppressWarnings("serial")
public class PipelineNodesTableModel extends AbstractDatabaseModel{
	private static final Log log = LogFactory.getLog(PipelineNodesTableModel.class);

	private class NodeWrapper{
		
		public PipelineDefinitionNode node;
		public PipelineDefinitionNode predecessorNode;
		public String predecessorId;

		public NodeWrapper( PipelineDefinitionNode node, PipelineDefinitionNode predecessorNode ){
			this.node = node;
			this.predecessorNode = predecessorNode;
			if( predecessorNode != null ){
				this.predecessorId = predecessorNode.getId() +"";
			}else{
				this.predecessorId = "START";
			}
		}
	}
	
	private PipelineDefinition pipeline;
	private List<NodeWrapper> pipelineNodes = new LinkedList<NodeWrapper>();

	public PipelineNodesTableModel( PipelineDefinition pipeline ) throws PipelineUIException {
		this.pipeline = pipeline;
	}
    
	public void loadFromDatabase(){
		log.debug("loadFromDatabase() - start");
		
        try{
            pipelineNodes = new LinkedList<NodeWrapper>();
            
            // TODO: currently only supports one root node
            PipelineDefinitionNode rootNode = pipeline.getRootNodes().get(0);
            pipelineNodes.add( new NodeWrapper( rootNode, null ));
            addChildren( pipelineNodes, rootNode );
        }catch(PigSecurityException ignore){
        }
		fireTableDataChanged();

		log.debug("loadFromDatabase() - end");
	}

	/**
	 * 
	 * @param list
	 * @param node
	 */
	private void addChildren(List<NodeWrapper> list, PipelineDefinitionNode node) {
		for (PipelineDefinitionNode childNode : node.getNextNodes() ) {
			list.add( new NodeWrapper( childNode, node ));
			addChildren( list, childNode );
		}
	}

	public PipelineDefinitionNode getPipelineNodeAtRow( int rowIndex ){
        validityCheck();
		return pipelineNodes.get( rowIndex ).node;
	}
	
	public PipelineDefinitionNode getPredecessorForNodeAtRow( int rowIndex ){
        validityCheck();
		return pipelineNodes.get( rowIndex ).predecessorNode;
	}
	
	public int getRowCount() {
        validityCheck();
		return pipelineNodes.size();
	}

	public int getColumnCount() {
		return 3;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        validityCheck();
		
		NodeWrapper nodeWrapper = pipelineNodes.get( rowIndex );
		
		switch( columnIndex ){
		case 0: return nodeWrapper.node.getId();
		case 1: return nodeWrapper.node.getModuleName();
		case 2: return nodeWrapper.predecessorId;
		}

		return "huh?";
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		switch( column ){
		case 0: return "ID";
		case 1: return "Module Name";
		case 2: return "Predecessor";
		}

		return "huh?";
	}
}
