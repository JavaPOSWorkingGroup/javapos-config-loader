# Change Log for javapos-config-loader

## 5.0.0

- replaced legacy logging implementation by the logging facade SLF4J version 1, see https://www.slf4j.org/ for details on how to integrate

## 4.0.2

- fixed resource loading from JAR file on the class-path (contributed by @art-and-co through [PR #13](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/issues/13))
- ensured populator file is used if defined (contributed by @art-and-co through [PR #13](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/issues/13))

## 4.0.1

- corrected logging output when loading class-path resources (solved GH issue [#11](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/issues/11))

## 4.0.0

- jpos.config.DefaultCompositeRegPopulator.load() is throwing more specific IllegalArgument exception instead of RuntimeException
- added Javax XML parser based XML registry populator implementation (contributed by @mjpcger)
- removed Xerces based XML registry populator implementations, mainly
    - `jpos.config.simple.xml.XercesRegPopulator`
    - `jpos.config.simple.xml.Xerces2RegPopulator`
- removed Xerces dependency at all, thus, it does not appear as transitive dependency in the Maven POM anymore
- added JavaPOS XML node name, attribute name, and fixed file names as constants to `jpos.config.simple.xml.JavaxRegPopulator` making them available for all (proprietary) XML implementations
- restricted XML parser's DTD and XSD resources access for security reasons
- renamed `jpos.profile.XercesProfileFactory` to `jpos.profile.DefaultProfileFactory`
- ensure all resources are well closed by stringent use of try-with-resource clauses
- fixing a NPE in case multi-propo definition is missing
- removed deprecated constructors at 
    - `jpos.config.simple.SimpleEntryRegistry`, and
    - `jpos.loader.simple.SimpleServiceManager`
- removed deprecated class `jpos.util.Tracer`
- ensure compatibility to webstart environments by using standard class-loader instead of the system class-loader (solves issue #1)

## 3.1.0

- Added missing devices to XML schema/DTD files and as DevCat interfaces (contribution by [@dougberkland](https://github.com/dougberkland))
    - jcl.xsd: added devices and versions for UnifiedPOS 1.11 through 1.15
    - jcl_profile.dtd: added new devices through UnifiedPOS 1.15
    - DevCat.java: added missing devices up to UnifiedPOS 1.15

## 3.0.0

- requires Java 8 runtime (therefore the major version change)
- publishes to MavenCentral only, not Bintray (as Bintray has been shut down)
- added missing device catgeories to _jpos/res/jcl.dtd_ to be UnifiedPOS 1.14 compliant (solved [#3](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/issues/3), contribution by [@mjpcger](https://github.com/mjpcger))
- switched to MavenCentral publishing after Bintray's JCenter has been shut down (solved [#5](https://github.com/JavaPOSWorkingGroup/javapos-config-loader/issues/5))
- added this change log

## 2.3.1

- this release has the same content as the 2.3.0 release but has the version corrected in the manifest file.

## 2.3.0

- his release corresponds to JCL version 2.3.0-RC3 as provided by https://sourceforge.net/projects/jposloader/files/jcl/2.3.0-RC3/jcl2.3.0-RC3.zip/download except for one interface class, which has been moved to javapos-contracts (but, which has not been changed since the first release); and all editor sources.

## 2.2.0

- this release corresponds to JCL version 2.2.0 as provided by https://sourceforge.net/projects/jposloader/files/jcl/2.2.0/jcl2.2.0.zip except for one interface class, which has been moved to javapos-contracts