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

import com.sun.org.apache.bcel.internal.generic.RETURN;

import exceptions.NotFoundException;
/**
 * Reste à gérer : 
 * 	plusieurs maladies traitées par un médicament
 * @author paul
 *
 */
public class XMLSearch implements Runnable{

	private Document dom;
	private ArrayList<Element> list;
	private String path = "";
	private String Msearch = "";
	private String Dsearch = "";
	private Merger merger;
	private ArrayList<Element> returnList;

	public XMLSearch(String Msearch, String Dsearch, String path) {
		this.Msearch = Msearch;
		this.Dsearch = Dsearch;
		this.path = path;
		this.merger = new Merger();
		this.list = new ArrayList<Element>();
		this.returnList = new ArrayList<Element>();
	}
	
	@Override
	public void run() {
		returnList.clear();
		returnList = getInfos();
	}

	public ArrayList<Element> getInfos(){
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

	private void parseDocument(){
		list.clear();
		org.w3c.dom.Element docEle = dom.getDocumentElement();
		ArrayList<Medic> med_res = new ArrayList<Medic>();
		ArrayList<Disease> dis_res = new ArrayList<Disease>();
		if (!Dsearch.equals("")) {
			dis_res = searchByDisease(docEle);
		}
		if (!Msearch.equals("")) {
			med_res = searchByMedic(docEle);
		}
		if (!(dis_res.size() == 0 && med_res.size() == 0)) {
			list.addAll(merger.DiseaseToElement(dis_res));
			list.addAll(merger.MedicToElement(med_res));
		}
	}

	private ArrayList<Medic> searchByMedic(org.w3c.dom.Element doc){
		ArrayList<Medic> list = new ArrayList<Medic>();
		String name = "";
		ArrayList<String> synonyms = new ArrayList<String>(), treat = new ArrayList<String>(), cause = new ArrayList<String>();
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
								if (node.item(j).getFirstChild().getNodeValue().toUpperCase().matches(Msearch.toUpperCase())) {
									find = node;
									name = find.item(j).getFirstChild().getNodeValue();
									for (int k = 0; k < find.getLength(); k++) {
										if (find.item(k).getNodeName().equals("indication")) {
											treat = getTreatment(find.item(k).getFirstChild().getNodeValue());
										}
										if (find.item(k).getNodeName().equals("toxicity")) {
											cause = getCause(((find.item(k).getFirstChild() != null) ? find.item(k).getFirstChild().getNodeValue() : ""));
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
									list.add(new Medic(name, treat, cause, synonyms, "DrugBank"));
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
		ArrayList<String> treat = new ArrayList<String>(), cause = new ArrayList<String>(), symptom = new ArrayList<String>(), name = new ArrayList<String>();
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
									if (node.item(j).getFirstChild().getNodeValue().toUpperCase().matches(".*"+Dsearch.toUpperCase()+".*")) {
										find = node;
										symptom = getSymptoms(find.item(j).getFirstChild().getNodeValue());
										for (int k = 0; k < find.getLength(); k++) {
											if (find.item(k).getNodeName().equals("name")) {
												treat = getTreatment(find.item(k).getFirstChild().getNodeValue());
											}
											if (find.item(k).getNodeName().equals("indication")) {
												name = getTreatment(find.item(k).getFirstChild().getNodeValue());
											}
											if (find.item(k).getNodeName().equals("toxicity")) {
												if (find.item(k).getFirstChild() != null) {
													cause = getCause(find.item(k).getFirstChild().getNodeValue());
												}
											}
										}

										list.add(new Disease(name.get(0), treat, symptom, cause, new ArrayList<String>(), "DrugBank"));
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

	private ArrayList<String> getTreatment(String s){
		ArrayList<String> l = new ArrayList<String>();
		int begin = (s.contains("treatment of"))? s.indexOf("treatment of") + "treatment of".length()+1 : 0;
		int end;
		if (s.contains(" in ")) {
			end = (s.indexOf(" in ") > begin) ? s.indexOf(" in ") : s.length();
		}else{
			end = s.length();
		}
		l.add(s.substring(begin, end));
		return l;
	}

	private ArrayList<String> getCause(String s){
		ArrayList<String> l = new ArrayList<String>();
		String temp;
		int begin = 0, end = 0;
		if (s.contains("adverse effects include ")) {
			begin = s.indexOf("adverse effects include ") + "adverse effects include ".length();
			end = s.substring(begin).indexOf(".") + begin;
			temp = s.substring(begin, end);
			if (temp.contains(",")) {
				for (String string : temp.split(", ")) {
					int b = 0, e = string.length();
					if (string.contains("and ")) {
						b = string.indexOf("and ") + "and ".length();
					}
					l.add(string.substring(b, e));
				}
			}else {
				l.add(temp);
			}
		}
		else {
			l.add(s);
		}
		return l;
	}

	private ArrayList<String> getSymptoms(String s){
		ArrayList<String> l = new ArrayList<String>();
		if (s.contains("characterized by")) {
			int begin = s.indexOf("characterized by") + "characterized by".length()+1;
			int end = (s.substring(begin).contains(".")) ? s.indexOf(".") : s.length();
			l.add(s.substring(begin, end));
		}else
			l.add(s);
		return l;
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

	public ArrayList<Element> getReturnList() {
		return returnList;
	}

	public void setReturnList(ArrayList<Element> returnList) {
		this.returnList = returnList;
	}
}
