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

import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;
import jpos.config.*;

/**
 * Simple implementation of the JposRegPopulator loading and saving from a
 * serialized set of entries
 * <p>
 * <b>NOTE</b>: this class must define a public no-argument ctor so that it may be created 
 * via reflection when its defined in the jpos.properties as 
 * the jpos.config.regPopulatorClass
 * </p>
 * @see jpos.util.JposProperties#JPOS_REG_POPULATOR_CLASS_PROP_NAME
 * @since 1.2 (NY 2K 99 meeting)
 * @author E. Michael Maximilien  (maxim@us.ibm.com)
 */
public class SimpleRegPopulator extends AbstractRegPopulator
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * Default ctor
     * @since 1.2 (NY 2K meeting)
     */
    public SimpleRegPopulator() 
    { super( SimpleRegPopulator.class.getName() ); }

    /**
     * 1-arg ctor that takes the unique ID string
	 * @param s the unique ID string
     * @since 1.3 (Washington DC 2001 meeting)
     */
    public SimpleRegPopulator( String s ) { super( s ); }

    //-------------------------------------------------------------------------
    // Public methods
    //

	/**
	 * @return the fully qualified class name implementing the 
	 * JposRegPopulator interface
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public String getClassName() 
	{ return SimpleRegPopulator.class.getName(); }

    /**
     * Tell the populator to save the current entries 
     * @param entries an enumeration of JposEntry objects
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if any error occurs while saving
     */
	@SuppressWarnings("unchecked")
	public void save( @SuppressWarnings("rawtypes") Enumeration entries ) throws Exception
    {
        saveJposEntries( entries );
    }

    /**
     * Tell the populator to save the current entries in the file specified 
     * @param entries an enumeration of JposEntry objects
     * @param fileName the file name to save entries
     * @since 1.3 (SF 2K meeting)
     * @throws java.lang.Exception if any error occurs while saving
     */
    public void save( @SuppressWarnings("rawtypes") Enumeration entries, String fileName ) throws Exception
    {
		File file = new File( fileName );
		try (FileOutputStream fos = new FileOutputStream( file )) {
			@SuppressWarnings("unchecked")
			Enumeration<JposEntry> typeSafeEntries = entries;
			saveJposEntries( typeSafeEntries, fos );
		}
    }

    /**
     * Tell the populator to load the entries 
     * @since 1.2 (NY 2K meeting)
     */
    public void load()
    {
    	clearAllJposEntries();
        Enumeration<JposEntry> entries = readJposEntries();
      
        while( entries.hasMoreElements() ) 
        {
            try
            {
                JposEntry entry = entries.nextElement();
                String logicalName = entry.getLogicalName();

                if( logicalName != null )
                	addJposEntry( logicalName, entry );

				lastLoadException = null;
            }
            catch( Exception e ) 
            {
				lastLoadException = e;
				tracer.println( "Error loading serialized JposEntry file: " + 
				                "Exception.message= " + e.getMessage() ); 
			}
        }
    }

    /**
     * Loads the entries specified in the fileName
     * @param fileName the entries file name
     * @since 1.3 (SF 2K meeting)
     */
    public void load( String fileName )
    {
        try (FileInputStream fis = new FileInputStream( fileName ))
        {
            clearAllJposEntries();
            Enumeration<JposEntry> entries = readJposEntries( fis );

            while( entries.hasMoreElements() ) 
            {
                JposEntry entry = entries.nextElement();
                String logicalName = (String)entry.
                getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );

                if( logicalName != null )
                	addJposEntry(logicalName, entry);
            }

			lastLoadException = null;
        }
        catch( Exception e ) 
        {
			lastLoadException = e;
			tracer.println( "Error loading serialized JposEntry file: " + 
			                "Exception.message=" + e.getMessage() ); 
		}
    }

    /**
     * @return the URL pointing to the entries file loaded or saved
     * @since 1.2 (NY 2K meeting)
     */
    public URL getEntriesURL() 
    {
        URL url = null;

        if( serInZipFile ){
            url = createURLFromFile( zipSerFile );
        }else if(serFile != null){
            url = createURLFromFile( serFile );
        }else{
            url  = createURLFromFile( new File( getPopulatorFileName()));
        }
        return url;
    }

	/**
	 * @return the name of this populator.  This should be a short descriptive name
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public String getName() { return SIMPLE_REG_POPULATOR_NAME_STRING; }

    //--------------------------------------------------------------------------
    // Protected methods
    //

    /**
     * Tries to save the entries as a ZipEntry in the ZipFile
     * @param entries an Enumeration of JposEntry objects
     * NOTE: if the the serialized entries is in a Sip/JAR file then if must be an
     * entry in the "root" of the Sip/JAR file...
     * Also when saving in a Zip/JAR file could get an error because the Zip/JAR file
     * is being used by a process in Win32 environment
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if any problems occurs while saving
     */
    protected void saveSerInZipFile( Enumeration<JposEntry> entries ) throws Exception
    {
        try( ZipOutputStream zos = new ZipOutputStream( new FileOutputStream( zipSerFile.getName() + ".temp.jar" ) ) )
        {
        	Enumeration<? extends ZipEntry> zipEntries = zipSerFile.entries();
        	
        	while( zipEntries.hasMoreElements() )
        	{
        		ZipEntry zipEntry = zipEntries.nextElement();
        		
        		zos.putNextEntry( zipEntry );
        		
        		if( zipEntry.getName() != serFileName )
        		{
        			InputStream is = zipSerFile.getInputStream( zipEntry );
        			
        			while( is.available() > 0 )
        			{
        				byte[] byteArray = new byte[ is.available() ];
        				
        				is.read( byteArray );
        				
        				zos.write( byteArray );
        			}
        			
        			zos.closeEntry();
        		}
        		else
        		{
        			try( ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( TEMP_SER_FILE_NAME ) ) )
        			{
        				while( entries.hasMoreElements() )
        				{
        					JposEntry entry = entries.nextElement();
        					
        					oos.writeObject( entry );
        				}
        			}
        			
        			
        			try( FileInputStream fis = new FileInputStream( TEMP_SER_FILE_NAME ) )
        			{
        				while( fis.available() > 0 )
        				{
        					byte[] byteArray = new byte[ fis.available() ];
        					
        					fis.read( byteArray );
        					
        					zos.write( byteArray );
        				}
        				
        				zos.closeEntry();
        			}
        		}
        	}
        }
    }

    /**
     * Tries to save the entries in the file where they were loaded
     * @param entries an Enumeration of JposEntry objects
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if any problems occurs while saving
     */
    protected void saveSerFile( Enumeration<JposEntry> entries ) throws Exception
    {
    	try (OutputStream os = new FileOutputStream( serFileName )) {
    		saveJposEntries( entries,  os);
    	}
    }

    /**
     * Save the JposEntry object to the OutputStream as serialized objects
     * @param entries an enumeration of JposEntry objects
     * @param os the OuputStream to save to
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if any error occurs while saving
     */
    protected void saveJposEntries( Enumeration<JposEntry> entries, OutputStream os ) 
    throws Exception
    {
        try (ObjectOutputStream oos = new ObjectOutputStream( os )) {
	        while( entries.hasMoreElements() )
	        {
	            JposEntry entry = entries.nextElement();
	
	            oos.writeObject( entry );
	        }
        }
    }

    /**
     * @return an ObjectInputStream of the first serFileName found.  The algorithm is:
     * 1) Goes through the CLASSPATH and get the first serialized file name in the paths in order
     * 2) If no simple ser file is found then looks in the JAR files in order
     * @since 1.2 (NY 2K meeting)
     */
    protected ObjectInputStream findSerOIS()
    {       
        List<String> classpathJarFiles = new ArrayList<>();

        //Try to find the serialized file in the directory of each path in CLASSPATH
        //As a side effect put each JAR/Zip file in the vector
        ObjectInputStream ois = findSerOISInClasspath( classpathJarFiles );

        //If no serialized file found in the directories of the path in the CLASSPATH then
        //try to open each JAR/Zip file and see if they contain a serialized file
        if( ois == null )
            ois = findSerOISInJar( classpathJarFiles );
        
        return ois;
    }

    /**
     * Finds the first serialized JposEntry file in directory of each classpath
     * @param jarZipFilePaths a list of JAR/Zip file names
     * @return an {@link ObjectInputStream} object opened for the given ZIP files paths.<br>
     * This object must be closed by the caller!  
     * @since 1.2 (NY 2K meeting)
     */
    protected ObjectInputStream findSerOISInClasspath( List<String> jarZipFilePaths )
    {
        ObjectInputStream ois = null;

        String classpath = System.getProperty( "java.class.path" );
        String pathSeparator = System.getProperty( "path.separator" );
        String fileSeparator = System.getProperty( "file.separator" );

        String path = "";

        //Searches for the serialized JposEntry file 
        for( StringTokenizer st = new StringTokenizer( classpath, pathSeparator, false ); 
             st.hasMoreTokens(); )
        {
            try 
            {
                path = st.nextToken().trim();
                if( path.equals("") ) continue;

                if( path.length() > 4 && ( path.endsWith( ".zip" ) || path.endsWith( ".jar" ) ) )
                    jarZipFilePaths.add( path );  
                else 
                {
                    absoluteFileName = path + fileSeparator + serFileName;

                    ois = new ObjectInputStream( new BufferedInputStream( new FileInputStream( absoluteFileName ) ) );

                    serFile = new File( absoluteFileName );
                    serInZipFile = false;
                    break;
                }
            }
            catch( Exception e ) { continue; }
        }

        return ois;
    }

    /**
     * Finds the first serialized JposEntry file in the JAR files
     * @param jarFilePaths a vector of JAR/Zip file names
     * @return an {@link ObjectInputStream} object opened for the given JAR files paths.<br>
     * This object must be closed by the caller!  
     * @since 1.2 (NY 2K meeting)
     */
    protected ObjectInputStream findSerOISInJar( List<String> jarFilePaths )
    {
        ObjectInputStream ois = null;

        for(String jarFileName : jarFilePaths)
        {

            try (ZipFile zipFile = new ZipFile( jarFileName ))
            {
                Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

                while( zipEntries.hasMoreElements() )
                {
                    ZipEntry zipEntry = zipEntries.nextElement();
                    String entryName = zipEntry.getName();
                    if( entryName.endsWith( serFileName ) )
                    {
                        ois = new ObjectInputStream( zipFile.getInputStream( zipEntry ) );
                        zipSerFile = zipFile;
                        serInZipFile = true;
                        break;
                    }
                }
            }
            catch( Exception e ) {}

            if( ois != null ) break;
        }

        return ois;
    }

    /** 
     * Searches the current class path for the serialized file and un-serializes the JposEntry objects
     * @return an Enumeration of JposEntry objects un-serializes from the file specified
     * @param is the InputStream from which to read the serialized entries from
     * @since 1.2 (NY 2K meeting)
     */
    protected Enumeration<JposEntry> readJposEntries( InputStream is )
    {
        List<JposEntry> entries = new ArrayList<>();

        try
        {
			//Added in 1.3 (SF-2K meeting) (not elegant since need to do
			//a instanceof operation but works :-)
            ObjectInputStream in = null;

			if( is instanceof ObjectInputStream )
                in = (ObjectInputStream)is;
			else
				if( is != null )
					in = new ObjectInputStream( is );

            if( in == null )
                tracer.println( "Can't find serialized JposEntry file: " + 
                                serFileName );
            else
                while( true )
                    entries.add( (JposEntry) in.readObject() );

            serFileName = absoluteFileName;
        } 
        catch( EOFException eofe ) {
        	tracer.println( "ERROR while reading serialized JposEntry file: " + 
  	              serFileName + " Exception.message=" + 
  	              eofe.getMessage() ); 
        	
        }
        catch( Exception e ) 
        { 
        	tracer.println( "ERROR while reading serialized JposEntry file: " + 
        	              serFileName + " Exception.message=" + 
        	              e.getMessage() ); 
        }
        return Collections.enumeration(entries);
    }

    /** 
     * @return an Enumeration of JposEntry objects
     * @since 1.2 (NY 2K meeting)
     */
    protected Enumeration<JposEntry> readJposEntries()
    {
        Enumeration<JposEntry> entries = null;

        if( isPopulatorFileDefined() ) {
        	try (InputStream inputStream = getPopulatorFileIS()) { 
        		entries = readJposEntries( inputStream ); 
        	}
        	catch( Exception e )
        	{ 
        		entries = Collections.enumeration(new ArrayList<>()); 
        	}
        }
        else {
        	try (InputStream inputStream = findSerOIS()) {
        		entries = readJposEntries( findSerOIS() );
        	}
        	catch( Exception e )
        	{ 
        		entries = Collections.enumeration(new ArrayList<>()); 
        	}
        }

        return entries;
    }

    /**
     *
     * @param entries an enumeration of JposEntry objects
     * @since 1.2 (NY 2K meeting)
     * @throws java.lang.Exception if any error occurs while saving
     */
    protected void saveJposEntries( Enumeration<JposEntry> entries ) throws Exception
    {
        if( isPopulatorFileDefined() )
        	try (OutputStream outputStream = getPopulatorFileOS()) {
        		saveJposEntries( entries, outputStream );
        	}
        else
        {
            if( serInZipFile )
                saveSerInZipFile( entries );
            else
                saveSerFile( entries );
        }
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private File serFile = null;
    private ZipFile zipSerFile = null;

    private boolean serInZipFile = false;

    private String absoluteFileName = "";
    private String serFileName = DEFAULT_JPOS_SER_FILE_NAME;

	private final Tracer tracer = TracerFactory.getInstance().
	                         createTracer( "SimpleRegPopulator" );	

    //--------------------------------------------------------------------------
    // Class constants
    //

    /**
     * The default serialized JposEntry file name
     * @since 1.2 (NY 2K meeting)
     */
    public static final String DEFAULT_JPOS_SER_FILE_NAME = "jpos.cfg";

	/**
	 * A tempory file name used for temporary storage
	 * @since 1.2 (NY 2K meeting)
	 */
    public static final String TEMP_SER_FILE_NAME = "__jpos_temp.cfg";
	
	/**
	 * The default name for the SimpleRegPopulator
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public static final String SIMPLE_REG_POPULATOR_NAME_STRING = "JCL Serialized Entries Populator";
}
