
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
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
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
     * Obtient la valeur de la propri�t� mobileTransferRequest.
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
     * D�finit la valeur de la propri�t� mobileTransferRequest.
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
     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
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
         * Obtient la valeur de la propri�t� mmHeaderInfo.
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
         * D�finit la valeur de la propri�t� mmHeaderInfo.
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
         * Obtient la valeur de la propri�t� externalRefNo.
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
         * D�finit la valeur de la propri�t� externalRefNo.
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
         * Obtient la valeur de la propri�t� mobileNo.
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
         * D�finit la valeur de la propri�t� mobileNo.
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
         * Obtient la valeur de la propri�t� mobileName.
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
         * D�finit la valeur de la propri�t� mobileName.
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
         * Obtient la valeur de la propri�t� mobileAlias.
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
         * D�finit la valeur de la propri�t� mobileAlias.
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
         * Obtient la valeur de la propri�t� accountNo.
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
         * D�finit la valeur de la propri�t� accountNo.
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
         * Obtient la valeur de la propri�t� accountAlias.
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
         * D�finit la valeur de la propri�t� accountAlias.
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
         * Obtient la valeur de la propri�t� accountName.
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
         * D�finit la valeur de la propri�t� accountName.
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
         * Obtient la valeur de la propri�t� transferDescription.
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
         * D�finit la valeur de la propri�t� transferDescription.
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
         * Obtient la valeur de la propri�t� charge.
         * 
         */
        public double getCharge() {
            return charge;
        }

        /**
         * D�finit la valeur de la propri�t� charge.
         * 
         */
        public void setCharge(double value) {
            this.charge = value;
        }

        /**
         * Obtient la valeur de la propri�t� tranDate.
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
         * D�finit la valeur de la propri�t� tranDate.
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
         * Obtient la valeur de la propri�t� udf1.
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
         * D�finit la valeur de la propri�t� udf1.
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
         * Obtient la valeur de la propri�t� udf2.
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
         * D�finit la valeur de la propri�t� udf2.
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
         * Obtient la valeur de la propri�t� udf3.
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
         * D�finit la valeur de la propri�t� udf3.
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
