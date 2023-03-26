
package com.banktowallet.b2w.b2w;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.banktowallet.b2w.b2w._1.HeaderResponse;


/**
 * <p>Classe Java pour GetAccountBalanceResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="GetAccountBalanceResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderResponse"/>
 *                   &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ccy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="branchCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="availableBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                   &lt;element name="currentBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                   &lt;element name="accountType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accountStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "GetAccountBalanceResponse", propOrder = {
    "_return"
})
public class GetAccountBalanceResponse {

    @XmlElement(name = "return")
    protected GetAccountBalanceResponse.Return _return;

    /**
     * Obtient la valeur de la propriété return.
     * 
     * @return
     *     possible object is
     *     {@link GetAccountBalanceResponse.Return }
     *     
     */
    public GetAccountBalanceResponse.Return getReturn() {
        return _return;
    }

    /**
     * Définit la valeur de la propriété return.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAccountBalanceResponse.Return }
     *     
     */
    public void setReturn(GetAccountBalanceResponse.Return value) {
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
     *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountAlias" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ccy" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="branchCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="availableBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *         &lt;element name="currentBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *         &lt;element name="accountType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accountStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "accountNo",
        "accountAlias",
        "accountName",
        "ccy",
        "branchCode",
        "availableBalance",
        "currentBalance",
        "accountType",
        "accountClass",
        "accountStatus",
        "udf1",
        "udf2",
        "udf3"
    })
    public static class Return {

        @XmlElement(required = true)
        protected HeaderResponse mmHeaderInfo;
        @XmlElement(required = true)
        protected String accountNo;
        @XmlElement(required = true)
        protected String accountAlias;
        @XmlElement(required = true)
        protected String accountName;
        @XmlElement(required = true)
        protected String ccy;
        @XmlElement(required = true)
        protected String branchCode;
        protected double availableBalance;
        protected double currentBalance;
        @XmlElement(required = true)
        protected String accountType;
        @XmlElement(required = true)
        protected String accountClass;
        @XmlElement(required = true)
        protected String accountStatus;
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
         *     {@link HeaderResponse }
         *     
         */
        public HeaderResponse getMmHeaderInfo() {
            return mmHeaderInfo;
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
         * Obtient la valeur de la propriété branchCode.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBranchCode() {
            return branchCode;
        }

        /**
         * Définit la valeur de la propriété branchCode.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBranchCode(String value) {
            this.branchCode = value;
        }

        /**
         * Obtient la valeur de la propriété availableBalance.
         * 
         */
        public double getAvailableBalance() {
            return availableBalance;
        }

        /**
         * Définit la valeur de la propriété availableBalance.
         * 
         */
        public void setAvailableBalance(double value) {
            this.availableBalance = value;
        }

        /**
         * Obtient la valeur de la propriété currentBalance.
         * 
         */
        public double getCurrentBalance() {
            return currentBalance;
        }

        /**
         * Définit la valeur de la propriété currentBalance.
         * 
         */
        public void setCurrentBalance(double value) {
            this.currentBalance = value;
        }

        /**
         * Obtient la valeur de la propriété accountType.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountType() {
            return accountType;
        }

        /**
         * Définit la valeur de la propriété accountType.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountType(String value) {
            this.accountType = value;
        }

        /**
         * Obtient la valeur de la propriété accountClass.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountClass() {
            return accountClass;
        }

        /**
         * Définit la valeur de la propriété accountClass.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountClass(String value) {
            this.accountClass = value;
        }

        /**
         * Obtient la valeur de la propriété accountStatus.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountStatus() {
            return accountStatus;
        }

        /**
         * Définit la valeur de la propriété accountStatus.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountStatus(String value) {
            this.accountStatus = value;
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
