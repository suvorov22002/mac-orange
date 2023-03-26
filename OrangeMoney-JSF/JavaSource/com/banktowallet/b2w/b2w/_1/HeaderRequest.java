
package com.banktowallet.b2w.b2w._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour HeaderRequest complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="HeaderRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="operatorCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestToken" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="affiliateCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderRequest", propOrder = {
    "operatorCode",
    "requestId",
    "requestToken",
    "requestType",
    "affiliateCode"
})
public class HeaderRequest {

    @XmlElement(required = true)
    protected String operatorCode;
    @XmlElement(required = true)
    protected String requestId;
    @XmlElement(required = true)
    protected String requestToken;
    @XmlElement(required = true)
    protected String requestType;
    @XmlElement(required = true)
    protected String affiliateCode;
    
    
    public HeaderRequest() {
		super();
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
     * Obtient la valeur de la propri�t� requestToken.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestToken() {
        return requestToken;
    }

    /**
     * D�finit la valeur de la propri�t� requestToken.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestToken(String value) {
        this.requestToken = value;
    }

    /**
     * Obtient la valeur de la propri�t� requestType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * D�finit la valeur de la propri�t� requestType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
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

}
