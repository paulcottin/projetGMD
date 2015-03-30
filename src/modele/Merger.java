package modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Merger {

	ArrayList<Element> list;

	public Merger() {
		list = new ArrayList<Element>();
	}
	
	public ArrayList<Element> testMerge(ArrayList<Medic> medics, ArrayList<Disease> diseases){
		ArrayList<Element> m = MedicToElement(medics);
		ArrayList<Element> d = DiseaseToElement(diseases);
		if (medics.size() < diseases.size()) {
			for (Element element : d) {
				merge(element, m);
			}
			return m;
		}else{
			for (Element element : m) {
				merge(element, d);
			}
			return d;
		}
	}
	
	private ArrayList<Element> DiseaseToElement(ArrayList<Disease> d){
		ArrayList<Element> e = new ArrayList<Element>();
		ArrayList<String> treat;
		String name;
		for (Disease di : d) {
			name = "";
			treat = new ArrayList<String>();
			treat.add(di.getName());
			for (String s: di.getTreatment()) {
				name += s+"\n";
			}
			e.add(new Element(name, treat, di.getCause(), di.getSymptoms(), new ArrayList<String>(), di.getSynonyms()));
		}
		return e;
	}
	
	private ArrayList<Element> MedicToElement(ArrayList<Medic> medic){
		ArrayList<Element> e = new ArrayList<Element>();
		for (Medic m : medic) {
			e.add(new Element(m.getName(), m.getTreat(), m.getCause(), new ArrayList<String>(), m.getSynonyms(), new ArrayList<String>()));
		}
		return e;
	}

//	public ArrayList<Element> mergeFile(String path1, String path2) throws IOException{
//		File f1 = new File(path1);
//		File f2 = new File(path2);
//		ArrayList<Element> list = new ArrayList<Element>();
//		String line = "";
//		Element e = new Element();
//		
//		if (f1.exists()) {
//			BufferedReader bw1 = new BufferedReader(new FileReader(f1));
//			while((line = bw1.readLine()) != null){
//				switch (line.substring(0, 1)) {
//				case "^":
//					if (line.substring(1) != null)
//						e.setName((e.getName() == "") ? line.substring(1) : e.getName()+"\n"+line.substring(1));
//					else
//						e.setName("");
//					break;
//				case "$":
//					if (line.substring(1) != null)
//						e.setCause((e.getCause()== "") ? line.substring(1) : e.getCause()+"\n"+line.substring(1));
//					else
//						e.setCause("");
//					break;
//				case "&":
//					if (line.substring(1) != null)
//						e.setTreat((e.getTreat() == "") ? line.substring(1) : e.getTreat()+"\n"+line.substring(1));
//					else
//						e.setTreat("");
//					break;
//				case "£":
//					if (line.substring(1) != null)
//						e.setSymptoms((e.getSymptoms() == "") ? line.substring(1) : e.getSymptoms()+"\n"+line.substring(1));
//					else
//						e.setSymptoms("");
//					break;
//				case "\\":
//					if (line.substring(1) != null)
//						e.getSynonyms().add(line.substring(1));
//					break;
//				}
//				if (line.equals("--")) {
//					merge(e, list);
//					e = new Element();
//				}
//			}
//			list.add(e);
//			bw1.close();
//		}
//		if (f2.exists()) {
//			BufferedReader bw2 = new BufferedReader(new FileReader(f2));
//			while((line = bw2.readLine()) != null){
//				switch (line.substring(0, 1)) {
//				case "^":
//					if (line.substring(1) != null)
//						e.setName((e.getName() == "") ? line.substring(1) : e.getName()+"\n"+line.substring(1));
//					else
//						e.setName("");
//					break;
//				case "$":
//					if (line.substring(1) != null)
//						e.setCause((e.getCause()== "") ? line.substring(1) : e.getCause()+"\n"+line.substring(1));
//					else
//						e.setCause("");
//					break;
//				case "&":
//					if (line.substring(1) != null)
//						e.setTreat((e.getTreat() == "") ? line.substring(1) : e.getTreat()+"\n"+line.substring(1));
//					else
//						e.setTreat("");
//					break;
//				case "£":
//					if (line.substring(1) != null)
//						e.setSymptoms((e.getSymptoms() == "") ? line.substring(1) : e.getSymptoms()+"\n"+line.substring(1));
//					else
//						e.setSymptoms("");
//					break;
//				case "\\":
//					if (line.substring(1) != null)
//						e.getSynonyms().add(line.substring(1));
//					break;
//				}
//				if (line.equals("--")) {
//					merge(e, list);
//					e = new Element();
//				}
//			}
//			bw2.close();
//		}
//		return list;
//	}
	
	private void merge(Element e, ArrayList<Element> list){
		Iterator<Element> iter = list.iterator();
		Element element;
		while(iter.hasNext()) {
			element = iter.next();
//			if (e.getName().contains(element.getName()) || element.getName().contains(e.getName())) {
//				element.setCause(element.getCause()+"\n"+e.getCause());
//				element.setSymptoms(element.getSymptoms()+"\n"+e.getSymptoms());
//				element.setTreat(element.getTreat()+"\n"+e.getTreat());
//				for (String s : e.getSynonyms()) {
//					element.getSynonyms().add(s);
//				}
//			}else{
				list.add(e);
//			}
		}
		if (list.size() == 0) {
			list.add(e);
		}
	}

}
