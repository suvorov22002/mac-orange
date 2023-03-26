
package com.banktowallet.b2w.b2w;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.banktowallet.b2w.b2w._1.HeaderResponse;
import com.banktowallet.b2w.b2w._1.TransactionDetail;


/**
 * <p>Classe Java pour GetMiniStatementResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="GetMiniStatementResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="mmHeaderInfo" type="{http://b2w.banktowallet.com/b2w/1.0}HeaderResponse"/>
 *                   &lt;element name="TransactionList" type="{http://b2w.banktowallet.com/b2w/1.0}TransactionDetail" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "GetMiniStatementResponse", propOrder = {
    "_return"
})
public class GetMiniStatementResponse {

    @XmlElement(name = "return")
    protected GetMiniStatementResponse.Return _return;

    /**
     * Obtient la valeur de la propriété return.
     * 
     * @return
     *     possible object is
     *     {@link GetMiniStatementResponse.Return }
     *     
     */
    public GetMiniStatementResponse.Return getReturn() {
        return _return;
    }

    /**
     * Définit la valeur de la propriété return.
     * 
     * @param value
     *     allowed object is
     *     {@link GetMiniStatementResponse.Return }
     *     
     */
    public void setReturn(GetMiniStatementResponse.Return value) {
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
     *         &lt;element name="TransactionList" type="{http://b2w.banktowallet.com/b2w/1.0}TransactionDetail" maxOccurs="unbounded" minOccurs="0"/>
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
        "transactionList"
    })
    public static class Return {

        @XmlElement(required = true)
        protected HeaderResponse mmHeaderInfo;
        @XmlElement(name = "TransactionList")
        protected List<TransactionDetail> transactionList;

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
         * Gets the value of the transactionList property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the transactionList property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTransactionList().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TransactionDetail }
         * 
         * 
         */
        public List<TransactionDetail> getTransactionList() {
            if (transactionList == null) {
                transactionList = new ArrayList<TransactionDetail>();
            }
            return this.transactionList;
        }

		/**
		 * @param transactionList the transactionList to set
		 */
		public void setTransactionList(List<TransactionDetail> transactionList) {
			this.transactionList = transactionList;
		}
        
        

    }

}
