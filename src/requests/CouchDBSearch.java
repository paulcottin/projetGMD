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
	private String clinicalSigns, disease;

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
		this.clinicalSigns = "";
		this.disease = "";
		
	}

	public void search(){
		this.disease = getDiseases();
		this.clinicalSigns = getClinicalSigns();
		try {
			JSONObject docDiseases = new JSONObject(getDiseases());
			int nbLignesDisease = docDiseases.getInt("total_rows");
			
			JSONArray rows = docDiseases.getJSONArray("rows");
			for (int i = 5; i < nbLignesDisease; i++) {
				getDiseaseInfos(rows.getJSONObject(i));
			}
			getClinicalInfo(clinicalSigns);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	private String getDiseases(){
		String ligne = "";
		URL url = null;
		try {
			url = new URL("http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseases");
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

	private void getDiseaseInfos(JSONObject rep) throws JSONException{
		try {

			if (!rep.isNull("value")) {
				JSONObject val = rep.getJSONObject("value");
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

	public String getClinicalSigns(){
		String ligne = "";
		URL url = null;

		try {
			url = new URL("http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/clinicalsigns/_view/GetDiseaseClinicalSignsNoLang");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String res="";

		try {
			HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
			connexion.setDoOutput(true);
			BufferedReader rd = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
			ligne = "";

			while((ligne = rd.readLine()) != null){
				// System.out.println(ligne);
				res+=ligne;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void getClinicalInfo(String rep) throws JSONException{
		try {
			JSONObject jsonObject = new JSONObject(rep);
			JSONArray rows = (JSONArray) (jsonObject).get("rows");
			int size = rows.length();
			for(int i=0; i<size; i++){
				JSONObject row = (JSONObject) rows.get(i);

				JSONObject val = (JSONObject) row.get("value");

				JSONObject disease = (JSONObject) val.get("disease");
				JSONObject name = (JSONObject) disease.get("Name");
				String text_name =  String.valueOf(name.get("text"));
				
				if (text_name.equals(dSearch)) {
					Disease d = new Disease();
					d.setName(text_name);
					JSONObject cl_sign = (JSONObject) val.get("clinicalSign");
					JSONObject name_cl = (JSONObject) cl_sign.get("Name");
					String text_cl =  String.valueOf(name_cl.get("text"));
					d.getCause().add(text_cl);
					
					dList.add(d);
					System.out.println("Le Nom est : " + text_name + " et le symptome est : " + text_cl);
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	public String getdSearch() {
		return dSearch;
	}

	public void setdSearch(String dSearch) {
		this.dSearch = dSearch;
	}
}
