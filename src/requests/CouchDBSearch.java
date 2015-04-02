package requests;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CouchDBSearch {

	static String DB_SERVER = "http://couchdb.telecomnancy.univ-lorraine.fr";
	static String DB = "orphadatabase";
	static String USER_NAME = "benhouss1u";
	static String USER_PSWD = "CouchDB2A";

	public CouchDBSearch() { 
		int nbLines = getNbLines();
		for (int i = 5; i < nbLines; i++) {
			try {
				getInfos(getRep(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	private String getRep(int i){
		String ligne = "";
		URL url = null;
		try {
			url = new URL("http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseases?key="+i);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		String res="";

		try {
			HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
			connexion.setDoOutput(true);
			BufferedReader rd = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
			ligne = "";

			while((ligne = rd.readLine()) != null){
				System.out.println(ligne);
				res+=ligne;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}



	public void getInfos(String rep) throws JSONException{
		try {
			JSONObject jsonObject = new JSONObject(rep);
			JSONArray rows = (JSONArray) (jsonObject).get("rows");
			JSONObject row = (JSONObject) rows.get(0);
			JSONObject val = (JSONObject) row.get("value");
			JSONObject name = (JSONObject) val.get("Name");
			String text_name =  String.valueOf(name.get("text"));
			JSONObject synlist = (JSONObject) val.get("SynonymList");
			String count_syn =  String.valueOf(synlist.get("count"));

			if (count_syn.equals("1")) {
				JSONObject syns = (JSONObject) synlist.get("Synonym");
				String text =  String.valueOf(syns.get("text"));
				System.out.println("Le Synonyme est : " + text + " et le Nom est : " + text_name);
			}else{
				JSONArray syns = (JSONArray) synlist.getJSONArray("Synonym");
				for(int i=0; i<Integer.parseInt(count_syn); i++){
					JSONObject syn = (JSONObject) syns.getJSONObject(i);
					String text =  String.valueOf(syn.get("text"));
					System.out.println("Le Synonyme est : " + text + " et le Nom est : " + text_name);
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
	
	private int getNbLines(){
		URL url = null;
		try {
			url = new URL("http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseases");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		int cpt = 0;
		try {
			HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
			connexion.setDoOutput(true);
			BufferedReader rd = new BufferedReader(new InputStreamReader(connexion.getInputStream()));

			while(rd.readLine() != null){
				cpt++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cpt;
	}
}
