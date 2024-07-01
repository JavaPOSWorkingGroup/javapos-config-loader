package jpos.config.simple;

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

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.URL;

import jpos.config.*;
import jpos.loader.JposServiceLoader;
import jpos.util.JposProperties;
import jpos.util.JposPropertiesConst;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;

/**
 * Common abstract superclass to help in the implementation of the 
 * JposRegPopulator
 * @see jpos.util.JposProperties#JPOS_REG_POPULATOR_CLASS_PROP_NAME
 * @since 1.2 (NY 2K 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public abstract class AbstractRegPopulator extends Object 
											 implements JposRegPopulator 
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * Creates a AbstractRegPopulator and sets the uniqueId with the string 
	 * passed
	 * @param id the String ID
	 * @since 1.3 (Washington DC 20001)
	 */
	public AbstractRegPopulator( String id ) { setUniqueId( id ); }

    //-------------------------------------------------------------------------
    // Public methods
    //

	/**
	 * @return a unique String ID for this JposRegPopulator instance
	 * Can be implemented in terms of the getClassName() method
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public String getUniqueId() 
	{ return ( uniqueId.equals( "" ) ? getClassName() : uniqueId ); }

    /**
     * @return an Enumeration of JposEntry objects
     * @since 1.2 (NY 2K meeting)
     */
    @SuppressWarnings("rawtypes")
	public Enumeration getEntries() 
	{
		List<JposEntry> entryList = new ArrayList<>();
		Enumeration<JposEntry> entries = jposEntries.elements(); 

		while( entries.hasMoreElements() )
			entryList.add( entries.nextElement() );

		return Collections.enumeration(entryList);
	}

	/**
	 * @return true if this populator is a composite populator or false otherwise
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public boolean isComposite() { return false; }

	/**
	 * @return a String representation of this JposRegPopulator
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public String toString() { return getName(); }

	/**
	 * @return the last exception (if any) during the last load or null if no 
	 * exception occurred
	 * @since 2.0.0
	 */
	public Exception getLastLoadException() { return lastLoadException; }

    //--------------------------------------------------------------------------
    // Protected methods
    //

    /**
     * @return a URL pointing to the entries file
     * @param file the File that this URL will point to
     * @since 1.2 (NY 2K meeting)
     */
    protected URL createURLFromFile( File file )
    {
        URL url = null;

        try
        { url = new URL( "file", "", file.getAbsolutePath() ); }
        catch( Exception e ) 
        {
        	tracer.println( "Error creating URL: Exception.message=" +
        					e.getMessage() );        	
        }

        return url;
    }

    /**
     * @return a URL pointing to the entries file
     * @param zipFile the ZipFile that this URL will point to
     * @since 1.2 (NY 2K meeting)
     */
    protected URL createURLFromFile( ZipFile zipFile )
    {
        URL url = null;
        
        try
        { 
			url = new URL( "jar", "", ( new File( zipFile.getName() ) ).
        							  getAbsolutePath() ); 
        }
        catch( Exception e ) 
        {
        	tracer.println( "Error creating URL: Exception.message=" +
        					e.getMessage() ); 
        }

        return url;
    }

    /**
     * The {@link Hashtable} of {@link JposEntry}s of this registry populator instance.<br>
     * @return the jposEntries Hashtable to allow access for subclasses
     * @since 1.2 (NY 2K meeting)
     * @deprecated use {@link #addJposEntry(String, JposEntry)} for adding and {@link #clearAllJposEntries()} for clearing, 
     * both are type safe
     */
    @Deprecated
    @SuppressWarnings("rawtypes")
	protected Hashtable getJposEntries() { return jposEntries; }

    /**
     * Adds a {@link JposEntry} to the internal data structure of {@link JposEntry}s represented by
     * this registry populator instance.
     * @param logicalName the logical name the {@link JposEntry} is associated with
     * @param entry the {@link JposEntry} object to be added
     * @since 4.0
     */
	protected void addJposEntry(String logicalName, JposEntry entry) {
		this.jposEntries.put(logicalName, entry); 
	}
	
	/**
	 * Clears all {@link JposEntry}s in the internal data structure of this registry populator instance.
	 */
	protected void clearAllJposEntries() {
		this.jposEntries.clear();
	}

    /**
     * @return true if a populator file (or URL) is defined
     * @since 1.2 (NY 2K meeting)
     */
    protected boolean isPopulatorFileDefined()
    {
        boolean defined = false;
        JposProperties jposProperties = 
        			   JposServiceLoader.getManager().getProperties();

        if( jposProperties.isPropertyDefined( JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME ) ||
        	jposProperties.isPropertyDefined( JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME ) ) 
        {
        	defined = true;
        }

        return defined;
    }

    /**
     * @return an InputStream object to the populator file
     * (multiple calls will return a new stream each time)
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if an error ocurs while getting the 
     * InputStream object
     */
    protected InputStream getPopulatorFileIS() throws Exception
    {
    	InputStream populatorIS;
    	
        JposProperties jposProperties = JposServiceLoader.
        								getManager().getProperties();

        if( jposProperties.isPropertyDefined( JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME ) )
        {
            populatorFileName = 
            		jposProperties.getPropertyString( 
            				JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME );

			tracer.println( "getPopulatorFileIS(): populatorFileName=" + 
							populatorFileName );

            populatorIS = new FileInputStream( populatorFileName );
        }
        else
        if( jposProperties.isPropertyDefined( JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME ) )
        {
            populatorFileURL = 
            		jposProperties.getPropertyString( 
            				JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME );

            URL url = new URL( populatorFileURL );

            populatorIS = url.openStream();
            
			tracer.println( "getPopulatorFileIS(): populatorFileURL=" + 
							populatorFileURL );            
        }
        else
        {
        	String msg = "jpos.config.populatorFile OR " +
        				 " jpos.config.populatorFileURL properties not defined";
			
			tracer.println( msg );        				 
        				 
            throw new Exception( msg );
        }

        return populatorIS;
    }

    /**
     * @return an OutputStream object to the populator file 
     * (multiple calls will return a new stream each time)
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if an error ocurs while getting the 
     * InputStream object
     */
    protected OutputStream getPopulatorFileOS() throws Exception
    {
    	OutputStream populatorOS;
    	
        JposProperties jposProperties = JposServiceLoader.
        								getManager().getProperties();

        if( jposProperties.
        	isPropertyDefined( JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME ) )
        {
            populatorFileName = 
            		jposProperties.getPropertyString( 
            				JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME );
            
            populatorOS = new FileOutputStream( populatorFileName );
        }
        else
        if( jposProperties.isPropertyDefined( JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME ) )
        {
            populatorFileURL = 
            		jposProperties.getPropertyString( 
            				JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME );

            URL url = new URL( populatorFileURL );

            populatorOS = url.openConnection().getOutputStream();
        }
        else
        {
        	String msg = "jpos.config.populatorFile OR " + 
        				 "jpos.config.populatorFileURL properties not defined";
        
        	tracer.println( msg );
        	
            throw new Exception( msg );
        }
        
        return populatorOS;
    }

    /**
     * @return the populatorFile names property value 
     * (returns "" if not defined)
     * @since 1.2 (NY 2K meeting)
     */
    protected String getPopulatorFileName() { return populatorFileName; }

    /**
     * @return the populatorURLFile names property value 
     * (returns "" if not defined)
     * @since 1.2 (NY 2K meeting)
     */
    protected String getPopulatorFileURL() { return populatorFileURL; }

	/**
	 * Sets the unique ID for this populator
	 * @param s the String object.  Needs to be unique
	 * @since 1.3 (Washington DC 2001)
	 */
	protected void setUniqueId( String s ) { uniqueId = s; }

    /**
     * Finds the first file matching the fileName in the CLASSPATH 
     * directory or each JAR or Zip file in the CLASSPATH.
     * 
     * @param fileName the fileName to find
     * @return an {@link InputStream} object opened for the given file name. <br>
     * This object must be closed by the caller!  
     * @since 2.0 (Long Beach 2001)
	 */
    protected InputStream findFileInClasspath( String fileName )
    {
        String classpath = System.getProperty( "java.class.path" );
        String pathSeparator = System.getProperty( "path.separator" );
        String fileSeparator = System.getProperty( "file.separator" );

        InputStream is = null;

		if( fileName.startsWith( "." ) || fileName.startsWith( fileSeparator ) )
		{
			try
			{ is = new BufferedInputStream( new FileInputStream( fileName ) ); }
			catch( IOException ioe ) 
			{ 
				is = null; 
				
				tracer.println( "findFileInClasspath: IOException.msg=" +
								ioe.getMessage() );
			}

			return is;
		}

        String path = "";

		List<String> jarZipFilesVector = new ArrayList<>();

        for( StringTokenizer st = new StringTokenizer( classpath, 
        												pathSeparator, false ); 
             st.hasMoreTokens(); )
        {
            try 
            {
                path = st.nextToken().trim();
                if( path.equals("") ) continue;

                if( ( path.length() > 4 ) && 
                    ( path.endsWith( ".zip" ) || path.endsWith( ".jar" ) ) )
                    jarZipFilesVector.add( path );  
                else 
                {
					String absoluteFileName = path + 
					( ( fileName.startsWith( fileSeparator ) || 
					path.endsWith( fileSeparator ) )  ? "" : fileSeparator )  + 
					fileName;

                    is = new BufferedInputStream( new FileInputStream( 
                    							  absoluteFileName ) );
                    break;
                }
            }
            catch( Exception e ) { continue; }
        }

		if( is == null ) 
			return findFileInJarZipFiles( fileName, jarZipFilesVector );

        return is;
    }

    /**
     * Finds the occurrence of the fileName in the JAR or Zip files.
	 * @param fileName the file to find
     * @param jarZipFilesVector a vector of JAR/Zip file names
     * @return an {@link InputStream} object opened for the given file name. <br>
     * This object must be closed by the caller!  
     * @since 2.0 (Long Beach 2001)
     * @deprecated use {@link #findFileInJarZipFiles(String, List)} instead
     */
    @Deprecated
    protected InputStream findFileInJarZipFiles( String fileName, Vector<String> jarZipFilesVector ) {
    	return findFileInJarZipFiles(fileName, jarZipFilesVector);
    }

    /**
     * Finds the occurrence of the fileName in the JAR or Zip files.
	 * @param fileName the file to find
     * @param jarZipFilesList a list of JAR/Zip file names
     * @return an {@link InputStream} object opened for the given file name. <br>
     * This object must be closed by the caller!  
     * @since 4.0 (Long Beach 2001)
     */
    private InputStream findFileInJarZipFiles( String fileName, List<String> jarZipFilesList )
    {
        InputStream is = null;

        for( int i = 0; i < jarZipFilesList.size(); ++i )
        {
            String jarZipFileName = jarZipFilesList.get( i );

            try (ZipFile zipFile = new ZipFile( jarZipFileName ))
            {
                Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

                while( zipEntries.hasMoreElements() )
                {
                    ZipEntry zipEntry = zipEntries.nextElement();
                    String entryName = zipEntry.getName();
                    
					if( entryName.endsWith( fileName ) )
                    {
                        is = new BufferedInputStream( zipFile.
                        	 getInputStream( zipEntry ) );
                        break;
                    }
                }
            }
            catch( Exception e ) 
            {
            	tracer.println( "findInJarZipFiles: Exception.message=" +
            					e.getMessage() );
            }

            if( is != null ) break;
        }

        return is;
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private final Hashtable<String, JposEntry> jposEntries = new Hashtable<>();

    private String populatorFileName = "";
    private String populatorFileURL = "";

	private String uniqueId = "";

	protected Exception lastLoadException = null;
	
	private Tracer tracer = TracerFactory.getInstance().
							 createTracer( "AbstractRegPopulator" );
}
