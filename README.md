[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/JavaPOSWorkingGroup/javapos-config-loader)
[![CI](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/actions/workflows/build.yml/badge.svg)](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/actions/workflows/build.yml)
[![Release Build](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/actions/workflows/release.yml/badge.svg)](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/actions/workflows/release.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.javapos/javapos-config-loader)](https://central.sonatype.com/artifact/org.javapos/javapos-config-loader)



JavaPOS Configuration Loader 
============================

JavaPOS Configuration Loader (aka JCL) for loading JavaPOS configurations and providing JavaPOS configuration data to applications

## Note for Test execution

If starting tests another way than with Gradle (e.g., with Eclipse's JUnit test runner), the Gradle task *prepareTestConfiguration* has 
to be executed first. This will copy the file [ javapos-config-loader/src/test/resources/jpos/res/jpos_junit.properties ](src/test/resources/jpos/res/jpos_junit.properties)
as initial *jpos.properties* file to the appropriate temporary resource test directory.

If starting the test with Gradle, this is not needed as Gradle's *test* task depends on it and will automatically execute this task before 
starting the tests.
