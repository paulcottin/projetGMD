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
import java.util.ArrayList;

import modele.Disease;
import modele.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CouchDBSearch {

	static String DB_SERVER = "http://couchdb.telecomnancy.univ-lorraine.fr";
	static String DB = "orphadatabase";
	static String USER_NAME = "benhouss1u";
	static String USER_PSWD = "CouchDB2A";

	private String dSearch;
	private ArrayList<Disease> dList;

	public CouchDBSearch() { 
		init();
	}

	public CouchDBSearch(String dSearch){
		init();
		this.dSearch = dSearch;
	}

	private void init(){
		this.dSearch = "";
		this.dList = new ArrayList<Disease>();
	}

	public void search(){
		int nbLines = getNbLines();
		for (int i = 5; i < nbLines-2; i++) {
			try {
				System.out.println(i);
				getInfos(getRep(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		System.out.println(dList.toString());
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
				//				System.out.println(ligne);
				res+=ligne;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}



	private void getInfos(String rep) throws JSONException{
		try {
			JSONObject jsonObject = new JSONObject(rep);
			JSONArray rows = jsonObject.getJSONArray("rows");

			if (!rows.isNull(0)) {
				JSONObject row = rows.getJSONObject(0);
				JSONObject val = row.getJSONObject("value");
				JSONObject name = val.getJSONObject("Name");
				String text_name =  name.getString("text");
				if (text_name.equals(dSearch)) {
					Disease d = new Disease();
					d.setName(text_name);
					ArrayList<String> synonyms = new ArrayList<String>();
					JSONObject synlist = val.getJSONObject("SynonymList");
					String count_syn =  synlist.getString("count");

					if (count_syn.equals("0")) {
						//Ne rien faire 
					}
					else if (count_syn.equals("1")) {
						JSONObject syns = (JSONObject) synlist.get("Synonym");
						String text =  syns.getString("text");
						//				System.out.println("Le Synonyme est : " + text + " et le Nom est : " + text_name);
						synonyms.add(text);
					}
					else{
						JSONArray syns = synlist.getJSONArray("Synonym");
						for(int i=0; i<Integer.parseInt(count_syn); i++){
							JSONObject syn = syns.getJSONObject(i);
							String text =  syn.getString("text");
							//											System.out.println("Le Synonyme est : " + text + " et le Nom est : " + text_name);
							synonyms.add(text);
						}
					}
					d.setSynonyms(synonyms);
					dList.add(d);
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

	public String getdSearch() {
		return dSearch;
	}

	public void setdSearch(String dSearch) {
		this.dSearch = dSearch;
	}
}
