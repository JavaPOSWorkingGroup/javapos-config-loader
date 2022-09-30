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

/**
 * Defines an interface for JavaPOS device categories
 * @since 1.3 (SF 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public interface DevCat
{
	//-------------------------------------------------------------------------
	// Constants
	//

	/** Indicates the version of JavaPOS that these DevCat apply to */
	public static final String JPOS_VERSION_STRING = "1.15";

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the String representation of this DevCat */
	public String toString();

	/**
	 * Accepts a DevCat Visitor object
	 * @param visitor the DevCat Visitor object
	 */
	public void accept( DevCatVisitor visitor );

	//-------------------------------------------------------------------------
	// Inner interfaces
	//

	/** Defines the DevCat for Belt */
	public interface Belt extends DevCat {}

	/** Defines the DevCat for BillAcceptor */
	public interface BillAcceptor extends DevCat {}

	/** Defines the DevCat for BillDispenser */
	public interface BillDispenser extends DevCat {}
	
	/** Defines the DevCat for Biometrics */
	public interface Biometrics extends DevCat {} 

	/**
	 * Defines the DevCat for BumpBar
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface BumpBar extends DevCat {}
 
	/** Defines the DevCat for CashChanger */
	public interface CashChanger extends DevCat {}

	/**
	 * Defines the DevCat for POSPrinter
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface CashDrawer extends DevCat {}

	/**
	 * Defines the DevCat for CAT
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface CAT extends DevCat {}

	/** Defines the DevCat for CheckScanner */
	public interface CheckScanner extends DevCat {}

	/** Defines the DevCat for CoinAcceptor */
	public interface CoinAcceptor extends DevCat {}

	/**
	 * Defines the DevCat for CoinDispenser
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface CoinDispenser extends DevCat {}

	/** Defines the DevCat for ElectronicJournal */
	public interface ElectronicJournal extends DevCat {}

	/** Defines the DevCat for ElectronicValueRW */
	public interface ElectronicValueRW extends DevCat {}

	/**
	 * Defines the DevCat for FiscalPrinter
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface FiscalPrinter extends DevCat {}

	/** Defines the DevCat for Gate */
	public interface Gate extends DevCat {} 

	/**
	 * Defines the DevCat for HardTotals
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface HardTotals extends DevCat {}

	/** Defines the DevCat for ImageScanner */
	public interface ImageScanner extends DevCat {}

	/** Defines the DevCat for ItemDispenser */
	public interface ItemDispenser extends DevCat {}

	/**
	 * Defines the DevCat for Keylock
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface Keylock extends DevCat {}

	/** Defines the DevCat for Lights */
	public interface Lights extends DevCat {}

	/**
	 * Defines the DevCat for POSPrinter
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface LineDisplay extends DevCat {}

	/**
	 * Defines the DevCat for MICR
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien  (maxim@us.ibm.com)
	 */
	public interface MICR extends DevCat {}

	/** Defines the DevCat for MotionSensor */
	public interface MotionSensor extends DevCat {} 

	/**
	 * Defines the DevCat for MSR
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface MSR extends DevCat {}

	/**
	 * Defines the DevCat for Pinpad
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface Pinpad extends DevCat {}

	/** Defines the DevCat for PointCardRW */
	public interface PointCardRW extends DevCat {}

	/**
	 * Defines the DevCat for POSKeyboard
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface POSKeyboard extends DevCat {}

	/**
	 * Defines the DevCat for POSPower
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface POSPower extends DevCat {}

	/**
	 * Defines the DevCat for POSPrinter
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface POSPrinter extends DevCat {}

	/**
	 * Defines the DevCat for RemoteOrderDisplay
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface RemoteOrderDisplay extends DevCat {}

	/** Defines the DevCat for RFIDScanner */
	public interface RFIDScanner extends DevCat {}

	/** Defines the DevCat for Scale */
	public interface Scale extends DevCat {}

	/**
	 * Defines the DevCat for Scanner
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface Scanner extends DevCat {}

	/**
	 * Defines the DevCat for SignatureCapture
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface SignatureCapture extends DevCat {}

	/** Defines the DevCat for SmartCardRW */
	public interface SmartCardRW extends DevCat {}

	/**
	 * Defines the DevCat for ToneIndicator
	 * @since 1.3 (SF 2K meeting)
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 */
	public interface ToneIndicator extends DevCat {}
}
