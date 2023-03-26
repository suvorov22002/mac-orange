
package com.banktowallet.b2w.b2w;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.banktowallet.b2w.b2w._1.HeaderRequest;


/**
 * <p>Classe Java pour CancelTransfer complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CancelTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TranRequestInfo" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderRequest"/>
 *                   &lt;element name="externalRefNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelTransfer", propOrder = {
    "tranRequestInfo"
})
public class CancelTransfer {

    @XmlElement(name = "TranRequestInfo")
    protected CancelTransfer.TranRequestInfo tranRequestInfo;

    /**
     * Obtient la valeur de la propriété tranRequestInfo.
     * 
     * @return
     *     possible object is
     *     {@link CancelTransfer.TranRequestInfo }
     *     
     */
    public CancelTransfer.TranRequestInfo getTranRequestInfo() {
        return tranRequestInfo;
    }

    /**
     * Définit la valeur de la propriété tranRequestInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelTransfer.TranRequestInfo }
     *     
     */
    public void setTranRequestInfo(CancelTransfer.TranRequestInfo value) {
        this.tranRequestInfo = value;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderRequest"/>
     *         &lt;element name="externalRefNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "mmHeaderInfo",
        "externalRefNo"
    })
    public static class TranRequestInfo {

        @XmlElement(required = true)
        protected HeaderRequest mmHeaderInfo;
        @XmlElement(required = true)
        protected String externalRefNo;

        /**
         * Obtient la valeur de la propriété mmHeaderInfo.
         * 
         * @return
         *     possible object is
         *     {@link HeaderRequest }
         *     
         */
        public HeaderRequest getMmHeaderInfo() {
            return mmHeaderInfo;
        }

        /**
         * Définit la valeur de la propriété mmHeaderInfo.
         * 
         * @param value
         *     allowed object is
         *     {@link HeaderRequest }
         *     
         */
        public void setMmHeaderInfo(HeaderRequest value) {
            this.mmHeaderInfo = value;
        }

        /**
         * Obtient la valeur de la propriété externalRefNo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExternalRefNo() {
            return externalRefNo;
        }

        /**
         * Définit la valeur de la propriété externalRefNo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExternalRefNo(String value) {
            this.externalRefNo = value;
        }

    }

}
