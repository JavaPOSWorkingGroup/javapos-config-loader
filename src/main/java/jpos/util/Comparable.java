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

/**
 * Interface defining a comparable object
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @version 1.2.0 (JDK 1.1.x)
 */
public interface Comparable
{
    /**
     * Compares this and other arguments for order
     * @param other object to compare to
     * @return same as defined by {@link Comparable#compareTo(Object)}
     */
    public int compareTo( Object other );

    /**
     * Indicates this object is "equal to" the other 
     * @param other object to compare to
     * @return same as defined by {@link Object#equals(Object)}
     */
    public boolean equals( Object other );
}
