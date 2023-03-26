package com.afb.dpd.orangemoney.jpa.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date Comptable
 *
 * @author Francis
 * @version 1.0
 */
public class DateComptable {

    /**
         * date comptable jour 
         */
        private Date dateComptSuivant = new Date();
        
        /**
         * date comptable jour 
         */
        private Date dateComptJr = new Date();
        
        /**
         * Date Compatble veille
         */
        private Date dateComptPrecedent = new Date();
        
        /**
         * Date comptable  jour
         */
        private String txtdateComptSuivant = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        
        /**
         * Date comptable  jour
         */
        private String txtdateComptJr  = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        /**
         * Date comptable  veille
         */
        private String txtdateComptPrecedent  = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        
        /**
         * 
         */
        public DateComptable() {
                super();
        }

        /**
         * @param dateComptJr
         * @param dateComptveil
         */
        public DateComptable(Date dateComptJr, Date dateComptveil) {
                super();
                this.dateComptJr = dateComptJr;
                this.dateComptPrecedent = dateComptveil;
        }

        
        
        /**
         * @param dateComptJr
         * @param dateComptveil
         * @param txtdateComptJr
         * @param txtdateComptveil
         */
        public DateComptable(Date dateComptJr, Date dateComptveil,
                        String txtdateComptJr, String txtdateComptveil) {
                super();
                this.dateComptJr = dateComptJr;
                this.dateComptPrecedent = dateComptveil;
                this.txtdateComptJr = txtdateComptJr;
                this.txtdateComptPrecedent = txtdateComptveil;
        }

        /**
         * @return the txtdateComptJr
         */
        public String getTxtdateComptJr() {
                return txtdateComptJr;
        }

        /**
         * @param txtdateComptJr the txtdateComptJr to set
         */
        public void setTxtdateComptJr(String txtdateComptJr) {
                this.txtdateComptJr = txtdateComptJr;
        }

        /**
         * @return the txtdateComptPrecedent
         */
        public String getTxtdateComptPrecedent() {
                return txtdateComptPrecedent;
        }

        /**
         * @param txtdateComptPrecedent the txtdateComptPrecedent to set
         */
        public void setTxtdateComptPrecedent(String txtdateComptPrecedent) {
                this.txtdateComptPrecedent = txtdateComptPrecedent;
        }

        /**
         * @return the dateComptJr
         */
        public Date getDateComptJr() {
                return dateComptJr;
        }

        /**
         * @param dateComptJr the dateComptJr to set
         */
        public void setDateComptJr(Date dateComptJr) {
                this.dateComptJr = dateComptJr;
        }

        /**
         * @return the dateComptPrecedent
         */
        public Date getDateComptPrecedent() {
                return dateComptPrecedent;
        }

        /**
         * @param dateComptPrecedent the dateComptPrecedent to set
         */
        public void setDateComptPrecedent(Date dateComptPrecedent) {
                this.dateComptPrecedent = dateComptPrecedent;
        }
        
        /**
         * @return the dateComptSuivant
         */
        public Date getDateComptSuivant() {
                return dateComptSuivant;
        }

        /**
         * @param dateComptSuivant the dateComptSuivant to set
         */
        public void setDateComptSuivant(Date dateComptSuivant) {
                this.dateComptSuivant = dateComptSuivant;
        }

        /**
         * @return the txtdateComptSuivant
         */
        public String getTxtdateComptSuivant() {
            return txtdateComptSuivant;
        }

        /**
         * @param txtdateComptSuivant the txtdateComptSuivant to set
         */
        public void setTxtdateComptSuivant(String txtdateComptSuivant) {
                this.txtdateComptSuivant = txtdateComptSuivant;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
                return "DateComptable [dateComptJr=" + dateComptJr + ", dateComptveil="
                                + dateComptPrecedent + ", txtdateComptJr=" + txtdateComptJr
                                + ", txtdateComptveil=" + txtdateComptPrecedent + "]";
        }

}
