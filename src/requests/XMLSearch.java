package requests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modele.Disease;
import modele.Element;
import modele.Medic;
import modele.Merger;

import org.omg.CORBA.portable.IndirectionException;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import exceptions.NotFoundException;
/**
 * Reste à gérer : 
 * 	plusieurs maladies traitées par un médicament
 * @author paul
 *
 */
public class XMLSearch {
	
	private String path = "";
	private String Msearch = "";
	private String Dsearch = "";
	private Merger merger;
	
	public XMLSearch(String Msearch, String Dsearch, String path) {
		this.Msearch = Msearch;
		this.Dsearch = Dsearch;
		this.path = path;
		this.merger = new Merger();
	}
	
	public ArrayList<Element> getInfos() throws NotFoundException{
		ArrayList<Medic> med_res = new ArrayList<Medic>();
		ArrayList<Disease> dis_res = new ArrayList<Disease>();
		File file = new File(path);
		try {
			if (!Dsearch.equals("")) {
				System.out.println("dsearch");
				dis_res = parserByDisease(file);
				writerD(dis_res);
			}
			if (!Msearch.equals("")) {
				System.out.println("msearch");
				med_res = parserByMedic(file);
				writerM(med_res);
			}
			if (dis_res.size() == 0 && med_res.size() == 0) {
				NotFoundException e = new NotFoundException();
				throw e;
			}
			ArrayList<Element> list = merger.mergeFile("_byM.txt", "_byD.txt"); 
			deleteTempFile();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Disease> parserByDisease(File f) throws IOException{
		ArrayList<Disease> list = new ArrayList<Disease>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String nameLine = "";
		String treatmentMedicsLine = "";
		String causeMedicsLine = "";
		String symptomsLine = "";
		
		String ligne = "";
		while((ligne = br.readLine()) != null){
			if (ligne != null && ligne.contains("<name")) {
				String content = "";
				content += ligne;
				while(ligne != null && !ligne.contains("</name>")){
					ligne = br.readLine();
					content += ligne;
				}
				nameLine = content;
			}
			if (ligne != null && ligne.contains("<indication")) {
				String content = "";
				content += ligne;
				while(ligne != null && !ligne.contains("</indication>")){
					ligne = br.readLine();
					content += ligne;
				}
				treatmentMedicsLine = getOutChars(content);
				int begin = content.indexOf("treatment of")-1;
				int end;
				if (treatmentMedicsLine.contains(" in ")) {
					end = (treatmentMedicsLine.indexOf(" in ") > begin) ? treatmentMedicsLine.indexOf(" in ") : treatmentMedicsLine.length();
				}else{
					end = treatmentMedicsLine.length();
				}
				treatmentMedicsLine = treatmentMedicsLine.substring(begin, end);

			}
			if (ligne != null && ligne.contains("<pharmacodynamics")) {
				String content = "";
				content += ligne;
				while(ligne != null && !ligne.contains("</pharmacodynamics>")){
					ligne = br.readLine();
					content += ligne;
				}
				symptomsLine = getOutChars(content);
				
				if (symptomsLine.contains("characterized by")) {
					
					int begin = content.indexOf("characterized by");
					int end;
					if (symptomsLine.contains(".")) {
						end = (symptomsLine.indexOf(".") > begin) ? symptomsLine.indexOf(".") : symptomsLine.length();
					}else{
						end = symptomsLine.length();
					}
					symptomsLine = symptomsLine.substring(begin, end);
				}else
					symptomsLine = "";
			}
			if (treatmentMedicsLine.contains(Dsearch)) {
				while(ligne != null && !ligne.contains("</drug>") && !ligne.contains("</indication")) {
					if (ligne != null && ligne.contains("<toxicity") && !ligne.contains("toxicity/>")) {
						String content = "";
						content += ligne;
						while(ligne != null && !ligne.contains("</toxicity>")){
							ligne = br.readLine();
							content += ligne;
						}
						causeMedicsLine= getOutChars(content);
						if (causeMedicsLine.contains("adverse effects include ")) {
							int begin = causeMedicsLine.indexOf("adverse effects include ") + "adverse effects include ".length();
							int end = causeMedicsLine.substring(begin).indexOf(".") + begin;
							causeMedicsLine = causeMedicsLine.substring(begin, end);
						}else
							causeMedicsLine = "";
							
					}
					ligne = br.readLine();
					if (ligne != null && ligne.contains("</drug>")) {
						list.add(new Disease(treatmentMedicsLine, getOutChars(nameLine), causeMedicsLine, symptomsLine));
						treatmentMedicsLine = "";
					}
				}
			}
		}	
		br.close();
		return list;
	}
	
	public ArrayList<Medic> parserByMedic(File f) throws IOException{
		ArrayList<Medic> list = new ArrayList<Medic>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String nameLine = "";
		String treatmentMedicsLine = "";
		String causeMedicsLine = "";
		String synonymsLine = "";
		ArrayList<String> syns = new ArrayList<String>();
		
		String ligne = "";
		while((ligne = br.readLine()) != null){
			if (ligne != null && ligne.contains("<name")) {
				String content = "";
				content += ligne;
				while(ligne != null && !ligne.contains("</name>")){
					ligne = br.readLine();
					content += ligne;
				}
				nameLine = content;
			}
			if (getOutChars(nameLine).equals(Msearch)) {
				while(ligne != null && !ligne.contains("</drug>")) {
					if (ligne != null && ligne.contains("<indication") && !ligne.contains("indication/>")) {
						String content = "";
						content += ligne;
						while(ligne != null && !ligne.contains("</indication>")){
							ligne = br.readLine();
							content += ligne;
						}
						treatmentMedicsLine = content;
					}
					if (ligne != null && ligne.contains("<toxicity") && !ligne.contains("toxicity/>")) {
						String content = "";
						content += ligne;
						while(ligne != null && !ligne.contains("</toxicity>")){
							ligne = br.readLine();
							content += ligne;
						}
						causeMedicsLine= getOutChars(content);
						if (causeMedicsLine.contains("adverse effects include ")) {
							int begin = causeMedicsLine.indexOf("adverse effects include ") + "adverse effects include ".length();
							int end = causeMedicsLine.substring(begin).indexOf(".") + begin;
							causeMedicsLine = causeMedicsLine.substring(begin, end);
						}else
							causeMedicsLine = "";
							
					}
					if (ligne != null && ligne.contains("<synonyms")) {
						while(ligne != null && !ligne.contains("</synonyms>")){
							ligne = br.readLine();
							if (!ligne.contains("</synonyms")) {
								synonymsLine = ligne;
								syns.add(getOutChars(synonymsLine));
							}
						}
					}
					ligne = br.readLine();
					if (ligne != null && ligne.contains("</drug>")) {
						list.add(new Medic(getOutChars(nameLine), getOutChars(treatmentMedicsLine), getOutChars(causeMedicsLine), syns));
						nameLine = "";
						syns = new ArrayList<String>();
					}
				}
			}
		}	
		br.close();
		return list;
	}
	
	private String getOutChars(String line){
		if(line.contains("<")){
			if(line.contains(" ")){
				int baliseClosed_temp1 = line.indexOf(">");
				int baliseClosed_temp2 = line.substring(line.indexOf("<")).indexOf(" ");
				int baliseClosed;
				if (baliseClosed_temp2 < baliseClosed_temp1 && baliseClosed_temp2 != -1) 
					baliseClosed = baliseClosed_temp2;
				else
					baliseClosed = baliseClosed_temp1;
				
				String baliseName = line.substring(line.indexOf("<")+1, baliseClosed);
				int endBalise = line.indexOf("</"+baliseName);
			return line.substring(baliseClosed_temp1+1, endBalise);
			}else
				return "";
		}else
			return line;
	}
	
	private void writerM(ArrayList<Medic> list) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("_byM.txt")));
		for (Medic medic : list) {
			bw.write("^"+medic.getName()+"\n");
			bw.write("&"+medic.getTreat()+"\n");
			bw.write("£"+medic.getCause()+"\n");
			bw.write("£"+medic.getSymptoms()+"\n");
			for (String s : medic.getSynonyms()) {
				bw.write("\\"+s+"\n");
			}
			bw.write("--");
		}
		System.out.println("m write");
		bw.close();
	}
	
	private void writerD(ArrayList<Disease> list) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("_byD.txt")));
		for (Disease dis : list) {
			bw.write("&"+dis.getName()+"\n");
			bw.write("^"+dis.getTreatment()+"\n");
			bw.write("£"+dis.getCause()+"\n");
			bw.write("£"+dis.getSymptoms()+"\n");
			bw.write("--");
		}
		bw.close();
		System.out.println("d write");
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
