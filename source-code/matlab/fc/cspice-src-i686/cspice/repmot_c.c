/*

-Procedure repmot_c  ( Replace marker with ordinal text )

-Abstract
 
   Replace a marker with the text representation of an ordinal number.
 
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
 
   CHARACTER 
   CONVERSION 
   STRING 
 
*/

   #include "SpiceUsr.h"
   #include "SpiceZfc.h"
   #include "SpiceZst.h"
   #include "SpiceZmc.h"


   void repmot_c ( ConstSpiceChar   * in,
                   ConstSpiceChar   * marker,
                   SpiceInt           value,
                   SpiceChar          repcase,
                   SpiceInt           lenout,
                   SpiceChar        * out      ) 

/*

-Brief_I/O
 
   VARIABLE  I/O  DESCRIPTION 
   --------  ---  -------------------------------------------------- 
   in         I   Input string. 
   marker     I   Marker to be replaced. 
   value      I   Replacement value.
   repcase    I   Case of replacement text. 
   lenout     I   Available space in output string.
   out        O   Output string. 
   MAXLON     P   Maximum length of an ordinal number. 
 
-Detailed_Input
 
   in             is an arbitrary character string. 
 
   marker         is an arbitrary character string. The first 
                  occurrence of marker in the input string is 
                  to be replaced by the text representation of 
                  the ordinal number value. 
 
                  Leading and trailing blanks in marker are not 
                  significant. In particular, no substitution is 
                  performed if marker is blank or empty. 
 
   value          is an arbitrary integer. 
 
   repcase        indicates the case of the replacement text. 
                  repcase may be any of the following: 
 
                     repcase   Meaning       Example 
                     -------   -----------   ----------------------- 
                     U, u      Uppercase     ONE HUNDRED FIFTY-THREE 
 
                     L, l      Lowercase     one hundred fifty-three 
 
                     C, c      Capitalized   One hundred fifty-three 
 
   lenout         is the allowed length of the output string.  This
                  length must large enough to hold the output string
                  plus the terminator.  If the output string is
                  expected to have x characters, lenout should be at
                  least x + 1. 
-Detailed_Output
 
   out            is the string obtained by substituting the text 
                  representation of the ordinal number value for 
                  the first occurrence of marker in the input string. 
 
                  out and in must be identical or disjoint. 
 
-Parameters
 
   MAXLON         is the maximum expected length of any ordinal 
                  text. 147 characters are sufficient to hold the 
                  text representing any ordinal value whose 
                  corresponding ordinal value is in the range 
 
                    ( -10**12, 10**12 ) 
 
                  An example of a number whose ordinal text 
                  representation is of maximum length is 
 
                     - 777 777 777 777 
-Files
 
   None. 
 
-Exceptions
 
   1) The error SPICE(NULLPOINTER) is signaled if any of 
      the input or output string pointers is null.

   2) If the marker string is blank or empty, this routine leaves 
      the input string unchanged, except that trailing blanks
      will be trimmed.  This case is not considered an error.

   3) If the output string is too short to accommodate a terminating
      null character, the error SPICE(STRINGTOOSHORT) is signaled.

   4) If out does not have sufficient length to accommodate the 
      result of the substitution, the result will be truncated on 
      the right. 
 
   5) If the value of repcase is not recognized, the error 
      will be diagnosed by routines in the call tree of this 
      routine.  out is not changed. 
 
-Particulars
 
   This is one of a family of related routines for inserting values 
   into strings. They are typically to construct messages that 
   are partly fixed, and partly determined at run time. For example, 
   a message like 
 
      "The fifty-first picture was found in directory [USER.DATA]." 
 
   might be constructed from the variable string 
 
      "The #1 picture was found in directory #2." 
 
   by the calls 
 
      repmot_c ( string, "#1",  51,  'L',      LENOUT, string );
      repmc_c  ( string, "#2", "[USER.DATA]",  LENOUT, string );
 
   which substitute the ordinal text "fifty-first" and the character 
   string "[USER.DATA]" for the markers "#1" and "#2" respectively. 
 
   The complete list of routines is shown below. 
 
      repmc_c  ( Replace marker with character string value ) 
      repmd_c  ( Replace marker with double precision value ) 
      repmf_c  ( Replace marker with formatted d.p. value   ) 
      repmi_c  ( Replace marker with integer value          ) 
      repmct_c ( Replace marker with cardinal text          ) 
      repmot_c ( Replace marker with ordinal text           ) 
 
-Examples
 
   The following examples illustrate the use of repmot_c to 
   replace a marker within a string with the the ordinal text 
   corresponding to an integer. 
 
   Uppercase 
   --------- 
 
      Let 
 
         marker = "#" 
         in     = "Invalid command.  The # word was not recognized." 
 
      Then following the call, 
              .
              .
              .
         #define   LENOUT                  201
              .
              .
              .
         repmot_c ( in, "#", 5, 'U', LENOUT, in );
  
      in is 
 
         "Invalid command.  The FIFTH word was not recognized." 
 
   Lowercase 
   --------- 
 
      Let 
 
         marker = " XX " 
         in     = "The XX word of the XX sentence was misspelled." 
 
      Then following the call, 
 
         repmot_c ( in, "  XX  ", 5, 'L', LENOUT, out );
 
      OUT is 
 
         "The fifth word of the XX sentence was misspelled." 
 
 
   Capitalized 
   ----------- 
 
      Let 
 
         marker == " XX " 
         in     == "Name:  YY.  Rank:  XX." 
 
      Then following the calls, 
 
        #include "SpiceUsr.h"
              .
              .
              .
         #define   LENOUT                  201
              .
              .
              .
         repmc_c  ( in,  "YY", "Moriarty", LENOUT, out );
         repmct_c ( out, "XX",  1,  'C',   LENOUT, out );

      out is 

         "Name:  Moriarty.  Rank:  First." 
 
-Restrictions
 
   1) value must be in the range accepted by subroutine intord_. 
      This range is currently 
 
         ( -10**12, 10**12 ) 
 
      Note that the endpoints of the interval are excluded. 
 
-Literature_References
 
   None. 
 
-Author_and_Institution
 
   N.J. Bachman   (JPL) 
   I.M. Underwood (JPL) 
 
-Version
 
   -CSPICE Version 1.0.0, 14-AUG-2002 (NJB) (IMU)

-Index_Entries
 
   replace marker with ordinal text 
 
-&
*/

