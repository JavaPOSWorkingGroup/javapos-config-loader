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
 * Default implementation of the PropType
 * @since 1.3 (SF 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public abstract class AbstractPropType implements PropType, Serializable
{
	private static final long serialVersionUID = 3913079070484345271L;

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return a String description of this PropType (can be HTML text) */
	public String getDescription() { return description; }

	//-------------------------------------------------------------------------
	// Package setter methods
	//

	/** 
	 * Sets the String description of this PropType (can be HTML text) 
	 * @param s the String
	 */
	void setDescription( String s ) { description = s; }

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private String description = "";
}
