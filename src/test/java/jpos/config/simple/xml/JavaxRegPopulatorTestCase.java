package jpos.config.simple.xml;

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

import jpos.config.*;
import jpos.config.simple.*;
import jpos.test.*;

/**
 * A JUnit TestCase for the Loading/saving XML entries
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class JavaxRegPopulatorTestCase extends AbstractRegPopulatorTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public JavaxRegPopulatorTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
        javaxRegPopulator = new JavaxRegPopulator();

        try
        {
            File file = new File( JCL_JUNIT_XML_FILE_NAME );
            file.delete();
        }                       
        catch( SecurityException se )
        {
            println( "Could not delete XML test file: " + JCL_JUNIT_XML_FILE_NAME );
            println( "   Exception message = " + se.getMessage() );
        }

        createDirectory(TEST_DATA_PATH);
		addToClasspath( TEST_DATA_PATH );
    }

	protected void tearDown() 
    {
        javaxRegPopulator = null;
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

    private JposEntry createJposEntry( String logicalName, String factoryClass,
                                       String serviceClass, String vendorName,
                                       String vendorURL, String deviceCategory,
                                       String jposVersion, String productName,
                                       String productDescription, String productURL )
    {
        JposEntry jposEntry = new SimpleEntry();

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

	//-------------------------------------------------------------------------
	// Public test methods
	//

	/** 
     * Test the loading/saving of XML entries using the XercesRegPopulator 
     */
	public void testJavaxPopulatorReloadToTheSameInstance()
	{
        //Save and load an empty set of registry entries
        List<JposEntry> v1 = new ArrayList<>();

        try
        {
            javaxRegPopulator.save( Collections.enumeration(v1), JCL_JUNIT_XML_FILE_NAME );
            javaxRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );
            
            assertNull(javaxRegPopulator.getLastLoadException());

            @SuppressWarnings("unchecked")
			Enumeration<JposEntry> entries = javaxRegPopulator.getEntries();

            assertTrue( "Expected an empty set of entries...", JUnitUtility.isIdentical( entries, Collections.enumeration(v1) ) );
            assertTrue( "Expected an empty set of entries...", JUnitUtility.isEquals( entries, Collections.enumeration(v1) ) );

	        //Add some entries and save and load
	        JposEntry entry1 = createJposEntry( "entry1", "com.xyz.jpos.XyzJposServiceInstanceFactory",
	                                            "com.xyz.jpos.LineDisplayService", "Xyz, Corp.", 
	                                            "http://www.javapos.com", "LineDisplay", "1.4a",
	                                            "Virtual LineDisplay JavaPOS Service", 
	                                            "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
	                                            "http://www.javapos.com" );
	
	        JposEntry entry2 = createJposEntry( "entry2", "com.xyz.jpos.XyzJposServiceInstanceFactory",
	                                            "com.xyz.jpos.LineDisplayService", "Xyz, Corp.", 
	                                            "http://www.javapos.com", "LineDisplay", "1.4a",
	                                            "Virtual LineDisplay JavaPOS Service", 
	                                            "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
	                                            "http://www.javapos.com" );

            v1.clear();
            v1.add( entry1 );
            v1.add( entry2 );

            javaxRegPopulator.save( Collections.enumeration(v1), JCL_JUNIT_XML_FILE_NAME );
            javaxRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            assertNull(javaxRegPopulator.getLastLoadException());

            @SuppressWarnings("unchecked")
			Enumeration<JposEntry> entries2 = javaxRegPopulator.getEntries();

            assertTrue( "Expected 2 entries...", JUnitUtility.isEquals( entries2, Collections.enumeration(v1) ) );
        
	        //Remove entries save and load reg
	        v1.remove( entry1 );

            javaxRegPopulator.save( Collections.enumeration(v1), JCL_JUNIT_XML_FILE_NAME );
            javaxRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            assertNull(javaxRegPopulator.getLastLoadException());

            @SuppressWarnings("unchecked")
			Enumeration<JposEntry> entries3 = javaxRegPopulator.getEntries();

            assertTrue( "Expected 1 entries...", JUnitUtility.isEquals( entries3, Collections.enumeration(v1) ) );
        }
        catch( Exception e )
        { 
        	assertTrue( "Got unexpected Exception from XercesRegPopulator.save method with message = " + e.getMessage(), true ); 
        }
	}

	/** 
     * Test the loading/saving of XML entries using the XercesRegPopulator 
     */
	public void testJavaxPopulator()
	{
        List<JposEntry> v1 = new ArrayList<>();

        for( int i = 0; i < 100; i++ )
        {

            JposEntry entry = createJposEntry( "entry" + i, "com.xyz.jpos.XyzJposServiceInstanceFactory",
                                               "com.xyz.jpos.LineDisplayService", "Xyz, Corp.", 
                                               "http://www.javapos.com", "LineDisplay", "1.4a",
                                               "Virtual LineDisplay JavaPOS Service", 
                                               "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
                                               "http://www.javapos.com" );
            v1.add( entry );
        }

        try
        {
            javaxRegPopulator.save( Collections.enumeration(v1), JCL_JUNIT_XML_FILE_NAME );
            javaxRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            assertNull(javaxRegPopulator.getLastLoadException());

            @SuppressWarnings("unchecked")
			Enumeration<JposEntry> entries = javaxRegPopulator.getEntries();

            assertTrue( "Expected 100 entries...", JUnitUtility.isEquals( entries, Collections.enumeration(v1) ) );
        }
        catch( Exception e )
        { 
        	fail( "Got unexpected Exception from XercesRegPopulator.save method with message = " + e.getMessage() ); 
        }
	}

	public void testGetName()
	{
		javaxRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

		assertTrue( javaxRegPopulator.getName().startsWith("JAVAX XML Entries Populator") );
	}

	public void testLoadDefect6562()
	{
        try
        {
			assertTrue( "Expected file: " + DEFECT_6562_XML_FILE + " to exist",
						( new File( DEFECT_6562_XML_FILE ) ).exists() );

            javaxRegPopulator.load( DEFECT_6562_XML_FILE );

            assertNull(javaxRegPopulator.getLastLoadException());

            @SuppressWarnings("unchecked")
			Enumeration<JposEntry> entries = javaxRegPopulator.getEntries();

			JposEntry defect6562Entry = (JposEntry)entries.nextElement();

			assertNotNull( "defect6562Entry == null", defect6562Entry );
			assertEquals( "defect6562Entry.logicalName != defect6562", 
						"defect6562", defect6562Entry.getLogicalName() );

        }
        catch( Exception e ) { 
        	fail( "Unexpected exception.message = " + e.getMessage() ); 
        }
	}

	public void testLoadwithPropType()
	{
        try
        {
            assertTrue( "Expected file: " + JCL_JUNIT_TEST_PROP_TYPE_XML_FILE + " to exist",
						( new File( JCL_JUNIT_TEST_PROP_TYPE_XML_FILE ) ).exists() );

            javaxRegPopulator.load( JCL_JUNIT_TEST_PROP_TYPE_XML_FILE );
            @SuppressWarnings("unchecked")
			Enumeration<JposEntry> entries = javaxRegPopulator.getEntries();

			JposEntry testPropTypeEntry = (JposEntry)entries.nextElement();

			assertNotNull( "testPropTypeEntry == null", testPropTypeEntry );
			assertEquals( "testPropTypeEntry.logicalName != testPropType", 
					"testPropType", testPropTypeEntry.getLogicalName() );

			assertEquals( "testPropTypeEntry.getProp( \"stringProp\" ).getType() != String.class", 
						String.class, testPropTypeEntry.getProp( "stringProp" ).getType() );

			assertEquals( "testPropTypeEntry.getProp( \"booleanProp\" ).getType() != Boolean.class", 
						Boolean.class, testPropTypeEntry.getProp( "booleanProp" ).getType() );
			
			assertEquals( "testPropTypeEntry.getProp( \"byteProp\" ).getType() != Byte.class", 
						Byte.class, testPropTypeEntry.getProp( "byteProp" ).getType() );
			
			assertEquals( "testPropTypeEntry.getProp( \"characterProp\" ).getType() != Character.class", 
						Character.class, testPropTypeEntry.getProp( "characterProp" ).getType() );
			
			assertEquals( "testPropTypeEntry.getProp( \"doubleProp\" ).getType() != Double.class", 
						Double.class, testPropTypeEntry.getProp( "doubleProp" ).getType() );
			
			assertEquals( "testPropTypeEntry.getProp( \"floatProp\" ).getType() != Float.class", 
						Float.class, testPropTypeEntry.getProp( "floatProp" ).getType() );
			
			assertEquals( "testPropTypeEntry.getProp( \"integerProp\" ).getType() != Integer.class", 
						Integer.class, testPropTypeEntry.getProp( "integerProp" ).getType() );
			
			assertEquals( "testPropTypeEntry.getProp( \"longProp\" ).getType() != Long.class", 
						Long.class, testPropTypeEntry.getProp( "longProp" ).getType() );

			assertEquals( "testPropTypeEntry.getProp( \"shortProp\" ).getType() != Short.class", 
						Short.class, testPropTypeEntry.getProp( "shortProp" ).getType() );
        }
        catch( Exception e ) { fail( "Unexpected exception.message = " + e.getMessage() ); }
	}

	public void testSaveWithPropType()
	{
		emptyTest();
	}

	public void testGetLastLoadException()
	{
		emptyTest();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

    private JavaxRegPopulator javaxRegPopulator = null;

	//-------------------------------------------------------------------------
	// Instance variables
	//

    public static final String JUNIT_CORP_STRING = "JUnit Corp.";
    
	public static final String JCL_JUNIT_XML_FILE_NAME = loadResourceAsTemporaryFile("jcl-junit.xml");
	public static final String DEFECT_6562_XML_FILE = loadResourceAsTemporaryFile("defect6562.xml");
	public static final String JCL_JUNIT_TEST_PROP_TYPE_XML_FILE = loadResourceAsTemporaryFile("jcl_junit_test_prop_type.xml");
}