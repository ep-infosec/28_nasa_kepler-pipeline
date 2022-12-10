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

package gov.nasa.kepler.ar.exporter.flux2;

import gov.nasa.kepler.ar.exporter.DefaultSingleQuarterTargetExporterSource;
import gov.nasa.kepler.common.pi.FluxTypeParameters.FluxType;
import gov.nasa.kepler.fc.gain.GainOperations;
import gov.nasa.kepler.fc.readnoise.ReadNoiseOperations;
import gov.nasa.kepler.hibernate.dr.LogCrud;
import gov.nasa.kepler.hibernate.gar.CompressionCrud;
import gov.nasa.kepler.hibernate.pdc.PdcCrud;
import gov.nasa.kepler.hibernate.tad.TargetCrud;
import gov.nasa.kepler.hibernate.tad.TargetTable;
import gov.nasa.kepler.hibernate.tad.UnifiedObservedTargetCrud;
import gov.nasa.kepler.mc.PdcProcessingCharacteristics;
import gov.nasa.kepler.mc.cm.CelestialObjectOperations;
import gov.nasa.kepler.mc.configmap.ConfigMapOperations;
import gov.nasa.kepler.mc.dr.DataAnomalyOperations;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Default implementations of the FluxTargetExporterSource.
 * 
 * @author Sean McCauliff
 *
 */
public abstract class DefaultFluxExporterSource extends DefaultSingleQuarterTargetExporterSource
        implements FluxExporterSource {

    private final PdcCrud pdcCrud;
    private volatile Map<Integer, PdcProcessingCharacteristics> keplerIdToPdcProcessingC;
    
     public DefaultFluxExporterSource(DataAnomalyOperations anomalyOperations,
                ConfigMapOperations configMapOps, TargetCrud targetCrud,
                CompressionCrud compressionCrud, GainOperations gainOps,
                ReadNoiseOperations readNoiseOps, TargetTable targetTable,
                LogCrud logCrud, CelestialObjectOperations celestialObjectOps, 
                int startKeplerId, int endKeplerId,
                UnifiedObservedTargetCrud unifiedCrud,
                PdcCrud pdcCrud,
                int k2Campaign) {
        super(anomalyOperations, configMapOps, targetCrud, compressionCrud, gainOps,
                readNoiseOps, targetTable, logCrud, celestialObjectOps,
                startKeplerId, endKeplerId, unifiedCrud, k2Campaign);
        this.pdcCrud = pdcCrud;
    }
     
    @Override
    public Map<Integer, PdcProcessingCharacteristics> pdcProcessingCharacteristics() {
        if (keplerIdToPdcProcessingC != null) {
            return keplerIdToPdcProcessingC;
        }
        
        List<gov.nasa.kepler.hibernate.pdc.PdcProcessingCharacteristics> characteristics = 
            pdcCrud.retrievePdcProcessingCharacteristics(FluxType.SAP,
            cadenceType(), keplerIds(), startCadence(), endCadence());
        
        keplerIdToPdcProcessingC = Maps.newHashMapWithExpectedSize(characteristics.size());
        for (gov.nasa.kepler.hibernate.pdc.PdcProcessingCharacteristics targetChar : characteristics) {
            if (targetChar == null) {
                continue;
            }
            keplerIdToPdcProcessingC.put(targetChar.getKeplerId(), new PdcProcessingCharacteristics(targetChar));
        }
        
        return keplerIdToPdcProcessingC;
    }

}
