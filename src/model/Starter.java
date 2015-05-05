package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import requests.SQLSearch;

public class Starter implements Runnable {

	ArrayList<Synonyms> synsList;
	String option;

	public Starter(String option) {
		init();
		this.option = option;
	}

	@Override
	public void run() {
		if (option.equals("CSV")) {
			try {
				 synsList = initCSVSynonyms();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (option.equals("SQL")) {
			synsList = initSQLSynonyms();
		}
		else if (option.equals("XML")) {
			synsList = initXMLSynonyms();
		}
		else if (option.equals("CouchDB")) {
			try {
				synsList = initCouchDBSynonyms();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void init(){
		synsList = new ArrayList<Synonyms>();
	}

	private ArrayList<Synonyms> initCouchDBSynonyms() throws JSONException{
		ArrayList<Synonyms> list = new ArrayList<Synonyms>();
		String ligne = "";
		URL url = null;

		try {
			url = new URL("http://couchdb.telecomnancy.univ-lorraine.fr/orphadatabase/_design/diseases/_view/GetDiseasesByName");
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
				res+=ligne;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray rows = new JSONObject(res).getJSONArray("rows");
		for (int i = 0; i < rows.length(); i++) {
			Synonyms s = new Synonyms();

			String name = rows.getJSONObject(i).getString("key");
			JSONObject synonyms = rows.getJSONObject(i).getJSONObject("value").getJSONObject("SynonymList");
			if (Integer.valueOf(synonyms.getString("count")) > 1) {
				for (int j = 0; j < Integer.valueOf(synonyms.getString("count")); j++) {
					s.getSynonyms().add(synonyms.getJSONArray("Synonym").getJSONObject(j).getString("text"));
				}
				s.setName(name);
				if (!name.equals(""))
					list.add(s);
			}
			else if (Integer.valueOf(synonyms.getString("count")) == 1) {
				s.getSynonyms().add(synonyms.getJSONObject("Synonym").getString("text"));
				s.setName(name);
				if (!name.equals(""))
					list.add(s);
			}
		}
		return list;
	}

	private ArrayList<Synonyms> initXMLSynonyms(){
		ArrayList<Synonyms> list = new ArrayList<Synonyms>();
		String name = ""; 
		ArrayList<String> syns = new ArrayList<String>();
		
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(Search.XML_PATH);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		org.w3c.dom.Element docEle = dom.getDocumentElement();
		
		NodeList general = docEle.getChildNodes();
		NodeList node;
		NodeList find;
		NodeList syn;
		if(general != null && general.getLength() > 0) {
			for(int i = 0 ; i < general.getLength();i++) {
				if (general.item(i).getNodeName().equals("drug")) {
					node = general.item(i).getChildNodes();
					if (node != null && node.getLength() > 0) {
						for (int j = 0; j < node.getLength(); j++) {
							if (node.item(j).getNodeName().equals("name")) {
								Synonyms s = new Synonyms();
								find = node;
								name = find.item(j).getFirstChild().getNodeValue();
								for (int k = 0; k < find.getLength(); k++) {
									if (find.item(k).getNodeName().equals("synonyms")) {
										syn = find.item(k).getChildNodes();
										for (int h = 0; h < syn.getLength(); h++) {
											if (syn.item(h).getNodeName().equals("synonym")) {
												syns.add(syn.item(h).getFirstChild().getNodeValue());
											}
										}
									}
								}
								s.setName(name);
								s.setSynonyms(syns);
								if (!name.equals(""))
									list.add(s);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	private ArrayList<Synonyms> initCSVSynonyms() throws IOException{
		ArrayList<Synonyms> list = new ArrayList<Synonyms>();
		BufferedReader br;
		
		br = new BufferedReader(new FileReader(new File(Search.CSV_PATH)));
		String line = "", name = "";
		ArrayList<String> syns = new ArrayList<String>();
		syns.clear();
		String[] tab;
		while((line = br.readLine()) != null){
			Synonyms s = new Synonyms();
			tab = lineTreatment(line);
			if (tab[1].contains("&/&")) {
				name = tab[1].split("&/&")[0];
			}else
				name = tab[1];
			if(tab.length > 2){
				s.setName(name);
				syns = getSynonyms(tab[1], tab[2]);
				s.setSynonyms(syns);
				if (!name.equals(""))
					list.add(s);
			}

		}
		br.close();
		return list;
	}
	
	private ArrayList<Synonyms> initSQLSynonyms(){
		ArrayList<Synonyms> Slist = new ArrayList<Synonyms>();
		ArrayList<Drug> list = new ArrayList<Drug>();
		try {
			Class.forName(SQLSearch.DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String myQuery1 = "SELECT drug_name1, drug_name2 FROM label_mapping";
		try {
			Connection con = DriverManager.getConnection(SQLSearch.DB_SERVER+SQLSearch.DB, SQLSearch.USER_NAME, SQLSearch.USER_PSWD);
			Statement st = con.createStatement();

			ResultSet res1 = st.executeQuery(myQuery1);

			String name1 = "", name2 = "";
			Synonyms s = new Synonyms();
			while(res1.next()){
				name1 = res1.getString("drug_name1");
				name2 = res1.getString("drug_name2");
				
				if (!name1.equals("") && !name2.equals("")) {
					s = new Synonyms();
					s.setName(name1);
					s.getSynonyms().add(name2);
					synsList.add(s);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Slist;
	}
	
	private ArrayList<String> getSynonyms(String name, String syns){
		ArrayList<String> list = new ArrayList<String>();
		if (name.contains("&/&")) {
			String[] tab = name.split("&/&");
			for (int i = 0; i < tab.length; i++) {
				list.add(tab[i]);
			}
		}
		if (syns.contains("|")) {
			String[] tab = syns.split("|");
			for (int i = 0; i < tab.length; i++) {
				list.add(tab[i]);
			}
		}
		if (syns.contains("&/&")) {
			String[] tab = syns.split("&/&");
			for (int i = 0; i < tab.length; i++) {
				list.add(tab[i]);
			}
		}
		return list;
	}
	
	private String[] lineTreatment(String line){
		String temp = "";
		if (line.contains("\"")) {
			String[] tab = line.split("\"");
			temp = "";
			for (int i = 1; i < tab.length; i=i+2) {
				tab[i] = tab[i].replaceAll(",", "&/&");
			}
			for (int i = 0; i < tab.length; i++) {
				temp += tab[i];
			}
		}else
			temp = line;
		return temp.split(",");
	}
	
	public ArrayList<Synonyms> getSynsList() {
		return synsList;
	}

	public void setSynsList(ArrayList<Synonyms> synsList) {
		this.synsList = synsList;
	}

}


