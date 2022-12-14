
# Name of the pipeline module
# Used as the name of the shared library (NAME.so)
# and as the name of the executable (NAME)
# 
# Copyright 2017 United States Government as represented by the
# Administrator of the National Aeronautics and Space Administration.
# All Rights Reserved.
# 
# NASA acknowledges the SETI Institute's primary role in authoring and
# producing the Kepler Data Processing Pipeline under Cooperative
# Agreement Nos. NNA04CC63A, NNX07AD96A, NNX07AD98A, NNX11AI13A,
# NNX11AI14A, NNX13AD01A & NNX13AD16A.
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
NAME=tps

# Relative path to the matlab directory
SOC_CODE_MATLAB_REL_PATH=../..

-include $(SOC_CODE_MATLAB_REL_PATH)/build/macros.mk

DVMEX = ../../dv/validate/$(MEX)

# Specify your .m files here. 
# Not all .m files used by your module need to be specified here
# the MATLAB compiler is pretty good about auto-detecting dependencies,
# so usually just specifying your entry point is good enough.  It does miss
# a few though (like calling a function dynamically using eval()), so in those
# cases just add them to the list here.
MSRC=\
	mfiles/tps_matlab_controller.m

MEXFLAGS = \
	-f mex/mexopts.sh -Imex 

DVMEXFLAGS = \
	-f mex/mexopts.sh -I$(DVMEX) 

# Specify your MEX code here
MEXSRC = \
	mex/fold_phases.c mex/fold_periods.c mex/find_ses_in_mes.c mex/moving_max.c mex/median_filter.c

DVMEXSRC = \
	$(DVMEX)/bootstrap.c $(DVMEX)/compute_binomial_coefficient.c \
	$(DVMEX)/estimate_iterations.c $(DVMEX)/compute_combinatoric_sums.c

DVMEXFILES := $(DVMEXSRC:.c=.$(MEXSUFFIX))
DVMEXFILES := $(DVMEXFILES:.cc=.$(MEXSUFFIX))
DVMEXFILES := $(patsubst $(DVMEX)/%,$(BUILDMEX)/%,$(DVMEXFILES))

-include $(SOC_CODE_MATLAB_REL_PATH)/build/rules.mk

$(BUILDMEX)/%.$(MEXSUFFIX) : $(DVMEX)/%.c
	-$(MKDIR) -p $(dir $@)
	$(MEX) -outdir $(dir $@) $(DVMEXFLAGS) -f $(DVMEX)/../mexopts.sh $< $(MEXEXTRA)

$(BUILDBIN) : $(DVMEXFILES) $(MSRC)
	-$(MKDIR) -p $(dir $@)
	$(MCC) $(MCCOPTS) \
		$(addprefix -a ,$(MEXFILES)) \
		$(addprefix -a ,$(DVMEXFILES)) \
		$(addprefix -a ,$(MCCARCHIVEFILES)) \
		-m -d $(BINDIR) -o $(NAME) $(MGENDIR)/$(NAME)_init.m \
		$(MGENDIR)/$(NAME)_main.m $(MSRC)
