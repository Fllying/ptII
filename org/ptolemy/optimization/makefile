# Makefile for Optimization library
#
# @Authors: Christopher Brooks
#
# @Version: $Id$
#
# @Copyright (c) 2012-2015 The Regents of the University of California.
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

ME =		org/ptolemy/optimization

DIRS =		demo doc test

# Root of the Ptolemy II directory
ROOT =		../../..

CLASSPATH =	$(ROOT)$(CLASSPATHSEPARATOR)$(JNA_JAR)

# Get configuration info
CONFIG =	$(ROOT)/mk/ptII.mk
include $(CONFIG)

# Variables and rules used to create source file distributions
PTPACKAGE = 	optimization
PTCLASSJAR =	$(PTPACKAGE).jar

# Include the .class files from these jars in PTCLASSALLJAR
#PTCLASSALLJAR = $(PTPACKAGE).jar

# Java Sources, keep these alphabetical,
JSRCS = \
	ApproximatedObjectiveFunction.java \
	BarrierMethod.java \
	CompositeOptimizer.java \
	CompositeOptimizerUsingGradient.java \
	ObjectiveFunction.java \
	ObjectiveFunctionWithSoftConstraints.java \
	PositionConstraintCalculator.java

OTHER_FILES_TO_BE_JARED = \
	CompositeOptimizerIcon.xml \
	compositeOptimizer.xml \
	optimization.xml \
	compositeOptimizerUsingGradient.xml  \
	trajectoryOptimizer.xml

# Files that are not compiled, such as .tcl and .itcl files.
EXTRA_SRCS = $(JSRCS) $(OTHER_FILES_TO_BE_JARED)

# Non-source files such as README files and subdirectories
MISC_FILES = doc

# make checkjunk will not report OPTIONAL_FILES as trash
# make distclean removes OPTIONAL_FILES
OPTIONAL_FILES = \
	demo \
	doc \
	test \
	'CompositeOptimizer$$1.class' \
	'CompositeOptimizer$$OptimizerComposite.class' \
	'CompositeOptimizer$$OptimizerDirector$$1.class' \
	'CompositeOptimizer$$OptimizerDirector.class' \
	'CompositeOptimizerUsingGradient$$1.class' \
	'CompositeOptimizerUsingGradient$$OptimizerComposite.class' \
	'CompositeOptimizerUsingGradient$$OptimizerDirector$$1.class' \
	'CompositeOptimizerUsingGradient$$OptimizerDirector$$2.class' \
	'CompositeOptimizerUsingGradient$$OptimizerDirector.class' \
	'ObjectiveFunctionForPhaseI.class'

KRUFT =

JCLASS = $(JSRCS:%.java=%.class)

all: sources jclass

install: all jars 

# Get the rest of the rules
include $(ROOT)/mk/ptcommon.mk
 


