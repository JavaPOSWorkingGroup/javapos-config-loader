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

import java.io.Serializable;

/**
 * Defines a Character property type
 * @since 1.3 (SF 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class CharacterPropType extends AbstractPropType implements PropType, Serializable
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	private static final long serialVersionUID = -8746645376918501427L;

	private CharacterPropType() {}

	//---------------------------------------------------------------------
	// Class methods
	//

	/** @return the unique instance of this class (create if necessary) */
	public static PropType getInstance()
	{
		return instance;
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return a String representation of this PropType */
	public String toString() { return "CharacterPropType"; }

	/** @return a Java class that defines this type */
	public Class<?> getJavaTypeClass() { return Character.class; }

	/**
	 * @return true if the object passed is of this PropType
	 * @param c the char primitive type
	 */
	public boolean isValidValue( char c ) { return true; }

	/**
	 * @return true if the object passed is of this PropType
	 * @param obj the Java Object
	 */
	public boolean isValidValue( Object obj ) { return ( obj instanceof Character ); }

	/**
	 * @return true if the PropValue passed is of this PropType
	 * @param propValue the PropValue
	 */
	public boolean isValidValue( PropValue propValue ) { return isValidValue( propValue.getValue() ); }

	//-------------------------------------------------------------------------
	// Class instance
	//

	private static final PropType instance = new CharacterPropType();
}
