package requests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.sun.org.apache.xml.internal.utils.SuballocatedByteVector;

import modele.Disease;
import modele.Element;
import modele.Medic;
import modele.Merger;


public class TextSearch {
	
	private String CSVpath, TXTpath, Dsearch, name;
	private Merger merger;
	private ArrayList<Element> list;
	private ArrayList<Disease> dList;
	
	public TextSearch(String Dsearch, String TXTpath, String CSVpath) {
		this.Dsearch = Dsearch;
		this.CSVpath= CSVpath;
		this.TXTpath = TXTpath;
		this.list = new ArrayList<Element>();
		this.dList = new ArrayList<Disease>();
		this.merger = new Merger();
	}
	
	public ArrayList<Element> getInfos(){
		try {
			parseCSV();
			parseTxt();
			list = merger.DiseaseToElement(dList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	private void parseCSV() throws IOException{
		BufferedReader br;
		
		br = new BufferedReader(new FileReader(new File(CSVpath)));
		String line = "", nom = "", medicName  ="";
		ArrayList<String> syns = new ArrayList<String>();
		String[] tab;
		while((line = br.readLine()) != null){
			tab = lineTreatment(line);
			if (tab[1].contains("&/&")) {
				nom = tab[1].split("&/&")[0];
			}else
				nom = tab[1];
			if (nom.equals(this.Dsearch)) {
				if (tab.length >1)
					medicName = nom;
				else
					medicName = "";
				if(tab.length > 2)
					syns = getSynonyms(tab[1], tab[2]);
				else
					syns = new ArrayList<String>();
				dList.add(new Disease(medicName, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), syns));
			}
		}
		br.close();
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
	
	private ArrayList<String> getSynonyms(String nom, String syns){
		ArrayList<String> list = new ArrayList<String>();
		if (nom.contains("&/&")) {
			String[] tab = nom.split("&/&");
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
	
	private void parseTxt() throws IOException{
		BufferedReader br;
		String line = "", name = "";
		boolean moved = false;
		ArrayList<String> symptoms = new ArrayList<String>();
		
		br = new BufferedReader(new FileReader(new File(TXTpath)));
		while((line = br.readLine()) != null){
			if (line.equals("*FIELD* TI")) {
				line = br.readLine();
				String item = line.substring(0, 1);
				if (item.equals("*")) {
					int end = line.length();
					if (line.contains(",")) {
						end = line.indexOf(",");
					}
					if (line.contains(";")) {
						end = ((end < line.indexOf(";") ? end : line.indexOf(";")));
					}
					name = line.substring(line.indexOf(" ")+1, end);
				}
				else if (item.matches("[0-9]")) {
					name = line.substring(line.indexOf(" ")+1, line.length());
				}
				else if (item.equals("#")) {
					int end = line.length();
					if (line.contains(";")) {
						end = line.indexOf(";");
					}
					if (line.substring(line.indexOf(",")+1).contains(",")) {
						end = line.substring(line.indexOf(",")+1).indexOf(",")+line.indexOf(",")+1;
					}
					name = line.substring(line.indexOf(" ")+1, end);
					if (name.contains(",")) {
						String[] tab = name.split(",");
						name = tab[1]+" "+tab[0];
					}
				}
				else if (item.equals("%")) {
					name = line.substring(line.indexOf(" ")+1, line.length());
				}
				else if (item.equals("+")) {
					if (line.contains(";"))
						name = line.substring(line.indexOf(" ")+1, line.indexOf(";"));
					else
						name = line.substring(line.indexOf(" ")+1, line.length());
				}
				else if (item.equals("^")) {
					moved = true;
				}
			}
			if (name.equals(Dsearch)) {
				this.name = name;
				if (line.contains("*FIELD* CS") && !moved) {
					while(!(line = br.readLine()).contains("*FIELD*")){
						if (!(line.contains(":") || line.contains("[")) && !line.equals("")) {
							String temp = line.replaceAll("\\t", "");
							int end;
							if (temp.contains(";"))
								end = temp.indexOf(";");
							else
								end = temp.length();
							symptoms.add(temp.substring(0, end).replaceAll("   ", ""));
						}
					}
				}
			}
			moved = false;
		}
		dList.add(new Disease(this.name, new ArrayList<String>(), new ArrayList<String>(), symptoms, new ArrayList<String>()));
		br.close();
	}

	public String getDsearch() {
		return Dsearch;
	}

	public void setDsearch(String dsearch) {
		Dsearch = dsearch;
	}

	public ArrayList<Element> getList() {
		return list;
	}

	public void setList(ArrayList<Element> list) {
		this.list = list;
	}

	public String getCSVpath() {
		return CSVpath;
	}

	public void setCSVpath(String cSVpath) {
		CSVpath = cSVpath;
	}

	public String getTXTpath() {
		return TXTpath;
	}

	public void setTXTpath(String tXTpath) {
		TXTpath = tXTpath;
	}

}
