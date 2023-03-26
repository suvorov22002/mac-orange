
package com.banktowallet.b2w.b2w._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.afb.dpd.orangemoney.jsf.tools.OMTools;


/**
 * <p>Classe Java pour TransactionDetail complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TransactionDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tranRefNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tranDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="tranType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ccy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="crDr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="narration" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionDetail", propOrder = {
    "tranRefNo",
    "tranDate",
    "tranType",
    "ccy",
    "crDr",
    "amount",
    "narration"
})
public class TransactionDetail {

    @XmlElement(required = true)
    protected String tranRefNo;
    
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected String tranDate;
    
    @XmlElement(required = true)
    protected String tranType;
    
    @XmlElement(required = true)
    protected String ccy;
    
    @XmlElement(required = true)
    protected String crDr;
    
    protected double amount;
    @XmlElement(required = true)
    protected String narration;
    
    
    public TransactionDetail(){
		super();
	}
    

    /**
	 * @param tranRefNo
	 * @param tranDate
	 * @param tranType
	 * @param ccy
	 * @param crDr
	 * @param amount
	 * @param narration
	 */
	public TransactionDetail(com.afb.dpd.orangemoney.jpa.entities.TransactionDetail det ) {
		super();
		this.tranRefNo = det.getTranRefNo();
		this.tranType = det.getTranType();
		this.tranDate = DateFormatUtils.format(det.getTranDate(),OMTools.datepatern);
		this.ccy = det.getCcy();
		this.crDr = det.getCrDr();
		this.amount = det.getAmount();
		this.narration = det.getNarration();
	}

	/**
     * Obtient la valeur de la propri�t� tranRefNo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranRefNo() {
        return tranRefNo;
    }

    /**
     * D�finit la valeur de la propri�t� tranRefNo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranRefNo(String value) {
        this.tranRefNo = value;
    }

    /**
     * Obtient la valeur de la propri�t� tranDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getTranDate() {
        return tranDate;
    }

    /**
     * D�finit la valeur de la propri�t� tranDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTranDate(String value) {
        this.tranDate = value;
    }

    /**
     * Obtient la valeur de la propri�t� tranType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * D�finit la valeur de la propri�t� tranType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranType(String value) {
        this.tranType = value;
    }

    /**
     * Obtient la valeur de la propri�t� ccy.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcy() {
        return ccy;
    }

    /**
     * D�finit la valeur de la propri�t� ccy.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcy(String value) {
        this.ccy = value;
    }

    /**
     * Obtient la valeur de la propri�t� crDr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrDr() {
        return crDr;
    }

    /**
     * D�finit la valeur de la propri�t� crDr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrDr(String value) {
        this.crDr = value;
    }

    /**
     * Obtient la valeur de la propri�t� amount.
     * 
     */
    public double getAmount() {
        return amount;
    }

    /**
     * D�finit la valeur de la propri�t� amount.
     * 
     */
    public void setAmount(double value) {
        this.amount = value;
    }

    /**
     * Obtient la valeur de la propri�t� narration.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarration() {
        return narration;
    }

    /**
     * D�finit la valeur de la propri�t� narration.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarration(String value) {
        this.narration = value;
    }

}
