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

import java.util.*;

import jpos.config.JposEntry;
import jpos.config.JposEntryConst;
import jpos.config.RS232Const;
import jpos.config.JposConfigException;
import jpos.config.simple.SimpleEntry;

/**
 * Simple class used to validate JposEntry objects
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Tokyo 2001 meeting)
 * @version 1.3.0
 */
public class JposEntryUtility 
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** Make ctor private (utility class) */
	private JposEntryUtility() {}

	//-------------------------------------------------------------------------
	// Public class mehods
	//

    /**
     * @return true if this entry is valid with all required properties
     * @param jposEntry the entry to validate
     */
    public static boolean isValidJposEntry( JposEntry jposEntry )
    {
        boolean valid = false;

        if( jposEntry.hasPropertyWithName( JposEntry.LOGICAL_NAME_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.SERVICE_CLASS_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.DEVICE_CATEGORY_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.JPOS_VERSION_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.VENDOR_NAME_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.VENDOR_URL_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.PRODUCT_NAME_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.PRODUCT_URL_PROP_NAME ) &&
            jposEntry.hasPropertyWithName( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME ) )
            valid = true;

        return valid;
    }

    /**
     * @return true if the property name passed is a required property
     * @param name the property name
     */
    public static boolean isRequiredPropName( String name )
    {
        boolean valid = false;

        if( name.equals( JposEntry.LOGICAL_NAME_PROP_NAME ) ||
            name.equals( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) ||
            name.equals( JposEntry.SERVICE_CLASS_PROP_NAME ) ||
            name.equals( JposEntry.DEVICE_CATEGORY_PROP_NAME ) ||
            name.equals( JposEntry.JPOS_VERSION_PROP_NAME ) ||
            name.equals( JposEntry.VENDOR_NAME_PROP_NAME ) ||
            name.equals( JposEntry.VENDOR_URL_PROP_NAME ) ||
            name.equals( JposEntry.PRODUCT_NAME_PROP_NAME ) ||
            name.equals( JposEntry.PRODUCT_URL_PROP_NAME ) ||
            name.equals( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME ) )
            valid = true;

        return valid;
    }

    /**
     * @return true if the property name passed is a RS232 property
     * @param name the property name
     */
    public static boolean isRS232PropName( String name )
    {
        boolean valid = false;
        
		if( name.equals( RS232Const.RS232_PORT_NAME_PROP_NAME ) ||
            name.equals( RS232Const.RS232_BAUD_RATE_PROP_NAME ) ||
            name.equals( RS232Const.RS232_DATA_BITS_PROP_NAME ) ||
            name.equals( RS232Const.RS232_PARITY_PROP_NAME ) ||
            name.equals( RS232Const.RS232_STOP_BITS_PROP_NAME ) ||
            name.equals( RS232Const.RS232_FLOW_CONTROL_PROP_NAME ) )
			valid = true;

        return valid;
    }

	/**
	 * Removes all RS232 specific utilities from this JposEntry
	 * @param jposEntry the JposEntry to modify
	 */
	public static void removeAllRS232Props( JposEntry jposEntry )
	{
		jposEntry.removeProperty( RS232Const.RS232_PORT_NAME_PROP_NAME );
		jposEntry.removeProperty( RS232Const.RS232_BAUD_RATE_PROP_NAME );
		jposEntry.removeProperty( RS232Const.RS232_DATA_BITS_PROP_NAME );
		jposEntry.removeProperty( RS232Const.RS232_PARITY_PROP_NAME );
		jposEntry.removeProperty( RS232Const.RS232_STOP_BITS_PROP_NAME );
		jposEntry.removeProperty( RS232Const.RS232_FLOW_CONTROL_PROP_NAME );
	}
	 

	/** @return an Iterator of all the standard property names */
	public static Iterator<String> getStandardPropNames() { return STANDARD_PROP_NAMES_LIST.iterator(); }

	/**
	 * @return a enumeration of all non-required property names from the jposEntry
	 * @param jposEntry the JposEntry
	 */
	public static Enumeration<String> getNonRequiredPropNames( JposEntry jposEntry )
	{
		List<String> list = new ArrayList<>();

		@SuppressWarnings("unchecked")
		Enumeration<String> names = jposEntry.getPropertyNames();
		while( names.hasMoreElements() )
		{
			String name = names.nextElement();

			if( !isRequiredPropName( name ) )
				list.add( name );
		}

		return Collections.enumeration(list);
	}

    /**
     * @return an Enumeration of property names that are required but missing
	 * from the JposEntry object passed
     * @param jposEntry the entry to validate
     */
    public static Enumeration<String> getMissingRequiredPropNames( JposEntry jposEntry )
    {
		List<String> list = new ArrayList<>();

		if( !jposEntry.hasPropertyWithName( JposEntry.LOGICAL_NAME_PROP_NAME ) )
			list.add( JposEntry.LOGICAL_NAME_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.SI_FACTORY_CLASS_PROP_NAME ) )
			list.add( JposEntry.SI_FACTORY_CLASS_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.SERVICE_CLASS_PROP_NAME ) )
			list.add( JposEntry.SERVICE_CLASS_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.DEVICE_CATEGORY_PROP_NAME ) )
			list.add( JposEntry.DEVICE_CATEGORY_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.JPOS_VERSION_PROP_NAME ) )
			list.add( JposEntry.JPOS_VERSION_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.VENDOR_NAME_PROP_NAME ) )
			list.add( JposEntry.VENDOR_NAME_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.VENDOR_URL_PROP_NAME )  )
			list.add( JposEntry.VENDOR_URL_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.PRODUCT_NAME_PROP_NAME )  )
			list.add( JposEntry.PRODUCT_NAME_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.PRODUCT_URL_PROP_NAME )  )
			list.add( JposEntry.PRODUCT_URL_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME ) )
			list.add( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME );

        return Collections.enumeration(list);
    }

    /**
     * @return an Enumeration of all RS232 property names that are missing
	 * from the JposEntry object passed
     * @param jposEntry the entry to validate
     */
    public static Enumeration<String> getMissingRS232PropNames( JposEntry jposEntry )
    {
		List<String> list = new ArrayList<>();

		if( !jposEntry.hasPropertyWithName( RS232Const.RS232_PORT_NAME_PROP_NAME ) )
			list.add( RS232Const.RS232_PORT_NAME_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( RS232Const.RS232_BAUD_RATE_PROP_NAME ) )
			list.add( RS232Const.RS232_BAUD_RATE_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( RS232Const.RS232_DATA_BITS_PROP_NAME ) )
			list.add( RS232Const.RS232_DATA_BITS_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( RS232Const.RS232_PARITY_PROP_NAME ) )
			list.add( RS232Const.RS232_PARITY_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( RS232Const.RS232_STOP_BITS_PROP_NAME ) )
			list.add( RS232Const.RS232_STOP_BITS_PROP_NAME );

		if( !jposEntry.hasPropertyWithName( RS232Const.RS232_FLOW_CONTROL_PROP_NAME ) )
			list.add( RS232Const.RS232_FLOW_CONTROL_PROP_NAME );

        return Collections.enumeration(list);
    }

	/**
	 * @return an Enumeration of all non-standard properties, that is vendor properties
	 * @param jposEntry the JposEntry to find the vendor property names from
	 */
	public static Enumeration<String> getVendorPropNames( JposEntry jposEntry )
	{
		List<String> list = new ArrayList<>();

		@SuppressWarnings("unchecked")
		Enumeration<String> propNames = jposEntry.getPropertyNames();
		while( propNames.hasMoreElements() )
			list.add( propNames.nextElement() );

		Iterator<String> standardPropNames = getStandardPropNames();

		while( standardPropNames.hasNext() )
		{
			String name = standardPropNames.next();
			list.remove( name );
		}

		return Collections.enumeration(list);
	}

    /**
     * Add defaults all required properties that are missing from this JposEntry
     * @param jposEntry the JposEntry object
     */
	public static void addMissingRequiredProps( JposEntry jposEntry )
	{
		Enumeration<String> missingPropNames = getMissingRequiredPropNames( jposEntry );

		JposEntry prototype = getEntryPrototype();

		while( missingPropNames.hasMoreElements() )
		{
			String propName = missingPropNames.nextElement();
			jposEntry.addProperty( propName, prototype.getPropertyValue( propName ) );
		}
	}																				  

	/**
	 * @return a prototypical instance of a JposEntry with all required properties set to
	 * their default values
	 */
	public static JposEntry getEntryPrototype()
	{
		if( prototypeJposEntry == null )
		{
			prototypeJposEntry = new SimpleEntry();

			prototypeJposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, JposEntryConst.LOGICAL_NAME_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, JposEntryConst.SI_FACTORY_CLASS_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.SERVICE_CLASS_PROP_NAME, JposEntryConst.SERVICE_CLASS_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, JposEntryConst.DEVICE_CATEGORY_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, JposEntryConst.JPOS_VERSION_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, JposEntryConst.VENDOR_NAME_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.VENDOR_URL_PROP_NAME, JposEntryConst.VENDOR_URL_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.PRODUCT_NAME_PROP_NAME, JposEntryConst.PRODUCT_NAME_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.PRODUCT_URL_PROP_NAME, JposEntryConst.PRODUCT_URL_DEFAULT_PROP_VALUE );
			prototypeJposEntry.addProperty( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, JposEntryConst.PRODUCT_DESCRIPTION_DEFAULT_PROP_VALUE );
		}

		return ( (SimpleEntry)prototypeJposEntry ).copy();
	}

	/**
	 * @return the short name of the Class object passed, that is return the name of the class
	 * w/o the package name
	 * @param classObject the Class object
	 * @since 2.0.0
	 */
	public static String shortClassName( @SuppressWarnings("rawtypes") Class classObject )
	{ return classObject.toString().substring( classObject.toString().lastIndexOf( "." ) + 1 ); }

	/** 
	 * @return true if the Object passed is a valid JposEntry property value of the type passed
	 * @param propValue the property value object
	 * @param propType the property type object
	 * @see jpos.config.JposEntryConst#PROP_TYPES
	 * @since 2.0.0
	 */
	public static boolean validatePropValue( Object propValue, @SuppressWarnings("rawtypes") Class propType )
	{
		if( propValue == null || propType == null ) return false;

		if( !isValidPropType( propType ) ) return false;

		return propType.isInstance( propValue );
	}

	/**
	 * Predicate for checking whether the given Java type is a valid JavaPOS property type. 
	 * @param propType a {@link Class} object as type
	 * @return true if the propType object passed is a valid JposEntry property type
	 * that is one of the JposEntryConst.PROP_TYPES
	 * @see jpos.config.JposEntryConst#PROP_TYPES
	 * @since 2.0.0
	 */
	public static boolean isValidPropType( @SuppressWarnings("rawtypes") Class propType )
	{
		if( propType == null ) return false;

		for( int i = 0; i < JposEntryConst.PROP_TYPES.length; ++i )
			if( propType.equals( JposEntryConst.PROP_TYPES[ i ] ) )
				return true;

		return false;
	}

	/**
	 * @return the default value object for the propType class passed
	 * @param propType the property type Class
	 * @exception jpos.config.JposConfigException if this property type is not 
	 * valid
	 */
	public static Object getDefaultValueForType( @SuppressWarnings("rawtypes") Class propType ) throws JposConfigException
	{
		if( !isValidPropType( propType ) )
			throw new JposConfigException( "Invalid property type: " + propType );

		Object propValue = "";

		try
		{
			if( propType.equals( String.class ) )
				propValue = "";
			else
			if( propType.equals( Boolean.class ) )
				propValue = Boolean.valueOf(false);
			else
			if( propType.equals( Character.class ) )
				propValue = Character.valueOf('a');
			else
			if( propType.equals( Double.class ) )
				propValue = Double.valueOf(0);
			else
			if( propType.equals( Float.class ) )
				propValue = Float.valueOf(0);
			else
			if( propType.equals( Byte.class ) )
				propValue = Byte.valueOf((byte) 0);
			else
			if( propType.equals( Integer.class ) )
				propValue = Integer.valueOf(0) ;
			else
			if( propType.equals( Long.class ) )
				propValue = Long.valueOf(0) ;
			else
			if( propType.equals( Short.class ) )
				propValue = Short.valueOf((short) 0);
		}
		catch( Exception e )
		{ throw new JposConfigException( "Invalid property type" ); }

		return propValue;		
	}

	/**
	 * @return the property value Object parsed from the String passed assuming that its
	 * of a valid property type and also that the string can be converted 
	 * @param stringValue the String value to parse
	 * @param propType the property value type
	 * @see jpos.config.JposEntryConst#PROP_TYPES
	 * @exception jpos.config.JposConfigException if this property value cannot be created
	 * from the arguments passed
	 * @since 2.0.0
	 */
	public static Object parsePropValue( String stringValue, @SuppressWarnings("rawtypes") Class propType ) throws JposConfigException
	{
		if( !isValidPropType( propType ) )
			throw new JposConfigException( "Invalid property type: " + propType );

		Object propValue = "";

		try
		{
			if( propType.equals( String.class ) )
				propValue = stringValue;
			else
			if( propType.equals( Boolean.class ) )
				propValue = Boolean.valueOf( stringValue );
			else
			if( propType.equals( Character.class ) )
				propValue = Character.valueOf( stringValue.charAt( 0 ) );
			else
			if( propType.equals( Double.class ) )
				propValue = Double.valueOf( stringValue );
			else
			if( propType.equals( Float.class ) )
				propValue = Float.valueOf( stringValue );
			else
			if( propType.equals( Byte.class ) )
				propValue = Byte.decode( stringValue );
			else
			if( propType.equals( Integer.class ) )
				propValue = Integer.decode( stringValue );
			else
			if( propType.equals( Long.class ) )
				propValue = Long.decode( stringValue );
			else
			if( propType.equals( Short.class ) )
				propValue = Short.decode( stringValue );
		}
		catch( Exception e )
		{ throw new JposConfigException( "Could not create property value of type: " + 
										 propType + " from string: " + stringValue ); }

		return propValue;
	}

	/** 
	 * @return the Class type for the property from the typeString passed.  The type
	 * string can either be the short name of the type or the fully-qualified class name
	 * @param typeString the type string name
	 * @throws jpos.config.JposConfigException if the typeString is not a valid property type string
	 */
	public static Class<?> propTypeFromString( String typeString ) throws JposConfigException
	{
		if( typeString == null ) throw new JposConfigException( "typeString cannot be null" );

		String className = ( typeString.startsWith( "java.lang." ) ? typeString : "java.lang." + typeString );

		try
		{
			Class<?> typeClass = Class.forName( className );

			if( !isValidPropType( typeClass ) )
				throw new JposConfigException( "Invalid property type: " + typeString );

			return typeClass;
		}
		catch( ClassNotFoundException cnfe ) { throw new JposConfigException( "Invalid typeString", cnfe ); }
	}

	//-------------------------------------------------------------------------
	// Class variables
	//

	private static JposEntry prototypeJposEntry = null;

	//-------------------------------------------------------------------------
	// Class constants
	//
	
	private static final List<String> STANDARD_PROP_NAMES_LIST = new ArrayList<>();

	//-------------------------------------------------------------------------
	// Static initializer
	//

	/** Initializes the standard set of properties */
	static
	{
		for( int i = 0; i < JposEntryConst.REQUIRED_PROPS.length; ++i )
			STANDARD_PROP_NAMES_LIST.add( JposEntryConst.REQUIRED_PROPS[ i ] );

		STANDARD_PROP_NAMES_LIST.add( JposEntryConst.DEVICE_BUS_PROP_NAME );

		for( int i = 0; i < RS232Const.RS232_PROPS.length; ++i )
			STANDARD_PROP_NAMES_LIST.add( RS232Const.RS232_PROPS[ i ] );
	}
}
