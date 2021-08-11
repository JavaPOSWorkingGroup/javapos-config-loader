[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/JavaPOSWorkingGroup/javapos-config-loader)
[![Build Status](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/workflows/Build/badge.svg)](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.javapos/javapos-config-loader/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.javapos/javapos-config-loader/)

JavaPOS Configuration Loader 
============================

JavaPOS Configuration Loader (aka JCL) for loading JavaPOS configurations and providing JavaPOS configuration data to applications

## Note for Test execution

If starting tests another way than with Gradle (e.g., with Eclipse's JUnit test runner), the Gradle task *prepareTestConfiguration* has 
to be executed first. This will copy the file [ javapos-config-loader/src/test/resources/jpos/res/jpos_junit.properties ](src/test/resources/jpos/res/jpos_junit.properties)
as initial *jpos.properties* file to the appropriate temporary resource test directory.

If starting the test with Gradle, this is not needed as Gradle's *test* task depends on it and will automatically execute this task before 
starting the tests.
