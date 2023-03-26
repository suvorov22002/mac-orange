
package com.banktowallet.b2w.b2w;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.banktowallet.b2w.b2w._1.HeaderRequest;


/**
 * <p>Classe Java pour AccountToWalletTransfer complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AccountToWalletTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MobileTransferRequest" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderRequest"/>
 *                   &lt;element name="externalRefNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="mobileNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="mobileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="mobileAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="transferDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ccy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                   &lt;element name="charge" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                   &lt;element name="tranDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                   &lt;element name="udf1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="udf2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="udf3" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "AccountToWalletTransfer", propOrder = {
    "mobileTransferRequest"
})
public class AccountToWalletTransfer {

    @XmlElement(name = "MobileTransferRequest")
    protected AccountToWalletTransfer.MobileTransferRequest mobileTransferRequest;

    /**
     * Obtient la valeur de la propriété mobileTransferRequest.
     * 
     * @return
     *     possible object is
     *     {@link AccountToWalletTransfer.MobileTransferRequest }
     *     
     */
    public AccountToWalletTransfer.MobileTransferRequest getMobileTransferRequest() {
        return mobileTransferRequest;
    }

    /**
     * Définit la valeur de la propriété mobileTransferRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountToWalletTransfer.MobileTransferRequest }
     *     
     */
    public void setMobileTransferRequest(AccountToWalletTransfer.MobileTransferRequest value) {
        this.mobileTransferRequest = value;
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
     *         &lt;element name="mobileNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="mobileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="mobileAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="transferDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ccy" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *         &lt;element name="charge" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *         &lt;element name="tranDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *         &lt;element name="udf1" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="udf2" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="udf3" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "mobileNo",
        "mobileName",
        "mobileAlias",
        "accountNo",
        "accountAlias",
        "accountName",
        "transferDescription",
        "ccy",
        "amount",
        "charge",
        "tranDate",
        "udf1",
        "udf2",
        "udf3"
    })
    public static class MobileTransferRequest {

        @XmlElement(required = true)
        protected HeaderRequest mmHeaderInfo;
        @XmlElement(required = true)
        protected String externalRefNo;
        @XmlElement(required = true)
        protected String mobileNo;
        @XmlElement(required = true)
        protected String mobileName;
        @XmlElement(required = true)
        protected String mobileAlias;
        @XmlElement(required = true)
        protected String accountNo;
        @XmlElement(required = true)
        protected String accountAlias;
        @XmlElement(required = true)
        protected String accountName;
        @XmlElement(required = true)
        protected String transferDescription;
        @XmlElement(required = true)
        protected String ccy;
        protected double amount;
        protected double charge;
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar tranDate;
        @XmlElement(required = true)
        protected String udf1;
        @XmlElement(required = true)
        protected String udf2;
        @XmlElement(required = true)
        protected String udf3;

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

        /**
         * Obtient la valeur de la propriété mobileNo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMobileNo() {
            return mobileNo;
        }

        /**
         * Définit la valeur de la propriété mobileNo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMobileNo(String value) {
            this.mobileNo = value;
        }

        /**
         * Obtient la valeur de la propriété mobileName.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMobileName() {
            return mobileName;
        }

        /**
         * Définit la valeur de la propriété mobileName.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMobileName(String value) {
            this.mobileName = value;
        }

        /**
         * Obtient la valeur de la propriété mobileAlias.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMobileAlias() {
            return mobileAlias;
        }

        /**
         * Définit la valeur de la propriété mobileAlias.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMobileAlias(String value) {
            this.mobileAlias = value;
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
         * Obtient la valeur de la propriété accountName.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountName() {
            return accountName;
        }

        /**
         * Définit la valeur de la propriété accountName.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountName(String value) {
            this.accountName = value;
        }

        /**
         * Obtient la valeur de la propriété transferDescription.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransferDescription() {
            return transferDescription;
        }

        /**
         * Définit la valeur de la propriété transferDescription.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransferDescription(String value) {
            this.transferDescription = value;
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
         * Obtient la valeur de la propriété charge.
         * 
         */
        public double getCharge() {
            return charge;
        }

        /**
         * Définit la valeur de la propriété charge.
         * 
         */
        public void setCharge(double value) {
            this.charge = value;
        }

        /**
         * Obtient la valeur de la propriété tranDate.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTranDate() {
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
        public void setTranDate(XMLGregorianCalendar value) {
            this.tranDate = value;
        }

        /**
         * Obtient la valeur de la propriété udf1.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUdf1() {
            return udf1;
        }

        /**
         * Définit la valeur de la propriété udf1.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUdf1(String value) {
            this.udf1 = value;
        }

        /**
         * Obtient la valeur de la propriété udf2.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUdf2() {
            return udf2;
        }

        /**
         * Définit la valeur de la propriété udf2.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUdf2(String value) {
            this.udf2 = value;
        }

        /**
         * Obtient la valeur de la propriété udf3.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUdf3() {
            return udf3;
        }

        /**
         * Définit la valeur de la propriété udf3.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUdf3(String value) {
            this.udf3 = value;
        }

    }

}
