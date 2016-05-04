package jpos;

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
///////////////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.*;

import jpos.util.*;
import jpos.config.*;
import jpos.config.simple.*;
import jpos.config.simple.xml.*;

import junit.framework.*;

/**
 * Super class of all jpos.* TestCase classes
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public abstract class JposTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public JposTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected methods
	//

	protected void backupPropFile() throws IOException
	{
		File propFile = new File( JCL_PROPERTIES_FILE_NAME );

		if( propFile.exists() )
			propFile.renameTo( new File( JCL_BACKUP_PROPERTIES_FILE_NAME ) );
	}

	protected void deletePropFile() throws IOException
	{
		File propFile = new File( JCL_PROPERTIES_FILE_NAME );
		if( propFile.exists() ) propFile.delete();
	}

	protected void restorePropFile() throws IOException
	{
		File propFile = new File( JCL_BACKUP_PROPERTIES_FILE_NAME );

		if( propFile.exists() )
			propFile.renameTo( new File( JCL_PROPERTIES_FILE_NAME ) );
	}

	protected void createPropFile( Properties props ) throws IOException
	{
		backupPropFile();
		deletePropFile();

		File propFile = new File( JCL_PROPERTIES_FILE_NAME );
		createParentDirectoryForFile(propFile);
		propFile.deleteOnExit();

		props.store( new FileOutputStream( propFile ),
					 "JposTestCase.createPropFile() --> " + JCL_PROPERTIES_FILE_NAME + " file" );
	}

	protected JposEntry createDefaultJposEntry( String logicalName )
	{
		return createJposEntry( logicalName, "factoryClass", "serviceClass", "vendorName",
								"http://www.vendorURL.com", "POSPrinter", "1.5", "productName",
                                "productDescription", "http://www.productURL.com", null );
	}

    protected JposEntry createJposEntry( String logicalName, String factoryClass,
                                         String serviceClass, String vendorName,
                                         String vendorURL, String deviceCategory,
                                         String jposVersion, String productName,
                                         String productDescription, String productURL,
									     JposRegPopulator populator )
    {
        JposEntry jposEntry = new SimpleEntry( populator );

        jposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, logicalName );
        jposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, factoryClass );
        jposEntry.addProperty( JposEntry.SERVICE_CLASS_PROP_NAME, serviceClass );
        jposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, vendorName );
        jposEntry.addProperty( JposEntry.VENDOR_URL_PROP_NAME, vendorURL );
        jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, deviceCategory );
        jposEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, jposVersion );
        jposEntry.addProperty( JposEntry.PRODUCT_NAME_PROP_NAME, productName );
        jposEntry.addProperty( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, productDescription );
        jposEntry.addProperty( JposEntry.PRODUCT_URL_PROP_NAME, productURL );

        return jposEntry;
    }

	protected void emptyTest()
	{
		String emptyTestString = "---->" + EMPTY_TEST_STRING_MSG + " in test class = " + this.getClass().getName() + "<----";

		println( emptyTestString );
	}

	protected void createSerializedEntriesFile( Iterator entries, String fileName ) throws Exception
	{
		SimpleRegPopulator simpleRegPopulator = new SimpleRegPopulator();

		createParentDirectoryForFileName(fileName);
		simpleRegPopulator.save( verifyCreateEntriesVector( entries ).elements(), fileName );

		assertTrue( "Did not create serialized file: " + fileName + " as expected!",
				( new File( fileName ) ).exists() );
	}

	protected void createXmlEntriesFile( Iterator entries, String fileName ) throws Exception
	{
		XmlRegPopulator xmlRegPopulator = new SimpleXmlRegPopulator();

		xmlRegPopulator.save( verifyCreateEntriesVector( entries ).elements(), fileName );

		assertTrue( "Did not create XML file: " + fileName + " as expected!",
				( new File( fileName ) ).exists() );
	}

	protected List logicalNames( List list )
	{
		return logicalNames( list.iterator() );
	}

	protected List logicalNames( Iterator iterator )
	{
		List list = new ArrayList();

		while( iterator.hasNext() )
		{
			String logicalName = ( (JposEntry)iterator.next() ).getLogicalName();
			list.add( logicalName );
		}

		return list;
	}

	protected void addToClasspath( String pathName )
	{
		if( System.getProperty( "java.class.path" ).endsWith( pathName ) == false )
            System.setProperty( "java.class.path", 
			   				    System.getProperty( "java.class.path" ) + 
							    System.getProperty( "path.separator" ) + pathName );
	}

	//-------------------------------------------------------------------------
	// Private methods
	//

	private Vector verifyCreateEntriesVector( Iterator entriesIterator ) throws IllegalArgumentException
	{
		Vector vector = new Vector();

		while( entriesIterator.hasNext() )
		{
			SimpleEntry entry = null;

			try{ entry = (SimpleEntry)entriesIterator.next(); }
			catch( ClassCastException cce )
			{ throw new IllegalArgumentException( "Iterator does not have SimpleEntry objects as expected" ); }

			vector.add( entry );
		}

		return vector;
	}

	//-------------------------------------------------------------------------
	// Public utility methods
	//

	public List createDefaultSerializedJposEntriesFile( String fileName, 
	 													 int maxNumber ) 
	throws Exception
	{
		return createDefaultSerializedJposEntriesFile( fileName, 0, 
														maxNumber );
	}

	public List createDefaultXmlJposEntriesFile( String fileName, 
												  int maxNumber ) 
	throws Exception
	{
		return createDefaultXmlJposEntriesFile( fileName, 0, maxNumber );
	}

	public List createDefaultSerializedJposEntriesFile( String fileName, 
														 int startIndex,
														 int maxNumber ) 
	throws Exception
	{
		List entryList = new ArrayList();

		for( int i = 0; i < maxNumber; ++i )
			entryList.add( createDefaultJposEntry( "logicalName" + 
												  ( i + startIndex ) ) );

		createParentDirectoryForFileName(fileName);
		( new SimpleRegPopulator() ).
		save( verifyCreateEntriesVector( entryList.iterator() ).elements(), 
										 fileName );

		assertTrue( "Did not create serialized file: " + 
					fileName + " as expected!",
				    ( new File( fileName ) ).exists() );

		return entryList;
	}

	public List createDefaultXmlJposEntriesFile( String fileName, 
												  int startIndex,
									      		  int maxNumber ) 
	throws Exception
	{
		List entryList = new ArrayList();

		for( int i = 0; i < maxNumber; ++i )
			entryList.add( createDefaultJposEntry( "logicalName" + 
			                                       ( i + startIndex ) ) );

		createParentDirectoryForFileName(fileName);
		( new SimpleXmlRegPopulator() ).
		save( verifyCreateEntriesVector( entryList.iterator() ).elements(), 
		                                 fileName );

		assertTrue( "Did not create XML file: " + fileName + " as expected!",
				    ( new File( fileName ) ).exists() );

		return entryList;
	}

	public Hashtable createHashtable( JposProperties props )
	{
		Hashtable table = new Hashtable();

		Enumeration names = props.getPropertyNames();

		while( names.hasMoreElements() )
		{
			String name = (String)names.nextElement();
			
			table.put( name, props.getPropertyString( name ) );
		}

		return table;
	}

	public void print( Iterator iterator )
	{
		while( iterator.hasNext() )
			println( iterator.next() );
	}

	public void print( List list ) { print( list.iterator() ); }

	public void print( Object obj )
	{
		if( CONSOLE_OUTPUT_ENABLED )
			System.out.print( obj.toString() );
	}

	public void println( Object obj ) { print( obj.toString() + "\n" ); }

	public void sleep( long time ) 
	{ try{ Thread.sleep( time ); } catch( Exception e ) {} }

	//-------------------------------------------------------------------------
	// Public testXyz methods
	//

	//-------------------------------------------------------------------------
	// Class Constants variables
	//

	public static final String EMPTY_TEST_STRING_MSG = "EMPTY TEST";

	public static final String TEST_RESOURCE_PATH = 
								 System.getProperty( "java.io.tmpdir" ) + 
								 "javapos-config-loader-test" + File.separator + 
								 "resources";

	public static final String JCL_PROPERTIES_FILE_NAME = 
								 TEST_RESOURCE_PATH + File.separator + 
								 "jpos" +  File.separator + 
								 "res" + File.separator + 
								 "jpos.properties";

	public static final String JCL_BACKUP_PROPERTIES_FILE_NAME = 
								 TEST_RESOURCE_PATH + File.separator + 
								 "jpos" + File.separator + 
								 "res" +  File.separator + 
								 "jpos_backup.properties";

	public static final String TEST_DATA_PATH = 
								 System.getProperty( "java.io.tmpdir" ) + 
								 "javapos-config-loader-test" + File.separator + 
								 "data" + File.separator ;

	public static final boolean CONSOLE_OUTPUT_ENABLED = false;
	public static final String JPOS_UTIL_TRACING_VALUE = "OFF"; 
	
    /**
     * Loads a resource and creates an identical temporary file
     * @param resourceName The name of the file that is able to be loaded as a resource
     * @return The absolute Path of the created temporary file
     */
	public static final String loadResourceAsTemporaryFile(String resourceName) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String prefix, suffix;
		try {
			if (resourceName.contains(".")) {
				prefix = resourceName.substring(0, resourceName.lastIndexOf('.'));
				prefix = prefix.replace('/', '_');
				suffix = resourceName.substring(resourceName.lastIndexOf('.'));
			} else {
				prefix = resourceName;
				suffix = "";
			}
			File tempfile = File.createTempFile(prefix, suffix);
			tempfile.deleteOnExit();
			inputStream = ClassLoader.getSystemResourceAsStream(resourceName);
			if (inputStream == null) {
				fail("Resource " + resourceName + " does not exist or is empty");
			}
			outputStream = new FileOutputStream(tempfile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			return tempfile.getAbsolutePath();
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
		}
	}

	private static void createParentDirectoryForFile(File file) {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
	}
	
	public static void createParentDirectoryForFileName(String fileName) {
		File file = new File(fileName);
		createParentDirectoryForFile(file);
	}

	public static void createDirectory(String dirPathName) {
        File dir = new File(dirPathName);
        if (!dir.exists()) dir.mkdirs();
	}
}