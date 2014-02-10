#!/bin/bash
FILES=tests/*
for f in $FILES
do
	echo "READ FILE $f"
	java -cp ./:lib/beaver-rt-0.9.11.jar CoolDriver $f
	echo ""
#	sleep 1

done
