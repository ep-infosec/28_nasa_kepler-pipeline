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

package gov.nasa.kepler.systest.sbt.data;

import static com.google.common.collect.Lists.newArrayList;
import gov.nasa.kepler.cal.io.HuffmanTable;
import gov.nasa.kepler.common.ConfigMap;
import gov.nasa.kepler.mc.gar.RequantTable;

import java.util.List;

/**
 * This class contains metadata for the spacecraft.
 * 
 * @author Miles Cote
 * 
 */
public class SbtSpacecraftMetadata implements SbtDataContainer {

    private List<ConfigMap> configMaps = newArrayList();
    private List<RequantTable> requantTables = newArrayList();
    private List<HuffmanTable> huffmanTables = newArrayList();

    @Override
    public String toMissingDataString(ToMissingDataStringParameters parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.toString();
    }

    public SbtSpacecraftMetadata() {
    }

    public SbtSpacecraftMetadata(List<ConfigMap> configMaps,
        List<RequantTable> requantTables, List<HuffmanTable> huffmanTables) {
        this.configMaps = configMaps;
        this.requantTables = requantTables;
        this.huffmanTables = huffmanTables;
    }

    public List<ConfigMap> getConfigMaps() {
        return configMaps;
    }

    public void setConfigMaps(List<ConfigMap> configMaps) {
        this.configMaps = configMaps;
    }

    public List<RequantTable> getRequantTables() {
        return requantTables;
    }

    public void setRequantTables(List<RequantTable> requantTables) {
        this.requantTables = requantTables;
    }

    public List<HuffmanTable> getHuffmanTables() {
        return huffmanTables;
    }

    public void setHuffmanTables(List<HuffmanTable> huffmanTables) {
        this.huffmanTables = huffmanTables;
    }

}
