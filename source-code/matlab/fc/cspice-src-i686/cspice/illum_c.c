/*

-Procedure illum_c ( Illumination angles )

-Abstract
 
   Find the illumination angles at a specified surface point of a 
   target body. 
 
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
 
   KERNEL 
   NAIF_IDS 
   SPK 
   TIME 
 
-Keywords
 
   GEOMETRY 
 
*/

   #include "SpiceUsr.h"
   #include "SpiceZfc.h"
   #include "SpiceZmc.h"
   #include "SpiceZim.h"
   #undef    illum_c


   void illum_c  ( ConstSpiceChar          * target,
                   SpiceDouble               et,
                   ConstSpiceChar          * abcorr, 
                   ConstSpiceChar          * obsrvr, 
                   ConstSpiceDouble          spoint [3],
                   SpiceDouble             * phase,
                   SpiceDouble             * solar,
                   SpiceDouble             * emissn     )
/*

-Brief_I/O
 
   Variable  I/O  Description 
   --------  ---  -------------------------------------------------- 
   target     I   Name of target body. 
   et         I   Epoch in ephemeris seconds past J2000. 
   abcorr     I   Desired aberration correction. 
   obsrvr     I   Name of observing body. 
   spoint     I   Body-fixed coordinates of a target surface point. 
   phase      O   Phase angle at the surface point. 
   solar      O   Solar incidence angle at the surface point. 
   emissn     O   Emission angle at the surface point. 
 
-Detailed_Input
 
   target         is the name of the target body.  `target' is
                  case-insensitive, and leading and trailing blanks in
                  `target' are not significant. Optionally, you may
                  supply a string containing the integer ID code for
                  the object. For example both "MOON" and "301" are
                  legitimate strings that indicate the moon is the
                  target body.
 
   et             is the epoch, specified in ephemeris seconds past 
                  J2000, at which the apparent illumination angles at 
                  the specified surface point on the target body, as 
                  seen from the observing body, are to be computed. 
 
   abcorr         is the aberration correction to be used in 
                  computing the location and orientation of the 
                  target body and the location of the Sun.  Possible 
                  values are: 
 
                     "NONE"        No aberration correction. 
 
                     "LT"          Correct the position and 
                                   orientation of target body for 
                                   light time, and correct the 
                                   position of the Sun for light 
                                   time. 
 
                     "LT+S"        Correct the observer-target vector 
                                   for light time and stellar 
                                   aberration, correct the 
                                   orientation of the target body 
                                   for light time, and correct the 
                                   target-Sun vector for light time 
                                   and stellar aberration. 
 
                     "CN"          Converged Newtonian light time
                                   corrections.  This is the same as LT
                                   corrections but with further
                                   iterations to a converged Newtonian
                                   light time solution. Given that
                                   relativistic effects may be as large
                                   as the higher accuracy achieved by
                                   this computation, this is correction
                                   is seldom worth the additional
                                   computations required unless the
                                   user incorporates additional
                                   relativistic corrections.  Both the
                                   state and rotation of the target
                                   body are corrected for light time.
 
                     "CN+S"        Converged Newtonian light time
                                   corrections and stellar aberration.
                                   Both the state and rotation of the
                                   target body are corrected for light
                                   time.
 
   obsrvr         is the name of the observing body.   This is
                  typically a spacecraft, the earth, or a surface point
                  on the earth.  `obsrvr' is case-insensitive, and
                  leading and trailing blanks in `obsrvr' are not
                  significant. Optionally, you may supply a string
                  containing the integer ID code for the object.  For
                  example both "EARTH" and "399" are legitimate strings
                  that indicate the earth is the observer.
 
                  `obsrvr' may be not be identical to `target'.
 
   spoint         is a surface point on the target body, expressed 
                  in rectangular body-fixed (body equator and prime 
                  meridian) coordinates.  `spoint' need not be visible 
                  from the observer's location at time `et'. 
 
-Detailed_Output
 
 
   phase          is the phase angle at `spoint', as seen from `obsrvr' 
                  at time `et'.  This is the angle between the 
                  spoint-obsrvr vector and the spoint-sun vector. 
                  Units are radians.  The range of `phase' is [0, pi]. 
                  See Particulars below for a detailed discussion of 
                  the definition.
 
   solar          is the solar incidence angle at `spoint', as seen 
                  from `obsrvr' at time `et'.  This is the angle 
                  between the surface normal vector at `spoint' and the 
                  spoint-sun vector.  Units are radians.  The range 
                  of `solar' is [0, pi]. See Particulars below for a 
                  detailed discussion of the definition.
 
   emissn         is the emission angle at `spoint', as seen from 
                  `obsrvr' at time `et'.  This is the angle between the 
                  surface normal vector at `spoint' and the 
                  spoint-observer vector.  Units are radians.  The 
                  range of `emissn' is [0, pi]. See Particulars below 
                  for a detailed discussion of the definition.

-Parameters
 
   None. 
 
-Exceptions
 
 
   1)  If `target' and `obsrvr' are not distinct, the error 
       SPICE(BODIESNOTDISTINCT) will be signaled. 
 
   2)  If no SPK (ephemeris) data are available for the observer, 
       target, and Sun at the time specified by `et', the error will 
       be diagnosed by routines called by this routine.  If light 
       time corrections are used, SPK data for the target body must 
       be available at the time et - lt, where `lt' is the one-way 
       light time from the target to the observer at `et'. 
       Additionally, SPK data must be available for the Sun at the 
       time et - lt - lt2, where lt2 is the light time from the Sun 
       to the target body at time et - lt. 
 
   3)  If PCK data defining the orientation or shape of the target 
       body are unavailable, the error will be diagnosed by routines 
       called by this routine. 
 
   4)  If no body-fixed frame is associated with the target body, 
       the error SPICE(NOFRAME) is signaled. 
 
   5)  If name of `target' or `obsrvr' cannot be translated to its 
       NAIF ID code, the error SPICE(IDCODENOTFOUND) is signaled. 
 
-Files
 
   No files are input to this routine.  However, illum_c expects 
   that the appropriate SPK and PCK files have been loaded via
   furnsh_c.

-Particulars
 
 
   The term "illumination angles" refers to following set of 
   angles: 
 
 
      solar incidence angle    Angle between the surface normal at the
                               specified surface point and the vector
                               from the surface point to the Sun.
 
      emission angle           Angle between the surface normal at the
                               specified surface point and the vector
                               from the surface point to the observer.
 
      phase angle              Angle between the vectors from the
                               surface point to the observing body and
                               from the surface point to the Sun.
 
 
   The diagram below illustrates the geometric relationships defining
   these angles.  The labels for the solar incidence, emission, and
   phase angles are "s.i.", "e.", and "phase".
 
 
                                                    * 
                                                   Sun 
 
                  surface normal vector 
                            ._                 _. 
                            |\                 /|  Sun vector 
                              \    phase      / 
                               \   .    .    / 
                               .            . 
                                 \   ___   / 
                            .     \/     \/ 
                                  _\ s.i./ 
                           .    /   \   / 
                           .   |  e. \ / 
       *             <--------------- *  surface point on 
    viewing            vector            target body 
    location           to viewing 
    (observer)         location 
 

   Note that if the target-observer vector, the target normal vector
   at the surface point, and the target-sun vector are coplanar, then
   phase is the sum of incidence and emission.  This is rarely true;
   usually 

      phase angle  <  solar incidence angle + emission angle

 
   All of the above angles can be computed using light time 
   corrections, light time and stellar aberration corrections, or 
   no aberration corrections.  The way aberration corrections 
   are used is described below. 
 
   Care must be used in computing light time corrections.  The 
   guiding principle used here is "describe what appears in 
   an image."  We ignore differential light time; the light times 
   from all points on the target to the observer are presumed to be 
   equal. 
 
 
      Observer-target body vector 
      --------------------------- 
 
      Let `et' be the epoch at which an observation or remote 
      sensing measurement is made, and let et - lt ("lt" stands 
      for "light time") be the epoch at which the photons received 
      at `et' were emitted from the body (we use the term "emitted" 
      loosely here). 
 
      The correct observer-target vector points from the observer's 
      location at `et' to the target body's location at et - lt. 
      The target-observer vector points in the opposite direction. 
 
      Since light time corrections are not symmetric, the correct 
      target-observer vector CANNOT be found by computing the light 
      time corrected position of the observer as seen from the 
      target body. 
 
 
      Target body's orientation 
      ------------------------- 
 
      Using the definitions of `et' and `lt' above, the target 
      body's orientation at et - lt is used.  The surface 
      normal is dependent on the target body's orientation, so 
      the body's orientation model must be evaluated for the correct 
      epoch. 
 
 
      Target body -- Sun vector 
      ------------------------- 
 
      All surface features on the target body will appear in 
      a measurement made at `et' as they were at et-lt.  In 
      particular, lighting on the target body is dependent on 
      the apparent location of the Sun as seen from the target 
      body at et-lt.  So, a second light time correction is used 
      in finding the apparent location of the Sun. 
 
 
   Stellar aberration corrections, when used, are applied as follows: 
 
 
      Observer-target body vector 
      --------------------------- 
 
      In addition to light time correction, stellar aberration is 
      used in computing the apparent target body position as seen 
      from the observer's location at time `et'.  This apparent 
      position defines the observer-target body vector. 
 
 
      Target body-Sun vector 
      ---------------------- 
 
      The target body-Sun vector is the apparent position of the Sun, 
      corrected for light time and stellar aberration, as seen from 
      the target body at time et-lt.  Note that the target body's 
      position is not affected by the stellar aberration correction 
      applied in finding its apparent position as seen by the 
      observer. 
 
 
   Once all of the vectors, as well as the target body's 
   orientation, have been computed with the proper aberration 
   corrections, the element of time is eliminated from the 
   computation.  The problem becomes a purely geometric one, 
   and is described by the diagram above. 
 
 
-Examples
 
   The numerical results shown for this example may differ across
   platforms.  The results depend on the SPICE kernels used as
   input, the compiler and supporting libraries, and the machine
   specific arithmetic implementation.

   In the following example program, the file

      spk_m_031103-040201_030502.bsp

   is a binary SPK file containing data for Mars Global Surveyor,
   Mars, and the Sun for a time interval bracketing the date

      2004 JAN 1 12:00:00 UTC.

   pck00007.tpc is a planetary constants kernel file containing
   radii and rotation model constants.  naif0007.tls is a
   leapseconds kernel.
   
   Find the phase, solar incidence, and emission angles at the
   sub-solar and sub-spacecraft points on Mars as seen from the Mars
   global surveyor spacecraft at a user-specified UTC time. Use light
   time and stellar aberration corrections.
 
      #include <string.h>
      #include <stdio.h>
      #include "SpiceUsr.h"

      int main()
      { 
         /.
         Local variables 
         ./
         SpiceChar             * obsrvr;
         SpiceChar             * target;
         SpiceChar             * utc;

         SpiceDouble             alt;
         SpiceDouble             et;
         SpiceDouble             sscemi;
         SpiceDouble             sscphs;
         SpiceDouble             sscsol;
         SpiceDouble             sslphs;
         SpiceDouble             sslsol;
         SpiceDouble             sslemi;
         SpiceDouble             ssolpt  [3];
         SpiceDouble             sscpt   [3];   


         /.
         Load kernel files.
         ./
         furnsh_c ( "naif0007.tls"                   );
         furnsh_c ( "pck00007.tpc"                   );
         furnsh_c ( "spk_m_031103-040201_030502.bsp" );
 
         /.
         Convert the UTC request time to ET (seconds past J2000 TDB).
         ./
         utc = "2004 JAN 1 12:00:00";

         str2et_c ( utc, &et );
                
         /.
         Assign observer and target names.  The acronym MGS
         indicates Mars Global Surveyor. See NAIF_IDS for a list
         of names recognized by SPICE.
         ./
         target = "Mars";
         obsrvr = "MGS";
  
         /.
         Find the sub-solar point on the Earth as seen from 
         the MGS spacecraft at et.  Use the "near point" 
         style of sub-point definition.  This makes it easy
         to verify the solar incidence angle.
         ./
         subsol_c ( "near point",  target,  et, 
                    "LT+S",        obsrvr,  ssolpt  );

         /.
         Now find the sub-spacecraft point.  Use the  
         "nearest point" definition of the sub-point  
         here---this makes it easy to verify the emission
         angle.
         ./                
         subpt_c  ( "near point",  target,  et, 
                    "LT+S",        obsrvr,  sscpt,  &alt ); 

         /.
         Find the phase, solar incidence, and emission 
         angles at the sub-solar point on the Earth as seen 
         from Mars Observer at time et. 
         ./
         illum_c ( target,  et,       "LT+S",    obsrvr,  
                   ssolpt,  &sslphs,  &sslsol,   &sslemi ); 

         /.
         Do the same for the sub-spacecraft point. 
         ./
         illum_c ( target,  et,       "LT+S",    obsrvr,  
                   sscpt,   &sscphs,  &sscsol,   &sscemi ); 

         /.
         Convert the angles to degrees and write them out. 
         ./
         sslphs *= dpr_c(); 
         sslsol *= dpr_c(); 
         sslemi *= dpr_c(); 

         sscphs *= dpr_c(); 
         sscsol *= dpr_c(); 
         sscemi *= dpr_c(); 

         printf ( "\n"
                  "UTC epoch is %s\n"
                  "\n"
                  "Illumination angles at the sub-solar point:\n"
                  "\n"
                  "Phase angle             (deg):  %f\n"
                  "Solar incidence angle   (deg):  %f\n"
                  "Emission angle          (deg):  %f\n"
                  "\n"
                  "The solar incidence angle should be 0.\n"
                  "The emission and phase angles should be "
                  "equal.\n"
                  "\n"
                  "\n"
                  "Illumination angles at the sub-s/c point:\n"
                  "\n"
                  "Phase angle             (deg):  %f\n"
                  "Solar incidence angle   (deg):  %f\n"
                  "Emission angle          (deg):  %f\n"
                  "\n"
                  "The emission angle should be 0.\n"
                  "The solar incidence and phase angles "
                  "should be equal.\n"
                  "\n"
                  "\n",
                  utc,
                  sslphs,
                  sslsol,
                  sslemi,                 
                  sscphs,
                  sscsol,
                  sscemi                                    );

         printf ( "\n" );

         return ( 0 );
      }
          
   
   When this program is executed, the output will be:


      UTC epoch is 2004 JAN 1 12:00:00

      Illumination angles at the sub-solar point:

      Phase angle             (deg):  150.210714
      Solar incidence angle   (deg):  0.000000
      Emission angle          (deg):  150.210714

      The solar incidence angle should be 0.
      The emission and phase angles should be equal.


      Illumination angles at the sub-s/c point:

      Phase angle             (deg):  123.398202
      Solar incidence angle   (deg):  123.398202
      Emission angle          (deg):  0.000000

      The emission angle should be 0.
      The solar incidence and phase angles should be equal.

          
-Restrictions

   None.
 
-Literature_References
 
   None. 
 
-Author_and_Institution
 
   C.H. Acton     (JPL)
   N.J. Bachman   (JPL) 
 
-Version
 
   -CSPICE Version 1.0.2, 22-JUL-2004 (NJB)

       Updated header to indicate that the `target' and `observer'
       input arguments can now contain string representations of
       integers.  

   -CSPICE Version 1.1.2, 27-JUL-2003   (NJB) (CHA)

       Various header corrections were made.  The example program
       was upgraded to use real kernels, and the program's output is
       shown.       

   -CSPICE Version 1.1.1, 04-SEP-2002   (NJB) 

       Updated Index_Entries header section.  Corrected error in
       erract_c call in header example.

   -CSPICE Version 1.1.0, 24-JUL-2001   (NJB)

       Changed prototype:  input spoint is now type 
       (ConstSpiceDouble [3]).  Implemented interface macro for 
       casting spoint array to const.

   -CSPICE Version 1.0.0, 25-MAY-1999 (NJB)

-Index_Entries
 
   illumination angles 
   lighting angles
   phase angle
   emission angle
   solar incidence angle
 
-&
*/

{ /* Begin illum_c */


   /*
   Participate in error tracing.
   */
   chkin_c ( "illum_c" );

   /*
   Check the input strings: target, abcorr, and obsrvr.  Make sure
   none of the pointers are null and that each string contains at 
   least one non-null character.
   */
   CHKFSTR ( CHK_STANDARD, "illum_c", target );
   CHKFSTR ( CHK_STANDARD, "illum_c", abcorr );
   CHKFSTR ( CHK_STANDARD, "illum_c", obsrvr );


   /*
   Call the f2c'd routine.
   */
   illum_ (  ( char         * ) target,
             ( doublereal   * ) &et, 
             ( char         * ) abcorr, 
             ( char         * ) obsrvr, 
             ( doublereal   * ) spoint, 
             ( doublereal   * ) phase, 
             ( doublereal   * ) solar, 
             ( doublereal   * ) emissn, 
             ( ftnlen         ) strlen(target), 
             ( ftnlen         ) strlen(abcorr), 
             ( ftnlen         ) strlen(obsrvr)  );
                         

   chkout_c ( "illum_c" );

} /* End illum_c */

