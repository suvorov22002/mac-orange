
package com.banktowallet.b2w.b2w._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Classe Java pour HeaderResponse complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="HeaderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="operatorCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="affiliateCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="responseCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="responseMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderResponse", propOrder = {
    "operatorCode",
    "requestId",
    "affiliateCode",
    "responseCode",
    "responseMessage"
})
public class HeaderResponse {

    @XmlElement(required = true)
    protected String operatorCode;
    @XmlElement(required = true)
    protected String requestId;
    @XmlElement(required = true)
    protected String affiliateCode;
    @XmlElement(required = true)
    protected String responseCode;
    @XmlElement(required = true)
    protected String responseMessage;
    

    public HeaderResponse() {
		super();
	}
    
    public HeaderResponse(HeaderRequest req) {
		super();
		this.operatorCode = req.getOperatorCode();
		this.requestId = req.getRequestId();
		this.affiliateCode = req.getAffiliateCode();
	}

    /**
     * Obtient la valeur de la propri�t� operatorCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperatorCode() {
        return operatorCode;
    }

    /**
     * D�finit la valeur de la propri�t� operatorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperatorCode(String value) {
        this.operatorCode = value;
    }

    /**
     * Obtient la valeur de la propri�t� requestId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * D�finit la valeur de la propri�t� requestId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Obtient la valeur de la propri�t� affiliateCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAffiliateCode() {
        return affiliateCode;
    }

    /**
     * D�finit la valeur de la propri�t� affiliateCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAffiliateCode(String value) {
        this.affiliateCode = value;
    }

    /**
     * Obtient la valeur de la propri�t� responseCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * D�finit la valeur de la propri�t� responseCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Obtient la valeur de la propri�t� responseMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * D�finit la valeur de la propri�t� responseMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }

}
