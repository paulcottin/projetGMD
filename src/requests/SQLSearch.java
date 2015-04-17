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
		this.medic.setName(Msearch);
		this.disease = new Disease();
		this.disease.setName(Dsearch);
	}

	public ArrayList<Element> search(){
		list.clear();
		if (!Dsearch.equals("")) {
			for (Element element : merger.DiseaseToElement(searchByDisease())) {
				list.add(element);
			}
		}
		if (!Msearch.equals("")) {
			for (Element element : merger.MedicToElement(searchByDrug())) {
				list.add(element);
			}
		}
		return list;
	}

	private ArrayList<Medic> searchByDrug(){
		ArrayList<Medic> list = new ArrayList<Medic>();
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String> List1 = new ArrayList<String>();
		String myQuery1 = "SELECT DISTINCT a.se_name, i.i_name FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label AND l.drug_name1 LIKE \"" + Msearch + "\" OR i.label = l.label AND i.label = a.label AND l.drug_name2 LIKE \"" + Msearch + "\" ;";
		int cpt = 0;
		try {
			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			Statement st = con.createStatement();

			ResultSet res1 = st.executeQuery(myQuery1);

			while(res1.next()){
				cpt++;
				medic.getCause().add(res1.getString("se_name"));
				medic.getTreat().add(res1.getString("i_name"));
			}
			res1.close();
			
			medic.setName(Msearch);
			medic.setOrigin("Sider2");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (cpt > 0) {
			list.add(medic);
		}
		return list;
	}

	private ArrayList<Disease> searchByDisease(){
		ArrayList<Disease> drug = new ArrayList<Disease>() ;
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String> List1 = new ArrayList<String>();		
		String myQuery1 = "SELECT DISTINCT i.i_name, l.drug_name1, l.drug_name2 FROM label_mapping l, indications_raw i WHERE i.label = l.label AND i.i_name LIKE \"" + Dsearch.toUpperCase() + "\" ORDER BY l.drug_name1;";

		try {
			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			Statement st = con.createStatement();
			ResultSet res1 = st.executeQuery(myQuery1);

			ArrayList<String> drugName;
			String name1 = "", name2 = "", diseaseName = "";
			while(res1.next()){
				drugName = new ArrayList<String>();
				name1 = res1.getString("drug_name1");
				name2 = res1.getString("drug_name2");
				diseaseName = res1.getString("i_name");
				if (!name1.equals("")) {
					drugName.add(res1.getString("drug_name1"));
				}
				if (!name2.equals("")) {
					drugName.add(res1.getString("drug_name2"));
				}
				drug.add(new Disease(diseaseName, drugName, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), "Sider2"));
//				System.out.println(res1.getString("i_name"));
			}
			res1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return drug;
	}

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
