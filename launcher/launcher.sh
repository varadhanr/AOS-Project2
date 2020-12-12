#!/bin/bash

# Change this to your netid
netid=vrr180003

# Root directory of your project
PROJDIR=$HOME/AOSProject1

GRADLEWDIR=/home/011/v/vr/vrr180003/AOS-Project2/gradlew

JARFILE=/home/011/v/vr/vrr180003/Graduate-Studies/AdvanceOS/AOS-Project2/build/libs/AOS-Project2-1.0.jar

# Directory where the config file is located on your local system
CONFIGLOCAL=config.dat

# Directory your java classes are in
BINDIR=$PROJDIR

# Your main project class
PROG=AOS-Project2/MainClass

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i

    j=$( echo $i | awk '{ print $1 }')
   # echo $i
    while [[ $n -lt $j ]]
    do
    	read line
        
    	p=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )
        port=$( echo $line | awk '{ print $3 }' )
	
	    #ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -cp $BINDIR $PROG $p &
        ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -jar $JARFILE $p &

        #ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host $GRADLEWDIR -p $PROJDIR run --args="${p}" &

        n=$(( n + 1 ))
    done
)
