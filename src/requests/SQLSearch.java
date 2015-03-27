package requests;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLSearch {

	static String DB_SERVER = "jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr/";
	static String DB = "gmd";
	static String DRIVER = "com.mysql.jdbc.Driver";
	static String USER_NAME = "gmd-read";
	static String USER_PSWD = "esial";
	
	
	public SQLSearch(){
		
		File file = new File("drug.csv");
//		FileWriter fw;
		
		
		try {
//			fw = new FileWriter(file);
			
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			
			String myQuery = "SELECT * FROM label_mapping l, adverse_effects_raw a, indications_raw i WHERE i.label = l.label AND i.label = a.label;";
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(myQuery);
			
			while(res.next()){
//				int id = res.getInt("id");
//				String nom2 = res.getString("drug_name2");
				String se_cui = res.getString("a.se_cui");
				String i_cui = res.getString("i.i_cui");
				String label = res.getString("l.label");
//				int age = res.getInt("age");
//				String ville = res.getString("ville");
				
				//fw.write(label+","/*+nom+","+prenom+","+age+","+tel+","+ville+","+email+*/"\n");
				System.out.println(label+ ", " +i_cui+ ", " +se_cui);

			}
			//fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
