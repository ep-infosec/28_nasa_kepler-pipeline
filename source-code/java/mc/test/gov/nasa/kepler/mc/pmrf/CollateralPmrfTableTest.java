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

package gov.nasa.kepler.mc.pmrf;

import java.util.Arrays;
import java.util.List;

import gov.nasa.kepler.common.Cadence.CadenceType;
import gov.nasa.kepler.fs.api.FsId;
import gov.nasa.kepler.mc.fs.CalFsIdFactory;
import gov.nasa.kepler.mc.fs.DrFsIdFactory;
import gov.nasa.kepler.mc.pmrf.CollateralPmrfTable.Duplication;


import org.junit.Test;

import com.google.common.collect.ImmutableList;

import static org.junit.Assert.*;
import static gov.nasa.kepler.common.CollateralType.*;


public class CollateralPmrfTableTest {

    private static final int CCD_MODULE = 2;
    private static final int CCD_OUTPUT = 1;
    
    private CollateralPmrfTable longCadenceTable() {
        byte[] typeColumn = new byte[] {
            BLACK_LEVEL.byteValue(),
            MASKED_SMEAR.byteValue(),
            VIRTUAL_SMEAR.byteValue()};
        short[] offsets = new short[] { (short)7, (short)8, (short)9};
        
        return  new CollateralPmrfTable(CadenceType.LONG,
            CCD_MODULE, CCD_OUTPUT, typeColumn, offsets);
    }
    
    @Test
    public void testPixelOffsets() {
        CollateralPmrfTable collateralPmrfTable = longCadenceTable();
        List<Short> pixelOffsets = collateralPmrfTable.getPixelCoordinates(null);
        assertEquals(ImmutableList.of((short)7, (short)8, (short)9), pixelOffsets);
        pixelOffsets = collateralPmrfTable.getPixelCoordinates(MASKED_SMEAR);
        assertEquals(ImmutableList.of((short)8), pixelOffsets);
    }
    
    @Test
    public void testCosmicRayFsIds() {
        CollateralPmrfTable collateralPmrfTable = longCadenceTable();
        List<FsId> cosmicRayFsIds = collateralPmrfTable.getCosmicRayFsIds(BLACK_LEVEL);
        FsId expectedId = CalFsIdFactory.getCosmicRaySeriesFsId(BLACK_LEVEL,
            CadenceType.LONG, CCD_MODULE, CCD_OUTPUT, 7);
        assertEquals(ImmutableList.of(expectedId), cosmicRayFsIds);
    }
    
    @Test
    public void testPixelFsIds() {
        CollateralPmrfTable collateralPmrfTable = longCadenceTable();
        List<FsId> pixelFsIds = collateralPmrfTable.getPixelFsIds(VIRTUAL_SMEAR);
        FsId expectedId = DrFsIdFactory.getCollateralPixelTimeSeries(DrFsIdFactory.TimeSeriesType.ORIG,
            CadenceType.LONG, VIRTUAL_SMEAR, CCD_MODULE,
            CCD_OUTPUT, 9);
        assertEquals(ImmutableList.of(expectedId), pixelFsIds);
    }
    
    @Test
    public void testCalibratedPixelFsIds() {
        CollateralPmrfTable collateralPmrfTable = longCadenceTable();
        List<FsId> calFsIds = collateralPmrfTable.getCalibratedPixelFsIds(BLACK_LEVEL);
        FsId expectedFsId = CalFsIdFactory.getCalibratedCollateralFsId(BLACK_LEVEL,
            CalFsIdFactory.PixelTimeSeriesType.SOC_CAL,
            CadenceType.LONG, CCD_MODULE, CCD_OUTPUT, 7);
        assertEquals(ImmutableList.of(expectedFsId), calFsIds);
    }
    
    @Test
    public void testUncertainityPixelFsIds() {
        CollateralPmrfTable collateralPmrfTable = longCadenceTable();
        List<FsId> calFsIds = collateralPmrfTable.getCalibratedUncertainityFsIds(BLACK_LEVEL);
        FsId expectedFsId = 
            CalFsIdFactory.getCalibratedCollateralFsId(BLACK_LEVEL,
            CalFsIdFactory.PixelTimeSeriesType.SOC_CAL_UNCERTAINTIES,
            CadenceType.LONG, CCD_MODULE, CCD_OUTPUT, 7);
        assertEquals(ImmutableList.of(expectedFsId), calFsIds);
    }
    
    @Test
    public void testDeduplicatingPixels() {
        byte[] typeColumn = new byte[] {
            BLACK_LEVEL.byteValue(),
            BLACK_LEVEL.byteValue(),
            MASKED_SMEAR.byteValue(),
            VIRTUAL_SMEAR.byteValue(),
            VIRTUAL_SMEAR.byteValue() };
            short[] offsets = new short[] { (short)7, (short) 0, (short)8, (short)9, (short) 9};

            CollateralPmrfTable collateralPmrfTable = 
                new CollateralPmrfTable(CadenceType.LONG,
                    CCD_MODULE, CCD_OUTPUT, typeColumn, offsets, Duplication.NOT_ALLOWED);
            
            byte[] expectedTypes = new byte[] { BLACK_LEVEL.byteValue(),
                BLACK_LEVEL.byteValue(),
                MASKED_SMEAR.byteValue(), VIRTUAL_SMEAR.byteValue()
            };
            
            short[] expectedOffsets = new short[] { (short) 0, (short) 7,
                (short) 8, (short) 9
            };
            
            assertTrue(Arrays.equals(expectedTypes, collateralPmrfTable.getCollateralPixelType()));
            assertTrue(Arrays.equals(expectedOffsets, collateralPmrfTable.getCcdRowOrColColumn()));
            
    }
}
