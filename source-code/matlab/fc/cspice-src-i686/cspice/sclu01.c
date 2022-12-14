/* sclu01.f -- translated by f2c (version 19980913).
   You must link the resulting object file with the libraries:
	-lf2c -lm   (in that order)
*/

#include "f2c.h"

/* Table of constant values */

static integer c__0 = 0;
static integer c__9 = 9;
static integer c__14 = 14;

/* $Procedure      SCLU01 ( SCLK look up, type 1 ) */
/* Subroutine */ int sclu01_0_(int n__, char *name__, integer *sc, integer *
	maxnv, integer *n, integer *ival, doublereal *dval, ftnlen name_len)
{
    /* Initialized data */

    static char namlst[80*9] = "SCLK01_COEFFICIENTS                         "
	    "                                    " "SCLK_PARTITION_START     "
	    "                                                       " "SCLK_P"
	    "ARTITION_END                                                    "
	    "          " "SCLK01_N_FIELDS                                    "
	    "                             " "SCLK01_OFFSETS                  "
	    "                                                " "SCLK01_MODULI"
	    "                                                                "
	    "   " "SCLK01_OUTPUT_DELIM                                       "
	    "                      " "SCLK01_KERNEL_ID                       "
	    "                                         " "SCLK01_TIME_SYSTEM  "
	    "                                                            ";
    static integer lb[9] = { 3,1,1,1,1,1,1,1,0 };
    static char nfdmsg[320] = "# not found. Did you load the SCLK kernel?   "
	    "                                                                "
	    "                                                                "
	    "                                                                "
	    "                                                                "
	    "                   ";
    static char nummsg[320] = "Invalid number of values found for #:  #.    "
	    "                                                                "
	    "                                                                "
	    "                                                                "
	    "                                                                "
	    "                   ";
    static char bvlmsg[320] = "Invalid value found for #:  #.               "
	    "                                                                "
	    "                                                                "
	    "                                                                "
	    "                                                                "
	    "                   ";

    /* System generated locals */
    integer i__1, i__2;

    /* Builtin functions */
    /* Subroutine */ int s_copy(char *, char *, ftnlen, ftnlen);
    integer s_cmp(char *, char *, ftnlen, ftnlen), s_rnge(char *, integer, 
	    char *, integer), i_dnnt(doublereal *);

    /* Local variables */
    integer i__;
    extern /* Subroutine */ int chkin_(char *, ftnlen), errch_(char *, char *,
	     ftnlen, ftnlen), repmc_(char *, char *, char *, char *, ftnlen, 
	    ftnlen, ftnlen, ftnlen), repmd_(char *, char *, doublereal *, 
	    integer *, char *, ftnlen, ftnlen, ftnlen);
    doublereal dtemp[50];
    logical found;
    extern /* Subroutine */ int repmi_(char *, char *, integer *, char *, 
	    ftnlen, ftnlen, ftnlen);
    extern integer isrchc_(char *, integer *, char *, ftnlen, ftnlen);
    extern /* Subroutine */ int sigerr_(char *, ftnlen);
    char tmpnam[80];
    extern /* Subroutine */ int chkout_(char *, ftnlen);
    char errmsg[320];
    extern /* Subroutine */ int setmsg_(char *, ftnlen), suffix_(char *, 
	    integer *, char *, ftnlen, ftnlen);
    extern logical return_(void);
    extern /* Subroutine */ int rtpool_(char *, integer *, doublereal *, 
	    logical *, ftnlen);

/* $ Abstract */

/*     Look up type 1 SCLK kernel data. */

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

/*     KERNEL */
/*     SCLK */

/* $ Keywords */

/*     UTILITY */

/* $ Declarations */
/* $ Brief_I/O */

/*     Variable  I/O  Entry points */
/*     --------  ---  -------------------------------------------------- */
/*     NAME       I   SCLD01, SCLI01 */
/*     SC         I   SCLD01, SCLI01 */
/*     MAXNV      I   SCLD01, SCLI01 */
/*     N          O   SCLD01, SCLI01 */
/*     IVAL       O   SCLI01 */
/*     DVAL       O   SCLD01 */
/*     MXNIV      P   SCLI01 */
/*     MXCOEF     P   SCLD01, SCLI01 */
/*     MXPART     P   SCLD01, SCLI01 */
/*     MXNFLD     P   SCLD01, SCLI01 */
/*     NDELIM     P   SCLI01 */
/*     MXTSYS     P   SCLI01 */

/* $ Detailed_Input */

/*     See entry points SCLI01, SCLD01. */

/* $ Detailed_Output */

/*     See entry points SCLI01, SCLD01. */

/* $ Parameters */

/*     MXNIV          is the maximum number of integer values that can */
/*                    be associated with any type 1 SCLK kernel variable. */
/*                    See note in $Restrictions. */

/*     MXCOEF         is the maximum number of coefficient sets in the */
/*                    array COEFFS that defines the mapping between */
/*                    encoded type 1 SCLK and a parallel time system. */
/*                    This array has dimension 3 x MXCOEF.  The value of */
/*                    MXCOEF may be increased as required. */

/*     MXPART         is the maximum number of partitions for any type 1 */
/*                    spacecraft clock.  Type 1 SCLK kernels contain */
/*                    start and stop times for each partition.  The value */
/*                    of MXPART may be increased as required. */

/*     MXNFLD         is an upper bound on the number of fields in a */
/*                    SCLK string. */

/*     NDELIM         is the number of delimiter codes. */

/*     MXTSYS         is the maximum number of supported parallel time */
/*                    systems that SCLK values may be mapped to or from. */

/* $ Exceptions */

/*     1)  IF SCLU01 is called directly, the error SPICE(BOGUSENTRY) is */
/*         signalled. */

/*     See entry points SCLI01, SCLD01 for descriptions of exceptions */
/*     specific to those routines. */

/* $ Files */

/*     None. */

/* $ Particulars */

/*     This routine is a utility whose purpose is to localize error */
/*     checking for type 1 SCLK kernel pool lookups in a single place. */

/*     SLCU01 exists solely as an umbrella routine in which the */
/*     variables for its entry points are declared.  SCLU01 should never */
/*     be called directly. */

/* $ Examples */

/*     See entry points SCLI01, SCLD01. */

/* $ Restrictions */

/*     1)  SCLU01 handles lookups of type 1 SCLK data only. */

/*     2)  The use of local storage to accommodate integer data is, */
/*         we hope, a temporary inconvenience.  There is no technical */
/*         reason why the kernel pool cannot have an interface routine */
/*         for integer lookups, and if such a routine existed, the */
/*         argument IVAL could be passed directly to that interface */
/*         routine. */

/* $ Literature_References */

/*     None. */

/* $ Author_and_Institution */

/*     N.J. Bachman   (JPL) */

/* $ Version */

/* -    SPICELIB Version 2.1.0, 19-OCT-1992 (NJB) */

/*        Entry points SCLI01 and SCLD01 were updated to fix a bug: */
/*        if a kernel pool lookup fails, the number of elements returned */
/*        N is now set to zero. */

/* -    SPICELIB Version 2.0.0, 17-APR-1992 (NJB) (WLT) */

/*        Entry point SCLI01 was updated to handle a time */
/*        system specification for the `parallel' time system */
/*        in the SCLK kernel.  Comment section for permuted index */
/*        source lines was added following the header. */

/* -    SPICELIB Version 1.0.0, 06-SEP-1990 (NJB) */

/* -& */
/* $ Index_Entries */

/*     lookup type_1 spacecraft_clock */

/* -& */
/* $ Revisions */

/* -    SPICELIB Version 2.1.0, 19-OCT-1992 (NJB) */

/*        Entry points SCLI01 and SCLD01 were updated to fix a bug: */
/*        if a kernel pool lookup fails, the number of elements returned */
/*        N is now set to zero.  Formerly, these routines returned */
/*        whatever value was returned by RTPOOL.  RTPOOL, however, */
/*        does not set N to zero when the data item requested from it */
/*        is not found. */

/* -    SPICELIB Version 2.0.0, 17-APR-1992 (NJB) (WLT) */

/*        Entry point SCLI01 was updated to handle a time */
/*        system specification for the `parallel' time system */
/*        in the SCLK kernel.  The update consists of these */
/*        changes: */

/*           -- The parameter MXTSYS is now defined. */

/*           -- The local saved variable NAMLST has been expanded */
/*              to include the name SCLK01_TIME_SYSTEM */

/*           -- The local saved variable LB has been expanded to */
/*              include the lower bound for the number of returned */
/*              values when SCLK01_TIME_SYSTEM_nn is looked up in */
/*              the kernel pool. */

/*           -- SCLI01 checks the value returned by RTPOOL when */
/*              SCLK01_TIME_SYSTEM_nn is looked up to verify that */
/*              it is within the range [1, MXTSYS]. */

/*        Also, a comment section for permuted index source lines was */
/*        added following the header. */

/* -& */

/*     SPICELIB functions */


/*     Local parameters */


/*     DELIDX is the index of the delimiter code name in NAMLST.  If */
/*     the declaration of NAMLST or assignment of values to NAMLST */
/*     changes, this parameter value may have to change. */


/*     NFLIDX is the index of the SCLK field count in NAMLST. */


/*     MODIDX is the index of the SCLK moduli in NAMLST. */


/*     SYSIDX is the index of the time system in NAMLST. */


/*     Local variables */


/*     Saved variables */


/*     Initial values */


/*     Names of type 1 SCLK items and lower bounds on the number of */
/*     associated values. */

    /* Parameter adjustments */
    if (ival) {
	}
    if (dval) {
	}

    /* Function Body */
    switch(n__) {
	case 1: goto L_scli01;
	case 2: goto L_scld01;
	}


/*     Standard SPICE error handling. */

    if (return_()) {
	return 0;
    } else {
	chkin_("SCLU01", (ftnlen)6);
    }
    sigerr_("SPICE(BOGUSENTRY)", (ftnlen)17);
    chkout_("SCLU01", (ftnlen)6);
    return 0;
/* $Procedure      SCLI01 ( SCLK lookup of integer data, type 1 ) */

L_scli01:
/* $ Abstract */

/*     Look up integer type 1 SCLK data from the kernel pool. */

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

/*     KERNEL */
/*     SCLK */

/* $ Keywords */

/*     UTILITY */

/* $ Declarations */

/*     CHARACTER*(*)         NAME */
/*     INTEGER               SC */
/*     INTEGER               MAXNV */
/*     INTEGER               N */
/*     INTEGER               IVAL   ( * ) */

/* $ Brief_I/O */

/*     Variable  I/O  Description */
/*     --------  ---  -------------------------------------------------- */
/*     NAME, */
/*     SC         I   Name of kernel data item, NAIF spacecraft ID code. */
/*     MAXNV      I   Maximum number of integer values to return. */
/*     N          O   Number of values actually returned. */
/*     IVAL       O   Returned integer values. */
/*     MXNIV      P   Maximum number of values that SCLI01 can return. */
/*     MXNFLD     P   Maximum number of fields in an SCLK string. */
/*     NDELIM     P   Maximum number of delimiter codes. */
/*     MXTSYS     P   Maximum number of supported parallel time systems. */

/* $ Detailed_Input */

/*     NAME, */
/*     SC             are, respectively, a name and a NAIF integer code */
/*                    of a spacecraft that together define the name of a */
/*                    requested kernel data item.  NAME is the full name */
/*                    as it appears in the SCLK kernel, except that it */
/*                    lacks the final underscore and spacecraft integer */
/*                    code (actually, the negative of the spacecraft */
/*                    code).  This routine combines NAME and SC to */
/*                    make up the appropriate kernel variable name. */

/*                    For example, to look up data associated with the */
/*                    name */

/*                       SCLK01_MODULI_77 */

/*                    you would supply NAME as */

/*                       SCLK01_MODULI */

/*                    and SC as -77. */


/*     MAXNV          is the maximum number of values to return.  MAXNV */
/*                    is used to prevent SCLI01 from writing past the end */
/*                    of the supplied array IVAL. */

/* $ Detailed_Output */

/*     N              is the number of values actually returned. */

/*     IVAL           is an array containing the requested integer */
/*                    kernel data item. */

/* $ Parameters */

/*     MXNIV          is the maximum number of integer values that can */
/*                    be associated with any type 1 SCLK kernel variable. */
/*                    See note in $Restrictions. */

/*     MXNFLD         is an upper bound on the number of fields in a */
/*                    SCLK string. */

/*     NDELIM         is the number of delimiter codes. */

/*     MXTSYS         is the maximum number of supported parallel time */
/*                    systems that SCLK values may be mapped to or from. */

/* $ Exceptions */


/*     1)  If item specified by NAME and SC is not found in the kernel */
/*         pool, and if the presence of the item is required, the error */
/*         SPICE(KERNELVARNOTFOUND) is signalled.  The output arguments */
/*         are not modified. */

/*         If the specified item is not required, the output argument N */
/*         will take the value 0, and the output argument IVAL is not */
/*         modified. */


/*     2)  This routine can check certain data for validity.  If any of */
/*         these items have invalid values, the error */
/*         SPICE(VALUEOUTOFRANGE) is signalled.  The output arguments are */
/*         not modified.  The values in question are: */

/*            -  The number of fields of a SCLK string */
/*            -  The number of delimiter codes */
/*            -  The output delimiter code */
/*            -  The time system code */

/* $ Files */

/*     None. */

/* $ Particulars */

/*     The purpose of this routine is to localize error checking for */
/*     lookups of type 1 SCLK kernel pool data.  This routine handles */
/*     lookups of integer data. */

/* $ Examples */

/*     1)  To get the number of SCLK fields for the Galileo spacecraft */
/*         clock, you can use the code fragment below: */

/*            C */
/*            C     Load the SCLK kernel in question.  We use a */
/*            C     made-up name for the kernel file; you would use */
/*            C     the actual name of your kernel file instead if you */
/*            C     were to carry out this procedure. */
/*            C */
/*                  CALL LDPOOL ( 'SAMPLE_GLL_SCLK.KER' ) */

/*                  SC   = -77 */
/*                  NAME = 'SCLK01_N_FIELDS' */

/*                  CALL SCLI01 ( NAME, SC, MXNFLD, N, NFIELD ) */


/*         After this subroutine call, NFIELD has the value 4. */


/* $ Restrictions */

/*     1)  SCLI01 assumes that a SCLK kernel appropriate to the */
/*         spacecraft identified by SC has been loaded. */

/*     2)  SCLI01 handles lookups of type 1 SCLK data only. */

/*     3)  If the number of values for any item read from the kernel */
/*         pool exceeds the maximum allowed value, it may not be */
/*         possible to diagnose the error correctly, since overwriting */
/*         of memory will occur.  This problem may be fixed in a later */
/*         implementation; what must change is that the kernel pool */
/*         lookup routine called here must allow the caller to specify */
/*         the maximum number of values to return.  The input argument */
/*         MAXNV is used by this routine in anticipation of this fix. */

/*     4)  The use of local storage to accommodate integer data is, */
/*         we hope, a temporary inconvenience.  There is no technical */
/*         reason why the kernel pool cannot have an interface routine */
/*         for integer lookups, and if such a routine existed, the */
/*         argument IVAL could be passed directly to that interface */
/*         routine. */

/* $ Literature_References */

/*     None. */

/* $ Author_and_Institution */

/*     N.J. Bachman   (JPL) */

/* $ Version */

/* -    SPICELIB Version 2.1.0, 19-OCT-1992 (NJB) */

/*        This entry point was updated to fix a bug:  if a kernel pool */
/*        lookup fails, the number of elements returned N is now set to */
/*        zero. */

/* -    SPICELIB Version 2.0.0, 17-APR-1992 (NJB) (WLT) */

/*        SCLI01 was updated to handle a time system specification for */
/*        the `parallel' time system in the SCLK kernel.  Some */
/*        corrections and other minor enhancements were made to the */
/*        header.  Comment section for permuted index source lines was */
/*        added following the header. */

/* -    SPICELIB Version 1.0.0, 06-SEP-1990 (NJB) */

/* -& */
/* $ Index_Entries */

/*     lookup of type_1 spacecraft_clock integer data */
/*     lookup type_1 spacecraft_clock integer data */

/* -& */
/* $ Revisions */

/* -    SPICELIB Version 2.1.0, 19-OCT-1992 (NJB) */

/*        This entry point was updated to fix a bug:  if a kernel pool */
/*        lookup fails, the number of elements returned N is now set to */
/*        zero.  Formerly, this routine returned whatever value was */
/*        returned by RTPOOL.  RTPOOL, however, does not set N to zero */
/*        when the data item requested from it is not found. */

/* -    SPICELIB Version 2.0.0, 17-APR-1992 (NJB) (WLT) */

/*        Entry point SCLI01 was updated to handle a time */
/*        system specification for the `parallel' time system */
/*        in the SCLK kernel.  The update consists of these */
/*        changes: */

/*           -- The parameter MXTSYS is now defined. */

/*           -- The local saved variable NAMLST has been expanded */
/*              to include the name SCLK01_TIME_SYSTEM */

/*           -- The local saved variable LB has been expanded to */
/*              include the lower bound for the number of returned */
/*              values when SCLK01_TIME_SYSTEM_nn is looked up in */
/*              the kernel pool. */

/*           -- SCLI01 checks the value returned by RTPOOL when */
/*              SCLK01_TIME_SYSTEM_nn is looked up to verify that */
/*              it is within the range [1, MXTSYS]. */

/*        Also, a comment section for permuted index source lines was */
/*        added following the header. */

/*        The $Exceptions header section was updated accordingly. */

/*        Some corrections and other minor enhancements were made to the */
/*        header. */

/* -& */

/*     Standard SPICE error handling. */

    if (return_()) {
	return 0;
    } else {
	chkin_("SCLI01", (ftnlen)6);
    }

/*     Form the name of the kernel pool data item, and do the lookup. */
/*     Note that eventually we should use a kernel pool lookup entry */
/*     that allows us to specify the maximum number of entries that */
/*     can be returned. */

    s_copy(tmpnam, name__, (ftnlen)80, name_len);
    suffix_("_#", &c__0, tmpnam, (ftnlen)2, (ftnlen)80);
    i__1 = -(*sc);
    repmi_(tmpnam, "#", &i__1, tmpnam, (ftnlen)80, (ftnlen)1, (ftnlen)80);
    rtpool_(tmpnam, n, dtemp, &found, (ftnlen)80);

/*     Make sure we found what we were looking for, if the item */
/*     is required. */

    if (! found) {

/*        Currently, the only item that is NOT required is the time */
/*        system specification.  In any case, no values will be returned. */

	*n = 0;
	if (s_cmp(name__, namlst + 640, name_len, (ftnlen)80) == 0) {
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	} else {
	    setmsg_(nfdmsg, (ftnlen)320);
	    errch_("#", tmpnam, (ftnlen)1, (ftnlen)80);
	    sigerr_("SPICE(KERNELVARNOTFOUND)", (ftnlen)24);
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	}
    }

/*     Grab integer copies of the kernel values. */

    i__1 = *n;
    for (i__ = 1; i__ <= i__1; ++i__) {
	ival[i__ - 1] = i_dnnt(&dtemp[(i__2 = i__ - 1) < 50 && 0 <= i__2 ? 
		i__2 : s_rnge("dtemp", i__2, "sclu01_", (ftnlen)670)]);
    }

/*     Now we must check that the number of returned values is in the */
/*     appropriate range.  We test for the following conditions: */

/*        - The number of SCLK fields is at least 1 and is not */
/*          more than MAXNV. */

/*        - The number of delimiter codes is at least 1 and is not */
/*          more than MAXNV. */

/*        - The output delimiter code is at least 1 and is not */
/*          greater than the number of delimiters. */

/*        - The time system code is at least 1 and is not greater */
/*          than MXTSYS. */


/*     See if the input name is in the list of items we know about. */
/*     If it is, perform the bound checks that apply. */

    i__ = isrchc_(name__, &c__9, namlst, name_len, (ftnlen)80);
    if (i__ != 0) {
	if (*n < lb[(i__1 = i__ - 1) < 9 && 0 <= i__1 ? i__1 : s_rnge("lb", 
		i__1, "sclu01_", (ftnlen)698)] || *n > *maxnv) {
	    repmc_(nummsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", n, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	}
    } else {
	if (*n > *maxnv) {
	    repmc_(nummsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", n, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	}
    }

/*     Check the value of the delimiter code itself. */

    if (s_cmp(name__, namlst + 480, name_len, (ftnlen)80) == 0) {
	if (ival[0] < 1 || ival[0] > 5) {
	    repmc_(bvlmsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", ival, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	}
    }

/*     Check the value of the field count, too. */

    if (s_cmp(name__, namlst + 240, name_len, (ftnlen)80) == 0) {
	if (ival[0] < 1 || ival[0] > 10) {
	    repmc_(bvlmsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", ival, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	}
    }

/*     Check the value of the time system code. */

    if (s_cmp(name__, namlst + 640, name_len, (ftnlen)80) == 0) {
	if (ival[0] < 1 || ival[0] > 2) {
	    repmc_(bvlmsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", ival, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLI01", (ftnlen)6);
	    return 0;
	}
    }
    chkout_("SCLI01", (ftnlen)6);
    return 0;
/* $Procedure      SCLD01 ( SCLK lookup of double precision data, type 1 ) */

L_scld01:
/* $ Abstract */

/*     Look up double precision type 1 SCLK data from the kernel pool. */

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

/*     KERNEL */
/*     SCLK */

/* $ Keywords */

/*     UTILITY */

/* $ Declarations */

/*     CHARACTER*(*)         NAME */
/*     INTEGER               SC */
/*     INTEGER               MAXNV */
/*     INTEGER               N */
/*     DOUBLE PRECISION      DVAL   ( * ) */

/* $ Brief_I/O */

/*     Variable  I/O  Description */
/*     --------  ---  -------------------------------------------------- */
/*     NAME, */
/*     SC         I   Name of kernel data item, NAIF spacecraft ID code. */
/*     MAXNV      I   Maximum number of d.p. values to return. */
/*     N          O   Number of values actually returned. */
/*     DVAL       O   Requested kernel data item. */
/*     MXCOEF     P   Maximum number of coefficient sets in SCLK kernel. */

/* $ Detailed_Input */

/*     NAME, */
/*     SC             are, respectively, a name and a NAIF integer code */
/*                    of a spacecraft that together define the name of a */
/*                    requested kernel data item.  NAME is the full name */
/*                    as it appears in the SCLK kernel, except that it */
/*                    lacks the final underscore and spacecraft integer */
/*                    code (actually, the negative of the spacecraft */
/*                    code).  This routine combines NAME and SC to */
/*                    make up the appropriate kernel variable name. */

/*                    For example, to look up data associated with the */
/*                    name */

/*                       SCLK01_COEFFICIENTS_77 */

/*                    you would supply NAME as */

/*                       SCLK01_COEFFICIENTS */

/*                    and SC as -77. */


/*     MAXNV          is the maximum number of values to return.  MAXNV */
/*                    is used to prevent SCLD01 from writing past the end */
/*                    of the supplied array DVAL. */

/* $ Detailed_Output */

/*     N              is the number of values actually returned. */

/*     DVAL           is an array containing the requested double */
/*                    precision kernel data item. */

/* $ Parameters */

/*     MXCOEF         is the maximum number of coefficient sets in the */
/*                    array COEFFS that defines the mapping between */
/*                    encoded type 1 SCLK and a parallel time system. */
/*                    This array has dimension 3 x MXCOEF.  The value of */
/*                    MXCOEF may be increased as required. */

/* $ Exceptions */

/*     1)  If item specified by NAME and SC is not found in the kernel */
/*         pool, the error SPICE(KERNELVARNOTFOUND) is signalled.  The */
/*         output arguments are not modified. */

/*     2)  This routine can check certain data for validity.  If any of */
/*         these items have invalid values, the error */
/*         SPICE(VALUEOUTOFRANGE) is signalled.  The output arguments are */
/*         not modified.  The values in question are: */

/*            - The number of coefficients. */
/*            - The number of partition start values. */
/*            - The number of partition end values. */
/*            - The number of moduli. */
/*            - The values of the moduli (lower bounds) */
/*            - The number of offsets. */
/*            - The number of kernel identifiers. */

/*     3)  If the partition times or SCLK coefficients themselves */
/*         are invalid, this routine does nothing about it.  It is */
/*         simply not possible to detect all of the possible errors */
/*         that these data may be subject to. */

/* $ Files */

/*     None. */

/* $ Particulars */

/*     The purpose of this routine is to localize error checking for */
/*     lookups of type 1 SCLK kernel pool data.  This routine handles */
/*     lookups of double precision data. */

/* $ Examples */

/*     1)  Check a NAIF SCLK kernel for accuracy by converting the */
/*         encoded SCLK coefficients to strings with partition numbers */
/*         and converting the parallel times to UTC strings.  Print out */
/*         the results in tabular form.  In this example, the spacecraft */
/*         is Mars Observer, which has NAIF ID code -94.  We could */
/*         make the program work for Galileo by using the NAIF ID code */
/*         -77 instead of -94. */

/*            C */
/*            C     Load the SCLK kernel in question, and also load */
/*            C     a leapseconds kernel.  We use made-up names for the */
/*            C     kernel file; you would use the actual names of your */
/*            C     kernel files instead if you were to carry out this */
/*            C     procedure. */
/*            C */
/*                  CALL LDPOOL ( 'SAMPLE_MO_SCLK.KER' ) */
/*                  CALL LDPOOL ( 'LEAPSECONDS.KER'    ) */

/*                  CONAME =  SCLK01_COEFFICIENTS */
/*                  SC     =  -94 */

/*            C */
/*            C     Grab the coefficients. */
/*            C */
/*                  CALL SCLD01 ( CONAME, SC, 3*MXCOEF, NCOEFF, COEFFS ) */

/*            C */
/*            C     The SCLK coefficients are in the first row of the */
/*            C     coefficients array; the parallel times are in the */
/*            C     second.  Since the parallel time system used for MO */
/*            C     is terrestrial dynamical time (TDT), we will convert */
/*            C     the parallel time values to ET (TDB) first and then */
/*            C     convert the resulting times to UTC. */
/*            C */
/*            C     In a more robust algorithm, we'd look up the parallel */
/*            C     time system code used in the SCLK kernel rather than */
/*            C     assume that it is a particular system.  We omit this */
/*            C     check for simplicity. */
/*            C */
/*            C     We decode the SCLK coefficients using SCDECD.  Write */
/*            C     out the results to a file we'll call COMPARE.DAT. */
/*            C */
/*                  OUTFIL = 'COMPARE.DAT' */

/*                  CALL WRLINE ( OUTFIL, '    SCLK               UTC' ) */
/*                  CALL WRLINE ( OUTFIL, ' '                          ) */

/*                  DO I = 1, NCOEFF / 3 */

/*                     CALL SCDECD ( -94,  COEFF(1,I),  CLKSTR ) */
/*            C */
/*            C        Convert the parallel time coefficients, which are */
/*            C        given in TDT, to ET.  UNITIM returns this value. */
/*            C */
/*                     CALL ET2UTC ( UNITIM ( COEFF(2,I), 'TDT', 'TDB' ), */
/*                 .                 'D', */
/*                 .                  3, */
/*                 .                  UTC    ) */

/*                     LINE = ' SCLK        UTC ' */

/*                     CALL REPMC  ( LINE, 'SCLK', CLKSTR, LINE ) */
/*                     CALL REPMC  ( LINE, 'UTC',  UTC,    LINE ) */

/*                     CALL WRLINE ( OUTFIL, LINE ) */

/*                  END DO */


/* $ Restrictions */

/*     1)  SCLD01 assumes that a SCLK kernel appropriate to the */
/*         spacecraft identified by SC has been loaded. */

/*     2)  SCLD01 handles lookups of type 1 SCLK data only. */

/*     3)  If the number of values for any item read from the kernel */
/*         pool exceeds the maximum allowed value, it is may not be */
/*         possible to diagnose the error correctly, since overwriting */
/*         of memory will occur.  This problem may be fixed in a later */
/*         implementation; what must change is that the kernel pool */
/*         lookup routine called here must allow the caller to specify */
/*         the maximum number of values to return.  The input argument */
/*         MAXNV is used by this routine in anticipation of this fix. */

/* $ Literature_References */

/*     None. */

/* $ Author_and_Institution */

/*     N.J. Bachman   (JPL) */

/* $ Version */

/* -    SPICELIB Version 2.1.0, 19-OCT-1992 (NJB) */

/*        This entry point was updated to fix a bug:  if a kernel pool */
/*        lookup fails, the number of elements returned N is now set to */
/*        zero. */

/* -    SPICELIB Version 2.0.0, 17-APR-1992 (NJB) (WLT) */

/*        One constant was changed in the code for clarity; no functional */
/*        change results from this.  Some corrections and other minor */
/*        enhancements were made to the header.  Comment section for */
/*        permuted index source lines was added following the header. */

/* -    SPICELIB Version 1.0.0, 06-SEP-1990 (NJB) */

/* -& */
/* $ Index_Entries */

/*     lookup of type_1 spacecraft_clock d.p. data */
/*     lookup type_1 spacecraft_clock d.p. data */

/* -& */
/* $ Revisions */

/* -    SPICELIB Version 2.1.0, 19-OCT-1992 (NJB) */

/*        This entry point was updated to fix a bug:  if a kernel pool */
/*        lookup fails, the number of elements returned N is now set to */
/*        zero.  Formerly, this routine returned whatever value was */
/*        returned by RTPOOL.  RTPOOL, however, does not set N to zero */
/*        when the data item requested from it is not found. */

/* -    SPICELIB Version 2.0.0, 17-APR-1992 (NJB) (WLT) */

/*        The constant 1 was changed to 1.D0 in the test for the */
/*        validity of the moduli for a spacecraft clock.  The change */
/*        was made simply for clarity. */

/*        Some corrections and other minor enhancements were made to the */
/*        header.  Comment section for permuted index source lines was */
/*        added following the header. */

/* -& */

/*     Standard SPICE error handling. */

    if (return_()) {
	return 0;
    } else {
	chkin_("SCLD01", (ftnlen)6);
    }

/*     Form the name of the kernel pool datum, and do the lookup. */
/*     Note that eventually we should use a kernel pool lookup entry */
/*     that allows us to specify the maximum number of entries that */
/*     can be returned. */

    s_copy(tmpnam, name__, (ftnlen)80, name_len);
    suffix_("_#", &c__0, tmpnam, (ftnlen)2, (ftnlen)80);
    i__1 = -(*sc);
    repmi_(tmpnam, "#", &i__1, tmpnam, (ftnlen)80, (ftnlen)1, (ftnlen)80);
    rtpool_(tmpnam, n, dval, &found, (ftnlen)80);

/*     Make sure we found what we were looking for. */

    if (! found) {

/*        No values are returned in this case. */

	*n = 0;
	setmsg_(nfdmsg, (ftnlen)320);
	errch_("#", tmpnam, (ftnlen)1, (ftnlen)80);
	sigerr_("SPICE(KERNELVARNOTFOUND)", (ftnlen)24);
	chkout_("SCLD01", (ftnlen)6);
	return 0;
    }

/*     Now we must check that the number of returned values is in the */
/*     appropriate range.  We test for the following conditions: */

/*        - The number of coefficients is at least 3. */

/*        - The number of partition start values is at least 1. */

/*        - The number of partition end values is at least 1. */

/*        - The number of moduli is at least 1. */

/*        - The number of offsets is at least 1. */



/*     See if the input name is in the list of items we know about. */
/*     If it is, perform the bounds checks that apply. */

    i__ = isrchc_(name__, &c__9, namlst, name_len, (ftnlen)80);
    if (i__ != 0) {
	if (*n < lb[(i__1 = i__ - 1) < 9 && 0 <= i__1 ? i__1 : s_rnge("lb", 
		i__1, "sclu01_", (ftnlen)1131)] || *n > *maxnv) {
	    repmc_(nummsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", n, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLD01", (ftnlen)6);
	    return 0;
	}
    } else {
	if (*n > *maxnv) {
	    repmc_(nummsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
		    ftnlen)80, (ftnlen)320);
	    repmi_(errmsg, "#", n, errmsg, (ftnlen)320, (ftnlen)1, (ftnlen)
		    320);
	    setmsg_(errmsg, (ftnlen)320);
	    sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
	    chkout_("SCLD01", (ftnlen)6);
	    return 0;
	}
    }

/*     Check the values of the moduli themselves. */

    if (s_cmp(name__, namlst + 400, name_len, (ftnlen)80) == 0) {
	i__1 = *n;
	for (i__ = 1; i__ <= i__1; ++i__) {
	    if (dval[0] < 1.) {
		repmc_(bvlmsg, "#", tmpnam, errmsg, (ftnlen)320, (ftnlen)1, (
			ftnlen)80, (ftnlen)320);
		repmd_(errmsg, "#", dval, &c__14, errmsg, (ftnlen)320, (
			ftnlen)1, (ftnlen)320);
		setmsg_(errmsg, (ftnlen)320);
		sigerr_("SPICE(VALUEOUTOFRANGE)", (ftnlen)22);
		chkout_("SCLD01", (ftnlen)6);
		return 0;
	    }
	}
    }
    chkout_("SCLD01", (ftnlen)6);
    return 0;
} /* sclu01_ */

/* Subroutine */ int sclu01_(char *name__, integer *sc, integer *maxnv, 
	integer *n, integer *ival, doublereal *dval, ftnlen name_len)
{
    return sclu01_0_(0, name__, sc, maxnv, n, ival, dval, name_len);
    }

/* Subroutine */ int scli01_(char *name__, integer *sc, integer *maxnv, 
	integer *n, integer *ival, ftnlen name_len)
{
    return sclu01_0_(1, name__, sc, maxnv, n, ival, (doublereal *)0, name_len)
	    ;
    }

/* Subroutine */ int scld01_(char *name__, integer *sc, integer *maxnv, 
	integer *n, doublereal *dval, ftnlen name_len)
{
    return sclu01_0_(2, name__, sc, maxnv, n, (integer *)0, dval, name_len);
    }

