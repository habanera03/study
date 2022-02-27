#!/bin/bash

if [ ! -d "test" ] ; then
    mkdir test
else
    rm -rf test/*
fi

if [ ! -d "test2" ] ; then
    mkdir test2
else
    rm -rf test2/*
fi

cp -rp change_lib/log4j-web-2.17.0.jar test/.
mv test/log4j-web-2.17.0.jar test/log4j-web-2.1.0.jar
cp -rp test/log4j-web-2.1.0.jar test/log4j-web-2.1.0.jar.back
cp -rp test/log4j-web-2.1.0.jar test/log4j-web-2.2.1.jar

cp -rp change_lib/* test2/.
mv test2/log4j-web-2.17.0.jar test2/log4j-web-2.1.0.jar
mv test2/log4j-core-2.17.0.jar test2/log4j-core-2.1.0.jar
