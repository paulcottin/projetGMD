package requests;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modele.Element;
import modele.Medic;
import modele.Merger;

import org.w3c.dom.Document;

public class SQLSearch {

	static String DB_SERVER = "jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr/";
	static String DB = "gmd";
	static String DRIVER = "com.mysql.jdbc.Driver";
	static String USER_NAME = "gmd-read";
	static String USER_PSWD = "esial";
	
	private Document dom;
	private ArrayList<Element> list;
	private String path = "";
	private String Msearch = "";
	private String Indications = "";
	private String Adverse_effects = "";
	private Merger merger;
	
	public SQLSearch(String Msearch, String Dsearch, String path) {
		this.setMsearch(Msearch);
		this.setPath(path);
		this.merger = new Merger();
		this.list = new ArrayList<Element>();
	}
		
	private ArrayList<Medic> searchByDrug(){
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> List = new ArrayList<String>();
				
		String myQuery = "SELECT DISTINCT a.se_name FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label AND l.drug_name1 = "+ Msearch +" ;";
		
		ArrayList<String> List2 = new ArrayList<String>();
		String myQuery2 = "SELECT DISTINCT i.i_name FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label AND l.drug_name1 = "+ Msearch +" ;";
		
		try {
			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(myQuery);
			ResultSet res2 = st.executeQuery(myQuery2);
			
			while(res.next()){
				List.add(res.getString("a.se_name"));
			}

			while(res2.next()){
				List.add(res2.getString("i.i_name"));
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
		
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


	
}
