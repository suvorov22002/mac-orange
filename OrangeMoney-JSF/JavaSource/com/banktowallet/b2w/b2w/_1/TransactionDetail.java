
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
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
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
     * Obtient la valeur de la propriété tranRefNo.
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
     * Définit la valeur de la propriété tranRefNo.
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
     * Obtient la valeur de la propriété tranDate.
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
     * Définit la valeur de la propriété tranDate.
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
     * Obtient la valeur de la propriété tranType.
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
     * Définit la valeur de la propriété tranType.
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
     * Obtient la valeur de la propriété ccy.
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
     * Définit la valeur de la propriété ccy.
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
     * Obtient la valeur de la propriété crDr.
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
     * Définit la valeur de la propriété crDr.
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
     * Obtient la valeur de la propriété amount.
     * 
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Définit la valeur de la propriété amount.
     * 
     */
    public void setAmount(double value) {
        this.amount = value;
    }

    /**
     * Obtient la valeur de la propriété narration.
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
     * Définit la valeur de la propriété narration.
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
