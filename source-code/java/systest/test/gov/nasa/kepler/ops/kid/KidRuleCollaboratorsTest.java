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

package gov.nasa.kepler.ops.kid;

import static org.junit.Assert.assertEquals;
import gov.nasa.kepler.ar.exporter.ktc.CompletedKtcEntry;
import gov.nasa.kepler.investigations.CollaboratorListType;
import gov.nasa.kepler.investigations.CollaboratorType;
import gov.nasa.kepler.investigations.InvestigationType;
import gov.nasa.kepler.investigations.LeaderType;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author Miles Cote
 * 
 */
public class KidRuleCollaboratorsTest {

    @Test
    public void testApplyForBaseInvestigation() {
        String investigationIdBase = "investigationIdBase";

        String nameBaseLeader = "nameBaseLeader";
        String nameCollaborator = "nameCollaborator";

        String emailBaseLeader = "emailBaseLeader";
        String emailCollaborator = "emailCollaborator";

        LeaderType leaderTypeBase = LeaderType.Factory.newInstance();
        leaderTypeBase.setName(nameBaseLeader);
        leaderTypeBase.setEmail(emailBaseLeader);

        InvestigationType kidInvestigation = InvestigationType.Factory.newInstance();
        kidInvestigation.setId(investigationIdBase);
        kidInvestigation.setLeader(leaderTypeBase);

        List<InvestigationType> kidInvestigations = ImmutableList.of(kidInvestigation);

        Map<String, List<CompletedKtcEntry>> investigationIdToKtcEntries = null;

        CollaboratorType collaboratorType = CollaboratorType.Factory.newInstance();
        collaboratorType.setName(nameCollaborator);
        collaboratorType.setEmail(emailCollaborator);

        List<CollaboratorType> collaboratorTypes = ImmutableList.of(collaboratorType);

        CollaboratorListType collaboratorListType = CollaboratorListType.Factory.newInstance();
        collaboratorListType.setCollaboratorArray(collaboratorTypes.toArray(new CollaboratorType[0]));

        InvestigationType baseInvestigation = InvestigationType.Factory.newInstance();
        baseInvestigation.setId(investigationIdBase);
        baseInvestigation.setLeader(leaderTypeBase);
        baseInvestigation.setCollaborators(collaboratorListType);

        Map<String, InvestigationType> baseInvestigationIdToBaseInvestigation = ImmutableMap.of(
            investigationIdBase, baseInvestigation);

        KidRuleCollaborators kidRuleCollaborators = new KidRuleCollaborators();
        kidRuleCollaborators.apply(kidInvestigations,
            investigationIdToKtcEntries, baseInvestigationIdToBaseInvestigation);

        CollaboratorType expectedCollaboratorType2 = CollaboratorType.Factory.newInstance();
        expectedCollaboratorType2.setName(nameCollaborator);
        expectedCollaboratorType2.setEmail(emailCollaborator);

        List<CollaboratorType> expectedCollaboratorTypes = ImmutableList.of(expectedCollaboratorType2);

        CollaboratorListType expectedCollaboratorListType = CollaboratorListType.Factory.newInstance();
        expectedCollaboratorListType.setCollaboratorArray(expectedCollaboratorTypes.toArray(new CollaboratorType[0]));

        InvestigationType expectedKidInvestigation = InvestigationType.Factory.newInstance();
        expectedKidInvestigation.setId(investigationIdBase);
        expectedKidInvestigation.setLeader(leaderTypeBase);
        expectedKidInvestigation.setCollaborators(expectedCollaboratorListType);

        List<InvestigationType> expectedKidInvestigations = ImmutableList.of(expectedKidInvestigation);

        assertEquals(expectedKidInvestigations.toString(),
            kidInvestigations.toString());
    }

    @Test
    public void testApplyForJointInvestigation() {
        String investigationIdBase = "investigationIdBase";
        String investigationIdJoint = "investigationIdBase_investigationIdBase";

        String nameBaseLeader = "nameBaseLeader";
        String nameJointLeader = "nameJointLeader";
        String nameCollaborator = "nameCollaborator";

        String emailBaseLeader = "emailBaseLeader";
        String emailJointLeader = "emailJointLeader";
        String emailCollaborator = "emailCollaborator";

        LeaderType leaderTypeBase = LeaderType.Factory.newInstance();
        leaderTypeBase.setName(nameBaseLeader);
        leaderTypeBase.setEmail(emailBaseLeader);

        LeaderType leaderTypeJoint = LeaderType.Factory.newInstance();
        leaderTypeJoint.setName(nameJointLeader);
        leaderTypeJoint.setEmail(emailJointLeader);

        InvestigationType kidInvestigation = InvestigationType.Factory.newInstance();
        kidInvestigation.setId(investigationIdJoint);
        kidInvestigation.setLeader(leaderTypeJoint);

        List<InvestigationType> kidInvestigations = ImmutableList.of(kidInvestigation);

        Map<String, List<CompletedKtcEntry>> investigationIdToKtcEntries = null;

        CollaboratorType collaboratorType = CollaboratorType.Factory.newInstance();
        collaboratorType.setName(nameCollaborator);
        collaboratorType.setEmail(emailCollaborator);

        CollaboratorType collaboratorType2 = CollaboratorType.Factory.newInstance();
        collaboratorType2.setName(nameJointLeader);
        collaboratorType2.setEmail(emailJointLeader);

        List<CollaboratorType> collaboratorTypes = ImmutableList.of(
            collaboratorType, collaboratorType2);

        CollaboratorListType collaboratorListType = CollaboratorListType.Factory.newInstance();
        collaboratorListType.setCollaboratorArray(collaboratorTypes.toArray(new CollaboratorType[0]));

        InvestigationType baseInvestigation = InvestigationType.Factory.newInstance();
        baseInvestigation.setId(investigationIdBase);
        baseInvestigation.setLeader(leaderTypeBase);
        baseInvestigation.setCollaborators(collaboratorListType);

        Map<String, InvestigationType> baseInvestigationIdToBaseInvestigation = ImmutableMap.of(
            investigationIdBase, baseInvestigation);

        KidRuleCollaborators kidRuleCollaborators = new KidRuleCollaborators();
        kidRuleCollaborators.apply(kidInvestigations,
            investigationIdToKtcEntries, baseInvestigationIdToBaseInvestigation);

        CollaboratorType expectedCollaboratorType1 = CollaboratorType.Factory.newInstance();
        expectedCollaboratorType1.setName(nameBaseLeader);
        expectedCollaboratorType1.setEmail(emailBaseLeader);

        CollaboratorType expectedCollaboratorType2 = CollaboratorType.Factory.newInstance();
        expectedCollaboratorType2.setName(nameCollaborator);
        expectedCollaboratorType2.setEmail(emailCollaborator);

        List<CollaboratorType> expectedCollaboratorTypes = ImmutableList.of(
            expectedCollaboratorType1, expectedCollaboratorType2);

        CollaboratorListType expectedCollaboratorListType = CollaboratorListType.Factory.newInstance();
        expectedCollaboratorListType.setCollaboratorArray(expectedCollaboratorTypes.toArray(new CollaboratorType[0]));

        InvestigationType expectedKidInvestigation = InvestigationType.Factory.newInstance();
        expectedKidInvestigation.setId(investigationIdJoint);
        expectedKidInvestigation.setLeader(leaderTypeJoint);
        expectedKidInvestigation.setCollaborators(expectedCollaboratorListType);

        List<InvestigationType> expectedKidInvestigations = ImmutableList.of(expectedKidInvestigation);

        assertEquals(expectedKidInvestigations.toString(),
            kidInvestigations.toString());
    }

}
