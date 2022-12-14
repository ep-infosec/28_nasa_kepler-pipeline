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

#include "DebugController.h"

#include "libdebug.h"

#include <iostream>

DebugController::DebugController(){
}

DebugController::~DebugController(){
}

int DebugController::doScience( DebugInputs& inputs, DebugOutputs& outputs ){

	// Call application and library initialization. Perform this 
	// initialization before calling any API functions or
	// Compiler-generated libraries.
	if (!mclInitializeApplication(NULL,0)){
		std::cerr << "could not initialize the application properly"
			<< std::endl;
		return -1;
	}

	if( !libdebugInitialize() ){
		std::cerr << "could not initialize the library properly"
			<< std::endl;
		return -1;
	}

	try{

		// populate inputs
		mwArray _original( inputs.original.size(), 1, mxDOUBLE_CLASS, mxREAL);
		mwArray _scalar( 1, 1, mxDOUBLE_CLASS, mxREAL);

		_original.SetData( &inputs.original[0], inputs.original.size() );
		_scalar.SetData( &inputs.scalar, 1 );
		
		// create outputs
		mwArray _result;
		mwArray _func1;
		mwArray _func2;
		
		// invoke MATLAB function
		debug_module( 3, _result, _func1, _func2, _original, _scalar );
		
		// store outputs
		int resultSize = _result.NumberOfElements();
		outputs.result.resize( resultSize );
		_result.GetData( &outputs.result[0], resultSize );
		outputs.func1 = _func1.Get( 1, 1 );
		outputs.func2 = _func2.Get( 1, 1 );
		
	}catch (const mwException& e){
		std::cerr << e.what() << std::endl;
		return -2;
	}catch (...){
		std::cerr << "Unexpected error thrown" << std::endl;
		return -3;
	}     
	
	// Call the application and library termination routine
	libdebugTerminate();

	mclTerminateApplication();
	return 0;
}

