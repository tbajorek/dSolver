# This is a Makefile for Java/C client-server
# system using RMI and JNI.
# WARNING: all warning are disabled with -w flag.

# For WFiIS Stud. Lab. on Taurus - with Java SDK 8

DHOST?=taurus
DPORT?=1444
SHOST?=taurus
SPORT?=1445
JAVA_HOME=/opt/jdk1.8.0_60
TARGETDIR=target/
CLASSESDIR=$(TARGETDIR)classes/
LIBDIR=$(TARGETDIR)lib/

PWD=$(shell pwd)
CLASSPATH=$(CLASSESDIR) #`pwd`
LD_LIBRARY_PATH=`pwd`

JAVACFLAGS=-classpath $(CLASSESDIR) -d $(CLASSESDIR)
all:    server client java.policy # make all
	@echo '----------------------------------------'
	@echo 'printing Java policy file to be used...'
	cat java.policy
	@echo '----------------------------------------'
	@echo "Start server for decomposition with most basic (too open) security policy file (with optional registry port) (& == in background):"
	@echo "( LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java -cp $(CLASSPATH) -Djava.security.policy=$(PWD)/java.policy -Djava.rmi.server.codebase=file://$(CLASSESDIR)/ server.Decomposer <$(DPORT)> ) &"
	@echo '----------------------------------------'
	@echo "Start server for solving with most basic (too open) security policy file (with optional registry port) (& == in background):"
	@echo "( LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java -cp $(CLASSPATH) -Djava.security.policy=$(PWD)/java.policy -Djava.rmi.server.codebase=file://$(CLASSESDIR)/ server.Solver <$(SPORT)> ) &"
	@echo '----------------------------------------'
	@echo "Start client:"
	@echo "$(JAVA_HOME)/bin/java -Djava.security.policy=$(PWD)/java.policy client.Client <$(DHOST)> <$(DPORT)> <$(SHOST)> <$(SPORT)>"

server: $(TARGETDIR) common
	@echo 'compiling server (Java)...'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) \
	./src/java/server/*.java
	@echo 'creating stub and skeleton files...'
	### not needed for JAVA_HOME > 1.4 ### $(JAVA_HOME)/bin/rmic PMimpl
	@echo 'creating JNI header files...'
	$(JAVA_HOME)/bin/javah -d src/clib/include -cp $(CLASSESDIR) -jni server.Decomposer
	$(JAVA_HOME)/bin/javah -d src/clib/include -cp $(CLASSESDIR) -jni server.Solver
	@echo 'compiling modules (C)...'

	gcc -fPIC -c src/clib/src/*.c -I$(JAVA_HOME)/include \
				-I$(JAVA_HOME)/include/linux -Isrc/clib/include
	@echo 'creating shared (dynamic) library...'
	ld -shared  -fopenmp -o $(LIBDIR)/libDecomposeWrapper.so decompose.o Decomposer.o
	ld -shared  -fopenmp -o $(LIBDIR)/libSolveWrapper.so solve.o Solver.o
	rm -f *.o

client: $(TARGETDIR) common# client.java # client files
	@echo 'compiling client...'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) ./src/java/client/*.java

clean:  # remove class files
	@echo 'clearing files...'
	rm -rf target
	rm -rf src/clib/include/*


common: $(TARGETDIR)
	@echo 'compiling java common...'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) ./src/java/common/*.java

$(TARGETDIR):
	mkdir -p $(TARGETDIR)
	mkdir -p $(LIBDIR)
	mkdir -p $(addprefix $(CLASSESDIR),$(shell cd src/java ;\
						find ./ -type d; cd ../../))

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
	-Djava.security.policy=$(PWD)/java.policy client.Client $(DHOST) $(DPORT) $(SHOST) $(SPORT))
