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

package gov.nasa.kepler.services.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.nasa.spiffy.common.pi.PipelineException;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public class MessageContext {
    private static final Log log = LogFactory.getLog(MessageContext.class);

    protected ObjectMessage jmsMessage = null;
    protected PipelineMessage pipelineMessage = null;

    public MessageContext(ObjectMessage inboundMessage) {
        this.jmsMessage = inboundMessage;
        try {
            Object jmsMessageObject = jmsMessage.getObject();
            pipelineMessage = (PipelineMessage) jmsMessageObject;
        } catch (JMSException e) {
            throw new PipelineException("failed to retrieve payload from JMS message", e);
        }
        if (pipelineMessage == null) {
            throw new PipelineException("JMS ObjectMessage contains no payload!");
        }
    }

    public MessageContext(PipelineMessage pipelineMessage) {
        this.pipelineMessage = pipelineMessage;
    }

    /**
     * @return Returns the pipelineMessage.
     */
    public PipelineMessage getPipelineMessage() {
        return pipelineMessage;
    }

    /**
     * @return Returns the jmsMessage.
     */
    public ObjectMessage getJmsMessage() {
        return jmsMessage;
    }
    
    public boolean isRedelivered(){
        try {
            return jmsMessage.getJMSRedelivered();
        } catch (JMSException e) {
            log.warn("Caught JMSException when checking for redelivery flag", e);
            return false;
        }
    }
    
    @Override
    public String toString(){
        String jmsId = "<unknown>";
        String pipelineMessageToString = "<unknown>";
        
        try {
            if(jmsId != null){
                jmsId = jmsMessage.getJMSMessageID();
            }
        } catch (JMSException e) {
        }
        
        if(pipelineMessage != null){
            pipelineMessageToString = pipelineMessage.toString();
        }

        return "JmsId: " + jmsId + ", " + "pipelineMsg: " + pipelineMessageToString;
    }
}
