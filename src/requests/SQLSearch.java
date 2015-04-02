package requests;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modele.Disease;
import modele.Element;
import modele.Medic;
import modele.Merger;

import org.w3c.dom.Document;

import exceptions.NotFoundException;

public class SQLSearch {

	static String DB_SERVER = "jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr/";
	static String DB = "gmd";
	static String DRIVER = "com.mysql.jdbc.Driver";
	static String USER_NAME = "gmd-read";
	static String USER_PSWD = "esial";

	private Medic medic;
	private Disease disease;
	private Document dom;
	private ArrayList<Element> list;
	private String path = "";
	private String Msearch = "";
	private String Dsearch = "";
	private String Indications = "";
	private String Adverse_effects = "";
	private Merger merger;
	

	public SQLSearch(String Msearch, String Dsearch) {

		this.setMsearch(Msearch);
		this.setDsearch(Dsearch);
		this.setPath(path);
		this.merger = new Merger();
		this.list = new ArrayList<Element>();
		this.medic = new Medic();
		medic.setName(Msearch);
		this.disease = new Disease();
		disease.setName(Dsearch);
	}

	private void parseDocument() throws NotFoundException{
		org.w3c.dom.Element docEle = dom.getDocumentElement();
		ArrayList<Medic> med_res = new ArrayList<Medic>();
		ArrayList<Disease> dis_res = new ArrayList<Disease>();
		if (!Dsearch.equals("")) {
			dis_res = searchByDisease();
//				writerD(dis_res);
		}
		if (!Msearch.equals("")) {
			med_res = searchByDrug();
//				writerM(med_res);
		}
		if (dis_res.size() == 0 && med_res.size() == 0) {
			NotFoundException e = new NotFoundException();
			throw e;
		}else {
//				list = merger.mergeFile("_byM.txt", "_byD.txt"); 
//				deleteTempFile();
			list = merger.testMerge(med_res, dis_res);
		}
	}
	
	public Medic searchByDrug(){
		System.out.println("coucou");
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> List1 = new ArrayList<String>();
		String myQuery1 = "SELECT DISTINCT a.se_name FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label AND l.drug_name1 = " + Msearch + " OR i.label = l.label AND i.label = a.label AND l.drug_name2 = " + Msearch + " ;";

		ArrayList<String> List2 = new ArrayList<String>();
		String myQuery2 = "SELECT DISTINCT i.i_name FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label AND l.drug_name1 = " + Msearch + " OR i.label = l.label AND i.label = a.label AND l.drug_name2 = " + Msearch + " ;";

		try {
			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			Statement st = con.createStatement();
			ResultSet res1 = st.executeQuery(myQuery1);
			ResultSet res2 = st.executeQuery(myQuery2);
			
			while(res1.next()){
				medic.getCause().add(res1.getString("se_name"));
				System.out.println(res1.getString("se_name"));
			}

			while(res2.next()){
				medic.getTreat().add(res2.getString("i_name"));
				System.out.println(res2.getString("i_name"));

			}			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return medic;
	}

	public ArrayList<Disease> searchByDisease(){
		ArrayList<Disease> drug = new ArrayList<Disease>() ;
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> List1 = new ArrayList<String>();		
		String myQuery1 = "SELECT DISTINCT i.i_name, l.drug_name1, l.drug_name2, a.se_name FROM label_mapping l, indications_raw i WHERE i.label = l.label AND i.i_name = " + Dsearch + ";";

		try {
			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			Statement st = con.createStatement();
			ResultSet res1 = st.executeQuery(myQuery1);

			while(res1.next()){
				disease = new Disease();
				if (res1.getString("drug_name1").equals("")) {
					disease.setName(res1.getString("drug_name2"));
					}else {
					disease.setName(res1.getString("drug_name1"));
					disease.getSynonym().add(res1.getString("drug_name2"));
					}
				drug.add(disease);
				System.out.println(res1.getString("i_name"));
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return disease;
	}



		//	public SQLSearch(){
		//		
		//		File file = new File("drug.csv");
		////		FileWriter fw;
		//		
		//		
		//		try {
		////			fw = new FileWriter(file);
		//			
		//			Class.forName(DRIVER);
		//			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
		//			
		//			String myQuery = "SELECT * FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label;";
		//			Statement st = con.createStatement();
		//			ResultSet res = st.executeQuery(myQuery);
		//			
		//			while(res.next()){
		////				int id = res.getInt("id");
		////				String nom2 = res.getString("drug_name2");
		//				String se_cui = res.getString("a.se_cui");
		//				String i_cui = res.getString("i.i_cui");
		//				String label = res.getString("l.label");
		////				int age = res.getInt("age");
		////				String ville = res.getString("ville");
		//				
		//				fw.write(label+","/*+nom+","+prenom+","+age+","+tel+","+ville+","+email+*/"\n");
		//				System.out.println(label+ ", " +i_cui+ ", " +se_cui);
		//
		//			}
		//			//fw.close();
		//		} catch (Exception e) {
		//			// TODO: handle exception
		//			e.printStackTrace();
		//		}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMsearch() {
		return Msearch;
	}

	public void setMsearch(String msearch) {
		Msearch = msearch;
	}

	public String getDsearch() {
		return Dsearch;
	}

	public void setDsearch(String dsearch) {
		Dsearch = dsearch;
	}



}
