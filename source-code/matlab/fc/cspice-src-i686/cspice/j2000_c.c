/*

-Procedure  j2000_c ( Julian Date of 2000 JAN 1.5 )

-Abstract

   Return the Julian Date of 2000 JAN 01 12:00:00 (2000 JAN 1.5).

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

   CONSTANTS

*/

   #include "SpiceUsr.h"

   SpiceDouble j2000_c ( void )

/*

-Brief_I/O

   The function returns the Julian Date of 2000 JAN 01 12:00:00
   (2000 JAN 1.5).

-Detailed Input

   None.

-Detailed Output

   The function returns 2451545.0, the Julian Date corresponding
   to 2000 JAN 01 12:00:00 (2000 JAN 1.5).

-Files

   None.

-Exceptions

   Error free.

-Particulars

   The function always returns the constant value shown above.

-Examples

   The following code fragment illustrates the use of j2000_c().


      Convert Julian ephemeris date to TDB seconds past the reference
      epoch (J2000).

      spref = ( jed - j2000_c() ) * spd_c();


-Restrictions

   None.

-Literature_References

   None.

-Author_and_Institution

   E.D. Wright     (JPL)
   W.L. Taber      (JPL)
   I.M. Underwood  (JPL)

-Version

   -CSPICE Version 1.0.0, 08-FEB-1998 (EDW)

-Index_Entries

   julian date of 2000 jan 1.5

-&
*/


{ /* Begin j2000_c */


   return  2451545.;


} /* End j2000_c */
