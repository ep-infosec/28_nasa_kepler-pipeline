/*

-Procedure byebye_ ( Exit a program indicating an error status )

-Abstract
 
   Exit an executing program returning a success or failure status 
   to the operating system.  Supports f2c'd code whose Fortran 
   counterpart calls the SPICELIB routine byebye. 
 
-Disclaimer

   THIS SOFTWARE AND ANY RELATED MATERIALS WERE CREATED BY THE
   CALIFORNIA INSTITUTE OF TECHNOLOGY (CALTECH) UNDER A U.S.
   GOVERNMENT CONTRACT WITH THE NATIONAL AERONAUTICS AND SPACE
   ADMINISTRATION (NASA). THE SOFTWARE IS TECHNOLOGY AND SOFTWARE
   PUBLICLY AVAILABLE UNDER U.S. EXPORT LAWS AND IS PROVIDED "AS-IS"
   TO THE RECIPIENT WITHOUT WARRANTY OF ANY KIND, INCLUDING ANY
   WARRANTIES OF PERFORMANCE OR MERCHANTABILITY OR FITNESS FOR A
   PARTICULAR USE OR PURPOSE (AS SET FORTH IN UNITED STATES UCC
   SECTIONS 2312-2313) OR FOR ANY PURPOSE WHATSOEVER, FOR THE
   SOFTWARE AND RELATED MATERIALS, HOWEVER USED.

   IN NO EVENT SHALL CALTECH, ITS JET PROPULSION LABORATORY, OR NASA
   BE LIABLE FOR ANY DAMAGES AND/OR COSTS, INCLUDING, BUT NOT
   LIMITED TO, INCIDENTAL OR CONSEQUENTIAL DAMAGES OF ANY KIND,
   INCLUDING ECONOMIC DAMAGE OR INJURY TO PROPERTY AND LOST PROFITS,
   REGARDLESS OF WHETHER CALTECH, JPL, OR NASA BE ADVISED, HAVE
   REASON TO KNOW, OR, IN FACT, SHALL KNOW OF THE POSSIBILITY.

   RECIPIENT BEARS ALL RISK RELATING TO QUALITY AND PERFORMANCE OF
   THE SOFTWARE AND ANY RELATED MATERIALS, AND AGREES TO INDEMNIFY
   CALTECH AND NASA FOR ALL THIRD-PARTY CLAIMS RESULTING FROM THE
   ACTIONS OF RECIPIENT IN THE USE OF THE SOFTWARE.

-Required_Reading
 
   None. 
 
-Keywords
 
   UTILITY 
 
*/
   #include <stdlib.h>
   #include "SpiceUsr.h"
   #include "SpiceZfc.h"

   int byebye_ ( char *status,   ftnlen statusLen ) 

/*

-Brief_I/O
 
   Variable  I/O  Description 
   --------  ---  -------------------------------------------------- 
   status     I   A string indicating the exit status of a program. 
	statusLen  I   Length of status string.
 
-Detailed_Input
 
   status     This is a character string which indicates the status 
				  to use when exiting a program. The two status values 
				  currently supported are "SUCCESS" and "FAILURE", which 
				  have their obvious meanings. The case of the input is 
				  not important, i.e., "Success" or "failure" are accepted. 
	 
				  If STATUS has a value of "SUCCESS", then the calling 
				  program will be terminated with the ANSI stdlib.h status 
				  code EXIT_SUCCESS.
					
				  If STATUS has a value of "FAILURE", then the calling 
				  program will be terminated with the ANSI stdlib.h status 
				  code EXIT_FAILURE.
	 
				  If STATUS has a value that is not recognized, the calling 
				  program will be terminated with the ANSI stdlib.h status
				  code EXIT_FAILURE.
			  
			  
	statusLen  is the length of the string passed in via the first
	           argument status.  This argument is provided for 
				  compatibility with the signature generated by running
				  f2c on the Fortran version of byebye.
 
-Detailed_Output
 
   None. 
 
-Parameters
 
   None. 
 
-Exceptions
 
   Error free. 
   
   If the input status value is not recognized, the effect is the same
   as if the input status were "FAILURE".
 
-Files
 
   None. 
 
-Particulars
 
   This routine should not be called by user applications.  It exists
   solely for the use of CSPICE functions produced by running f2c 
   on Fortran code.
   
   This subroutine is called by sigerr_ to exit a program 
   returning a success or failure indication to the operating 
   system.  
 
-Examples
 
   To exit a program indicating success: 
 
      byebye_ ( "SUCCESS", 7 );
 
   To exit a program indicating failure: 
 
      byebye_ ( "FAILURE", 7 );
 
-Restrictions
 
   1) This function should not be called directly by user's application
      software.  
 
-Literature_References
 
   None. 
 
-Author_and_Institution
 
   N.J. Bachman       (JPL)
   K.R. Gehringer     (JPL) 
 
-Version
 
   -CSPICE Version 1.0.0, 04-NOV-1998 (NJB) (KRG)

-Index_Entries
 
   gracefully exit a program 
 
-&
*/

{ /* Begin byebye_ */



   if (  eqstr_c ( status, "SUCCESS" )  )
   {
      exit ( EXIT_SUCCESS );
   }
   else
   {
      exit ( EXIT_FAILURE );
   }
   
	return ( 0 );

} /* End byebye_ */

