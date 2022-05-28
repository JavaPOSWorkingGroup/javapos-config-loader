package jpos.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which 
// is an OSS Apache-like license.  The complete license is located at:
//    http://www.ibm.com/developerworks/library/os-cpl.html
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Simple Version class indicating that prints out the curent version of the JCL
 * @since 1.2 (NY 2K meeting)
 * @author E. Michael Maximilien  (maxim@us.ibm.com)
 */
public final class Version extends Object
{
    /**
     * Main entry point for the Version application
     * @param args a String[] of arguments
     * @since 1.2 (NY 2K meeting)
     */
    public static void main( String[] args )
    {
        System.out.println( "JavaPOS jpos.config/loader (JCL) version " + 
							JCL_VERSION_STRING );
    }

    /**
     * @return a String of the version number of the JCL
     * @since 1.2 (NY 2K meeting)
     */
    public static String getVersionString() { return JCL_VERSION_STRING; }

    //--------------------------------------------------------------------------
    // Class variables
    //

    private static final String JCL_VERSION_STRING = getImplementationVersion();
    
    final private static String getImplementationVersion()
    {
        try
        {
            // Retrieve the corresponding manifest file of the calling class
            String classContainer = Version.class.getProtectionDomain().getCodeSource().getLocation().toString();
            
            Manifest manifest = readManifest(classContainer);

            // Retrieve all the custom entries included in the manifest file
            Attributes.Name keyName = new Attributes.Name("Implementation-Version");
            Attributes mainAttributes = manifest.getMainAttributes();
            if (mainAttributes == null || !mainAttributes.containsKey(keyName)) {
                return "version_determination_failed";
            }
            
            return mainAttributes.getValue(keyName);
        }
        catch (Exception e)
        {
            return "version_determination_failed";
        }
    }

	private static Manifest readManifest(String classContainer) throws IOException {
        URL manifestUrl = new URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
        InputStream manifestIn;
    	try {
    		manifestIn = manifestUrl.openStream();
    	} 
    	catch (IOException ioe) {
    		// try from file for testing and development environments
    		try  {
    			manifestIn = new URL(classContainer + "/META-INF/MANIFEST.MF").openStream();
        	} 
    		catch (IOException ioeForFileAccess) {
        		throw ioe;
        	}
    	}
        	
        try {
        	return new Manifest(manifestIn);
        } 
        finally {
        	try { manifestIn.close(); }	catch (IOException ioe) {} // ignore exception 
        }
	}

}
