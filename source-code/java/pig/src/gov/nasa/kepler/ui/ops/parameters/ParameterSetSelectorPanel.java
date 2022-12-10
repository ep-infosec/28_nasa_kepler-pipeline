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

package gov.nasa.kepler.ui.ops.parameters;

import gov.nasa.kepler.hibernate.pi.ParameterSet;
import gov.nasa.kepler.hibernate.pi.ParameterSetName;
import gov.nasa.kepler.ui.config.parameters.ParameterSetListModel;
import gov.nasa.spiffy.common.pi.Parameters;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * Select a {@link ParameterSetName} from a list or create a new one.
 * 
 * @author tklaus
 *
 */
@SuppressWarnings("serial")
public class ParameterSetSelectorPanel extends javax.swing.JPanel {
    private JList paramSetList;
    private JScrollPane scrollPane;
    private ParameterSetListModel paramSetListModel;

    private Class<? extends Parameters> filterClass;

    public ParameterSetSelectorPanel() {
        this.filterClass = null;
        initGUI();
    }

    public ParameterSetSelectorPanel(Class<? extends Parameters> filterClass) {
        this.filterClass = filterClass;
        initGUI();
    }

    public ParameterSet getSelected(){
        int selectedIndex = paramSetList.getSelectedIndex();
        
        if(selectedIndex != -1){
            return (ParameterSet) paramSetListModel.getElementAt(selectedIndex);
        }else{
            return null;
        }
    }
    
    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            setPreferredSize(new Dimension(400, 300));
            {
                scrollPane = new JScrollPane();
                this.add(scrollPane, BorderLayout.CENTER);
                {
                    paramSetListModel = new ParameterSetListModel(filterClass);
                    paramSetList = new JList();
                    scrollPane.setViewportView(paramSetList);
                    paramSetList.setModel(paramSetListModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
