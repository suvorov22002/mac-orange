package com.afb.dpd.orangemoney.jsf.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import com.afb.dpd.mobilemoney.dao.IOrangeMoneyDAOLocal;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public class OMTools {
	
	/*
	- register : https://b2w-ow-sb.orange-money.com/register?bic=BNKSXGWE

	- idle : https://b2w-ow-sb.orange-money.com/idle/?bic=BNKSXGWE

	- status inquiry : https://b2w-ow-sb.orange-money.com/status-inquiry?bic=BNKSXGWE

	 */
	

	// CCEICMCX
	//*** public static String BIC = "BNKSXGWE"; //test
	public static String BIC = "CCEICMCX"; //prod

	//*** public static String registerService = "https://b2w-ow-sb.orange-money.com/register/?bic="+BIC; //test 
	public static String registerService = "https://ws.b2w.om.prod/b2w/afriland/register/?bic="+BIC; // nouveau lien prod
		
	//*** public static String inquiryService = "https://b2w-ow-sb.orange-money.com/status-inquiry?bic="+BIC; //test
	public static String inquiryService = "https://ws.b2w.om.prod/b2w/afriland/status-inquiry/?bic="+BIC; // nouveau lien prod
	 											
	
	//*** public static String idlService = "https://b2w-ow-sb.orange-money.com/idle?bic="+BIC; //test
	public static String idlService = "https://ws.b2w.om.prod/b2w/afriland/idle/?bic="+BIC; // nouveau lien prod
	

	public static String requeryService = "";

	public static String  OK = "200";

	// 950
	// 952
	//*** public static String  currency = "952"; //test
	public static String  currency = "950"; //prod
	
	public static String  currencyXAF = "XAF";
	
	public static String  datepatern = "yyyy-MM-dd HH:mm:ss";
	
	public static String  monthpatern =  "MMMM";
	
	public static String  datepaternLong = "yyyy-MM-ddTHH:mm:ss.sssZ";
	
	// ORANGEMONEYCX
	// ORANGEMONEYSX
	//*** public static String operatorCode = "ORANGEMONEYSX"; //test
	public static String operatorCode = "ORANGEMONEYCX"; //prod
	
	// OMC
	// OML
	//*** public static String affiliateCode = "OML"; //test
	public static String affiliateCode = "OMC"; //prod
	
	public static short  All = 3;
	
	public static Integer bankPINLength = 22;

	// CCEICMCX
	// BNKSXGWE
	//*** public static String Allias = "BNKSXGWE"; //test
	public static String Allias = "CCEICMCX"; //prod
	
	
	
	public static String FACT_MAC = "MAC";
	public static String FACT_COM = "COM MAC";
	public static String FACT_TAX = "TAX MAC";
	
	public static String RGUL_MAC = "RGUL";
	public static String RGUL_COM = "COM RGUL";
	public static String RGUL_TAX = "TAX RGUL";
	
	
	/**
	 * Service de gestion des utilisateurs du portail
	 */
	public static IOrangeMoneyDAOLocal appDAOLocal;
	

	/**
	 * Converti le fichier passe en parametre en tableau d'octets 
	 * @param file
	 * @return
	 */
	public static byte[] getStreamDownloadResourceFile(File file) {

		byte[] data = null;

		try {

			// Si le fichier n'existe pas on sort
			if(!file.exists()) return data;

			// Obtention d'un InputStream du le fichier html genere
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			// Initilisation d'un flux de sortie
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// Initialisation d'un Byte
			int b;

			// Parcours du fichier et ecriture dans le flux de sortie
			while((b = bis.read()) != -1) baos.write(b);

			// On referme
			baos.flush();

			// Construction du tableau de byte a partir du flux de sortie
			data = baos.toByteArray();

			// Fermeture des flux
			bis.close();

		} catch (Exception e) {

			// On relance
			throw new RuntimeException(e);
		}

		return data;

	}


	/**
	 * Methode de generation d'un tableau d'octets de l'etat
	 * @param reportName URL du fichier jasper 
	 * @param map Parametres de l'etat
	 * @param maCollection Source de donnees de l'etat
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getReportPDFBytes(String reportName, HashMap<Object, Object> map, Collection<?> maCollection) throws Exception {

		try {
			
			map = new HashMap<Object, Object>();
			map.put("logoAFB", OMTools.getReportsDir()+"logoentete.png");
			map.put("logo", OMTools.getReportsDir()+"Logo-OrangeMoney.png");
			map.put("logoOgMo", OMTools.getReportsDir()+"OgMo_icon.png");
			map.put("SUBREPORT_DIR", OMTools.getReportsDir());
			map.put("codeUser", OMViewHelper.getSessionUser().getLogin());
			
			// Construction du JasperPrint
			JasperPrint jp = printReport(reportName, map, maCollection);
			// Construction du tableau de bytes
			//System.out.println("-------------getReportPDFBytes---jp------"+jp);
			byte[] element = JasperExportManager.exportReportToPdf(jp);
			//System.out.println("-------------getReportPDFBytes---element------"+element);
			return element;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}


	public static void exportReportToPDFFile(String reportName, HashMap<Object, Object> map, Collection<?> maCollection, String fileName) throws Exception {

		// Construction du JasperPrint
		JasperPrint jp = printReport(reportName, map, maCollection);

		// Construction du tableau de bytes
		JasperExportManager.exportReportToPdfFile(jp, fileName);
	}

	public static InputStream getLogoAFB() throws Exception {
		return new BufferedInputStream( OMTools.class.getClassLoader().getResourceAsStream("com/afb/dpd/mobilemoney/jsf/tools/logoentete.png") );
	}

	public static InputStream getLogoOgMo() throws Exception {
		return new BufferedInputStream( OMTools.class.getClassLoader().getResourceAsStream("com/afb/dpd/mobilemoney/jsf/tools/OgMo_icon.png") );
	}

	public static InputStream getLogoEntete() throws Exception {
		return new BufferedInputStream( OMTools.class.getClassLoader().getResourceAsStream("com/afb/dpd/mobilemoney/jsf/tools/Logo-OrangeMoney.png") );
	}

	/**
	 * Methode de construction de l'etat
	 * @param reportName
	 * @param map
	 * @param maCollection
	 * @return
	 * @throws Exception
	 */
	private static JasperPrint printReport(String reportName, HashMap<Object, Object> map, Collection<?> maCollection) throws Exception {

		// Construction de la source de donnees de l'etat
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(maCollection);

		// Construction de l'etat
		JasperPrint jp = null;
		try{
			//System.out.println("------------reportName-----------"+reportName);
			//System.out.println("------------dataSource-----------"+dataSource);
			//System.out.println("------------map-----------"+map);
			jp =  JasperFillManager.fillReport(reportName, map, dataSource);
			//System.out.println("------------reportName-----------"+reportName);
			//System.out.println("------------dataSource-----------"+dataSource);
			//System.out.println("------------map-----------"+map);
			//System.out.println("------------jp-----------"+jp);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jp;
	}

	
	
	public static String getReportsDir() {
		return PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_REPORTS_DIR + File.separator + OMViewHelper.APPLICATION_EAR + File.separator;
	}

	public static String getDownloadDir() {
		return PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator;
	}
	
	
	public static String getPiecesJointesDir() {
		return PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_REPORTS_DIR + File.separator + "OrangeMoney" + File.separator + "PJs" + File.separator;
	}
	
	
	
	
	/*
	public static String getBIC() {
		Parameters params = appDAOLocal.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
		if(StringUtils.isNotBlank(params.getBankBIC())) return params.getBankBIC();
		else return OMTools.BIC; 
	}
	
	
	public static String getLienRegister() {
		//**** IOrangeMoneyDAOLocal appDAOLocal;
		Log logger = LogFactory.getLog(ContextLoaderListener.class);
		Context ctx = null;
		try {

			// Initialisation du contexte JNDI
			ctx = new InitialContext();
			logger.info("Initialisation du Contexte OK XXXXXX!!!");

		} catch (Exception e) {
			// On relance l'exception
			throw new RuntimeException("Erreur lors de l'initialisation du Contexte JNDI XXXXXX", e);
		}

		try{

			appDAOLocal = (IOrangeMoneyDAOLocal)ctx.lookup(OMViewHelper.APPLICATION_EAR + "/" + IOrangeMoneyDAOLocal.SERVICE_NAME + "/local" );
			logger.info("Demarrage des services metiers OK XXXXXX !!!");

		}catch(Exception e){
			// On relance l'exception
			throw new RuntimeException("Erreur lors du chargement des Services Métiers XXXXXX", e);

		}
		
		Parameters params = appDAOLocal.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
		if(StringUtils.isNotBlank(params.getRegisterService())) return params.getRegisterService() + getBIC();
		else return OMTools.registerService; 
	}
	
	public static String getLienStatut() {
		Parameters params = appDAOLocal.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
		if(StringUtils.isNotBlank(params.getInquiryService())) return params.getInquiryService() + getBIC();
		else return OMTools.inquiryService; 
	}
	
	public static String getIdlService() {
		Parameters params = OMViewHelper.appDAOLocal.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
		if(StringUtils.isNotBlank(params.getIdlService())) return params.getIdlService() + getBIC();
		else return OMTools.idlService; 
	}
	*/
		
}
