#!/bin/bash
# 
# Copyright 2017 United States Government as represented by the
# Administrator of the National Aeronautics and Space Administration.
# All Rights Reserved.
# 
# This file is available under the terms of the NASA Open Source Agreement
# (NOSA). You should have received a copy of this agreement with the
# Kepler source code; see the file NASA-OPEN-SOURCE-AGREEMENT.doc.
# 
# No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY
# WARRANTY OF ANY KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY,
# INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE
# WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM
# INFRINGEMENT, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR
# FREE, OR ANY WARRANTY THAT DOCUMENTATION, IF PROVIDED, WILL CONFORM
# TO THE SUBJECT SOFTWARE. THIS AGREEMENT DOES NOT, IN ANY MANNER,
# CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY PRIOR RECIPIENT
# OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR ANY
# OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.
# FURTHER, GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES
# REGARDING THIRD-PARTY SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE,
# AND DISTRIBUTES IT "AS IS."
#
# Waiver and Indemnity: RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS
# AGAINST THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND
# SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT. IF RECIPIENT'S USE OF
# THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES, DEMANDS, DAMAGES,
# EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES FROM
# PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT
# SOFTWARE, RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED
# STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY
# PRIOR RECIPIENT, TO THE EXTENT PERMITTED BY LAW. RECIPIENT'S SOLE
# REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE, UNILATERAL
# TERMINATION OF THIS AGREEMENT.
#

# This script is used to launch nas-build.sh as a qsub job to build the SOC code tree on Pleiades (NASA Ames Supercomputer)
#
# usage: nas-build.sh KEPLER_ROOT CONFIG_TEMPLATE_FILENAME
#    ex: nas-build-launcher.sh /path/to/TEST pleiades-LAB.template
#

set -o errexit

if [ $# -ne 2 ]
then
  echo "Usage: `basename $0` KEPLER_ROOT CONFIG_TEMPLATE_FILENAME"
  exit -1
fi

KEPLER_ROOT=${1}
CONFIG_TEMPLATE_FILENAME=${2}

if [ ! -d $KEPLER_ROOT ]
then
    echo "KEPLER_ROOT does not exist or is not a directory"
    exit -1
fi

CONFIG_TEMPLATE_PATH=$KEPLER_ROOT/skel/etc/$CONFIG_TEMPLATE_FILENAME

if [ ! -f $CONFIG_TEMPLATE_PATH ]
then
    echo "$CONFIG_TEMPLATE_FILENAME does not exist or is not a regular file: "  $CONFIG_TEMPLATE_PATH
    exit -1
fi

echo $KEPLER_ROOT/skel/bin/nas-build.sh $KEPLER_ROOT $CONFIG_TEMPLATE_FILENAME | qsub -N SOC-BUILD -q devel -rn -l walltime=02:00:00 -l select=1:model=ivy -W group_list=s1089 -v LM_LICENSE_FILE=1705@redirect

