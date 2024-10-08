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
 * Defines a default a Visitor interface for the DevCat hiearchy
 * @since 1.3 (SF 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class DefaultDevCatV implements DevCatVisitor
{
	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * Visits a  Belt DevCat
	 * @param devCat the DevCat
	 */
	public void visitBelt( DevCat devCat ) {}
	
	/**
	 * Visits a  BillAcceptor DevCat
	 * @param devCat the DevCat
	 */
	public void visitBillAcceptor( DevCat devCat ) {}
	
	/**
	 * Visits a  BillDispenser DevCat
	 * @param devCat the DevCat
	 */
	public void visitBillDispenser( DevCat devCat ) {}

	/**
	 * Visits a  Biometrics DevCat
	 * @param devCat the DevCat
	 */
	public void visitBiometrics( DevCat devCat ) {}
	
	/**
	 * Visits a  DevCat
	 * @param devCat the DevCat
	 */
	public void visitBumpBar( DevCat devCat ) {}

	/**
	 * Visits a CashChanger DevCat
	 * @param devCat the DevCat
	 */
	public void visitCashChanger( DevCat devCat ) {}

	/**
	 * Visits a CashDrawer DevCat
	 * @param devCat the DevCat
	 */
	public void visitCashDrawer( DevCat devCat ) {}

	/**
	 * Visits a CheckScanner DevCat
	 * @param devCat the DevCat
	 */
	public void visitCheckScanner( DevCat devCat ) {}

	/**
	 * Visits a CAT DevCat
	 * @param devCat the DevCat
	 */
	public void visitCAT( DevCat devCat ) {}
	
	/**
	 * Visits a CoinAcceptor DevCat
	 * @param devCat the DevCat
	 */
	public void visitCoinAcceptor( DevCat devCat ) {}

	/**
	 * Visits a CoinDispenser DevCat
	 * @param devCat the DevCat
	 */
	public void visitCoinDispenser( DevCat devCat ) {}

	/**
	 * Visits a ElectronicJournal DevCat
	 * @param devCat the DevCat
	 */
	public void visitElectronicJournal( DevCat devCat ) {}
	
	/**
	 * Visits a ElectronicValueRW DevCat
	 * @param devCat the DevCat
	 */
	public void visitElectronicValueRW( DevCat devCat ) {}
	
	/**
	 * Visits a FiscalPrinter DevCat
	 * @param devCat the DevCat
	 */
	public void visitFiscalPrinter( DevCat devCat ) {}

	/**
	 * Visits a Gate DevCat
	 * @param devCat the DevCat
	 */
	public void visitGate( DevCat devCat ) {}
	
	/**
	 * Visits a HardTotals DevCat
	 * @param devCat the DevCat
	 */
	public void visitHardTotals( DevCat devCat ) {}
	
	/**
	 * Visits a ItemDispenser DevCat
	 * @param devCat the DevCat
	 */
	public void visitItemDispenser( DevCat devCat ) {}
	
	/**
	 * Visits a ImageScanner DevCat
	 * @param devCat the DevCat
	 */
	public void visitImageScanner( DevCat devCat ) {}

	/**
	 * Visits a Keylock DevCat
	 * @param devCat the DevCat
	 */
	public void visitKeylock( DevCat devCat ) {}
	
	/**
	 * Visits a Lights DevCat
	 * @param devCat the DevCat
	 */
	public void visitLights( DevCat devCat ) {}
	
	
	/**
	 * Visits a LineDisplay DevCat
	 * @param devCat the DevCat
	 */
	public void visitLineDisplay( DevCat devCat ) {}

	/**
	 * Visits a MICR DevCat
	 * @param devCat the DevCat
	 */
	public void visitMICR( DevCat devCat ) {}

	/**
	 * Visits a MotionSensor DevCat
	 * @param devCat the DevCat
	 */
	public void visitMotionSensor( DevCat devCat ) {}

	/**
	 * Visits a MSR DevCat
	 * @param devCat the DevCat
	 */
	public void visitMSR( DevCat devCat ) {}

	/**
	 * Visits a Pinpad DevCat
	 * @param devCat the DevCat
	 */
	public void visitPinpad( DevCat devCat ) {}

	/**
	 * Visits a POSKeyboard DevCat
	 * @param devCat the DevCat
	 */
	public void visitPOSKeyboard( DevCat devCat ) {}

	/**
	 * Visits a POSPower DevCat
	 * @param devCat the DevCat
	 */
	public void visitPOSPower( DevCat devCat ) {}

	/**
	 * Visits a POSPrinter DevCat
	 * @param devCat the DevCat
	 */
	public void visitPOSPrinter( DevCat devCat ) {}

	/**
	 * Visits a RemoteOrderDisplay DevCat
	 * @param devCat the DevCat
	 */
	public void visitRemoteOrderDisplay( DevCat devCat ) {}

	/**
	 * Visits a RFIDScanner DevCat
	 * @param devCat the DevCat
	 */
	public void visitRFIDScanner( DevCat devCat ) {}
	
	/**
	 * Visits a Scanner DevCat
	 * @param devCat the DevCat
	 */
	public void visitScanner( DevCat devCat ) {}

	/**
	 * Visits a SignatureCapture DevCat
	 * @param devCat the DevCat
	 */
	public void visitSignatureCapture( DevCat devCat ) {}

	/**
	 * Visits a Scale DevCat
	 * @param devCat the DevCat
	 */
	public void visitScale( DevCat devCat ) {}
	
	/**
	 * Visits a SmartCardRW DevCat
	 * @param devCat the DevCat
	 */
	public void visitSmartCardRW( DevCat devCat ) {}

	/**
	 * Visits a ToneIndicator DevCat
	 * @param devCat the DevCat
	 */
	public void visitToneIndicator( DevCat devCat ) {}

	/**
	 * Visits a PointCardRW DevCat
	 * @param devCat the DevCat
	 */
	public void visitPointCardRW( DevCat devCat ) {}	
}
