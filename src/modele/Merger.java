package modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Merger {

	ArrayList<Element> list;

	public Merger() {
		list = new ArrayList<Element>();
	}

	public ArrayList<Element> mergeFile(String path1, String path2) throws IOException{
		File f1 = new File(path1);
		File f2 = new File(path2);
		ArrayList<Element> list = new ArrayList<Element>();
		String line = "";
		Element e = new Element();
		
		if (f1.exists()) {
			BufferedReader bw1 = new BufferedReader(new FileReader(f1));
			while((line = bw1.readLine()) != null){
				switch (line.substring(0, 1)) {
				case "^":
					if (line.substring(1) != null)
						e.setName((e.getName() == "") ? line.substring(1) : e.getName()+"\n"+line.substring(1));
					else
						e.setName("");
					break;
				case "$":
					if (line.substring(1) != null)
						e.setCause((e.getCause()== "") ? line.substring(1) : e.getCause()+"\n"+line.substring(1));
					else
						e.setCause("");
					break;
				case "&":
					if (line.substring(1) != null)
						e.setTreat((e.getTreat() == "") ? line.substring(1) : e.getTreat()+"\n"+line.substring(1));
					else
						e.setTreat("");
					break;
				case "£":
					if (line.substring(1) != null)
						e.setSymptoms((e.getSymptoms() == "") ? line.substring(1) : e.getSymptoms()+"\n"+line.substring(1));
					else
						e.setSymptoms("");
					break;
				case "\\":
					if (line.substring(1) != null)
						e.getSynonyms().add(line.substring(1));
					break;
				}
				if (line.equals("--")) {
					merge(e, list);
					e = new Element();
				}
			}
			bw1.close();
		}
		if (f2.exists()) {
			BufferedReader bw2 = new BufferedReader(new FileReader(f2));
			while((line = bw2.readLine()) != null){
				switch (line.substring(0, 1)) {
				case "^":
					if (line.substring(1) != null)
						e.setName((e.getName() == "") ? line.substring(1) : e.getName()+"\n"+line.substring(1));
					else
						e.setName("");
					break;
				case "$":
					if (line.substring(1) != null)
						e.setCause((e.getCause()== "") ? line.substring(1) : e.getCause()+"\n"+line.substring(1));
					else
						e.setCause("");
					break;
				case "&":
					if (line.substring(1) != null)
						e.setTreat((e.getTreat() == "") ? line.substring(1) : e.getTreat()+"\n"+line.substring(1));
					else
						e.setTreat("");
					break;
				case "£":
					if (line.substring(1) != null)
						e.setSymptoms((e.getSymptoms() == "") ? line.substring(1) : e.getSymptoms()+"\n"+line.substring(1));
					else
						e.setSymptoms("");
					break;
				case "\\":
					if (line.substring(1) != null)
						e.getSynonyms().add(line.substring(1));
					break;
				}
				if (line.equals("--")) {
					merge(e, list);
					e = new Element();
				}
			}
			bw2.close();
		}
		for (Element element : list) {
			System.out.println(element.toString());
		}
		return list;
	}
	
	private void merge(Element e, ArrayList<Element> list){
		int i =0;
		System.out.println("Element : "+e.toString()+"\n***************");
		for (Element element : list) {
			if (e.getName().contains(element.getName()) || element.getName().contains(e.getName())) {
				System.out.println("find");
				element.setCause(element.getCause()+"\n"+e.getCause());
				element.setSymptoms(element.getSymptoms()+"\n"+e.getSymptoms());
				element.setTreat(element.getTreat()+"\n"+e.getTreat());
				for (String s : e.getSynonyms()) {
					element.getSynonyms().add(s);
				}
			}else{
				System.out.println("else");
				list.add(e);
			}
		}
		if (list.size() == 0) {
			list.add(e);
		}
	}

}
