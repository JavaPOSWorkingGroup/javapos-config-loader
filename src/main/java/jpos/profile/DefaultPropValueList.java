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

import java.util.*;

/**
 * Default implementation of the PropValueList interface
 * @since 1.3 (SF 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
class DefaultPropValueList implements PropValueList
{
	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the current size of this list */
	public int getSize() { return list.size(); }

	/** @return true if this list is empty */
	public boolean isEmpty() { return list.isEmpty(); }

	/**
	 * Adds a new PropValue to this list
	 * @param propValue the propValue to add
	 */
	public void add( PropValue propValue ) { list.add( propValue ); }

	/**
	 * Removes the PropValue to this list
	 * @param propValue the propValue to remove
	 */
	public void remove( PropValue propValue ) { list.remove( propValue ); }

	/** Removes all PropValue in this list */
	public void removeAll() { list.clear(); }

	/**
	 * @return true if the PropValue is already in the list
	 * @param propValue the PropValue
	 */
	public boolean contains( PropValue propValue ) { return list.contains( propValue ); }

	/** @return an PropValueList.Iterator object for this list */
	public PropValueList.Iterator iterator() { return this.new DefaultIterator(); }

	//-------------------------------------------------------------------------
	// Inner interfaces
	//

	/**
	 * Simple interface to iterate through the list
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 * @since 1.3 (SF 2K meeting)
	 */
	class DefaultIterator implements PropValueList.Iterator
	{ 
		//---------------------------------------------------------------------
		// Ctor(s)
		//

		/** Default ctor */
		DefaultIterator() { iterator = list.iterator(); }

		//---------------------------------------------------------------------
		// Public methods
		//

		/** @return the next PropValue in the iterator */
		public PropValue next() { return iterator.next(); }

		/** @return true if there is a next PropValue in the iterator */
		public boolean hasNext() { return iterator.hasNext(); }

		//---------------------------------------------------------------------
		// Instance variables
		//

		private final java.util.Iterator<PropValue> iterator;
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private List<PropValue> list = new ArrayList<>();
}
