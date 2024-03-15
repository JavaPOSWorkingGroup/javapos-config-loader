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
import org.xml.sax.*;
import org.xml.sax.ext.DefaultHandler2;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

/**
 * Simple implementation of the JposRegPopulator that loads and saves
 * the entries in XML using Javax API.
 * NOTE: this class must define a public no-argument constructor so that it may be
 * created via reflection when it is defined in the jpos.properties as
 * the jpos.config.regPopulatorClass
 * @see jpos.util.JposProperties#JPOS_REG_POPULATOR_CLASS_PROP_NAME
 * @since 4.0
 * @author M. Conrad (martin.conrad@freenet.de)
 */
public class JavaxRegPopulator
        extends AbstractRegPopulator
        implements XmlRegPopulator {
    /**
     * Default ctor
     */
    public JavaxRegPopulator() {
        super(JavaxRegPopulator.class.getName());
    }

    /**
     * 1-arg constructor that takes the unique ID
     *
     * @param s the unique ID string
     * @since 1.3
     */
    public JavaxRegPopulator(String s) {
        super(s);
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    @Override
    public String getClassName() {
        return JavaxRegPopulator.class.getName();
    }

    @Override
    public URL getEntriesURL() {
        URL url = null;

        if (getPopulatorFileURL() != null &&
                !getPopulatorFileURL().equals(""))
            try {
                url = new URL(getPopulatorFileURL());
            } catch (Exception e) {
                tracer.println("getEntriesURL: Exception.message=" +
                        e.getMessage());
            }
        else
            url = createURLFromFile(new File(getPopulatorFileName()));

        tracer.println("getPopulatorFileURL()=" + getPopulatorFileURL());
        tracer.println("getPopulatorFileName()=" + getPopulatorFileName());

        return url;
    }


    @Override
    public void save(Enumeration entries)
            throws Exception {
        if (isPopulatorFileDefined())
            save(entries, getPopulatorFileOS());
        else
            try (FileOutputStream os = new FileOutputStream(DEFAULT_XML_FILE_NAME)) {
                save(entries, os);
            }
    }

    @Override
    public void save(Enumeration entries, String fileName)
            throws Exception {
        try (FileOutputStream os = new FileOutputStream(fileName)) {
            save(entries, os);
        }
    }

    @Override
    public void load() {
        try (InputStream is = isPopulatorFileDefined() ? new FileInputStream(DEFAULT_XML_FILE_NAME) : getPopulatorFileIS()) {
            load(is);
        } catch (Exception e) {
            tracer.println("Error while loading populator file Exception.message: " +
                    e.getMessage());
            lastLoadException = e;
        }
    }

    @Override
    public void load(String fileName) {
        try (InputStream is = new File(fileName).exists() ? new FileInputStream(fileName) : findFileInClasspath(fileName)) {
            load(is);
        } catch (Exception e) {
            tracer.println("Error while loading populator file Exception.message: " +
                    e.getMessage());
            lastLoadException = e;
        }
    }

    @Override
    public String getName() {
        return JAVAX_REG_POPULATOR_NAME_STRING;
    }

    //--------------------------------------------------------------------------
    // Private methods
    //

    private void save(Enumeration<JposEntry> entries, OutputStream outputStream)
            throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        Document document = createEmptyDocument();

        insertJposEntriesInDoc(entries, document);
        insertDateSavedComment(document);

        DOMSource source = new DOMSource(document);
        TransformerFactory factory = TransformerFactory.newInstance();
  //      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "file,jar:file");
  //      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file,jar:file");
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
        transformer.transform(source, new StreamResult(outputStream));
    }

    private Document createEmptyDocument()
            throws ParserConfigurationException {
        DOMImplementation domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
        DocumentType docType = domImpl.createDocumentType("JposEntries", DTD_DOC_TYPE_VALUE, DTD_FILE_NAME);

        return domImpl.createDocument(null, "JposEntries", docType);
    }


    private void insertJposEntriesInDoc(Enumeration<JposEntry> entries, Document doc) {
        while (entries.hasMoreElements()) {
            JposEntry jposEntry = entries.nextElement();

            if (JposEntryUtility.isValidJposEntry(jposEntry)) {
                Element jposEntryElement = doc.createElement("JposEntry");

                jposEntryElement.setAttribute("logicalName", jposEntry.getLogicalName());
                insertJposPropertiesInElement(doc, jposEntry, jposEntryElement);
                doc.getDocumentElement().appendChild(jposEntryElement);
            }
        }
    }

    private void insertJposPropertiesInElement(Document doc, JposEntry jposEntry, Element jposEntryElement) {
        Element creation = doc.createElement("creation");
        Element jpos = doc.createElement("jpos");
        Element product = doc.createElement("product");
        Element vendor = doc.createElement("vendor");

        for (Element tag : new Element[]{creation, vendor, jpos, product})
            jposEntryElement.appendChild(tag);

        List<JposEntry.Prop> sortedProps = getSortedList(jposEntry.getProps());
        for (JposEntry.Prop prop : sortedProps) {
            if (notAttribute(prop, creation, JposEntry.SERVICE_CLASS_PROP_NAME, "serviceClass") &&
                    notAttribute(prop, creation, JposEntry.SI_FACTORY_CLASS_PROP_NAME, "factoryClass") &&
                    notAttribute(prop, vendor, JposEntry.VENDOR_NAME_PROP_NAME, "name") &&
                    notAttribute(prop, vendor, JposEntry.VENDOR_URL_PROP_NAME, "url") &&
                    notAttribute(prop, jpos, JposEntry.JPOS_VERSION_PROP_NAME, "version") &&
                    notAttribute(prop, jpos, JposEntry.DEVICE_CATEGORY_PROP_NAME, "category") &&
                    notAttribute(prop, product, JposEntry.PRODUCT_NAME_PROP_NAME, "name") &&
                    notAttribute(prop, product, JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, "description") &&
                    notAttribute(prop, product, JposEntry.PRODUCT_URL_PROP_NAME, "url")
            )
                addPropElement(doc, jposEntryElement, prop);
        }
    }

    private List<JposEntry.Prop> getSortedList(Iterator<JposEntry.Prop> props) {
        ArrayList<JposEntry.Prop> sortedProps = new ArrayList<>();
        props.forEachRemaining(sortedProps::add);
        Collections.sort(sortedProps, new Comparator<JposEntry.Prop>() {
            @Override
            public int compare(JposEntry.Prop o1, JposEntry.Prop o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return sortedProps;
    }

    private boolean notAttribute(JposEntry.Prop prop, Element elem, String propName, String attrName) {
        if (prop.getName().equals(propName)) {
            String value = prop.getValueAsString();
            /* Change /* to //* to generate implicit attribute values only if value is not empty.
            if( value.length() > 0 ||
                    ( !JposEntry.PRODUCT_URL_PROP_NAME.equals(propName) &&
                            !JposEntry.VENDOR_URL_PROP_NAME.equals(propName)
                    )
            )
                //*/
            elem.setAttribute(attrName, prop.getValueAsString());
            return false;
        }
        return true;
    }

    private static void addPropElement(Document doc, Element jposEntryElement, JposEntry.Prop prop) {
        Element propElement = doc.createElement("prop");
        jposEntryElement.appendChild(propElement);
        propElement.setAttribute("name", prop.getName());
        propElement.setAttribute("value", prop.getValueAsString());
        if (!(prop.getValue() instanceof String))
            propElement.setAttribute("type", prop.getValue().getClass().getSimpleName());
    }

    private void insertDateSavedComment(Document document) {
        String dateString = DateFormat.getInstance().format(new Date(System.currentTimeMillis()));
        String commentString = "Saved by javapos-config-loader (JCL) version " + Version.getVersionString()
                + " on " + dateString;
        Comment comment = document.createComment(commentString);
        document.getDocumentElement().insertBefore(comment, document.getDocumentElement().getFirstChild());
    }

    private void load(InputStream inputStream) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        JavaxSaxHandler saxHandler = new JavaxSaxHandler();
        try {
            InputStream is = findFileInClasspath(XSD_FILE_NAME);
            StreamSource ss = new StreamSource(is == null ? new FileInputStream(XSD_FILE_NAME) : is);
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(ss);
            parserFactory.setSchema(schema);
        } catch (Exception e) {
            parserFactory.setValidating(true);
        }
        parserFactory.setNamespaceAware(true);
        jposEntryList.clear();
        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "file");
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file,jar:file");
            parser.parse(inputStream, saxHandler);
        } catch (SAXException e) {
            tracer.println("SAX Parser error, msg=" + e.getMessage());
            lastLoadException = e;
        } catch (IOException e) {
            tracer.println("XML file access error, msg=" + e.getMessage());
            lastLoadException = e;
        } catch (ParserConfigurationException e) {
            tracer.println("SAX Parser configuration error, msg=" + e.getMessage());
            lastLoadException = e;
        }
        Iterator<JposEntry> entries = jposEntryList.iterator();
        while (entries.hasNext()) {
            JposEntry jposEntry = entries.next();
            getJposEntries().put(jposEntry.getLogicalName(), jposEntry);
        }
    }

    //--------------------------------------------------------------------------
    // Private inner classes
    //

    private class JavaxSaxHandler
            extends DefaultHandler2 {
        private JposEntry currentEntry = null;
        private SAXException theException = null;

        //----------------------------------------------------------------
        // ContentHandler

        @Override
        public void startElement(String uri, String lname, String qname, Attributes attrs) {
            tracer.print("StartElement: " + qname);
            if (theException != null) {
                tracer.println(": Parse error: " + theException.getMessage());
                lastLoadException = theException;
                theException = null;
                return;
            }
            if (qname.equals("JposEntries"))
                jposEntryList.clear();
            else if (qname.equals("JposEntry"))
                currentEntry = new SimpleEntry(attrs.getValue("logicalName"), JavaxRegPopulator.this);
            else if (currentEntry != null) {
                String temp;

                if (qname.equals("creation")) {
                    currentEntry.addProperty(JposEntry.SI_FACTORY_CLASS_PROP_NAME, attrs.getValue("factoryClass"));
                    currentEntry.addProperty(JposEntry.SERVICE_CLASS_PROP_NAME, attrs.getValue("serviceClass"));
                } else if (qname.equals("vendor")) {
                    currentEntry.addProperty(JposEntry.VENDOR_NAME_PROP_NAME, attrs.getValue("name"));
                    addOptionalProperty(JposEntry.VENDOR_URL_PROP_NAME, attrs.getValue("url"));
                } else if (qname.equals("jpos")) {
                    currentEntry.addProperty(JposEntry.JPOS_VERSION_PROP_NAME, attrs.getValue("version"));
                    currentEntry.addProperty(JposEntry.DEVICE_CATEGORY_PROP_NAME, attrs.getValue("category"));
                } else if (qname.equals("product")) {
                    currentEntry.addProperty(JposEntry.PRODUCT_NAME_PROP_NAME, attrs.getValue("name"));
                    currentEntry.addProperty(JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, attrs.getValue("description"));
                    addOptionalProperty(JposEntry.PRODUCT_URL_PROP_NAME, attrs.getValue("url"));
                } else if (qname.equals("prop")) {
                    temp = attrs.getValue("type");
                    try {
                        Class<?> type = temp == null ? String.class : Class.forName("java.lang." + temp);
                        Object obj = null;
                        obj = JposEntryUtility.parsePropValue(attrs.getValue("value"), type);
                        currentEntry.addProperty(attrs.getValue("name"), obj);
                    } catch (Exception e) {
                        currentEntry = null;
                        String msg = "Invalid prop: name=" + attrs.getValue("name")
                                + ":value=" + attrs.getValue("value");
                        tracer.println(": " + msg);
                        lastLoadException = new SAXException(msg, e);
                    }
                    ;
                }
            }
            tracer.println("");
        }

        private void addOptionalProperty(String name, String value) {
            currentEntry.addProperty(name, value == null ? "" : value);
        }

        @Override
        public void endElement(String uri, String lname, String qname) {
            if (theException == null)
                tracer.println("EndElement: " + qname);
            else {
                tracer.println("EndElement: " + theException.getMessage());
                theException = null;
            }
            if (qname.equals("JposEntry")) {
                if (currentEntry != null)
                    jposEntryList.add(currentEntry);
                currentEntry = null;
                theException = null;
            }
        }

        //----------------------------------------------------------------
        // ErrorHandler

        @Override
        public void error(SAXParseException e) {
            currentEntry = null;
            theException = e;
        }

        @Override
        public void fatalError(SAXParseException e) {
            currentEntry = null;
            theException = e;
        }

        //----------------------------------------------------------------
        // EntityResolver

        @Override
        public InputSource resolveEntity(String name, String publicId, String uri, String systemId) {
            tracer.println("JposEntityResolver:resolveEntity:publicId=" +
                    publicId);

            tracer.println("JposEntityResolver:resolveEntity:systemId=" +
                    systemId);

            if (publicId.equals(DTD_DOC_TYPE_VALUE)) {
                InputStream is =
                        getClass().getResourceAsStream(DTD_FILE_NAME);

                if (is == null)
                    is = findFileInClasspath(DTD_FILE_NAME);

                if (is != null)
                    return new InputSource(new InputStreamReader(is));
            }

            return null;
        }
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private List<JposEntry> jposEntryList = new LinkedList<>();

    private Tracer tracer = TracerFactory.getInstance().
            createTracer(this.getClass().getSimpleName());

    //--------------------------------------------------------------------------
    // Constants
    //

    public static final String DTD_FILE_PATH = "jpos/res";
    public static final String DTD_FILE_NAME = DTD_FILE_PATH + "/jcl.dtd";

    public static final String XSD_FILE_NAME = DTD_FILE_PATH + "/jcl.xsd";

    public static final String DTD_DOC_TYPE_VALUE = "-//JavaPOS//DTD//EN";

    private static final String JAVAX_REG_POPULATOR_NAME_STRING = "JAVAX XML Entries Populator";
}
