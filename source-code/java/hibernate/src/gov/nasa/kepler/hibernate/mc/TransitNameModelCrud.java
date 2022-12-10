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

package gov.nasa.kepler.hibernate.mc;

import gov.nasa.kepler.hibernate.AbstractCrud;
import gov.nasa.kepler.hibernate.dbservice.DatabaseService;
import gov.nasa.kepler.hibernate.pi.ModelCrud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

/**
 * Contains {@link TransitNameModel} data access operations.
 * 
 * @author Forrest Girouard
 */
public class TransitNameModelCrud extends AbstractCrud implements
    ModelCrud<TransitNameModel> {

    public static final String TYPE = "TRANSIT_NAME";

    public TransitNameModelCrud() {
        this(null);
    }

    public TransitNameModelCrud(DatabaseService databaseService) {
        super(databaseService);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void create(TransitNameModel model) {
        List<TransitName> transitNames = create(model.getTransitNames());

        TransitNameModel transitNameModelToCreate = new TransitNameModel(
            model.getRevision(), transitNames);
        getSession().save(transitNameModelToCreate);
    }

    /**
     * Creates the {@link TransitName}s in the database. If a
     * {@link TransitName} already exists in the database, then it is not
     * re-created.
     * 
     * @param transitNames
     * @return new {@link List} of {@link TransitName}s which should be used in
     * place of the input {@link List} {@link TransitName}s.
     */
    private List<TransitName> create(List<TransitName> transitNames) {
        Map<TransitName, TransitName> transitNameKeyToTransitNameValue = new HashMap<TransitName, TransitName>();
        for (TransitName transitName : retrieveAllTransitNames()) {
            transitNameKeyToTransitNameValue.put(transitName, transitName);
        }

        List<TransitName> transitNamesToReturn = new ArrayList<TransitName>();
        for (TransitName transitName : transitNames) {
            TransitName transitNameValue = transitNameKeyToTransitNameValue.get(transitName);
            if (transitNameValue == null) {
                transitNameValue = transitName;
                transitNameKeyToTransitNameValue.put(transitName, transitName);
                getSession().save(transitName);
            }

            transitNamesToReturn.add(transitNameValue);
        }

        return transitNamesToReturn;
    }

    private List<TransitName> retrieveAllTransitNames() {
        Query query = getSession().createQuery("from TransitName");

        List<TransitName> list = list(query);

        return list;
    }

    @Override
    public TransitNameModel retrieve(int revision) {
        Query q = getSession().createQuery(
            "from TransitNameModel where " + "revision = :revision ");
        q.setParameter("revision", revision);

        TransitNameModel model = uniqueResult(q);

        return model;
    }

    @Override
    public void delete(TransitNameModel model) {
        getSession().delete(model);
    }
}
