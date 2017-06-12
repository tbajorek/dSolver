# This is a Makefile for Java/C client-server
# system using RMI and JNI.
# WARNING: all warning are disabled with -w flag.

# For WFiIS Stud. Lab. on Taurus - with Java SDK 8

DHOST?=taurus
DPORT?=1444
SHOST?=taurus
SPORT?=1445
AFILE?=resources/A3.txt
BFILE?=resources/b3.txt
XFILE?=resources/x3.txt
# JAVA_HOME?=/opt/jdk1.8.0_60
JAVA_HOME?=/usr/lib/jvm/java-8-oracle# Lukasz & taurus

TARGETDIR=target
CLASSESDIR=$(TARGETDIR)/classes/
LIBDIR=$(TARGETDIR)/lib/

PWD=$(shell pwd)
CLASSPATH=$(CLASSESDIR) #`pwd`
LD_LIBRARY_PATH=`pwd`

JAVACFLAGS=-classpath $(CLASSESDIR) -d $(CLASSESDIR)

all:    server client # make all

server: $(TARGETDIR) common
	@echo 'compiling server (Java) STARTED'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) \
	./src/java/server/*.java
	@echo 'compiling server (Java) SUCCESSED'
	@echo " "
	#@echo 'creating stub and skeleton files...'
	### not needed for JAVA_HOME > 1.4 ### $(JAVA_HOME)/bin/rmic PMimpl
	@echo " "
	@echo 'creating JNI header files STARTED'
	$(JAVA_HOME)/bin/javah -d src/clib/include -cp $(CLASSESDIR) \
	-jni server.Decomposer
	$(JAVA_HOME)/bin/javah -d src/clib/include -cp $(CLASSESDIR) \
	-jni server.Solver
	@echo 'creating JNI header files SUCCESSED'
	@echo " "
	@echo 'compiling modules (C) STARTED'

	gcc -fPIC -c src/clib/src/*.c -I$(JAVA_HOME)/include \
				-I$(JAVA_HOME)/include/linux -Isrc/clib/include
	@echo 'creating shared (dynamic) library...'
	ld -shared -o $(LIBDIR)/libDecomposeWrapper.so decompose.o Decomposer.o
	ld -shared -o $(LIBDIR)/libSolveWrapper.so solve.o Solver.o
	rm -f *.o
	@echo 'compiling modules (C) SUCCESSED'
	@echo " "

client: $(TARGETDIR) common# client.java # client files
	@echo 'compiling client STARTED'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) ./src/java/client/*.java
	@echo 'compiling client SUCCESSED'
	@echo " "

clean:  # remove class files
	@echo 'clearing files STARTED'
	rm -rf target
	rm -rf src/clib/include/*
	@echo 'clearing files SUCCESSED'
	@echo " "


common: $(TARGETDIR)
	@echo 'compiling java common STARTED'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) ./src/java/common/*.java
	@echo 'compiling java common SUCCESSED'
	@echo " "

$(TARGETDIR):
	@echo 'creating directories structure STARTED'
	mkdir -p $(TARGETDIR)
	mkdir -p $(LIBDIR)
	mkdir -p $(addprefix $(CLASSESDIR),$(shell cd src/java ;\
						find ./ -type d; cd ../../))
	@echo 'creating directories structure SUCCESSED'
	@echo " "

rundserver:
	( LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java -cp $(CLASSPATH) \
	-Djava.security.policy=$(PWD)/java.policy \
	-Djava.rmi.server.codebase=file://$(CLASSESDIR)/ server.Decomposer $(DPORT) )

runsserver:
	( LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java -cp $(CLASSPATH) \
	-Djava.security.policy=$(PWD)/java.policy \
	-Djava.rmi.server.codebase=file://$(CLASSESDIR)/ server.Solver $(SPORT) )

runclient:
	(LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java \
	-cp $(CLASSPATH) \
	-Djava.security.policy=$(PWD)/java.policy \
	 client.Client $(DHOST) $(DPORT) $(SHOST) $(SPORT) $(AFILE) $(BFILE) $(XFILE))

help:
	@echo '------------------------HELP------------------------'
	@echo 'all      - Requires java.policy file.'
	@echo '           Can trigger server and client task.'
	@echo 'server   - Compiles Java server package.'
	@echo	'           Creates JNI header files for server.Decomposer'
	@echo	'           and server.Solver classes.'
	@echo	'           Compiles C modules and creates C libraries.'
	@echo	'           Can trigger $(TARGETDIR) task. Triggers common task.'
	@echo 'client   - Compiles Java client package.'
	@echo	'           Can trigger $(TARGETDIR) task. Triggers common task.'
	@echo 'common   - Compiles Java common package.'
	@echo	'           Can trigger $(TARGETDIR) task.'
	@echo '$(TARGETDIR)   - Creates directories structure for output files.'
	@echo	'           Can trigger $(TARGETDIR) task.'
	@echo 'clean    - Removes JNI header files and target directory with content.'
	@echo 'runclient- Runs client. Default parameters:'
	@echo '          	DHOST=$(DHOST)   Server host to decompose LU'
	@echo '          	DPORT=$(DPORT)   Server port to decompose LU'
	@echo '          	SHOST=$(SHOST)   Server host to solve equations'
	@echo '          	SPORT=$(SPORT)   Server port to solve equations'
	@echo '          	AFILE=$(AFILE)   File with A matrix'
	@echo '          	BFILE=$(BFILE)   File with b vector'
	@echo '          	XFILE=$(XFILE)   File path where computing'
	@echo '          	                         results will be saved'
	@echo '          	IMPORTANT: Before runnning this task execute make all,'
	@echo '          	make rundserver and make runsserver'
	@echo 'rundserver-Start server for decomposition. Default parameter:'
	@echo '          	DPORT=$(DPORT)   Server port to decompose LU'
	@echo '          	IMPORTANT: Before runnning this task execute make server,'
	@echo 'runsserver-Start server for solving. Default parameter:'
	@echo '          	SPORT=$(SPORT)   Server port to solve equations'
	@echo '          	IMPORTANT: Before runnning this task execute make server,'
	@echo ''
	@echo '----------------------Advices----------------------'
	@echo '1) Check, if JAVA_HOME variable value in makefile'
	@echo '    is compatible with your java location.'
	@echo '    DEFAULT: JAVA_HOME=$(JAVA_HOME)'
