package requests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import modele.Disease;
import modele.Element;
import modele.Medic;
import modele.Merger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import exceptions.NotFoundException;
/**
 * Reste à gérer : 
 * 	plusieurs maladies traitées par un médicament
 * @author paul
 *
 */
public class XMLSearch {

	private Document dom;
	private ArrayList<Element> list;
	private String path = "";
	private String Msearch = "";
	private String Dsearch = "";
	private Merger merger;

	public XMLSearch(String Msearch, String Dsearch, String path) {
		this.Msearch = Msearch;
		this.Dsearch = Dsearch;
		this.path = path;
		this.merger = new Merger();
		this.list = new ArrayList<Element>();
	}

	public ArrayList<Element> getInfos() throws NotFoundException{
		parseXmlFile();
		parseDocument();
		return list;
	}

	private void parseXmlFile(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(path);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void parseDocument() throws NotFoundException{
		list.clear();
		org.w3c.dom.Element docEle = dom.getDocumentElement();
		ArrayList<Medic> med_res = new ArrayList<Medic>();
		ArrayList<Disease> dis_res = new ArrayList<Disease>();
		if (!Dsearch.equals("")) {
			dis_res = searchByDisease(docEle);
			//				writerD(dis_res);
		}
		if (!Msearch.equals("")) {
			med_res = searchByMedic(docEle);
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

	private ArrayList<Medic> searchByMedic(org.w3c.dom.Element doc){
		ArrayList<Medic> list = new ArrayList<Medic>();
		String name = "", treat = "", cause = "";
		ArrayList<String> synonyms = new ArrayList<String>();
		NodeList general = doc.getChildNodes();
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
								if (node.item(j).getFirstChild().getNodeValue().matches(Msearch)) {
									find = node;
									name = find.item(j).getFirstChild().getNodeValue();
									for (int k = 0; k < find.getLength(); k++) {
										if (find.item(k).getNodeName().equals("indication")) {
											treat = getTreatment(find.item(k).getFirstChild().getNodeValue());
										}
										if (find.item(k).getNodeName().equals("toxicity")) {
											cause = getCause(find.item(k).getFirstChild().getNodeValue());
										}
										if (find.item(k).getNodeName().equals("synonyms")) {
											syn = find.item(k).getChildNodes();
											for (int h = 0; h < syn.getLength(); h++) {
												if (syn.item(h).getNodeName().equals("synonym")) {
													synonyms.add(syn.item(h).getFirstChild().getNodeValue());
												}
											}
										}
									}
									ArrayList<String> c = new ArrayList<String>();
									c.add(cause);
									ArrayList<String> t = new ArrayList<String>();
									t.add(treat);
									list.add(new Medic(name, t, c, synonyms, "DrugBank"));
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	private ArrayList<Disease> searchByDisease(org.w3c.dom.Element doc){
		ArrayList<Disease> list = new ArrayList<Disease>();
		String name = "", treat = "", cause = "", symptom = "";
		NodeList general = doc.getChildNodes();
		NodeList node;
		NodeList find;
		if(general != null && general.getLength() > 0) {
			for(int i = 0 ; i < general.getLength();i++) {
				if (general.item(i).getNodeName().equals("drug")) {
					node = general.item(i).getChildNodes();
					if (node != null && node.getLength() > 0) {
						for (int j = 0; j < node.getLength(); j++) {
							if (node.item(j).getNodeName().equals("pharmacodynamics")) {
								if (node.item(j).getFirstChild() != null) {
									if (node.item(j).getFirstChild().getNodeValue().matches(".*"+Dsearch+".*")) {
										find = node;
										symptom = find.item(j).getFirstChild().getNodeValue();
										for (int k = 0; k < find.getLength(); k++) {
											if (find.item(k).getNodeName().equals("name")) {
												name = getTreatment(find.item(k).getFirstChild().getNodeValue());
											}
											if (find.item(k).getNodeName().equals("indication")) {
												treat = getTreatment(find.item(k).getFirstChild().getNodeValue());
											}
											if (find.item(k).getNodeName().equals("toxicity")) {
												if (find.item(k).getFirstChild() != null) {
													cause = getCause(find.item(k).getFirstChild().getNodeValue());
												}
											}
										}
										ArrayList<String> c = new ArrayList<String>();
										c.add(cause);
										ArrayList<String> t = new ArrayList<String>();
										t.add(treat);
										ArrayList<String> s = new ArrayList<String>();
										s.add(symptom);
										list.add(new Disease(name, t, c, s, new ArrayList<String>(), "DrugBank"));
									}
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	private String getTreatment(String s){
		int begin = (s.contains("treatment of"))? s.indexOf("treatment of") + "treatment of".length()+1 : 0;
		int end;
		if (s.contains(" in ")) {
			end = (s.indexOf(" in ") > begin) ? s.indexOf(" in ") : s.length();
		}else{
			end = s.length();
		}
		return s.substring(begin, end);
	}

	private String getCause(String s){
		int begin = 0, end = 0;
		if (s.contains("adverse effects include ")) {
			begin = s.indexOf("adverse effects include ") + "adverse effects include ".length();
			end = s.substring(begin).indexOf(".") + begin;
		}
		return s.substring(begin, end);
	}

	private void writerM(ArrayList<Medic> list) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("_byM.txt")));
		for (Medic medic : list) {
			bw.write("^"+medic.getName()+"\n");
			bw.write("&"+medic.getTreat()+"\n");
			bw.write("£"+medic.getCause()+"\n");
			for (String s : medic.getSynonyms()) {
				bw.write("\\"+s+"\n");
			}
			bw.write("--");
		}
		bw.close();
	}

	private void writerD(ArrayList<Disease> list) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("_byD.txt")));
		for (Disease dis : list) {
			bw.write("&"+dis.getName()+"\n");
			bw.write("^"+dis.getTreatment()+"\n");
			bw.write("£"+dis.getCause()+"\n");
			bw.write("$"+dis.getSymptoms()+"\n");
			bw.write("--");
		}
		bw.close();
	}

	private void deleteTempFile(){
		File f1 = new File("_byD.txt");
		File f2 = new File("_byM.txt");
		f1.delete();
		f2.delete();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMSearch() {
		return Msearch;
	}

	public void setMSearch(String search) {
		this.Msearch = search;
	}

	public String getDSearch() {
		return Dsearch;
	}

	public void setDSearch(String search) {
		this.Dsearch = search;
	}

}
