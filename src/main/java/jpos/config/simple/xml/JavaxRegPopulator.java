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
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which
// is an OSS Apache-like license.  The complete license is located at:
//    http://www.ibm.com/developerworks/library/os-cpl.html
//
///////////////////////////////////////////////////////////////////////////////

import jpos.config.JposEntry;
import jpos.config.simple.AbstractRegPopulator;
import jpos.config.simple.SimpleEntry;
import jpos.loader.Version;
import jpos.util.JposEntryUtility;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

/**
 * Simple implementation of the JposRegPopulator that loads and saves
 * the entries in XML using Javax api.
 * NOTE: this class must define a public no-argument constructor so that it may be
 * created via reflection when it is defined in the jpos.properties as
 * the jpos.config.regPopulatorClass
 * @see jpos.util.JposProperties#JPOS_REG_POPULATOR_CLASS_PROP_NAME
 * @since 1.16
 * @author M. Conrad (martin.conrad@freenet.de)
 */
public class JavaxRegPopulator
        extends AbstractRegPopulator
        implements XmlRegPopulator
{
    /**
     * Default ctor
     */
    public JavaxRegPopulator()
    { super( JavaxRegPopulator.class.getName() ); }

    /**
     * 1-arg constructor that takes the unique ID
     *
     * @param s the unique ID string
     * @since 1.3 (Washington DC 2001)
     */
    public JavaxRegPopulator( String s )
    { super(s); }

    //-------------------------------------------------------------------------
    // Public methods
    //

    @Override
    public void save( Enumeration entries )
            throws Exception
    {
        if( isPopulatorFileDefined() )
            save(entries,  getPopulatorFileOS());
        else
            try( FileOutputStream os = new FileOutputStream( "jpos.xml" ) )
            { save( entries, os ); }
            catch (Exception e)
            { throw e; }
    }

    @Override
    public void save( Enumeration entries, String fileName )
            throws Exception
    {
        try( FileOutputStream os = new FileOutputStream( fileName ) )
        { save(entries, os); }
        catch ( Exception e )
        { throw e; }
    }

    @Override
    public String getClassName() { return JavaxRegPopulator.class.getName(); }

    @Override
    public void load()
    {
        try( InputStream is = isPopulatorFileDefined() ? new FileInputStream( DEFAULT_XML_FILE_NAME ) : getPopulatorFileIS() )
        {
            load( is );
        }
        catch( Exception e )
        {
            tracer.println( "Error while loading populator file Exception.message: " +
                    e.getMessage() );
            lastLoadException = e;
        }
    }

    @Override
    public void load( String fileName )
    {
        try( InputStream is = new File( fileName ).exists() ? new FileInputStream( fileName ) : findFileInClasspath( fileName ) )
        {
            load( is );
        }
        catch( Exception e )
        {
            tracer.println( "Error while loading populator file Exception.message: " +
                    e.getMessage() );
            lastLoadException = e;
        }
    }

    private void load( InputStream inputStream )
    {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        JavaxSaxHandler saxHandler = new JavaxSaxHandler();

        parserFactory.setNamespaceAware( true );
        parserFactory.setValidating( true );
        jposEntryList.clear();
        try
        { parserFactory.newSAXParser().parse( inputStream, saxHandler ); }
        catch (SAXException e)
        {
            tracer.println( "SAX Parser error, msg=" + e.getMessage() );
            lastLoadException = e;
        }
        catch ( IOException e )
        {
            tracer.println( "XML file access error, msg=" + e.getMessage() );
            lastLoadException = e;
        }
        catch ( ParserConfigurationException e )
        {
            tracer.println( "SAX Parser configuration error, msg=" + e.getMessage() );
            lastLoadException = e;
        }
        Iterator entries = jposEntryList.iterator();
        while( entries.hasNext() )
        {
            JposEntry jposEntry = (JposEntry) entries.next();
            getJposEntries().put( jposEntry.getLogicalName(), jposEntry );
        }
    }

    @Override
    public URL getEntriesURL()
    {
        //------------------------------------------------
        // Copied from AbstractXercesRegPopulator
        URL url = null;

        if( getPopulatorFileURL() != null &&
                !getPopulatorFileURL().equals( "" ) )
            try
            { url =  new URL( getPopulatorFileURL() ); }
            catch( Exception e )
            {
                tracer.println( "getEntriesURL: Exception.message=" +
                        e.getMessage() );
            }
        else
            url = createURLFromFile( new File( getPopulatorFileName() ) );

        //<temp>
        tracer.println( "getPopulatorFileURL()=" + getPopulatorFileURL() );
        tracer.println( "getPopulatorFileName()=" + getPopulatorFileName() );
        //</temp>

        return url;
    }

    @Override
    public String getName() { return JAVAX_REG_POPULATOR_NAME_STRING; }

    //--------------------------------------------------------------------------
    // Protected methods
    //

    /** @return the Tracer object */
    protected Tracer getTracer() { return tracer; }

    /**
     * @return a String with the document type definition value.  For DTD this
     * would be the DTD relative path/file and for schemas the XSD
     * relative path/file
     * @since 2.1.0
     */
    protected String getDoctypeValue() { return "jpos/res/jcl.dtd"; }

    private void save( Enumeration entries, OutputStream outputStream )
            throws Exception
    {
        Document document = CreateEmptyDocument();

        insertJposEntriesInDoc( entries, document );
        insertDateSavedComment( document );

        DOMSource source = new DOMSource( document );
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "4" );
        transformer.setOutputProperty( OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId() );
        transformer.setOutputProperty( OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId() );
        transformer.transform( source, new StreamResult( outputStream ) );
    }

    private Document CreateEmptyDocument()
            throws ParserConfigurationException
    {
        DOMImplementation domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
        DocumentType docType = domImpl.createDocumentType( "JposEntries", "-//JavaPOS//DTD//EN", getDoctypeValue() );

        return domImpl.createDocument(null, "JposEntries", docType);
    }

    private void insertJposEntriesInDoc( Enumeration entries, Document doc )
    {
        while( entries.hasMoreElements() )
        {
            JposEntry jposEntry = (JposEntry) entries.nextElement();

            if( JposEntryUtility.isValidJposEntry( jposEntry ) )
            {
                Element jposEntryElement = doc.createElement( "JposEntry" );

                jposEntryElement.setAttribute( "logicalName", jposEntry.getLogicalName() );
                insertJposPropertiesInElement( doc, jposEntry, jposEntryElement );
                doc.getDocumentElement().appendChild( jposEntryElement );
            }
        }
    }

    private void insertJposPropertiesInElement( Document doc, JposEntry jposEntry, Element jposEntryElement )
    {
        Element creation = doc.createElement( "creation" );
        Element jpos = doc.createElement( "jpos" );
        Element product = doc.createElement( "product" );
        Element vendor = doc.createElement( "vendor" );

        for( Element tag : new Element[] { creation, vendor, jpos, product } )
            jposEntryElement.appendChild(tag);

        List<JposEntry.Prop> sortedProps = getSortedList( jposEntry.getProps() );
        for( JposEntry.Prop prop : sortedProps )
        {
            if( notAttribute( prop, creation, JposEntry.SERVICE_CLASS_PROP_NAME, "serviceClass" ) &&
                    notAttribute( prop, creation, JposEntry.SI_FACTORY_CLASS_PROP_NAME, "factoryClass" ) &&
                    notAttribute( prop, vendor, JposEntry.VENDOR_NAME_PROP_NAME, "name" ) &&
                    notAttribute( prop, vendor, JposEntry.VENDOR_URL_PROP_NAME, "url" ) &&
                    notAttribute( prop, jpos, JposEntry.JPOS_VERSION_PROP_NAME, "version" ) &&
                    notAttribute( prop, jpos, JposEntry.DEVICE_CATEGORY_PROP_NAME, "category" ) &&
                    notAttribute( prop, product, JposEntry.PRODUCT_NAME_PROP_NAME, "name" ) &&
                    notAttribute( prop, product, JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, "description" ) &&
                    notAttribute( prop, product, JposEntry.PRODUCT_URL_PROP_NAME, "url" )
            )
                addPropElement( doc, jposEntryElement, prop );
        }
    }

    private List<JposEntry.Prop> getSortedList( Iterator props )
    {
        ArrayList<JposEntry.Prop> sortedProps = new ArrayList<>();
        while( props.hasNext() )
        {
            JposEntry.Prop entry = (JposEntry.Prop) props.next();
            int lowBound = 0;
            int highBound = sortedProps.size() - 1;

            while( lowBound <= highBound )
            {
                int middle = (lowBound + highBound) / 2;
                int compareresult = entry.getName().compareToIgnoreCase( sortedProps.get( middle ).getName() );
                if( compareresult == 0 )
                    lowBound = highBound = middle;
                else if ( compareresult > 0 )
                    lowBound = middle + 1;
                else
                    highBound = middle - 1;
            }
            sortedProps.add( lowBound, entry );
        }
        return sortedProps;
    }

    private boolean notAttribute( JposEntry.Prop prop, Element elem, String propName, String attrName )
    {
        if( prop.getName().equals( propName ) )
        {
            String value = prop.getValueAsString();
            /* Change /* to //* to generate implicit attribute values only if value is not empty.
            if( value.length() > 0 ||
                    ( !JposEntry.PRODUCT_URL_PROP_NAME.equals(propName) &&
                            !JposEntry.VENDOR_URL_PROP_NAME.equals(propName)
                    )
            )
                //*/
                elem.setAttribute( attrName, prop.getValueAsString() );
            return false;
        }
        return true;
    }

    private static void addPropElement( Document doc, Element jposEntryElement, JposEntry.Prop prop )
    {
        Element more = doc.createElement( "prop" );
        jposEntryElement.appendChild( more );
        more.setAttribute( "name", prop.getName() );
        more.setAttribute( "value", prop.getValueAsString() );
        if( !( prop.getValue() instanceof String ) )
            more.setAttribute( "type", prop.getValue().getClass().getSimpleName() );
    }

    private void insertDateSavedComment( Document document )
    {
        String dateString = DateFormat.getInstance().format( new Date( System.currentTimeMillis() ) );
        String commentString = "Saved by JavaPOS jpos.config/loader (JCL) version " + Version.getVersionString()
                + " on " + dateString;
        Comment comment = document.createComment( commentString );
        document.getDocumentElement().insertBefore( comment, document.getDocumentElement().getFirstChild() );
    }

    private Tracer tracer = TracerFactory.getInstance().createTracer( "JavaxRegPopulator" );
    private static final String JAVAX_REG_POPULATOR_NAME_STRING = "JAVAX XML Entries Populator";

    private List jposEntryList = new LinkedList();

    private class JavaxSaxHandler
            extends DefaultHandler
    {
        private JposEntry CurrentEntry = null;
        private SAXException TheException = null;

        @Override
        public void startElement( String uri, String lname, String qname, Attributes attrs )
        {
            tracer.print( "StartElement: "+ qname );
            if( TheException != null )
            {
                tracer.println( ": Parse error: " + TheException.getMessage() );
                lastLoadException = TheException;
                TheException = null;
                return;
            }
            if( qname.equals( "JposEntries" ) )
                jposEntryList.clear();
            else if( qname.equals( "JposEntry" ) )
                CurrentEntry = new SimpleEntry(attrs.getValue("logicalName"), JavaxRegPopulator.this);
            else if( CurrentEntry != null )
            {
                String temp;

                if( qname.equals( "creation" ) )
                {
                    CurrentEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, attrs.getValue( "factoryClass" ) );
                    CurrentEntry.addProperty( JposEntry.SERVICE_CLASS_PROP_NAME, attrs.getValue( "serviceClass" ) );
                }
                else if( qname.equals( "vendor" ) )
                {
                    CurrentEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, attrs.getValue( "name" ) );
                    addOptionalProperty( JposEntry.VENDOR_URL_PROP_NAME, attrs.getValue( "url" ) );
                }
                else if( qname.equals( "jpos" ) )
                {
                    CurrentEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, attrs.getValue( "version" ) );
                    CurrentEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, attrs.getValue( "category" ) );
                }
                else if( qname.equals( "product" ) )
                {
                    CurrentEntry.addProperty( JposEntry.PRODUCT_NAME_PROP_NAME, attrs.getValue( "name" ) );
                    CurrentEntry.addProperty( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, attrs.getValue( "description" ) );
                    addOptionalProperty( JposEntry.PRODUCT_URL_PROP_NAME, attrs.getValue( "url" ) );
                }
                else if( qname.equals( "prop" ) )
                {
                    temp = attrs.getValue( "type" );
                    try
                    {
                        Class type = temp == null ? String.class : Class.forName( "java.lang." + temp );
                        Object obj = null;
                        obj = JposEntryUtility.parsePropValue( attrs.getValue( "value" ), type );
                        CurrentEntry.addProperty( attrs.getValue( "name" ), obj );
                    }
                    catch( Exception e )
                    {
                        CurrentEntry = null;
                        String msg = "Invalid prop: name=" + attrs.getValue( "name" )
                                + ":value=" + attrs.getValue( "value" );
                        tracer.println( ": " + msg );
                        lastLoadException =  new SAXException( msg, e );
                    };
                }
            }
            tracer.println( "" );
        }

        private void addOptionalProperty( String name, String value )
        {
            CurrentEntry.addProperty( name, value == null ? "" : value );
        }

        @Override
        public void endElement( String uri, String lname, String qname )
        {
            if( TheException == null )
                tracer.println( "EndElement: " + qname );
            else
            {
                tracer.println( "EndElement: " + TheException.getMessage() );
                TheException = null;
            }
            if( qname.equals( "JposEntry" ) )
            {
                if( CurrentEntry != null )
                    jposEntryList.add( CurrentEntry );
                CurrentEntry = null;
                TheException = null;
            }
        }

        @Override
        public void error( SAXParseException e )
        {
            CurrentEntry = null;
            TheException = e;
        }

        @Override
        public void fatalError( SAXParseException e )
        {
            CurrentEntry = null;
            TheException = e;
        }
    }
}
