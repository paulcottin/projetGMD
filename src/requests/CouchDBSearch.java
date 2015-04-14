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
import modele.Element;
import modele.Merger;
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
	private Merger merger;

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
		this.merger = new Merger();

	}

	public ArrayList<Element> search(){
		this.disease = getDisease();
//		this.clinicalSigns = getClinicalSigns();
		try {
			getDiseaseInfos(this.disease);
//			getClinicalInfo(this.clinicalSigns);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
//		System.out.println("dlist results : "+dList.toString());
		return merger.DiseaseToElement(dList);
	}

	private String getDisease(){
		String ligne = "";
		URL url = null;
		String cle_bis = dSearch.replace(" ","%20");

		try {
			url = new URL("http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseasesByName?key=\""+cle_bis+"\"");
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
				//	System.out.println(ligne);
				res+=ligne;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void getDiseaseInfos(String rep) throws JSONException{
		Disease d = new Disease();
		try {
			JSONObject jsonObject = new JSONObject(rep);
			JSONArray rows = (JSONArray) (jsonObject).get("rows");
			int size = rows.length();
			for(int k=0; k<size; k++){
				JSONObject row = (JSONObject) rows.get(k);
				JSONObject val = (JSONObject) row.get("value");
				JSONObject name = (JSONObject) val.get("Name");
				String text_name =  String.valueOf(name.get("text"));
				JSONObject synlist = (JSONObject) val.get("SynonymList");
				String count_syn =  String.valueOf(synlist.get("count"));
				d.setName(text_name);
				d.setOrigin("OrphaData");
				if (count_syn.equals("0")) {
//					System.out.println("Le Nom est : " + text_name + "\nEt il n'a pas de Synonymes");		
				} else if (count_syn.equals("1")) {
					JSONObject syns = (JSONObject) synlist.get("Synonym");
					String text =  String.valueOf(syns.get("text"));
					d.getSynonyms().add(text);
//					System.out.println("Le Nom est : " + text_name + "\nEt le Synonyme est : " + text );
				}else{
					JSONArray syns = (JSONArray) synlist.getJSONArray("Synonym");
					String liste="";
					for(int i=0; i<Integer.parseInt(count_syn); i++){
						JSONObject syn = (JSONObject) syns.getJSONObject(i);
						String text =  String.valueOf(syn.get("text"));
						d.getSynonyms().add(text);
						liste+="\n\t"+text;
					}
//					System.out.println("Le Nom est : " + text_name + "\nEt les Synonymes sont : " + liste);
				}
				dList.add(d);
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
//		System.out.println("ajout dlist : "+d.toString());
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
					d.setOrigin("OrphaData");
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
