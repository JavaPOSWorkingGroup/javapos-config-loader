package jpos.profile;

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
import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import jpos.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the ProfileFactory interface using an 
 * XML parser to create profiles from the XML file passed
 * @since 4.0
 */
public class DefaultProfileFactory implements ProfileFactory
{
	private static final Logger log = LoggerFactory.getLogger(DefaultProfileFactory.class);
	
	//-------------------------------------------------------------------------
	// Private methods
	//

	/**
	 * @return a Profile object created from the Document object
	 * @param document the XML document object
	 * @exception jpos.profile.ProfileException if document is not in correct 
	 * format
	 */
	private Profile extractProfile( Document document ) throws ProfileException
	{
		Element profileElement = document.getDocumentElement();

		String name = profileElement.getAttribute( "name" );
		DefaultProfile profile = new DefaultProfile( name );

		NodeList nodeList = profileElement.getElementsByTagName( "ProfileInfo" );

		if( nodeList.getLength() != 1 )
			throw new ProfileException( "Profile does not contain 1 ProfileInfo element" );

		Element profileInfoElement = (Element)nodeList.item( 0 );

		profile.setVersion( profileInfoElement.getAttribute( "version" ) );
		profile.setVendorName( profileInfoElement.getAttribute( "vendorName" ) );

		try
		{
			String vendorUrlString = profileInfoElement.getAttribute( "vendorUrl" );
			profile.setVendorUrl( new URL( vendorUrlString ) );
		}
		catch( MalformedURLException e )
		{ throw new ProfileException( "ProfileInfo contains an invalid vendorUrl string" ); }

		profile.setDescription( profileInfoElement.getAttribute( "description" ) );

		return profile;
	}

	//-------------------------------------------------------------------------
	// Package methods
	//
	
    /**
     * Parses the XML file into a valid XML document for the profile DTD
     * @param xmlFileName the XML file name
	 * @exception jpos.profile.ProfileException if the XML file could not be parsed
     */
	Document parse( String xmlFileName ) throws ProfileException
    {
		XmlHelper xmlHelper = new XmlHelper();

        try
        {
			xmlHelper.setDtdFileName( PROFILE_DTD_FILE_NAME );
			xmlHelper.setDtdFilePath( PROFILE_DTD_FILE_PATH );
            xmlHelper.checkAndCreateTempDtd();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setNamespaceAware( true );
			docFactory.setValidating( true );

			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			DefaultErrorHandler errorHandler = this.new DefaultErrorHandler();
			docBuilder.setErrorHandler( errorHandler );
            
			Document document = docBuilder.parse( new File( xmlFileName ) );

			if( !errorHandler.getErrorList().isEmpty() ||
				!errorHandler.getFatalErrorList().isEmpty() )
				{
					String msg = "Error while parsing XML file, set properties"+
					             " jpos.tracing = ON in jpos.properties" + 
					             " file for details";
					throw new ProfileException( msg );					
				}

			return document;
        }
        catch( IOException ioe )
        {
			String msg = "Error loading XML profile file";
			log.error( "{}: Exception.message = {}", msg, ioe.getMessage() ); 
			throw new ProfileException( msg, ioe ); 
		}
        catch( SAXException se )
        { 
			String msg = "Error parsing XML profile file";
			log.error( "{}: Exception.message = {}", msg, se.getMessage() ); 
			throw new ProfileException( msg, se ); 
		}
		catch( ParserConfigurationException pce )
		{
			String msg = "Error creating XML parser";
			log.error( "{}: Exception.message = {}", msg, pce.getMessage() ); 
			throw new ProfileException( msg, pce ); 
		}
		finally
        { xmlHelper.removeTempDtd(); }
    }

    /**
     * Parses the XML file into a valid XML document for the profile Schemas
     * @param xmlFileName the XML file name
	 * @exception jpos.profile.ProfileException if the XML file could not be parsed
     */
	Document parseSchema( String xmlFileName ) throws ProfileException
    {
        try
        {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setNamespaceAware( true );
			docFactory.setValidating( true );

			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			DefaultErrorHandler errorHandler = this.new DefaultErrorHandler();
			docBuilder.setErrorHandler( errorHandler );
            
            return docBuilder.parse( new File( xmlFileName ) ); 
        }
        catch( IOException ioe )
        {
			String msg = "Error loading XML profile file";
			log.error( "{}: Excpetion.message = {}", msg, ioe.getMessage() ); 
			throw new ProfileException( msg, ioe ); 
		}
        catch( SAXException se )
        { 
			String msg = "Error parsing XML profile file";
			log.error( "{}: Exception.message = {}", msg, se.getMessage() ); 

			throw new ProfileException( msg, se ); 
		}
		catch( ParserConfigurationException pce )
		{
			String msg = "Error creating XML parser";
			log.error( "{}: Exception.message = {}", msg, pce.getMessage() ); 
			throw new ProfileException( msg, pce ); 
		}
    }

    /**
     * Loads the Profile specified in the xmlFileName as a Profile object
     * @param xmlFileName the XML file name
	 * @exception jpos.profile.ProfileException if the XML file could not be 
	 * parsed and the profile created
     */
	Profile load( String xmlFileName ) throws ProfileException
    {			   
		Document document = parse( xmlFileName );

		return extractProfile( document );
    }

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * @return a Profile object created parsing the XML file provided
	 * @param xmlProfileFileName the XML profile file
	 * @exception jpos.profile.ProfileException if there is an error loading the profile
	 */
	public Profile createProfile( String xmlProfileFileName ) throws ProfileException
	{
		return load( xmlProfileFileName );
	}

	//-------------------------------------------------------------------------
	// Inner classes
	//

	/**
	 * ErrorHandler inner class used to capture errors while parsing XML document
	 * @since 1.3 (Washington DC 2001 meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	class DefaultErrorHandler implements org.xml.sax.ErrorHandler
	{
		//---------------------------------------------------------------------
		// Package methods
		//

		List<SAXParseException> getErrorList() { return errorList; }

		List<SAXParseException> getWarningList() { return warningList; }

		List<SAXParseException> getFatalErrorList() { return fatalErrorList; }

		//---------------------------------------------------------------------
		// Public methods
		//

		public void warning( SAXParseException e ) throws SAXException 
		{
			log.warn( "Line {}: WARNING SAXParseException.message = {}", e.getLineNumber(), e.getMessage() );
			warningList.add( e );
		}
		
		public void error( SAXParseException e ) throws SAXException 
		{
			log.error( "Line {}: ERROR SAXParseException.message = {}", e.getLineNumber(), e.getMessage() );
			errorList.add( e );
		}
		
		public void fatalError( SAXParseException e ) throws SAXException 
		{
			log.error( "Line {}: FATALERROR SAXParseException.message = {}", e.getLineNumber(), e.getMessage() );
			fatalErrorList.add( e );
		}

		//---------------------------------------------------------------------
		// Private variables
		//

		private final List<SAXParseException> warningList = new ArrayList<>();
		private final List<SAXParseException> errorList = new ArrayList<>();
		private final List<SAXParseException> fatalErrorList = new ArrayList<>();
	}

	//-------------------------------------------------------------------------
	// Class constants
	//

    public static final String PROFILE_DTD_FILE_NAME = "jcl_profile.dtd";
    public static final String PROFILE_DTD_FILE_PATH = "jpos" + File.separator + "res" + File.separator;
}
