
package com.banktowallet.b2w.b2w;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.banktowallet.b2w.b2w._1.HeaderResponse;


/**
 * <p>Classe Java pour AccountToWalletTransferResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AccountToWalletTransferResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderResponse"/>
 *                   &lt;element name="externalRefNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="CBAReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "AccountToWalletTransferResponse", propOrder = {
    "_return"
})
public class AccountToWalletTransferResponse {

    @XmlElement(name = "return")
    protected AccountToWalletTransferResponse.Return _return;

    /**
     * Obtient la valeur de la propriété return.
     * 
     * @return
     *     possible object is
     *     {@link AccountToWalletTransferResponse.Return }
     *     
     */
    public AccountToWalletTransferResponse.Return getReturn() {
        return _return;
    }

    /**
     * Définit la valeur de la propriété return.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountToWalletTransferResponse.Return }
     *     
     */
    public void setReturn(AccountToWalletTransferResponse.Return value) {
        this._return = value;
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
     *         &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderResponse"/>
     *         &lt;element name="externalRefNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="CBAReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "externalRefNo",
        "cbaReferenceNo"
    })
    public static class Return {

        @XmlElement(required = true)
        protected HeaderResponse mmHeaderInfo;
        @XmlElement(required = true)
        protected String externalRefNo;
        @XmlElement(name = "CBAReferenceNo", required = true)
        protected String cbaReferenceNo;

        /**
         * Obtient la valeur de la propriété mmHeaderInfo.
         * 
         * @return
         *     possible object is
         *     {@link HeaderResponse }
         *     
         */
        public HeaderResponse getMmHeaderInfo() {
            return mmHeaderInfo;
        }
        
        public static Return getInstance(){
        	return new Return(); 
        }

        /**
         * Définit la valeur de la propriété mmHeaderInfo.
         * 
         * @param value
         *     allowed object is
         *     {@link HeaderResponse }
         *     
         */
        public void setMmHeaderInfo(HeaderResponse value) {
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

        /**
         * Obtient la valeur de la propriété cbaReferenceNo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCBAReferenceNo() {
            return cbaReferenceNo;
        }

        /**
         * Définit la valeur de la propriété cbaReferenceNo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCBAReferenceNo(String value) {
            this.cbaReferenceNo = value;
        }

    }

}
