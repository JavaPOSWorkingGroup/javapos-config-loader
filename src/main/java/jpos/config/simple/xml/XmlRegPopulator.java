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

import jpos.config.JposRegPopulator;

/**
 * Defines an interface to load, save JposEntries
 * @since 1.2 (NY 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public interface XmlRegPopulator extends JposRegPopulator
{
    //-------------------------------------------------------------------------
    // Class constants
    //

    /**
     * Default name to save entries if the XML populator is chosen but
     * no populator file is specified
     * @since 1.2 (NY 2K meeting)
     */
    public static final String DEFAULT_XML_FILE_NAME = "jpos.xml";

    /**
     * Default path for dtd and / or xsd file
     * @since 4.0
     */
    public static final String DTD_FILE_PATH = "jpos/res";

    /**
     * Default name for dtd file
     * @since 4.0
     */
    public static final String DTD_FILE_NAME = DTD_FILE_PATH + "/jcl.dtd";

    /**
     * Default name for xsd file
     * @since 4.0
     */
    public static final String XSD_FILE_NAME = DTD_FILE_PATH + "/jcl.xsd";

    /**
     * Default DTD document type value
     */
    public static final String DTD_DOC_TYPE_VALUE = "-//JavaPOS//DTD//EN";

    /**
     * Define for tag name <i>JposEntries</i>
     * @since 4.0
     */
    public static final String XML_TAG_JPOSENTRIES = "JposEntries";

    /**
     * Define for tag name <i>JposEntry</i>
     * @since 4.0
     */
    public static final String XML_TAG_JPOSENTRY = "JposEntry";

    /**
     * Define for tag name <i>creation</i>
     * @since 4.0
     */
    public static final String XML_TAG_CREATION = "creation";

    /**
     * Define for tag name <i>vendor</i>
     * @since 4.0
     */
    public static final String XML_TAG_VENDOR = "vendor";

    /**
     * Define for tag name <i>jpos</i>
     * @since 4.0
     */
    public static final String XML_TAG_JPOS = "jpos";

    /**
     * Define for tag name <i>product</i>
     * @since 4.0
     */
    public static final String XML_TAG_PRODUCT = "product";

    /**
     * Define for tag name <i>prop</i>
     * @since 4.0
     */
    public static final String XML_TAG_PROP = "prop";

    /**
     * Define for attribute name <i>serviceClass</i>
     * @since 4.0
     */
    public static final String XML_ATTR_SERVICECLASS = "serviceClass";

    /**
     * Define for attribute name <i>factoryClass</i>
     * @since 4.0
     */
    public static final String XML_ATTR_FACTORYCLASS= "factoryClass";

    /**
     * Define for attribute name <i>name</i>
     * @since 4.0
     */
    public static final String XML_ATTR_NAME = "name";

    /**
     * Define for attribute name <i>url</i>
     * @since 4.0
     */
    public static final String XML_ATTR_URL = "url";

    /**
     * Define for attribute name <i>name</i>
     * @since 4.0
     */
    public static final String XML_ATTR_VERSION = "version";

    /**
     * Define for attribute name <i>category</i>
     * @since 4.0
     */
    public static final String XML_ATTR_CATEGORY = "category";

    /**
     * Define for attribute name <i>description</i>
     * @since 4.0
     */
    public static final String XML_ATTR_DESCRIPTION = "description";

    /**
     * Define for attribute name <i>value</i>
     * @since 4.0
     */
    public static final String XML_ATTR_VALUE = "value";

    /**
     * Define for attribute name <i>type</i>
     * @since 4.0
     */
    public static final String XML_ATTR_TYPE = "type";
}