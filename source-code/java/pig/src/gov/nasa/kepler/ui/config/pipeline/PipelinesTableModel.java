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

import gov.nasa.kepler.hibernate.pi.AuditInfo;
import gov.nasa.kepler.hibernate.pi.PipelineDefinition;
import gov.nasa.kepler.hibernate.services.User;
import gov.nasa.kepler.ui.PigSecurityException;
import gov.nasa.kepler.ui.PipelineUIException;
import gov.nasa.kepler.ui.models.AbstractDatabaseModel;
import gov.nasa.kepler.ui.proxy.PipelineDefinitionCrudProxy;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class PipelinesTableModel extends AbstractDatabaseModel {
    private static final Log log = LogFactory.getLog(PipelinesTableModel.class);

    private List<PipelineDefinition> pipelines = new LinkedList<PipelineDefinition>();
    private PipelineDefinitionCrudProxy pipelineDefinitionCrud;

    public PipelinesTableModel() throws PipelineUIException {
        pipelineDefinitionCrud = new PipelineDefinitionCrudProxy();
    }

    public void loadFromDatabase() {
        log.debug("loadFromDatabase() - start");

        if(pipelines != null){
            pipelineDefinitionCrud.evictAll(pipelines);
        }
        
        try{
            pipelines = pipelineDefinitionCrud.retrieveLatestVersions();
        }catch(PigSecurityException ignore){
        }

        fireTableDataChanged();

        log.debug("loadFromDatabase() - end");
    }

    public PipelineDefinition getPipelineAtRow(int rowIndex) {
        validityCheck();
        return pipelines.get(rowIndex);
    }

    public int getRowCount() {
        validityCheck();
        return pipelines.size();
    }

    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        validityCheck();

        PipelineDefinition pipeline = pipelines.get(rowIndex);

        AuditInfo auditInfo = pipeline.getAuditInfo();
        
        User lastChangedUser = null;
        Date lastChangedTime = null;

        if(auditInfo != null){
            lastChangedUser = auditInfo.getLastChangedUser();
            lastChangedTime = auditInfo.getLastChangedTime();
        }
        
        switch (columnIndex) {
            case 0:
                return pipeline.getId();
            case 1:
                return pipeline.getName();
            case 2:
                return pipeline.getVersion();
            case 3:
                return pipeline.isLocked();
            case 4:
                if(lastChangedUser != null){
                    return lastChangedUser.getLoginName();
                }else{
                    return "---";
                }
            case 5:
                if(lastChangedTime != null){
                    return lastChangedTime;
                }else{
                    return "---";
                }
        }

        return "huh?";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return Boolean.class;
            case 4:
                return String.class;
            case 5:
                return Object.class;
            default:
                return String.class;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Name";
            case 2:
                return "Version";
            case 3:
                return "Locked";
            case 4:
                return "User";
            case 5:
                return "Mod. Time";
        }
        return "huh?";
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
