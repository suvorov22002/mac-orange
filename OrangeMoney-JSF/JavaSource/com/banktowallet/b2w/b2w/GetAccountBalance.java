
package com.banktowallet.b2w.b2w;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.banktowallet.b2w.b2w._1.HeaderRequest;


/**
 * <p>Classe Java pour GetAccountBalance complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="GetAccountBalance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountBalanceInquiryRequest" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderRequest"/>
 *                   &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "GetAccountBalance", propOrder = {
    "accountBalanceInquiryRequest"
})
public class GetAccountBalance {

    @XmlElement(name = "AccountBalanceInquiryRequest")
    protected GetAccountBalance.AccountBalanceInquiryRequest accountBalanceInquiryRequest;

    /**
     * Obtient la valeur de la propriété accountBalanceInquiryRequest.
     * 
     * @return
     *     possible object is
     *     {@link GetAccountBalance.AccountBalanceInquiryRequest }
     *     
     */
    public GetAccountBalance.AccountBalanceInquiryRequest getAccountBalanceInquiryRequest() {
        return accountBalanceInquiryRequest;
    }

    /**
     * Définit la valeur de la propriété accountBalanceInquiryRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAccountBalance.AccountBalanceInquiryRequest }
     *     
     */
    public void setAccountBalanceInquiryRequest(GetAccountBalance.AccountBalanceInquiryRequest value) {
        this.accountBalanceInquiryRequest = value;
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
     *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "accountNo",
        "accountAlias",
        "reason"
    })
    public static class AccountBalanceInquiryRequest {

        @XmlElement(required = true)
        protected HeaderRequest mmHeaderInfo;
        @XmlElement(required = true)
        protected String accountNo;
        @XmlElement(required = true)
        protected String accountAlias;
        @XmlElement(required = true)
        protected String reason;

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
         * Obtient la valeur de la propriété accountNo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountNo() {
            return accountNo;
        }

        /**
         * Définit la valeur de la propriété accountNo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountNo(String value) {
            this.accountNo = value;
        }

        /**
         * Obtient la valeur de la propriété accountAlias.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountAlias() {
            return accountAlias;
        }

        /**
         * Définit la valeur de la propriété accountAlias.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountAlias(String value) {
            this.accountAlias = value;
        }

        /**
         * Obtient la valeur de la propriété reason.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReason() {
            return reason;
        }

        /**
         * Définit la valeur de la propriété reason.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReason(String value) {
            this.reason = value;
        }

    }

}
