package modele;

import java.util.ArrayList;
import java.util.Iterator;

import javax.print.attribute.standard.Sides;

public class Merger {

	ArrayList<Element> list;

	public Merger() {
		list = new ArrayList<Element>();
	}

	public ArrayList<Element> testMerge(ArrayList<Medic> medics, ArrayList<Disease> diseases){
		ArrayList<Element> m = MedicToElement(medics);
		ArrayList<Element> d = DiseaseToElement(diseases);
		if (medics.size() < diseases.size() && medics.size() != 0 && diseases.size() != 0) {
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

	public ArrayList<Element> DiseaseToElement(ArrayList<Disease> d){
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

	public ArrayList<Element> MedicToElement(ArrayList<Medic> medic){
		ArrayList<Element> e = new ArrayList<Element>();
		for (Medic m : medic) {
			e.add(new Element(m.getName(), m.getTreat(), m.getCause(), new ArrayList<String>(), m.getSynonyms(), new ArrayList<String>()));
		}
		return e;
	}
	
	public ArrayList<Element> getOutDuplicates(ArrayList<Element> l){
		ArrayList<Element> list = new ArrayList<Element>();
		ArrayList<String> names = new ArrayList<String>();
		boolean find = false;
		for (Element element : l) {
			Element e = new Element();
			e.setName(element.getName());
			if(!names.contains(e.getName())){
				names.add(e.getName());
				for (int i = 0; i < element.getTreat().size(); i++) {
					System.out.println(element.getTreat().get(i));
					for (int j = i+1; j < element.getTreat().size(); j++) {
						System.out.println("\t"+element.getTreat().get(j));
						if (element.getTreat().get(i).equals(element.getTreat().get(j))) {
							find = true;
						}
					}
					if (!find) {
						e.getTreat().add(element.getTreat().get(i));
					}
					find = false;
				}
				for (int i = 0; i < element.getCause().size(); i++) {
					System.out.println(element.getCause().get(i));
					for (int j = i+1; j < element.getCause().size(); j++) {
						System.out.println("\t"+element.getCause().get(j));
						if (element.getCause().get(i).equals(element.getCause().get(j))) {
							find = true;
						}
					}
					if (!find) {
						e.getCause().add(element.getCause().get(i));
					}
					find = false;
				}
				for (int i = 0; i < element.getSymptoms().size(); i++) {
					System.out.println(element.getSymptoms().get(i));
					for (int j = i+1; j < element.getSymptoms().size(); j++) {
						System.out.println("\t"+element.getSymptoms().get(j));
						if (element.getSymptoms().get(i).equals(element.getSymptoms().get(j))) {
							find = true;
						}
					}
					if (!find) {
						e.getSymptoms().add(element.getSymptoms().get(i));
					}
					find = false;
				}
				for (int i = 0; i < element.getSynonyms().size(); i++) {
					System.out.println(element.getSynonyms().get(i));
					for (int j = i+1; j < element.getSynonyms().size(); j++) {
						System.out.println("\t"+element.getSynonyms().get(j));
						if (element.getSynonyms().get(i).equals(element.getSynonyms().get(j))) {
							find = true;
						}
					}
					if (!find) {
						e.getSynonyms().add(element.getSynonyms().get(i));
					}
					find = false;
				}
				for (int i = 0; i < element.getDiseaseSynonyms().size(); i++) {
					System.out.println(element.getDiseaseSynonyms().get(i));
					for (int j = i+1; j < element.getDiseaseSynonyms().size(); j++) {
						System.out.println("\t"+element.getDiseaseSynonyms().get(j));
						if (element.getDiseaseSynonyms().get(i).equals(element.getDiseaseSynonyms().get(j))) {
							find = true;
						}
					}
					if (!find) {
						e.getDiseaseSynonyms().add(element.getDiseaseSynonyms().get(i));
					}
					find = false;
				}
				list.add(e);
			}
		}
		return list;
	}

	public void merge(Element e, ArrayList<Element> list){
		boolean sameDisease = false, sameMedic = false;
		ArrayList<Element> l = new ArrayList<Element>();
		Element element;
		if (list.size() == 0) {
			l.add(e);
		}
		else{
			System.out.println("size : "+list.size());
			Iterator<Element> iter = list.iterator();
			while(iter.hasNext()) {
				element = iter.next();
				if (!element.getName().equals("") && !e.getName().equals("")) {
					//Si même nom
					if (element.getName().equals(e.getName())) {
						sameMedic = true;
//						System.out.println("mm nom");
						if (!element.getTreat().equals(e.getTreat())) {
							element.setTreat(e.getTreat());
						}
						if (!element.getSymptoms().equals(e.getSymptoms())) {
							element.setSymptoms(e.getSymptoms());
						}
						if (!element.getSynonyms().equals(e.getSynonyms())) {
							element.setSynonyms(e.getSynonyms());
						}
						if (!element.getCause().equals(e.getCause())) {
							element.setCause(e.getCause());
						}
					}
				}
				else if (!element.getTreat().equals("") && !element.getTreat().equals("")) {
					//Si même maladie
					if (element.getTreat().equals(e.getTreat())) {
						sameDisease = true;
//						System.out.println("mm maladie");
						if (!element.getName().equals(e.getName())) {
							element.setName(e.getName());
						}
						if (!element.getSymptoms().equals(e.getSymptoms())) {
							element.setSymptoms(e.getSymptoms());
						}
						if (!element.getSynonyms().equals(e.getSynonyms())) {
							element.setSynonyms(e.getSynonyms());
						}
						if (!element.getCause().equals(e.getCause())) {
							element.setCause(e.getCause());
						}
					}
					if (!element.getName().equals(e.getName()) && !element.getTreat().equals(e.getTreat())) {
//						System.out.println("pas meme");
						l.add(e);
					}
				}
				else{
//					System.out.println("ajout");
					l.add(e);
				}
				sameDisease = false; 
				sameMedic = false;
			}
		}
		list.addAll(l);
	}
}
