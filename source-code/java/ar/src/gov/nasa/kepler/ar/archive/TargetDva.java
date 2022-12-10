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

package gov.nasa.kepler.ar.archive;

import gov.nasa.spiffy.common.persistable.Persistable;

/**
 * The DVA time series for a target.
 * 
 * @author Sean McCauliff
 *
 */
public class TargetDva implements Persistable {

    private int keplerId;
    /** The following are all nCadences long. */
    private float[] columnDva;
    private boolean[] columnGapIndicator;
    private float[] rowDva;
    private boolean[] rowGapIndicator;
    
    public TargetDva(int keplerId, float[] columnDva,
        boolean[] columnGapIndicator, float[] rowDva, boolean[] rowGapIndicator) {
        super();
        this.keplerId = keplerId;
        this.columnDva = columnDva;
        this.columnGapIndicator = columnGapIndicator;
        this.rowDva = rowDva;
        this.rowGapIndicator = rowGapIndicator;
    }
    
    
    /**
     * Don't use this constructor.
     */
    public TargetDva() {
        
    }


    public int getKeplerId() {
        return keplerId;
    }


    public void setKeplerId(int keplerId) {
        this.keplerId = keplerId;
    }


    public float[] getColumnDva() {
        return columnDva;
    }


    public void setColumnDva(float[] columnDva) {
        this.columnDva = columnDva;
    }


    public boolean[] getColumnGapIndicator() {
        return columnGapIndicator;
    }


    public void setColumnGapIndicator(boolean[] columnGapIndicator) {
        this.columnGapIndicator = columnGapIndicator;
    }


    public float[] getRowDva() {
        return rowDva;
    }


    public void setRowDva(float[] rowDva) {
        this.rowDva = rowDva;
    }


    public boolean[] getRowGapIndicator() {
        return rowGapIndicator;
    }


    public void setRowGapIndicator(boolean[] rowGapIndicator) {
        this.rowGapIndicator = rowGapIndicator;
    }


    public void fillGaps(float gapFill) {
        for (int i=0; i < rowDva.length; i++) {
            if (rowGapIndicator[i]) {
                rowDva[i] = gapFill;
            }
        }
        for (int i=0; i < columnDva.length; i++) {
            if (columnGapIndicator[i]) {
                columnDva[i] = gapFill;
            }
        }
    }
    
    
}
