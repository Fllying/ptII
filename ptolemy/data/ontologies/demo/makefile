# Makefile for ontologies demos
#
# @Authors: Christopher Brooks (makefile only)
#
# $Id$
#
# @Copyright (c) 2009-2014 The Regents of the University of California.
# All rights reserved.
#
# Permission is hereby granted, without written agreement and without
# license or royalty fees, to use, copy, modify, and distribute this
# software and its documentation for any purpose, provided that the
# above copyright notice and the following two paragraphs appear in all
# copies of this software.
#
# IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
# FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
# ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
# THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
# SUCH DAMAGE.
#
# THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
# PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
# CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
# ENHANCEMENTS, OR MODIFICATIONS.
#
# 						PT_COPYRIGHT_VERSION_2
# 						COPYRIGHTENDKEY

ME =		ptolemy/data/ontologies/demo

DIRS = 	CarTracking ConstAbstractInterpretation ConstAbstractInterpretationObservable \
	ConstNonconst ConstPropagation DimensionSystemExample \
	ExceptionAnalysis MonotonicityAnalysis \
	Observable ProductLattices UnitSystemExample

# Root of the Ptolemy II directory
ROOT =		../../../..

CLASSPATH =	$(ROOT)

# Get configuration info
CONFIG =	$(ROOT)/mk/ptII.mk
include $(CONFIG)

# Used to build jar files
PTPACKAGE = 	demo
# Include the .class files from these jars in PTCLASSALLJAR
PTCLASSALLJARS = \
	CarTracking/CarTracking.jar \
	ConstAbstractInterpretation/ConstAbstractInterpretation.jar \
	ConstAbstractInterpretationObservable/ConstAbstractInterpretationObservable.jar \
	ConstNonconst/ConstNonconst.jar \
	ConstPropagation/ConstPropagation.jar \
	DimensionSystemExample/DimensionSystemExample.jar \
	ExceptionAnalysis/ExceptionAnalysis.jar \
	MonotonicityAnalysis/MonotonicityAnalysis.jar \
	Observable/Observable.jar \
	ProductLattices/ProductLattices.jar \
	UnitSystemExample/UnitSystemExample.jar

PTCLASSALLJAR = $(PTPACKAGE).jar

JSRCS =

OTHER_FILES_TO_BE_JARED =

EXTRA_SRCS =	$(JSRCS) $(OTHER_FILES_TO_BE_JARED)

# Sources that may or may not be present, but if they are present, we don't
# want make checkjunk to barf on them.
# if $(DIRS) includes demo, then do not set MISC_FILES= $(DIRS) 
MISC_FILES = $(DIRS)

# make checkjunk will not report OPTIONAL_FILES as trash
# make distclean removes OPTIONAL_FILES
OPTIONAL_FILES = 

JCLASS = $(JSRCS:%.java=%.class)

all: jclass
install: jclass jars


# Get the rest of the rules
include $(ROOT)/mk/ptcommon.mk