{ /* Begin repmot_c */

   /*
   Local variables 
   */
   ConstSpiceChar        * markPtr;


   /*
   Use discovery check-in. 

   Make sure no string argument pointers are null.
   */
   CHKPTR( CHK_DISCOVER, "repmot_c", in     );
   CHKPTR( CHK_DISCOVER, "repmot_c", marker );
   CHKPTR( CHK_DISCOVER, "repmot_c", out    );


   /*
   If the output string can't hold a terminating null character,
   we can't proceed. 
   */
   if ( lenout < 1 )
   {
      chkin_c  ( "repmot_c"                                   );
      setmsg_c ( "String length lenout must be >= 1; actual "
                 "value = #."                                 );
      errint_c ( "#", lenout                                  );
      sigerr_c ( "SPICE(STRINGTOOSHORT)"                      );
      chkout_c ( "repmot_c"                                   );
      return;
   }


   /*
   If the output string has no room for data characters, we simply
   terminate the string.
   */
   if ( lenout == 1 )
   {
      out[0] = NULLCHAR;
      return;
   }


   /*
   If the input string has zero length, the output is empty as well. 
   */
   if ( in[0] == NULLCHAR )
   {
      out[0] = NULLCHAR;

      return;
   }


   /*
   If the marker is empty, pass a blank marker to the f2c'd routine.
   Otherwise, pass in the marker.
   */
   if ( marker[0] == NULLCHAR )
   {
      markPtr = " ";
   }
   else
   {
      markPtr = marker;
   }
   
   /*
   Simply call the f2c'd routine. 
   */
   repmot_ ( ( char     * ) in,
             ( char     * ) markPtr,
             ( integer  * ) &value,
             ( char     * ) &repcase,
             ( char     * ) out,
             ( ftnlen     ) strlen(in),
             ( ftnlen     ) strlen(markPtr),
             ( ftnlen     ) 1,
             ( ftnlen     ) lenout-1         );

   /*
   Convert the output string from Fortran to C style. 
   */
   F2C_ConvertStr ( lenout, out );
   
 
} /* End repmot_c */
