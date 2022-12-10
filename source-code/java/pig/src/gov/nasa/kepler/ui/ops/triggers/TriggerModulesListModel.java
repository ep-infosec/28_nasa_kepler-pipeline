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

package gov.nasa.kepler.ui.ops.triggers;

import gov.nasa.kepler.hibernate.pi.PipelineDefinition;
import gov.nasa.kepler.hibernate.pi.PipelineDefinitionNode;
import gov.nasa.kepler.hibernate.pi.TriggerDefinition;
import gov.nasa.kepler.hibernate.pi.TriggerDefinitionNode;
import gov.nasa.kepler.ui.proxy.PipelineDefinitionCrudProxy;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

@SuppressWarnings("serial")
public class TriggerModulesListModel extends AbstractListModel implements ComboBoxModel {

    private List<String> moduleNames = new LinkedList<String>();
    private List<PipelineDefinitionNode> pipelineNodes = new LinkedList<PipelineDefinitionNode>();
    private List<TriggerDefinitionNode> triggerNodes = new LinkedList<TriggerDefinitionNode>();

    private String selectedName = null;
    private TriggerDefinition trigger;

    public TriggerModulesListModel(TriggerDefinition trigger) {
        this.trigger = trigger;
     
        loadFromDatabase();
    }

    public void loadFromDatabase(){
        moduleNames.clear();
        pipelineNodes.clear();
        triggerNodes.clear();

        PipelineDefinitionCrudProxy pipelineDefinitionCrud = new PipelineDefinitionCrudProxy();
        PipelineDefinition latestPipelineDefinition = pipelineDefinitionCrud.retrieveLatestVersionForName(trigger.getPipelineDefinitionName());

        latestPipelineDefinition.buildPaths();

        addNodes(trigger, latestPipelineDefinition.getRootNodes());

        if (moduleNames.size() > 0) {
            selectedName = moduleNames.get(0);
        }
        
        fireContentsChanged(this, 0, moduleNames.size()-1);
    }
    
    private void addNodes(TriggerDefinition trigger, List<PipelineDefinitionNode> nodes) {

        for (PipelineDefinitionNode node : nodes) {
            TriggerDefinitionNode triggerNode = trigger.findNodeForPath(node.getPath());

            if (triggerNode != null) {
                moduleNames.add(node.getModuleName()
                    .getName() + " (node:" + node.getId() + ")");
                pipelineNodes.add(node);

                triggerNodes.add(triggerNode);
            }

            addNodes(trigger, node.getNextNodes());
        }
    }

    public PipelineDefinitionNode getPipelineNodeAt(int index) {
        return pipelineNodes.get(index);
    }

    public PipelineDefinitionNode getSelectedPipelineNode() {
        int selectedIndex = moduleNames.indexOf(selectedName);

        if (selectedIndex != -1) {
            return pipelineNodes.get(selectedIndex);
        } else {
            return null;
        }
    }

    public TriggerDefinitionNode getTriggerNodeAt(int index) {
        return triggerNodes.get(index);
    }

    public Object getElementAt(int index) {
        return moduleNames.get(index);
    }

    public int getSize() {
        return moduleNames.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    @Override
    public Object getSelectedItem() {
        return selectedName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    @Override
    public void setSelectedItem(Object selectedItem) {
        selectedName = (String) selectedItem;
    }
}
