/* zzekgrcp.f -- translated by f2c (version 19980913).
   You must link the resulting object file with the libraries:
	-lf2c -lm   (in that order)
*/

#include "f2c.h"

/* $Procedure      ZZEKGRCP ( EK, get record companion pointer ) */
/* Subroutine */ int zzekgrcp_(integer *handle, integer *recptr, integer *ptr)
{
    extern /* Subroutine */ int zzekpgch_(integer *, char *, ftnlen);
    extern logical failed_(void);
    extern /* Subroutine */ int dasrdi_(integer *, integer *, integer *, 
	    integer *);
    integer loc;

/* $ Abstract */

/*     Get the companion pointer of a specified EK record. */

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

/*     EK */

/* $ Keywords */

/*     EK */
/*     PRIVATE */

/* $ Declarations */
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


/*     Include Section:  EK Segment Descriptor Parameters */

/*        eksegdsc.inc  Version 8  06-NOV-1995 (NJB) */


/*     All `base addresses' referred to below are the addresses */
/*     *preceding* the item the base applies to.  This convention */
/*     enables simplied address calculations in many cases. */

/*     Size of segment descriptor.  Note:  the include file ekcoldsc.inc */
/*     must be updated if this parameter is changed.  The parameter */
/*     CDOFF in that file should be kept equal to SDSCSZ. */


/*     Index of the segment type code: */


/*     Index of the segment's number.  This number is the segment's */
/*     index in the list of segments contained in the EK to which */
/*     the segment belongs. */


/*     Index of the DAS integer base address of the segment's integer */
/*     meta-data: */


/*     Index of the DAS character base address of the table name: */


/*     Index of the segment's column count: */


/*     Index of the segment's record count: */


/*     Index of the root page number of the record tree: */


/*     Index of the root page number of the character data page tree: */


/*     Index of the root page number of the double precision data page */
/*     tree: */


/*     Index of the root page number of the integer data page tree: */


/*     Index of the `modified' flag: */


/*     Index of the `initialized' flag: */


/*     Index of the shadowing flag: */


/*     Index of the companion file handle: */


/*     Index of the companion segment number: */


/*     The next three items are, respectively, the page numbers of the */
/*     last character, d.p., and integer data pages allocated by the */
/*     segment: */


/*     The next three items are, respectively, the page-relative */
/*     indices of the last DAS word in use in the segment's */
/*     last character, d.p., and integer data pages: */


/*     Index of the DAS character base address of the column name list: */


/*     The last descriptor element is reserved for future use.  No */
/*     parameter is defined to point to this location. */


/*     End Include Section:  EK Segment Descriptor Parameters */

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


/*     Include Section:  EK Record Pointer Parameters */

/*        ekrecptr.inc Version 2  18-JUL-1995 (NJB) */


/*     This file declares parameters used in EK record pointers. */
/*     Each segment references data in a given record via two levels */
/*     of indirection:  a record number points to a record pointer, */
/*     which is a structured array of metadata and data pointers. */

/*     Record pointers always occupy contiguous ranges of integer */
/*     addresses. */

/*     The parameter declarations in this file depend on the assumption */
/*     that integer pages contain 256 DAS integer words and that the */
/*     maximum number of columns in a segment is 100.  Record pointers */
/*     are stored in integer data pages, so they must fit within the */
/*     usable data area afforded by these pages.  The size of the usable */
/*     data area is given by the parameter IPSIZE which is declared in */
/*     ekdatpag.inc.  The assumed value of IPSIZE is 254. */


/*     The first element of each record pointer is a status indicator. */
/*     The meanings of status indicators depend on whether the parent EK */
/*     is shadowed or not.  For shadowed EKs, allowed status values and */
/*     their meanings are: */

/*        OLD       The record has not been modified since */
/*                  the EK containing the record was opened. */

/*        UPDATE    The record is an update of a previously existing */
/*                  record.  The original record is now on the */
/*                  modified record list. */

/*        NEW       The record has been added since the EK containing the */
/*                  record was opened.  The record is not an update */
/*                  of a previously existing record. */

/*        DELOLD    This status applies only to a backup record. */
/*                  DELOLD status indicates that the record corresponds */
/*                  to a deleted OLD record in the source segment. */

/*        DELNEW    This status applies only to a backup record. */
/*                  DELNEW status indicates that the record corresponds */
/*                  to a deleted NEW record in the source segment. */

/*        DELUPD    This status applies only to a backup record. */
/*                  DELUPD status indicates that the record corresponds */
/*                  to a deleted UPDATEd record in the source segment. */

/*     In EKs that are not shadowed, all records have status OLD. */



/*     The following parameters refer to indices within the record */
/*     pointer structure: */

/*     Index of status indicator: */


/*     Each record pointer contains a pointer to its companion:  for a */
/*     record belonging to a shadowed EK, this is the backup counterpart, */
/*     or if the parent EK is itself a backup EK, a pointer to the */
/*     record's source record.  The pointer is UNINIT (see below) if the */
/*     record is unmodified. */

/*     Record companion pointers contain record numbers, not record */
/*     base addresses. */

/*     Index of record's companion pointer: */


/*     Each data item is referenced by an integer.  The meaning of */
/*     this integer depends on the representation of data in the */
/*     column to which the data item belongs.  Actual lookup of a */
/*     data item must be done by subroutines appropriate to the class of */
/*     the column to which the item belongs.  Note that data items don't */
/*     necessarily occupy contiguous ranges of DAS addresses. */

/*     Base address of data pointers: */


/*     Maximum record pointer size: */


/*     Data pointers are given the value UNINIT to start with; this */
/*     indicates that the data item is uninitialized.  UNINIT is */
/*     distinct from the value NULL.  NOBACK indicates an uninitialized */
/*     backup column entry. */


/*     End Include Section:  EK Record Pointer Parameters */

/* $ Brief_I/O */

/*     Variable  I/O  Description */
/*     --------  ---  -------------------------------------------------- */
/*     HANDLE     I   File handle. */
/*     RECPTR     I   Record pointer. */
/*     PTR        O   Pointer to companion of specified EK record. */

/* $ Detailed_Input */

/*     HANDLE         is a file handle of an EK open for read or write */
/*                    access. */

/*     RECPTRC        is a pointer to the record whose companion pointer */
/*                    is desired. */

/* $ Detailed_Output */

/*     PTR            is a pointer to the companion record of the */
/*                    specified input record.  If the input record */
/*                    belongs to a shadowed EK, the companion record */
/*                    is the corresponding backup record.  If the input */
/*                    record is a backup record, the companion record */
/*                    is the corresponding source record. */

/* $ Parameters */

/*     None. */

/* $ Exceptions */

/*     1)  If HANDLE is invalid, the error will be diagnosed by routines */
/*         called by this routine. */

/*     2)  If an I/O error occurs while reading the indicated file, the */
/*         error will be diagnosed by routines called by this routine. */

/* $ Files */

/*     See the EK Required Reading for a discussion of the EK file */
/*     format. */

/* $ Particulars */

/*     This routine sets the companion record pointer of a specified EK */
/*     record. */

/* $ Examples */

/*     See EKROLL. */

/* $ Restrictions */

/*     None. */

/* $ Literature_References */

/*     None. */

/* $ Author_and_Institution */

/*     N.J. Bachman   (JPL) */

/* $ Version */

/* -    Beta Version 1.0.0, 10-OCT-1995 (NJB) */

/* -& */

/*     SPICELIB functions */


/*     Local variables */


/*     Use discovery check-in. */


/*     Is this file handle valid--is the file open for paged write */
/*     access?  Signal an error if not. */

    zzekpgch_(handle, "WRITE", (ftnlen)5);
    if (failed_()) {
	return 0;
    }

/*     Compute the companion pointer and set the pointer. */

    loc = *recptr + 2;
    dasrdi_(handle, &loc, &loc, ptr);
    return 0;
} /* zzekgrcp_ */

