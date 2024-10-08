package jpos.config;

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
 * NOTE: This class indicates the same version number as the jpos.loader.Version
 *       and is added as a convinience
 * @since 1.2 (NY 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public final class Version 
{
    /**
     * Main entry point for the Version application
     * @param args a String[] of arguments
     * @since 1.2 (NY 2K meeting)
     */
    public static void main( String[] args )
    {
        System.out.println( "JavaPOS jpos.config/loader (JCL) version " + 
        					jpos.loader.Version.getVersionString() );
    }

    /**
     * @return a String of the version number of the JCL
     * @since 1.2 (NY 2K meeting)
     */
    public static String getVersionString() 
    { return jpos.loader.Version.getVersionString(); }
}
