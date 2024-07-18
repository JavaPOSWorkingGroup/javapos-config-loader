package jpos.util;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for various File related actions and methods
 * @author E. Michael Maximilien
 * @version 0.0.1
 * @since 2.1.0
 */
public class FileUtil 
{	
	private static final String JAVA_CLASS_PATH_PROP_NAME = "java.class.path";
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
    //-------------------------------------------------------------------------
    // Ctor(s)
    //
	
	/** Make ctor protected to avoid contruction but allows subclassing */
	protected FileUtil() {}

    //-------------------------------------------------------------------------
    // Private/protected static methods
    //

	protected static synchronized List<String> getCpDirList()
	{
		String classpath = System.getProperty( JAVA_CLASS_PATH_PROP_NAME );								
		
		List<String> cpDirList = new ArrayList<>();
		
		StringTokenizer st = new StringTokenizer( classpath, File.pathSeparator );
		while( st.hasMoreTokens() )
		{
			String cpEntry = st.nextToken();
			
			if( cpEntry.toLowerCase().endsWith( "jar" ) ||
			    cpEntry.toLowerCase().endsWith( "zip" ) )
			    cpDirList.add( cpEntry.
			    			   substring( 0, cpEntry.
			    			   				 lastIndexOf( File.separator ) ) );
			else
				cpDirList.add( cpEntry );
		}

		return cpDirList;
	}
	
	protected static synchronized List<String> getJarList()
	{
		String classpath = System.getProperty( JAVA_CLASS_PATH_PROP_NAME );
								
		List<String> cpJarFilesList = new ArrayList<>();
		
		StringTokenizer st = new StringTokenizer( classpath, File.pathSeparator );
		while( st.hasMoreTokens() )
		{
			String cpEntry = st.nextToken();
			
			if( cpEntry.toLowerCase().endsWith( "jar" ) ||
			    cpEntry.toLowerCase().endsWith( "zip" ) )
			    cpJarFilesList.add( cpEntry );
		}

		return cpJarFilesList;		
	}
	
	protected static synchronized JarEntry getJarEntry( JarFile jarFile, String fileName )
	{
		log.debug( "<getJarEntry jarFile={} fileName={}>", jarFile, fileName );
		
		try {
			
			if( jarFile == null ) return null;
			
			Enumeration<JarEntry> entries = jarFile.entries();
			while( entries.hasMoreElements() )
			{
				JarEntry jarEntry = entries.nextElement();
				
				if( jarEntry.getName().equals( fileName ) ) 
				{
					log.debug( "jarEntry.getName()={}", jarEntry.getName() );
					return jarEntry;
				}
			}
			
			log.warn( "Could not find JarEntry with fileName={}", fileName );		
			return null;
		}
		finally {			
			log.debug( "</getJarEntry>");		
		}
		
	}
    	
    /** 
     * @return the File object if found otherwise returns null
	 * @param fileName the relative fileName to search for
	 * directories specified by CLASSPATH
     */
    protected static synchronized JarFile lookForFileInJars( String fileName )
	{
		try
		{
			log.debug( "<lookForFileInJars fileName={}>", fileName );
			
			String classpath = System.getProperty( JAVA_CLASS_PATH_PROP_NAME );

			log.debug( "classpath={}", classpath );
						
			List<String> cpJarFilesList = getJarList();
						
			for( int i = 0; i < cpJarFilesList.size(); ++i )
			{
				String jarFileName = cpJarFilesList.get( i );
				
				log.debug( "jarFileName={}", jarFileName );
		
				JarFile jarFile = new JarFile( new File( jarFileName ) );

				JarEntry jarEntry = getJarEntry( jarFile, fileName );
				
				if( jarEntry != null ) return jarFile;
			}
						
			return null;
		}
		catch( Exception ioe ) { return null; }
		finally
		{
			log.debug( "</lookForFileInJars>" );
		}
	}	
	
    //-------------------------------------------------------------------------
    // Public static methods
    //

	/**
	 * @return true if the File passed by name below could be located by
	 * searching the classpath and or JAR files in CLASSPATH
	 * @param fileName the relative fileName to search for
	 * @param searchInClassPath if true the file will be searched in all 
	 * directories specified by CLASSPATH
	 * @param searchInJarFile if true the file will be searched in all the JAR
	 * files that are located in CLASSPATH
	 */
	public static synchronized boolean 
	locateFile( String fileName, boolean searchInClassPath, 
	            boolean searchInJarFile )
	{
		File file = findFile( fileName, searchInClassPath );
		
		if( file != null ) return true;
		
		if( searchInJarFile )
		{
			JarFile jarFile = lookForFileInJars( fileName );
			
			if( jarFile != null ) return true;
		}
		
		return false;
	}

    /** 
     * @return the File object if found otherwise returns null
	 * @param fileName the relative fileName to search for
	 * @param searchInClassPath if true the file will be searched in all 
	 * directories specified by CLASSPATH
     */
    public static synchronized File findFile( String fileName, 
    											 boolean searchInClassPath )
	{
		try
		{
			File file = new File( fileName );
			if( file.exists() ) return file;
			
			List<String> cpDirList = getCpDirList();
						
			if( searchInClassPath )
				for( int i = 0; i < cpDirList.size(); ++i )
				{
					String path = cpDirList.get( i );
					File file2 = new File( path + File.separator + fileName );
	
					if( file2.exists() ) return file2;
				}
						
			return null;
		}
		catch( Exception ioe ) { return null; }
	}	                                            

	/**
	 * @return a FileIOStream object for the file passed by name below could 
	 * be located by searching the classpath and or JAR files in CLASSPATH
	 * @param fileName the relative fileName to search for
	 * @param searchInClassPath if true the file will be searched in all 
	 * directories specified by CLASSPATH
	 * @param searchInJarFile if true the file will be searched in all the JAR
	 * files that are located in CLASSPATH
	 * @throws java.io.FileNotFoundException if the file could not be found
	 * @throws java.io.IOException if an error occurred while loading file
	 */
	public static synchronized InputStream 
	loadFile( String fileName, boolean searchInClassPath, boolean searchInJarFile ) 
	throws IOException
	{	    
		log.debug( "<loadFile fileName={} searchInClassPath={} searchInJarFile={}>",
				fileName, searchInClassPath, searchInJarFile);
		
		File locatedFile = findFile( fileName, searchInClassPath );
		
		if( locatedFile != null ) return new FileInputStream( locatedFile );

		if( !searchInJarFile ) 
			throw new FileNotFoundException( "Could not find file classpath resources: " + fileName );
		
		JarFile locatedJarFile = lookForFileInJars( fileName );
		
		if( locatedJarFile == null ) 
			throw new FileNotFoundException( "Could not find file in JAR files: " + fileName );
		
		JarEntry locatedJarEntry = getJarEntry( locatedJarFile, fileName );

		if( locatedJarEntry != null )
			return locatedJarFile.getInputStream( locatedJarEntry );
		
		throw new FileNotFoundException( "Could not find file: " + fileName );
	}
	
}