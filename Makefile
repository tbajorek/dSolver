# This is a Makefile for Java/C client-server
# system using RMI and JNI.
# WARNING: all warning are disabled with -w flag.

# For WFiIS Stud. Lab. on Taurus - with Java SDK 8

JDK        = /usr/lib/jvm/java-8-oracle
PWD=`pwd`
CLASSPATH=`pwd`
LD_LIBRARY_PATH=`pwd`

all:    server client java.policy # make all
	@echo 'printing Java policy file to be used...'
	cat java.policy
	@echo "Start RMI registry (server-side name server) in other directory (& == in background):"
	@echo "and on default port 1099 (or some other unused TCP port):"
	@echo "( cd /tmp ; CLASSPATH=`pwd` $(JDK)/bin/rmiregistry 1099 -Djava.rmi.server.codebase=file://$(PWD)/ ) &"
	@echo "Start server with most basic (too open) security policy file (with optional registry port) (& == in background):"
	@echo "( LD_LIBRARY_PATH=`pwd` $(JDK)/bin/java -cp . -Djava.security.policy=$(PWD)/java.policy -Djava.rmi.server.codebase=file://$(PWD)/ PMimpl ) &"
	@echo "Start client:"
	@echo "$(JDK)/bin/java -Djava.security.policy=$(PWD)/java.policy client <server_host> <power> <optional_registry_port>"
	
server: PMimpl.java PMiface.java \
	PMdata.java PMimpl.c \
	PowerMean.c    # server files
	@echo 'compiling server (Java)...'
	$(JDK)/bin/javac PMiface.java \
	PMimpl.java PMdata.java
	@echo 'creating stub and skeleton files...'
	### not needed for JDK > 1.4 ### $(JDK)/bin/rmic PMimpl
	@echo 'creating JNI header files...'
	$(JDK)/bin/javah -jni PMimpl
	@echo 'compiling modules (C)...'
	gcc -fPIC -c *.c -I$(JDK)/include -I$(JDK)/include/linux
	@echo 'creating shared (dynamic) library...'
	ld -shared -o libPowerMeanWrapper.so *.o
	rm -f *.o
	
client: client.java # client files
	@echo 'compiling client...'
	$(JDK)/bin/javac client.java

clean:  # remove class files
	@echo 'clearing files...'
	rm -f client *.class *.so *.h *.o

