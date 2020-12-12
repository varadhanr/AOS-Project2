#!/bin/bash


# Change this to your netid
netid=vrr180003

#
# Root directory of your project
PROJDIR=$HOME/TestProj

#
# Directory where the config file is located on your local system
CONFIGLOCAL=config.dat

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    j=$( echo $i | awk '{ print $1}')
    while [[ $n -lt $j ]]
    do
    	read line
        host=$( echo $line | awk '{ print $2 }' )

        echo $host
        ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host killall -u $netid &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
