/* spkezr.f -- translated by f2c (version 19980913).
   You must link the resulting object file with the libraries:
	-lf2c -lm   (in that order)
*/

#include "f2c.h"

/* $Procedure SPKEZR ( S/P Kernel, easier reader ) */
/* Subroutine */ int spkezr_(char *targ, doublereal *et, char *ref, char *
	abcorr, char *obs, doublereal *starg, doublereal *lt, ftnlen targ_len,
	 ftnlen ref_len, ftnlen abcorr_len, ftnlen obs_len)
{
    /* Builtin functions */
    /* Subroutine */ int s_copy(char *, char *, ftnlen, ftnlen);
    integer s_cmp(char *, char *, ftnlen, ftnlen);

    /* Local variables */
    extern /* Subroutine */ int zzbodn2c_(char *, integer *, logical *, 
	    ftnlen), chkin_(char *, ftnlen);
    integer obsid;
    extern logical beint_(char *, ftnlen);
    extern /* Subroutine */ int errch_(char *, char *, ftnlen, ftnlen);
    logical found;
    char error[80];
    extern /* Subroutine */ int spkez_(integer *, doublereal *, char *, char *
	    , integer *, doublereal *, doublereal *, ftnlen, ftnlen);
    integer targid;
    extern /* Subroutine */ int sigerr_(char *, ftnlen), nparsi_(char *, 
	    integer *, char *, integer *, ftnlen, ftnlen), chkout_(char *, 
	    ftnlen), setmsg_(char *, ftnlen);
    extern logical return_(void);
    integer ptr;

/* $ Abstract */

/*     Return the state (position and velocity) of a target body */
/*     relative to an observing body, optionally corrected for light */
/*     time (planetary aberration) and stellar aberration. */

/* $ Disclaimer */

/*     THIS SOFTWARE AND ANY RELATED MATERIALS WERE CREATED BY THE */
/*     CALIFORNIA INSTITUTE OF TECHNOLOGY (CALTECH) UNDER A U.S. */
/*     GOVERNMENT CONTRACT WITH THE NATIONAL AERONAUTICS AND SPACE */
/*     ADMINISTRATION (NASA). THE SOFTWARE IS TECHNOLOGY AND SOFTWARE */
/*     PUBLICLY AVAILABLE UNDER U.S. EXPORT LAWS AND IS PROVIDED "AS-IS" */
/*     TO THE RECIPIENT WITHOUT WARRANTY OF ANY KIND, INCLUDING ANY */
/*     WARRANTIES OF PERFORMANCE OR MERCHANTABILITY OR FITNESS FOR A */
/*     PARTICULAR USE OR PURPOSE (AS SET FORTH IN UNITED STATES UCC */
/*     SECTIONS 2312-2313) OR FOR ANY PURPOSE WHATSOEVER, FOR THE */
/*     SOFTWARE AND RELATED MATERIALS, HOWEVER USED. */

/*     IN NO EVENT SHALL CALTECH, ITS JET PROPULSION LABORATORY, OR NASA */
/*     BE LIABLE FOR ANY DAMAGES AND/OR COSTS, INCLUDING, BUT NOT */
/*     LIMITED TO, INCIDENTAL OR CONSEQUENTIAL DAMAGES OF ANY KIND, */
/*     INCLUDING ECONOMIC DAMAGE OR INJURY TO PROPERTY AND LOST PROFITS, */
/*     REGARDLESS OF WHETHER CALTECH, JPL, OR NASA BE ADVISED, HAVE */
/*     REASON TO KNOW, OR, IN FACT, SHALL KNOW OF THE POSSIBILITY. */

/*     RECIPIENT BEARS ALL RISK RELATING TO QUALITY AND PERFORMANCE OF */
/*     THE SOFTWARE AND ANY RELATED MATERIALS, AND AGREES TO INDEMNIFY */
/*     CALTECH AND NASA FOR ALL THIRD-PARTY CLAIMS RESULTING FROM THE */
/*     ACTIONS OF RECIPIENT IN THE USE OF THE SOFTWARE. */

/* $ Required_Reading */

/*     SPK */
/*     NAIF_IDS */
/*     FRAMES */
/*     TIME */

/* $ Keywords */

/*     EPHEMERIS */

/* $ Declarations */
/* $ Abstract */

/*     The parameters below form an enumerated list of the recognized */
/*     frame types.  They are: INERTL, PCK, CK, TK, DYN.  The meanings */
/*     are outlined below. */

/* $ Disclaimer */

/*     THIS SOFTWARE AND ANY RELATED MATERIALS WERE CREATED BY THE */
/*     CALIFORNIA INSTITUTE OF TECHNOLOGY (CALTECH) UNDER A U.S. */
/*     GOVERNMENT CONTRACT WITH THE NATIONAL AERONAUTICS AND SPACE */
/*     ADMINISTRATION (NASA). THE SOFTWARE IS TECHNOLOGY AND SOFTWARE */
/*     PUBLICLY AVAILABLE UNDER U.S. EXPORT LAWS AND IS PROVIDED "AS-IS" */
/*     TO THE RECIPIENT WITHOUT WARRANTY OF ANY KIND, INCLUDING ANY */
/*     WARRANTIES OF PERFORMANCE OR MERCHANTABILITY OR FITNESS FOR A */
/*     PARTICULAR USE OR PURPOSE (AS SET FORTH IN UNITED STATES UCC */
/*     SECTIONS 2312-2313) OR FOR ANY PURPOSE WHATSOEVER, FOR THE */
/*     SOFTWARE AND RELATED MATERIALS, HOWEVER USED. */

/*     IN NO EVENT SHALL CALTECH, ITS JET PROPULSION LABORATORY, OR NASA */
/*     BE LIABLE FOR ANY DAMAGES AND/OR COSTS, INCLUDING, BUT NOT */
/*     LIMITED TO, INCIDENTAL OR CONSEQUENTIAL DAMAGES OF ANY KIND, */
/*     INCLUDING ECONOMIC DAMAGE OR INJURY TO PROPERTY AND LOST PROFITS, */
/*     REGARDLESS OF WHETHER CALTECH, JPL, OR NASA BE ADVISED, HAVE */
/*     REASON TO KNOW, OR, IN FACT, SHALL KNOW OF THE POSSIBILITY. */

/*     RECIPIENT BEARS ALL RISK RELATING TO QUALITY AND PERFORMANCE OF */
/*     THE SOFTWARE AND ANY RELATED MATERIALS, AND AGREES TO INDEMNIFY */
/*     CALTECH AND NASA FOR ALL THIRD-PARTY CLAIMS RESULTING FROM THE */
/*     ACTIONS OF RECIPIENT IN THE USE OF THE SOFTWARE. */

/* $ Parameters */

/*     INERTL      an inertial frame that is listed in the routine */
/*                 CHGIRF and that requires no external file to */
/*                 compute the transformation from or to any other */
/*                 inertial frame. */

/*     PCK         is a frame that is specified relative to some */
/*                 INERTL frame and that has an IAU model that */
/*                 may be retrieved from the PCK system via a call */
/*                 to the routine TISBOD. */

/*     CK          is a frame defined by a C-kernel. */

/*     TK          is a "text kernel" frame.  These frames are offset */
/*                 from their associated "relative" frames by a */
/*                 constant rotation. */

/*     DYN         is a "dynamic" frame.  These currently are */
/*                 parameterized, built-in frames where the full frame */
/*                 definition depends on parameters supplied via a */
/*                 frame kernel. */

/* $ Author_and_Institution */

/*     N.J. Bachman    (JPL) */
/*     W.L. Taber      (JPL) */

/* $ Literature_References */

/*     None. */

/* $ Version */

/* -    SPICELIB Version 3.0.0, 28-MAY-2004 (NJB) */

/*       The parameter DYN was added to support the dynamic frame class. */

/* -    SPICELIB Version 2.0.0, 12-DEC-1996 (WLT) */

/*        Various unused frames types were removed and the */
/*        frame time TK was added. */

/* -    SPICELIB Version 1.0.0, 10-DEC-1995 (WLT) */

/* -& */
/* $ Brief_I/O */

/*     Variable  I/O  Description */
/*     --------  ---  -------------------------------------------------- */
/*     TARG       I   Target body name. */
/*     ET         I   Observer epoch. */
/*     REF        I   Reference frame of output state vector. */
/*     ABCORR     I   Aberration correction flag. */
/*     OBS        I   Observing body name. */
/*     STARG      O   State of target. */
/*     LT         O   One way light time between observer and target. */

/* $ Detailed_Input */

/*     TARG        is the name of a target body.  Optionally, you may */
/*                 supply the integer ID code for the object as */
/*                 an integer string.  For example both 'MOON' and */
/*                 '301' are legitimate strings that indicate the */
/*                 moon is the target body. */

/*                 The target and observer define a state vector whose */
/*                 position component points from the observer to the */
/*                 target. */

/*     ET          is the ephemeris time, expressed as seconds past J2000 */
/*                 TDB, at which the state of the target body relative to */
/*                 the observer is to be computed.  ET refers to time at */
/*                 the observer's location. */

/*     REF         is the name of the reference frame relative to which */
/*                 the output state vector should be expressed. This may */
/*                 be any frame supported by the SPICE system, including */
/*                 built-in frames (documented in the Frames Required */
/*                 Reading) and frames defined by a loaded frame kernel */
/*                 (FK). */

/*                 When REF designates a non-inertial frame, the */
/*                 orientation of the frame is evaluated at an epoch */
/*                 dependent on the selected aberration correction. */
/*                 See the description of the output state vector STARG */
/*                 for details. */

/*     ABCORR      indicates the aberration corrections to be applied */
/*                 to the state of the target body to account for one-way */
/*                 light time and stellar aberration.  See the discussion */
/*                 in the Particulars section for recommendations on */
/*                 how to choose aberration corrections. */

/*                 ABCORR may be any of the following: */

/*                    'NONE'     Apply no correction. Return the */
/*                               geometric state of the target body */
/*                               relative to the observer. */

/*                 The following values of ABCORR apply to the */
/*                 "reception" case in which photons depart from the */
/*                 target's location at the light-time corrected epoch */
/*                 ET-LT and *arrive* at the observer's location at ET: */

/*                    'LT'       Correct for one-way light time (also */
/*                               called "planetary aberration") using a */
/*                               Newtonian formulation. This correction */
/*                               yields the state of the target at the */
/*                               moment it emitted photons arriving at */
/*                               the observer at ET. */

/*                               The light time correction uses an */
/*                               iterative solution of the light time */
/*                               equation (see Particulars for details). */
/*                               The solution invoked by the 'LT' option */
/*                               uses one iteration. */

/*                    'LT+S'     Correct for one-way light time and */
/*                               stellar aberration using a Newtonian */
/*                               formulation. This option modifies the */
/*                               state obtained with the 'LT' option to */
/*                               account for the observer's velocity */
/*                               relative to the solar system */
/*                               barycenter. The result is the apparent */
/*                               state of the target---the position and */
/*                               velocity of the target as seen by the */
/*                               observer. */

/*                    'CN'       Converged Newtonian light time */
/*                               correction.  In solving the light time */
/*                               equation, the 'CN' correction iterates */
/*                               until the solution converges (three */
/*                               iterations on all supported platforms). */

/*                               The 'CN' correction typically does not */
/*                               substantially improve accuracy because */
/*                               the errors made by ignoring */
/*                               relativistic effects may be larger than */
/*                               the improvement afforded by obtaining */
/*                               convergence of the light time solution. */
/*                               The 'CN' correction computation also */
/*                               requires a significantly greater number */
/*                               of CPU cycles than does the */
/*                               one-iteration light time correction. */

/*                    'CN+S'     Converged Newtonian light time */
/*                               and stellar aberration corrections. */


/*                 The following values of ABCORR apply to the */
/*                 "transmission" case in which photons *depart* from */
/*                 the observer's location at ET and arrive at the */
/*                 target's location at the light-time corrected epoch */
/*                 ET+LT: */

/*                    'XLT'      "Transmission" case:  correct for */
/*                               one-way light time using a Newtonian */
/*                               formulation. This correction yields the */
/*                               state of the target at the moment it */
/*                               receives photons emitted from the */
/*                               observer's location at ET. */

/*                    'XLT+S'    "Transmission" case:  correct for */
/*                               one-way light time and stellar */
/*                               aberration using a Newtonian */
/*                               formulation  This option modifies the */
/*                               state obtained with the 'XLT' option to */
/*                               account for the observer's velocity */
/*                               relative to the solar system */
/*                               barycenter. The position component of */
/*                               the computed target state indicates the */
/*                               direction that photons emitted from the */
/*                               observer's location must be "aimed" to */
/*                               hit the target. */

/*                    'XCN'      "Transmission" case:  converged */
/*                               Newtonian light time correction. */

/*                    'XCN+S'    "Transmission" case:  converged */
/*                               Newtonian light time and stellar */
/*                               aberration corrections. */


/*                 Neither special nor general relativistic effects are */
/*                 accounted for in the aberration corrections applied */
/*                 by this routine. */

/*                 Case and blanks are not significant in the string */
/*                 ABCORR. */

/*     OBS         is the name of an observing body.  Optionally, you */
/*                 may supply the ID code of the object as an integer */
/*                 string. For example, both 'EARTH' and '399' are */
/*                 legitimate strings to supply to indicate the the */
/*                 observer is Earth. */

/* $ Detailed_Output */

/*     STARG       is a Cartesian state vector representing the position */
/*                 and velocity of the target body relative to the */
/*                 specified observer. STARG is corrected for the */
/*                 specified aberrations, and is expressed with respect */
/*                 to the reference frame specified by REF.  The first */
/*                 three components of STARG represent the x-, y- and */
/*                 z-components of the target's position; the last three */
/*                 components form the corresponding velocity vector. */

/*                 The position component of STARG points from the */
/*                 observer's location at ET to the aberration-corrected */
/*                 location of the target.  Note that the sense of the */
/*                 position vector is independent of the direction of */
/*                 radiation travel implied by the aberration */
/*                 correction. */

/*                 The velocity component of STARG is obtained by */
/*                 evaluating the target's geometric state at the light */
/*                 time corrected epoch, so for aberration-corrected */
/*                 states, the velocity is not precisely equal to the */
/*                 time derivative of the position. */

/*                 Units are always km and km/sec. */

/*                 Non-inertial frames are treated as follows: letting */
/*                 LTCENT be the one-way light time between the observer */
/*                 and the central body associated with the frame, the */
/*                 orientation of the frame is evaluated at ET-LTCENT, */
/*                 ET+LTCENT, or ET depending on whether the requested */
/*                 aberration correction is, respectively, for received */
/*                 radiation, transmitted radiation, or is omitted. */
/*                 LTCENT is computed using the method indicated by */
/*                 ABCORR. */

/*     LT          is the one-way light time between the observer and */
/*                 target in seconds. If the target state is corrected */
/*                 for aberrations, then LT is the one-way light time */
/*                 between the observer and the light time corrected */
/*                 target location. */

/* $ Parameters */

/*     None. */

/* $ Exceptions */

/*     1) If name of target or observer cannot be translated to its */
/*        NAIF ID code, the error SPICE(IDCODENOTFOUND) is signaled. */

/*     2) If the reference frame REF is not a recognized reference */
/*        frame the error 'SPICE(UNKNOWNFRAME)' is signaled. */

/*     3) If the loaded kernels provide insufficient data to */
/*        compute the requested state vector, the deficiency will */
/*        be diagnosed by a routine in the call tree of this routine. */

/*     4) If an error occurs while reading an SPK or other kernel file, */
/*        the error  will be diagnosed by a routine in the call tree */
/*        of this routine. */

/* $ Files */

/*     This routine computes states using SPK files that have been */
/*     loaded into the SPICE system, normally via the kernel loading */
/*     interface routine FURNSH. See the routine FURNSH and the SPK */
/*     and KERNEL Required Reading for further information on loading */
/*     (and unloading) kernels. */

/*     If the output state STARG is to be expressed relative to a */
/*     non-inertial frame, or if any of the ephemeris data used to */
/*     compute STARG are expressed relative to a non-inertial frame in */
/*     the SPK files providing those data, additional kernels may be */
/*     needed to enable the reference frame transformations required to */
/*     compute the state.  Normally these additional kernels are PCK */
/*     files or frame kernels.  Any such kernels must already be loaded */
/*     at the time this routine is called. */

/* $ Particulars */

/*     This routine is part of the user interface to the SPICE ephemeris */
/*     system.  It allows you to retrieve state information for any */
/*     ephemeris object relative to any other in a reference frame that */
/*     is convenient for further computations. */

/*     This routine is identical in function to the routine SPKEZ except */
/*     that it allows you to refer to ephemeris objects by name (via a */
/*     character string). */


/*     Aberration corrections */
/*     ====================== */

/*     In space science or engineering applications one frequently */
/*     wishes to know where to point a remote sensing instrument, such */
/*     as an optical camera or radio antenna, in order to observe or */
/*     otherwise receive radiation from a target.  This pointing problem */
/*     is complicated by the finite speed of light:  one needs to point */
/*     to where the target appears to be as opposed to where it actually */
/*     is at the epoch of observation.  We use the adjectives */
/*     "geometric," "uncorrected," or "true" to refer to an actual */
/*     position or state of a target at a specified epoch.  When a */
/*     geometric position or state vector is modified to reflect how it */
/*     appears to an observer, we describe that vector by any of the */
/*     terms "apparent," "corrected," "aberration corrected," or "light */
/*     time and stellar aberration corrected." The SPICE Toolkit can */
/*     correct for two phenomena affecting the apparent location of an */
/*     object:  one-way light time (also called "planetary aberration") */
/*     and stellar aberration. */

/*     One-way light time */
/*     ------------------ */

/*     Correcting for one-way light time is done by computing, given an */
/*     observer and observation epoch, where a target was when the */
/*     observed photons departed the target's location.  The vector from */
/*     the observer to this computed target location is called a "light */
/*     time corrected" vector.  The light time correction depends on the */
/*     motion of the target relative to the solar system barycenter, but */
/*     it is independent of the velocity of the observer relative to the */
/*     solar system barycenter. Relativistic effects such as light */
/*     bending and gravitational delay are not accounted for in the */
/*     light time correction performed by this routine. */

/*     Stellar aberration */
/*     ------------------ */

/*     The velocity of the observer also affects the apparent location */
/*     of a target:  photons arriving at the observer are subject to a */
/*     "raindrop effect" whereby their velocity relative to the observer */
/*     is, using a Newtonian approximation, the photons' velocity */
/*     relative to the solar system barycenter minus the velocity of the */
/*     observer relative to the solar system barycenter.  This effect is */
/*     called "stellar aberration."  Stellar aberration is independent */
/*     of the velocity of the target.  The stellar aberration formula */
/*     used by this routine does not include (the much smaller) */
/*     relativistic effects. */

/*     Stellar aberration corrections are applied after light time */
/*     corrections:  the light time corrected target position vector is */
/*     used as an input to the stellar aberration correction. */

/*     When light time and stellar aberration corrections are both */
/*     applied to a geometric position vector, the resulting position */
/*     vector indicates where the target "appears to be" from the */
/*     observer's location. */

/*     As opposed to computing the apparent position of a target, one */
/*     may wish to compute the pointing direction required for */
/*     transmission of photons to the target.  This also requires */
/*     correction of the geometric target position for the effects of */
/*     light time and stellar aberration, but in this case the */
/*     corrections are computed for radiation traveling *from* the */
/*     observer to the target. */

/*     The "transmission" light time correction yields the target's */
/*     location as it will be when photons emitted from the observer's */
/*     location at ET arrive at the target.  The transmission stellar */
/*     aberration correction is the inverse of the traditional stellar */
/*     aberration correction:  it indicates the direction in which */
/*     radiation should be emitted so that, using a Newtonian */
/*     approximation, the sum of the velocity of the radiation relative */
/*     to the observer and of the observer's velocity, relative to the */
/*     solar system barycenter, yields a velocity vector that points in */
/*     the direction of the light time corrected position of the target. */

/*     One may object to using the term "observer" in the transmission */
/*     case, in which radiation is emitted from the observer's location. */
/*     The terminology was retained for consistency with earlier */
/*     documentation. */

/*     Below, we indicate the aberration corrections to use for some */
/*     common applications: */

/*        1) Find the apparent direction of a target for a remote-sensing */
/*           observation. */

/*              Use 'LT+S':  apply both light time and stellar */
/*              aberration corrections. */

/*           Note that using light time corrections alone ('LT') is */
/*           generally not a good way to obtain an approximation to an */
/*           apparent target vector:  since light time and stellar */
/*           aberration corrections often partially cancel each other, */
/*           it may be more accurate to use no correction at all than to */
/*           use light time alone. */


/*        2) Find the corrected pointing direction to radiate a signal */
/*           to a target.  This computation is often applicable for */
/*           implementing communications sessions. */

/*              Use 'XLT+S':  apply both light time and stellar */
/*              aberration corrections for transmission. */


/*        3) Compute the apparent position of a target body relative */
/*           to a star or other distant object. */

/*              Use 'LT' or 'LT+S' as needed to match the correction */
/*              applied to the position of the distant object.  For */
/*              example, if a star position is obtained from a catalog, */
/*              the position vector may not be corrected for stellar */
/*              aberration.  In this case, to find the angular */
/*              separation of the star and the limb of a planet, the */
/*              vector from the observer to the planet should be */
/*              corrected for light time but not stellar aberration. */


/*        4) Obtain an uncorrected state vector derived directly from */
/*           data in an SPK file. */

/*              Use 'NONE'. */


/*        5) Use a geometric state vector as a low-accuracy estimate */
/*           of the apparent state for an application where execution */
/*           speed is critical. */

/*              Use 'NONE'. */


/*        6) While this routine cannot perform the relativistic */
/*           aberration corrections required to compute states */
/*           with the highest possible accuracy, it can supply the */
/*           geometric states required as inputs to these computations. */

/*              Use 'NONE', then apply relativistic aberration */
/*              corrections (not available in the SPICE Toolkit). */


/*     Below, we discuss in more detail how the aberration corrections */
/*     applied by this routine are computed. */

/*        Geometric case */
/*        ============== */

/*        SPKEZR begins by computing the geometric position T(ET) of the */
/*        target body relative to the solar system barycenter (SSB). */
/*        Subtracting the geometric position of the observer O(ET) gives */
/*        the geometric position of the target body relative to the */
/*        observer. The one-way light time, LT, is given by */

/*                  | T(ET) - O(ET) | */
/*           LT = ------------------- */
/*                          c */

/*        The geometric relationship between the observer, target, and */
/*        solar system barycenter is as shown: */


/*           SSB ---> O(ET) */
/*            |      / */
/*            |     / */
/*            |    / */
/*            |   /  T(ET) - O(ET) */
/*            V  V */
/*           T(ET) */


/*        The returned state consists of the position vector */

/*           T(ET) - O(ET) */

/*        and a velocity obtained by taking the difference of the */
/*        corresponding velocities.  In the geometric case, the */
/*        returned velocity is actually the time derivative of the */
/*        position. */


/*        Reception case */
/*        ============== */

/*        When any of the options 'LT', 'CN', 'LT+S', 'CN+S' is selected */
/*        for ABCORR, SPKEZR computes the position of the target body at */
/*        epoch ET-LT, where LT is the one-way light time.  Let T(t) and */
/*        O(t) represent the positions of the target and observer */
/*        relative to the solar system barycenter at time t; then LT is */
/*        the solution of the light-time equation */

/*                  | T(ET-LT) - O(ET) | */
/*           LT = ------------------------                            (1) */
/*                           c */

/*        The ratio */

/*            | T(ET) - O(ET) | */
/*          ---------------------                                     (2) */
/*                    c */

/*        is used as a first approximation to LT; inserting (2) into the */
/*        right hand side of the light-time equation (1) yields the */
/*        "one-iteration" estimate of the one-way light time ("LT"). */
/*        Repeating the process until the estimates of LT converge */
/*        yields the "converged Newtonian" light time estimate ("CN"). */

/*        Subtracting the geometric position of the observer O(ET) gives */
/*        the position of the target body relative to the observer: */
/*        T(ET-LT) - O(ET). */

/*           SSB ---> O(ET) */
/*            | \     | */
/*            |  \    | */
/*            |   \   | T(ET-LT) - O(ET) */
/*            |    \  | */
/*            V     V V */
/*           T(ET)  T(ET-LT) */

/*        The position component of the light time corrected state */
/*        is the vector */

/*           T(ET-LT) - O(ET) */

/*        The velocity component of the light time corrected state */
/*        is the difference */

/*           T_vel(ET-LT) - O_vel(ET) */

/*        where T_vel and O_vel are, respectively, the velocities of the */
/*        target and observer relative to the solar system barycenter at */
/*        the epochs ET-LT and ET.  This is NOT the derivative of the */
/*        light time corrected position because this does not take into */
/*        account the time derivative of LT. */

/*        If correction for stellar aberration is requested, the target */
/*        position is rotated toward the solar system */
/*        barycenter-relative velocity vector of the observer.  The */
/*        rotation is computed as follows: */

/*           Let r be the light time corrected vector from the observer */
/*           to the object, and v be the velocity of the observer with */
/*           respect to the solar system barycenter. Let w be the angle */
/*           between them. The aberration angle phi is given by */

/*              sin(phi) = v sin(w) / c */

/*           Let h be the vector given by the cross product */

/*              h = r X v */

/*           Rotate r by phi radians about h to obtain the apparent */
/*           position of the object. */

/*        The velocity component of the output state STARG is */
/*        not corrected for stellar aberration. */


/*        Transmission case */
/*        ================== */

/*        When any of the options 'XLT', 'XCN', 'XLT+S', 'XCN+S' is */
/*        selected, SPKEZR computes the position of the target body T at */
/*        epoch ET+LT, where LT is the one-way light time.  LT is the */
/*        solution of the light-time equation */

/*                  | T(ET+LT) - O(ET) | */
/*           LT = ------------------------                            (3) */
/*                            c */

/*        Subtracting the geometric position of the observer, O(ET), */
/*        gives the position of the target body relative to the */
/*        observer: T(ET-LT) - O(ET). */

/*                   SSB --> O(ET) */
/*                  / |    * */
/*                 /  |  *  T(ET+LT) - O(ET) */
/*                /   |* */
/*               /   *| */
/*              V  V  V */
/*          T(ET+LT)  T(ET) */

/*        The position component of the light-time corrected state */
/*        is the vector */

/*           T(ET+LT) - O(ET) */

/*        The velocity component of the light-time corrected state */
/*        consists of the difference */

/*           T_vel(ET+LT) - O_vel(ET) */

/*        where T_vel and O_vel are, respectively, the velocities of the */
/*        target and observer relative to the solar system barycenter at */
/*        the epochs ET+LT and ET.  This is NOT the derivative of the */
/*        light time corrected position because this does not take into */
/*        account the time derivative of LT. */

/*        If correction for stellar aberration is requested, the target */
/*        position is rotated away from the solar system barycenter- */
/*        relative velocity vector of the observer. The rotation is */
/*        computed as in the reception case, but the sign of the */
/*        rotation angle is negated. */

/*        The velocity component of the output state STARG is */
/*        not corrected for stellar aberration. */


/*     Precision of light time corrections */
/*     =================================== */

/*        Corrections using one iteration of the light time solution */
/*        ---------------------------------------------------------- */

/*        When the requested aberration correction is 'LT', 'LT+S', */
/*        'XLT', or 'XLT+S', only one iteration is performed in the */
/*        algorithm used to compute LT. */

/*        The relative error in this computation */

/*           | LT_ACTUAL - LT_COMPUTED |  /  LT_ACTUAL */

/*        is at most */

/*            (V/C)**2 */
/*           ---------- */
/*            1 - (V/C) */

/*        which is well approximated by (V/C)**2, where V is the */
/*        velocity of the target relative to an inertial frame and C is */
/*        the speed of light. */

/*        For nearly all objects in the solar system V is less than 60 */
/*        km/sec.  The value of C is 300000 km/sec.  Thus the one */
/*        iteration solution for LT has a potential relative error of */
/*        not more than 4*10**-8.  This is a potential light time error */
/*        of approximately 2*10**-5 seconds per astronomical unit of */
/*        distance separating the observer and target.  Given the bound */
/*        on V cited above: */

/*           As long as the observer and target are */
/*           separated by less than 50 astronomical units, */
/*           the error in the light time returned using */
/*           the one-iteration light time corrections */
/*           is less than 1 millisecond. */


/*        Converged corrections */
/*        --------------------- */

/*        When the requested aberration correction is 'CN', 'CN+S', */
/*        'XCN', or 'XCN+S', three iterations are performed in the */
/*        computation of LT.  The relative error present in this */
/*        solution is at most */

/*            (V/C)**4 */
/*           ---------- */
/*            1 - (V/C) */

/*        which is well approximated by (V/C)**4.  Mathematically the */
/*        precision of this computation is better than a nanosecond for */
/*        any pair of objects in the solar system. */

/*        However, to model the actual light time between target and */
/*        observer one must take into account effects due to general */
/*        relativity.  These may be as high as a few hundredths of a */
/*        millisecond for some objects. */

/*        When one considers the extra time required to compute the */
/*        converged Newtonian light time (the state of the target */
/*        relative to the solar system barycenter is looked up three */
/*        times instead of once) together with the real gain in */
/*        accuracy, it seems unlikely that you will want to request */
/*        either the "CN" or "CN+S" light time corrections.  However, */
/*        these corrections can be useful for testing situations where */
/*        high precision (as opposed to accuracy) is required. */


/*     Relativistic Corrections */
/*     ========================= */

/*     This routine does not attempt to perform either general or */
/*     special relativistic corrections in computing the various */
/*     aberration corrections.  For many applications relativistic */
/*     corrections are not worth the expense of added computation */
/*     cycles.  If however, your application requires these additional */
/*     corrections we suggest you consult the astronomical almanac (page */
/*     B36) for a discussion of how to carry out these corrections. */


/* $ Examples */

/*     1)  Load a planetary ephemeris SPK, then look up a series of */
/*         geometric states of the moon relative to the earth, */
/*         referenced to the J2000 frame. */


/*               IMPLICIT NONE */
/*         C */
/*         C     Local constants */
/*         C */
/*               CHARACTER*(*)         FRAME */
/*               PARAMETER           ( FRAME  = 'J2000' ) */

/*               CHARACTER*(*)         ABCORR */
/*               PARAMETER           ( ABCORR = 'NONE' ) */

/*         C */
/*         C     The name of the SPK file shown here is fictitious; */
/*         C     you must supply the name of an SPK file available */
/*         C     on your own computer system. */
/*         C */
/*               CHARACTER*(*)         SPK */
/*               PARAMETER           ( SPK    = 'planet.bsp' ) */

/*         C */
/*         C     ET0 represents the date 2000 Jan 1 12:00:00 TDB. */
/*         C */
/*               DOUBLE PRECISION      ET0 */
/*               PARAMETER           ( ET0    = 0.0D0 ) */

/*         C */
/*         C     Use a time step of 1 hour; look up 100 states. */
/*         C */
/*               DOUBLE PRECISION      STEP */
/*               PARAMETER           ( STEP   = 3600.0D0 ) */

/*               INTEGER               MAXITR */
/*               PARAMETER           ( MAXITR = 100 ) */

/*               CHARACTER*(*)         OBSRVR */
/*               PARAMETER           ( OBSRVR = 'Earth' ) */

/*               CHARACTER*(*)         TARGET */
/*               PARAMETER           ( TARGET = 'Moon' ) */

/*         C */
/*         C     Local variables */
/*         C */
/*               DOUBLE PRECISION      ET */
/*               DOUBLE PRECISION      LT */
/*               DOUBLE PRECISION      STATE ( 6 ) */

/*               INTEGER               I */

/*         C */
/*         C     Load the SPK file. */
/*         C */
/*               CALL FURNSH ( SPK ) */

/*         C */
/*         C     Step through a series of epochs, looking up a */
/*         C     state vector at each one. */
/*         C */
/*               DO I = 1, MAXITR */

/*                  ET = ET0 + (I-1)*STEP */

/*                  CALL SPKEZR ( TARGET, ET, FRAME, ABCORR, OBSRVR, */
/*              .                 STATE,  LT                        ) */

/*                  WRITE (*,*) 'ET = ', ET */
/*                  WRITE (*,*) 'J2000 x-position (km):   ', STATE(1) */
/*                  WRITE (*,*) 'J2000 y-position (km):   ', STATE(2) */
/*                  WRITE (*,*) 'J2000 z-position (km):   ', STATE(3) */
/*                  WRITE (*,*) 'J2000 x-velocity (km/s): ', STATE(4) */
/*                  WRITE (*,*) 'J2000 y-velocity (km/s): ', STATE(5) */
/*                  WRITE (*,*) 'J2000 z-velocity (km/s): ', STATE(6) */
/*                  WRITE (*,*) ' ' */

/*               END DO */

/*               END */


/* $ Restrictions */

/*     None. */

/* $ Literature_References */

/*     SPK Required Reading. */

/* $ Author_and_Institution */

/*     C.H. Acton      (JPL) */
/*     B.V. Semenov    (JPL) */
/*     N.J. Bachman    (JPL) */

/* $ Version */

/* -    SPICELIB Version 3.0.2, 20-OCT-2003 (EDW) */

/*        Added mention that LT returns in seconds. */

/* -    SPICELIB Version 3.0.1, 29-JUL-2003 (NJB) (CHA) */

/*        Various minor header changes were made to improve clarity. */

/* -    SPICELIB Version 3.0.0, 31-DEC-2001 (NJB) */

/*        Updated to handle aberration corrections for transmission */
/*        of radiation.  Formerly, only the reception case was */
/*        supported.  The header was revised and expanded to explain */
/*        the functionality of this routine in more detail. */

/* -    Spicelib Version 2.0.0, 21-FEB-1997 (WLT) */

/*        Extended the functionality of the routine.  Users may */
/*        now entered the id code of an object as an ascii string */
/*        and the string will be converted to the corresponding */
/*        integer representation. */

/* -    Spicelib Version 1.1.0, 09-JUL-1996 (WLT) */

/*        Corrected the description of LT in the Detailed Output */
/*        section of the header. */

/* -    SPICELIB Version 1.0.0, 25-SEP-1995 (BVS) */

/* -& */
/* $ Index_Entries */

/*     using body names get target state relative to an observer */
/*     get state relative to observer corrected for aberrations */
/*     read ephemeris data */
/*     read trajectory data */

/* -& */
/* $ Revisions */

/* -& */


/*     SPICELIB functions */


/*     Local parameters */


/*     Local variables */


/*     Standard SPICE error handling. */

    if (return_()) {
	return 0;
    } else {
	chkin_("SPKEZR", (ftnlen)6);
    }

/*     Starting from translation of target name to its code */

    zzbodn2c_(targ, &targid, &found, targ_len);
    if (! found) {
	if (beint_(targ, targ_len)) {
	    s_copy(error, " ", (ftnlen)80, (ftnlen)1);
	    nparsi_(targ, &targid, error, &ptr, targ_len, (ftnlen)80);
	    if (s_cmp(error, " ", (ftnlen)80, (ftnlen)1) != 0) {
		found = FALSE_;
	    } else {
		found = TRUE_;
	    }
	}
    }
    if (! found) {
	setmsg_("The target, '#', is not a recognized name for an ephemeris "
		"object. The cause of this problem may be that you need an up"
		"dated version of the SPICE Toolkit. Alternatively you may ca"
		"ll SPKEZ directly if you know the SPICE ID codes for both '#"
		"' and '#' ", (ftnlen)249);
	errch_("#", targ, (ftnlen)1, targ_len);
	errch_("#", targ, (ftnlen)1, targ_len);
	errch_("#", obs, (ftnlen)1, obs_len);
	sigerr_("SPICE(IDCODENOTFOUND)", (ftnlen)21);
	chkout_("SPKEZR", (ftnlen)6);
	return 0;
    }

/*     Now do the same for observer */

    zzbodn2c_(obs, &obsid, &found, obs_len);
    if (! found) {
	if (beint_(obs, obs_len)) {
	    s_copy(error, " ", (ftnlen)80, (ftnlen)1);
	    nparsi_(obs, &obsid, error, &ptr, obs_len, (ftnlen)80);
	    if (s_cmp(error, " ", (ftnlen)80, (ftnlen)1) != 0) {
		found = FALSE_;
	    } else {
		found = TRUE_;
	    }
	}
    }
    if (! found) {
	setmsg_("The observer, '#', is not a recognized name for an ephemeri"
		"s object. The cause of this problem may be that you need an "
		"updated version of the SPICE toolkit. Alternatively you may "
		"call SPKEZ directly if you know the SPICE ID codes for both "
		"'#' and '#' ", (ftnlen)251);
	errch_("#", obs, (ftnlen)1, obs_len);
	errch_("#", targ, (ftnlen)1, targ_len);
	errch_("#", obs, (ftnlen)1, obs_len);
	sigerr_("SPICE(IDCODENOTFOUND)", (ftnlen)21);
	chkout_("SPKEZR", (ftnlen)6);
	return 0;
    }

/*     After all translations are done we can call SPKEZ. */

    spkez_(&targid, et, ref, abcorr, &obsid, starg, lt, ref_len, abcorr_len);
    chkout_("SPKEZR", (ftnlen)6);
    return 0;
} /* spkezr_ */

