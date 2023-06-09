package com.afb.dpd.orangemoney.jpa.tools;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

public class bkmvtiPush implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;


	public bkmvtiPush() {}

	private Long id;
	
	private String age ="00001"; // Agence CHAR 5 1
	private String dev = "001"; // Code devise CHAR 3 3
	private String cha; // Chapitre comptable CHAR 10 4
	private String ncp; // Num�ro de compte CHAR 11 6
	private String suf = OgMoHelper.padText("", OgMoHelper.RIGHT, 2, " "); // Suffixe compte CHAR 2 7
	private String ope; // Code op�ration CHAR 3 5
	private String mvti = OgMoHelper.padText("", OgMoHelper.RIGHT, 6, " "); // Num�ro de mouvement CHAR 6
	private String rgp = OgMoHelper.padText("", OgMoHelper.RIGHT, 3, " "); // Code regroupement pour �dition CHAR 3
	private String uti = "AUTO"; // Code utilisateur CHAR 10
	private String eve; // Num�ro d'�v�nement CHAR 6
	private String clc; // Cl� de contr�le compte CHAR 2
	@JsonIgnore
	private String dco; // Date comptable DATE 2
	private String ser = OgMoHelper.padText("", OgMoHelper.RIGHT, 4, " "); // Code service CHAR 4
	@JsonIgnore
	private String dva; // Date de valeur DATE
	private Double mon; // Montant DECIMAL 19 - 4
	private String sen; // Sens CHAR 1 "D" = D�bit "C" = Cr�dit
	private String lib; // Libell� CHAR 30
	private String exo = "N"; // Exon�ration de commission de mouvement CHAR 1 "O" = Oui "N" = Non
	private String pie = OgMoHelper.padText("", OgMoHelper.RIGHT, 11, " "); // Num�ro de pi�ce CHAR 11
	private String rlet = OgMoHelper.padText("", OgMoHelper.RIGHT, 8, " "); // R�f�rence de lettrage CHAR 8
	private String des1 = OgMoHelper.padText("", OgMoHelper.RIGHT, 4, " "); // Code d�saccord 1 CHAR 4
	private String des2 = OgMoHelper.padText("", OgMoHelper.RIGHT, 4, " "); // Code d�saccord 2 CHAR 4
	private String des3 = OgMoHelper.padText("", OgMoHelper.RIGHT, 4, " "); // Code d�saccord 3 CHAR 4
	private String des4 = OgMoHelper.padText("", OgMoHelper.RIGHT, 4, " "); // Code d�saccord 4 CHAR 4
	private String des5 = OgMoHelper.padText("", OgMoHelper.RIGHT, 4, " "); // Code d�saccord 5 CHAR 4
	private String utf = OgMoHelper.padText("", OgMoHelper.RIGHT, 10, " "); // Code utilisateur ayant rappel� pour forcer CHAR 10
	private String uta = OgMoHelper.padText("", OgMoHelper.RIGHT, 12, " "); // Code utilisateur ayant autoris� CHAR 12
	private Double tau = 1d; // Taux de change DECIMAL 15 - 7
	@JsonIgnore
	private String din; // Date d'indisponible DATE
	private String tpr = OgMoHelper.padText("", OgMoHelper.RIGHT, 12, " "); // Zone utilis�e sp�cifiquement CHAR 12
	private Double npr = 0d; // Zone utilis�e sp�cifiquement DECIMAL 12 - 0
	private String ncc = OgMoHelper.padText("", OgMoHelper.RIGHT, 11, " "); // Num�ro de compte de rattachement CHAR 11
	private String suc = OgMoHelper.padText("", OgMoHelper.RIGHT, 2, " "); // Suffixe compte de rattachement CHAR 2
	private String esi = " "; // Zone utilis�e sp�cifiquement CHAR 1
	private String imp = "O"; // Calcul date de valeur mouvement inter-agences CHAR 1 "O" = Oui "N" = Non
	private String cta = "O"; // Mouvement � envoyer en agence CHAR 1 "O" = Oui "N" = Non
	private String mar = " "; // Code de mise � jour arr�t�s CHAR 1
	@JsonIgnore
	private String dech; // Date d'�ch�ance DATE
	private String agsa = "00001"; // Code agence de saisie CHAR 5
	private String agde; // Code agence destinatrice (permet d'identifier la CHAR 5
	private String devc = "001"; // Code devise d'origine CHAR 3
	private Double mctv = 0d; // Montant d'origine DECIMAL 19 - 4
	private String pieo = OgMoHelper.padText("", OgMoHelper.RIGHT, 11, " "); // Num�ro de pi�ce comptable d'origine CHAR 11
	private String iden = OgMoHelper.padText("", OgMoHelper.RIGHT, 6, " "); // Code identifiant CHAR 6
	private String lang = OgMoHelper.padText("", OgMoHelper.RIGHT, 3, " "); // Code langue CHAR 3
	private String libnls = OgMoHelper.padText("", OgMoHelper.RIGHT, 30, " "); // Libell� mouvement autre langue CHAR 30
	private String modu = OgMoHelper.padText("", OgMoHelper.RIGHT, 3, " "); // Code module VARCHAR 0 - 3
	private String refdos = " "; // R�f�rence du dossier VARCHAR 0 - 50
	private String refana = OgMoHelper.padText("", OgMoHelper.RIGHT, 25, " "); // R�f�rence analytique CHAR 25
	private String label = OgMoHelper.padText("ORANGE MONEY WALET2BANK", OgMoHelper.RIGHT, 25, " "); // Label du mouvement VARCHAR 0 - 25
	private String nat = OgMoHelper.padText("", OgMoHelper.RIGHT, 6, " "); // Nature de transaction VARCHAR 0 - 6
	private String eta = "VA"; // Code �tat de l'�v�nement VARCHAR 0 - 2
	private String destana = OgMoHelper.padText("", OgMoHelper.RIGHT, 30, " "); // Destination analytique du mouvement VARCHAR 0 - 30
	private String fusion = " "; // Code mouvement fusionn� CHAR 1
	
	private Long version;
	
	
	/**
	 * @param age
	 * @param dev
	 * @param cha
	 * @param ncp
	 * @param suf
	 * @param ope
	 * @param mvti
	 * @param rgp
	 * @param uti
	 * @param eve
	 * @param clc
	 * @param dco
	 * @param ser
	 * @param dva
	 * @param mon
	 * @param sen
	 * @param lib
	 * @param exo
	 * @param pie
	 * @param rlet
	 * @param des1
	 * @param des2
	 * @param des3
	 * @param des4
	 * @param des5
	 * @param utf
	 * @param uta
	 * @param tau
	 * @param din
	 * @param tpr
	 * @param npr
	 * @param ncc
	 * @param suc
	 * @param esi
	 * @param imp
	 * @param cta
	 * @param mar
	 * @param dech
	 * @param agsa
	 * @param agde
	 * @param devc
	 * @param mctv
	 * @param pieo
	 * @param iden
	 * @param lang
	 * @param libnls
	 * @param modu
	 * @param refdos
	 * @param refana
	 * @param label
	 * @param nat
	 * @param eta
	 * @param destana
	 * @param fusion
	 */
	public bkmvtiPush(bkmvti mvt) {
		super();
		this.age = mvt.getAge();
		this.dev = mvt.getDev();
		this.cha = mvt.getCha();
		this.ncp = mvt.getNcp();
		this.suf = mvt.getSuf();
		this.ope = mvt.getOpe();
		this.mvti = mvt.getMvti();
		this.rgp = mvt.getRgp();
		this.uti = mvt.getUti();
		this.eve = mvt.getEve();
		this.clc = mvt.getClc();
		this.dco = new SimpleDateFormat("yyyy-MM-dd").format(mvt.getDco());
		this.din = new SimpleDateFormat("yyyy-MM-dd").format(mvt.getDin());
		this.dva = new SimpleDateFormat("yyyy-MM-dd").format(mvt.getDva());
		this.dech = new SimpleDateFormat("yyyy-MM-dd").format(mvt.getDech());
		//this.dco = dco;
		this.ser = mvt.getSer();
		this.mon = mvt.getMon();
		this.sen = mvt.getSen();
		this.lib = mvt.getLib();
		this.exo = mvt.getExo();
		this.pie = mvt.getPie();
		this.rlet = mvt.getRlet();
		this.des1 = mvt.getDes1();
		this.des2 = mvt.getDes2();
		this.des3 = mvt.getDes3();
		this.des4 = mvt.getDes4();
		this.des5 = mvt.getDes5();
		this.utf = mvt.getUtf();
		this.uta = mvt.getUta();
		this.tau = mvt.getTau();
		this.tpr = mvt.getTpr();
		this.npr = mvt.getNpr();
		this.ncc = mvt.getNcc();
		this.suc = mvt.getSuc();
		this.esi = mvt.getEsi();
		this.imp = mvt.getImp();
		this.cta = mvt.getCta();
		this.mar = mvt.getMar();
		this.agsa = mvt.getAgsa();
		this.agde = mvt.getAgde();
		this.devc = mvt.getDevc();
		this.mctv = mvt.getMctv();
		this.pieo = mvt.getPieo();
		this.iden = mvt.getIden();
		this.lang = mvt.getLang();
		this.libnls = mvt.getLibnls();
		this.modu = mvt.getModu();
		this.refdos = mvt.getRefdos();
		this.refana = mvt.getRefana();
		this.label = mvt.getLabel();
		this.nat = mvt.getNat();
		this.eta = mvt.getEta();
		this.destana = mvt.getDestana();
		this.fusion = mvt.getFusion();
		
		System.out.println("*************** this.dco dva din dech *************** : " + this.dco + " -- " + this.dva + " -- " + this.din + " -- " + this.dech);
		
	}

	@JsonIgnore
	public String getSaveQuery() {
		return "INSERT INTO BKMVTI (AGE, DEV, CHA, NCP, SUF, OPE,MVTI, RGP, UTI,EVE, CLC, DCO, SER, DVA, MON,SEN, LIB, EXO,PIE, RLET, DES1,DES2, DES3, DES4, " + 
				"DES5, UTF, UTA, TAU, DIN, TPR, NPR, NCC, SUC, ESI, IMP, CTA,MAR, DECH, AGSA,AGDE, DEVC, MCTV, PIEO, IDEN, LANG, LIBNLS, MODU, REFDOS, " +
				"REFANA, LABEL, NAT, ETA, DESTANA, FUSION) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	
	@JsonIgnore
	public String getFileLine(){
		String noseq = "";
		String line = "";
		String agem = "";
		String schema = "";
		String ceticpt = "";
				
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		line = age + "|" + dev + "|" + cha + "|" + ncp + "|" + suf + "|" + ope + "|" + mvti + "|" + rgp + "|" + uti + "|" + eve + "|" + clc + "|" + sdf.format(dco) + "|" + ser + "|" + (dva != null ? sdf.format(dva) : "(null)") + "|" + String.valueOf(mon).replace(".", ",") + "|" + sen + "|" + lib + "|" + exo + "|" + pie + "|" + rlet + "|" + des1 + "|" + des2 + "|" + des3 + "|" + des4 + "|" + des5 + "|" + utf + "|" + uta + "|" + String.valueOf(tau).replace(".", ",") + "|" + (din == null ? "(null)" : sdf.format(din)) + "|" + tpr + "|" + String.valueOf(npr).replace(".", ",") + "|" + ncc + "|" + suc + "|" + esi + "|" + imp + "|" + cta + "|" + mar + "|" + (dech == null ? "(null)" : sdf.format(dech)) + "|" + agsa + "|" + agem + "|" + agde + "|" + devc + "|" + String.valueOf(mctv).replace(".", ",") + "|" + pieo + "|" + iden + "|" + noseq + "|" + lang + "|" + libnls + "|" + modu + "|" + refdos + "|" + refana + "|" + label + "|" + nat + "|" + eta + "|" + schema +  "|" + ceticpt + "|" + destana + "|" + fusion + "|";
		return line;
	}
	
	@JsonIgnore
	public Object[] getQueryValues() {
			
		Object[] values = new Object[54];
		
		values[0] = age;
		values[1] = dev;
		values[2] = cha;
		values[3] = ncp;
		values[4] = suf;
		values[5] = ope;
		values[6] = mvti;
		values[7] = rgp;
		values[8] = uti;
		values[9] = eve;
		values[10] = clc;
		values[11] = dco;
		values[12] = ser;
		values[13] = dva;
		values[14] = mon;
		values[15] = sen;
		values[16] = lib;
		values[17] = exo;
		values[18] = pie;
		values[19] = rlet;
		values[20] = des1;
		values[21] = des2;
		values[22] = des3;
		values[23] = des4;
		values[24] = des5;
		values[25] = utf;
		values[26] = uta;
		values[27] = tau;
		values[28] = din;
		values[29] = tpr;
		values[30] = npr;
		values[31] = ncc;
		values[32] = suc;
		values[33] = esi;
		values[34] = imp;
		values[35] = cta;
		values[36] = mar;
		values[37] = dech;
		values[38] = agsa;
		values[39] = agde;
		values[40] = devc;
		values[41] = mctv;
		values[42] = pieo;
		values[43] = iden;
		values[44] = lang;
		values[45] = libnls;
		values[46] = modu;
		values[47] = refdos;
		values[48] = refana;
		values[49] = label;
		values[50] = nat;
		values[51] = eta;
		values[52] = destana;
		values[53] = fusion;
		
		return values;
	
	}
	
	
	
	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}
	/**
	 * @return the dev
	 */
	public String getDev() {
		return dev;
	}
	/**
	 * @param dev the dev to set
	 */
	public void setDev(String dev) {
		this.dev = dev;
	}
	/**
	 * @return the cha
	 */
	public String getCha() {
		return cha;
	}
	/**
	 * @param cha the cha to set
	 */
	public void setCha(String cha) {
		this.cha = cha;
	}
	/**
	 * @return the ncp
	 */
	public String getNcp() {
		return ncp;
	}
	/**
	 * @param ncp the ncp to set
	 */
	public void setNcp(String ncp) {
		this.ncp = ncp;
	}
	/**
	 * @return the suf
	 */
	public String getSuf() {
		return suf;
	}
	/**
	 * @param suf the suf to set
	 */
	public void setSuf(String suf) {
		this.suf = suf;
	}
	/**
	 * @return the ope
	 */
	public String getOpe() {
		return ope;
	}
	/**
	 * @param ope the ope to set
	 */
	public void setOpe(String ope) {
		this.ope = ope;
	}
	/**
	 * @return the mvti
	 */
	public String getMvti() {
		return mvti;
	}
	/**
	 * @param mvti the mvti to set
	 */
	public void setMvti(String mvti) {
		this.mvti = mvti;
	}
	/**
	 * @return the rgp
	 */
	public String getRgp() {
		return rgp;
	}
	/**
	 * @param rgp the rgp to set
	 */
	public void setRgp(String rgp) {
		this.rgp = rgp;
	}
	/**
	 * @return the uti
	 */
	public String getUti() {
		return uti;
	}
	/**
	 * @param uti the uti to set
	 */
	public void setUti(String uti) {
		this.uti = uti;
	}
	/**
	 * @return the eve
	 */
	public String getEve() {
		return eve;
	}
	/**
	 * @param eve the eve to set
	 */
	public void setEve(String eve) {
		this.eve = eve;
	}
	/**
	 * @return the clc
	 */
	public String getClc() {
		return clc;
	}
	/**
	 * @param clc the clc to set
	 */
	public void setClc(String clc) {
		this.clc = clc;
	}
	/**
	 * @return the dco
	 */
	public String getDco() {
		return dco;
	}
	/**
	 * @param dco the dco to set
	 */
	public void setDco(String dco) {
		this.dco = dco;
	}
	/**
	 * @return the ser
	 */
	public String getSer() {
		return ser;
	}
	/**
	 * @param ser the ser to set
	 */
	public void setSer(String ser) {
		this.ser = ser;
	}
	/**
	 * @return the dva
	 */
	public String getDva() {
		return dva;
	}
	/**
	 * @param dva the dva to set
	 */
	public void setDva(String dva) {
		this.dva = dva;
	}
	/**
	 * @return the mon
	 */
	public Double getMon() {
		return mon;
	}
	/**
	 * @param mon the mon to set
	 */
	public void setMon(Double mon) {
		this.mon = mon;
	}
	/**
	 * @return the sen
	 */
	public String getSen() {
		return sen;
	}
	/**
	 * @param sen the sen to set
	 */
	public void setSen(String sen) {
		this.sen = sen;
	}
	/**
	 * @return the lib
	 */
	public String getLib() {
		return lib;
	}
	/**
	 * @param lib the lib to set
	 */
	public void setLib(String lib) {
		this.lib = lib;
	}
	/**
	 * @return the exo
	 */
	public String getExo() {
		return exo;
	}
	/**
	 * @param exo the exo to set
	 */
	public void setExo(String exo) {
		this.exo = exo;
	}
	/**
	 * @return the pie
	 */
	public String getPie() {
		return pie;
	}
	/**
	 * @param pie the pie to set
	 */
	public void setPie(String pie) {
		this.pie = pie;
	}
	/**
	 * @return the rlet
	 */
	public String getRlet() {
		return rlet;
	}
	/**
	 * @param rlet the rlet to set
	 */
	public void setRlet(String rlet) {
		this.rlet = rlet;
	}
	/**
	 * @return the des1
	 */
	public String getDes1() {
		return des1;
	}
	/**
	 * @param des1 the des1 to set
	 */
	public void setDes1(String des1) {
		this.des1 = des1;
	}
	/**
	 * @return the des2
	 */
	public String getDes2() {
		return des2;
	}
	/**
	 * @param des2 the des2 to set
	 */
	public void setDes2(String des2) {
		this.des2 = des2;
	}
	/**
	 * @return the des3
	 */
	public String getDes3() {
		return des3;
	}
	/**
	 * @param des3 the des3 to set
	 */
	public void setDes3(String des3) {
		this.des3 = des3;
	}
	/**
	 * @return the des4
	 */
	public String getDes4() {
		return des4;
	}
	/**
	 * @param des4 the des4 to set
	 */
	public void setDes4(String des4) {
		this.des4 = des4;
	}
	/**
	 * @return the des5
	 */
	public String getDes5() {
		return des5;
	}
	/**
	 * @param des5 the des5 to set
	 */
	public void setDes5(String des5) {
		this.des5 = des5;
	}
	/**
	 * @return the utf
	 */
	public String getUtf() {
		return utf;
	}
	/**
	 * @param utf the utf to set
	 */
	public void setUtf(String utf) {
		this.utf = utf;
	}
	/**
	 * @return the uta
	 */
	public String getUta() {
		return uta;
	}
	/**
	 * @param uta the uta to set
	 */
	public void setUta(String uta) {
		this.uta = uta;
	}
	/**
	 * @return the tau
	 */
	public Double getTau() {
		return tau;
	}
	/**
	 * @param tau the tau to set
	 */
	public void setTau(Double tau) {
		this.tau = tau;
	}
	/**
	 * @return the din
	 */
	public String getDin() {
		return din;
	}
	/**
	 * @param din the din to set
	 */
	public void setDin(String din) {
		this.din = din;
	}
	/**
	 * @return the tpr
	 */
	public String getTpr() {
		return tpr;
	}
	/**
	 * @param tpr the tpr to set
	 */
	public void setTpr(String tpr) {
		this.tpr = tpr;
	}
	/**
	 * @return the npr
	 */
	public Double getNpr() {
		return npr;
	}
	/**
	 * @param npr the npr to set
	 */
	public void setNpr(Double npr) {
		this.npr = npr;
	}
	/**
	 * @return the ncc
	 */
	public String getNcc() {
		return ncc;
	}
	/**
	 * @param ncc the ncc to set
	 */
	public void setNcc(String ncc) {
		this.ncc = ncc;
	}
	/**
	 * @return the suc
	 */
	public String getSuc() {
		return suc;
	}
	/**
	 * @param suc the suc to set
	 */
	public void setSuc(String suc) {
		this.suc = suc;
	}
	/**
	 * @return the esi
	 */
	public String getEsi() {
		return esi;
	}
	/**
	 * @param esi the esi to set
	 */
	public void setEsi(String esi) {
		this.esi = esi;
	}
	/**
	 * @return the imp
	 */
	public String getImp() {
		return imp;
	}
	/**
	 * @param imp the imp to set
	 */
	public void setImp(String imp) {
		this.imp = imp;
	}
	/**
	 * @return the cta
	 */
	public String getCta() {
		return cta;
	}
	/**
	 * @param cta the cta to set
	 */
	public void setCta(String cta) {
		this.cta = cta;
	}
	/**
	 * @return the mar
	 */
	public String getMar() {
		return mar;
	}
	/**
	 * @param mar the mar to set
	 */
	public void setMar(String mar) {
		this.mar = mar;
	}
	/**
	 * @return the dech
	 */
	public String getDech() {
		return dech;
	}
	/**
	 * @param dech the dech to set
	 */
	public void setDech(String dech) {
		this.dech = dech;
	}
	/**
	 * @return the agsa
	 */
	public String getAgsa() {
		return agsa;
	}
	/**
	 * @param agsa the agsa to set
	 */
	public void setAgsa(String agsa) {
		this.agsa = agsa;
	}
	/**
	 * @return the agde
	 */
	public String getAgde() {
		return agde;
	}
	/**
	 * @param agde the agde to set
	 */
	public void setAgde(String agde) {
		this.agde = agde;
	}
	/**
	 * @return the devc
	 */
	public String getDevc() {
		return devc;
	}
	/**
	 * @param devc the devc to set
	 */
	public void setDevc(String devc) {
		this.devc = devc;
	}
	/**
	 * @return the mctv
	 */
	public Double getMctv() {
		return mctv;
	}
	/**
	 * @param mctv the mctv to set
	 */
	public void setMctv(Double mctv) {
		this.mctv = mctv;
	}
	/**
	 * @return the pieo
	 */
	public String getPieo() {
		return pieo;
	}
	/**
	 * @param pieo the pieo to set
	 */
	public void setPieo(String pieo) {
		this.pieo = pieo;
	}
	/**
	 * @return the iden
	 */
	public String getIden() {
		return iden;
	}
	/**
	 * @param iden the iden to set
	 */
	public void setIden(String iden) {
		this.iden = iden;
	}
	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	/**
	 * @return the libnls
	 */
	public String getLibnls() {
		return libnls;
	}
	/**
	 * @param libnls the libnls to set
	 */
	public void setLibnls(String libnls) {
		this.libnls = libnls;
	}
	/**
	 * @return the modu
	 */
	public String getModu() {
		return modu;
	}
	/**
	 * @param modu the modu to set
	 */
	public void setModu(String modu) {
		this.modu = modu;
	}
	/**
	 * @return the refdos
	 */
	public String getRefdos() {
		return refdos;
	}
	/**
	 * @param refdos the refdos to set
	 */
	public void setRefdos(String refdos) {
		this.refdos = refdos;
	}
	/**
	 * @return the refana
	 */
	public String getRefana() {
		return refana;
	}
	/**
	 * @param refana the refana to set
	 */
	public void setRefana(String refana) {
		this.refana = refana;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the nat
	 */
	public String getNat() {
		return nat;
	}
	/**
	 * @param nat the nat to set
	 */
	public void setNat(String nat) {
		this.nat = nat;
	}
	/**
	 * @return the eta
	 */
	public String getEta() {
		return eta;
	}
	/**
	 * @param eta the eta to set
	 */
	public void setEta(String eta) {
		this.eta = eta;
	}
	/**
	 * @return the destana
	 */
	public String getDestana() {
		return destana;
	}
	/**
	 * @param destana the destana to set
	 */
	public void setDestana(String destana) {
		this.destana = destana;
	}
	/**
	 * @return the fusion
	 */
	public String getFusion() {
		return fusion;
	}
	/**
	 * @param fusion the fusion to set
	 */
	public void setFusion(String fusion) {
		this.fusion = fusion;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public String getFormattedDCO() {
		return PortalHelper.DEFAULT_DATE_FORMAT.format(dco);
	}
	
	@JsonIgnore
	public String getFormattedMon() {
		return OgMoHelper.espacement(mon);
	}
	
	
	public Long getVersion() {
		return version;
	}
	
	
}
