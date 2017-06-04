# This is a Makefile for Java/C client-server
# system using RMI and JNI.
# WARNING: all warning are disabled with -w flag.

# For WFiIS Stud. Lab. on Taurus - with Java SDK 8

SERVERHOST?=taurus
PORT?=1444
JAVA_HOME=/usr/lib/jvm/java-8-oracle/
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
	@echo "Start RMI registry (server-side name server) in other directory (& == in background):"
	@echo "and on default port 1099 (or some other unused TCP port):"
	@echo "( cd /tmp ; CLASSPATH=$(CLASSPATH) $(JAVA_HOME)/bin/rmiregistry 1099 -Djava.rmi.server.codebase=file://$(PWD)/ ) &"
	@echo '----------------------------------------'
	@echo "Start server with most basic (too open) security policy file (with optional registry port) (& == in background):"
	@echo "( LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java -cp $(CLASSPATH) -Djava.security.policy=$(PWD)/java.policy -Djava.rmi.server.codebase=file://$(CLASSESDIR)/ server.PMimpl ) &"
	@echo '----------------------------------------'
	@echo "Start client:"
	@echo "$(JAVA_HOME)/bin/java -Djava.security.policy=$(PWD)/java.policy client <$(SERVERHOST)> <power> <$(PORT)>"

server: $(TARGETDIR) common
	@echo 'compiling server (Java)...'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) \
	./src/java/server/*.java
	@echo 'creating stub and skeleton files...'
	### not needed for JAVA_HOME > 1.4 ### $(JAVA_HOME)/bin/rmic PMimpl
	@echo 'creating JNI header files...'
	$(JAVA_HOME)/bin/javah -d src/clib/include -cp $(CLASSESDIR) -jni server.PMimpl
	@echo 'compiling modules (C)...'

	gcc -fPIC -c src/clib/src/*.c -I$(JAVA_HOME)/include \
				-I$(JAVA_HOME)/include/linux -Isrc/clib/include
	@echo 'creating shared (dynamic) library...'
	ld -shared  -o $(LIBDIR)/libPowerMeanWrapper.so *.o
	rm -f *.o

client: $(TARGETDIR) common# client.java # client files
	@echo 'compiling client...'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) ./src/java/client/*.java

clean:  # remove class files
	@echo 'clearing files...'
	rm -rf target
	rm -rf src/clib/include/server_PMimpl.h


common: $(TARGETDIR)
	@echo 'compiling java common...'
	$(JAVA_HOME)/bin/javac $(JAVACFLAGS) ./src/java/common/*.java

$(TARGETDIR):
	mkdir -p $(TARGETDIR)
	mkdir -p $(LIBDIR)
	mkdir -p $(addprefix $(CLASSESDIR),$(shell cd src/java ;\
						find ./ -type d; cd ../../))

runserver:
	( LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java -cp $(CLASSPATH) \
	-Djava.security.policy=$(PWD)/java.policy \
	-Djava.rmi.server.codebase=file://$(CLASSESDIR)/ server.PMimpl $(PORT) )

runclient:
	(LD_LIBRARY_PATH=$(LIBDIR) $(JAVA_HOME)/bin/java \
	-cp $(CLASSPATH) \
	-Djava.security.policy=$(PWD)/java.policy client.client $(SERVERHOST) 43 $(PORT))
