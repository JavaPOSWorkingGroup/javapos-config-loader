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
import java.util.Objects;

/**
 * Class implementing the PropValue interface
 * @since 1.3 (SF 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
class DefaultPropValue implements PropValue, Serializable
{
	private static final long serialVersionUID = 8895132456252900824L;

	/**
	 * 2-arg ctor taking Object value
	 * @param obj the Object value
	 * @param propType the PropType
	 */
	DefaultPropValue( Object obj, PropType propType ) 
	{ 
		value = obj; 
		type = propType;
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the Java object that this value represents */
	public Object getValue() { return value; }

	/** @return a String representation of this value */
	public String toString() { return value.toString(); }

	/** @return the PropType for this PropValue */
	public PropType getType() { return type; }

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultPropValue other = (DefaultPropValue) obj;
		return Objects.equals(type, other.type) && Objects.equals(value, other.value);
	}
	
	//-------------------------------------------------------------------------
	// Instance variables
	//

	private Object value = null;

	private PropType type = null;
}
