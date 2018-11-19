JavaPOS Configuration Loader [![Build Status](https://travis-ci.org/JavaPOSWorkingGroup/javapos-config-loader.svg?branch=master)](https://travis-ci.org/JavaPOSWorkingGroup/javapos-config-loader) [ ![Download](https://api.bintray.com/packages/javaposworkinggroup/maven/javapos-config-loader/images/download.svg) ](https://bintray.com/javaposworkinggroup/maven/javapos-config-loader/_latestVersion)
============================

JavaPOS Configuration Loader (aka JCL) for loading JavaPOS configurations and providing JavaPOS configuration data to applications

## Note for Test execution

If starting tests another way than with Gradle (e.g., with Eclipse's JUnit test runner), the Gradle task *prepareTestConfiguration* has 
to be executed first. This will copy the file [ javapos-config-loader/src/test/resources/jpos/res/jpos_junit.properties ](src/test/resources/jpos/res/jpos_junit.properties)
as initial *jpos.properties* file to the appropriate temporary resource test directory.

If starting the test with Gradle, this is not needed as Gradle's *test* task depends on it and will automatically execute this task before 
starting the tests.


*Note*: This project may be edited directly in the browser using the gitpod application which is provides a full blown IDE.

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io#https://github.com/JavaPOSWorkingGroup/javapos-config-loader)
