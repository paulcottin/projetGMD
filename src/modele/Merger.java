package modele;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Sides;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
			Pattern p = Pattern.compile(";");
			String[] items = p.split(name);
			e.add(new Element(name, treat, di.getCause(), di.getSymptoms(), new ArrayList<String>(), di.getSynonyms(), di.getOrigin()));
		}
		return e;
	}

	public ArrayList<Element> MedicToElement(ArrayList<Medic> medic){
		ArrayList<Element> e = new ArrayList<Element>();
		for (Medic m : medic) {
			e.add(new Element(m.getName(), m.getTreat(), m.getCause(), new ArrayList<String>(), m.getSynonyms(), new ArrayList<String>(), m.getOrigin()));
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
			e.setOrigin(element.getOrigin());
			if (!e.getName().equals("") && element.getTreat() != null) {
				if(!names.contains(e.getName())){
					names.add(e.getName());
					for (int i = 0; i < element.getTreat().size(); i++) {
						for (int j = i+1; j < element.getTreat().size(); j++) {
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
						for (int j = i+1; j < element.getCause().size(); j++) {
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
						for (int j = i+1; j < element.getSymptoms().size(); j++) {
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
						for (int j = i+1; j < element.getSynonyms().size(); j++) {
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
						for (int j = i+1; j < element.getDiseaseSynonyms().size(); j++) {
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
		}
		return list;
	}

	public ArrayList<Element> merge(Element e, ArrayList<Element> list){
//		boolean sameDisease = false, sameMedic = false;
//		ArrayList<Element> l = new ArrayList<Element>();
//		Element element;
//		if (list.size() == 0) {
//			l.add(e);
//		}
//		else{
////			System.out.println("size : "+list.size());
//			Iterator<Element> iter = list.iterator();
//			while(iter.hasNext()) {
//				element = iter.next();
//				if (!element.getName().equals("") && !e.getName().equals("")) {
//					//Si même nom
//					if (element.getName().equals(e.getName())) {
//						sameMedic = true;
////						System.out.println("mm nom");
//						if (!element.getTreat().equals(e.getTreat())) {
//							element.setTreat(e.getTreat());
//						}
//						if (!element.getSymptoms().equals(e.getSymptoms())) {
//							element.setSymptoms(e.getSymptoms());
//						}
//						if (!element.getSynonyms().equals(e.getSynonyms())) {
//							element.setSynonyms(e.getSynonyms());
//						}
//						if (!element.getCause().equals(e.getCause())) {
//							element.setCause(e.getCause());
//						}
//					}
//				}
//				else if (element.getTreat() != null && e.getTreat() != null) {
//					for (int i = 0; i < e.getTreat().size(); i++) {
//						if (!element.getTreat().contains(e.getTreat().get(i))) {
//							element.getTreat().add(e.getTreat().get(i));
//						}
//					}
//					if (element.getTreat().equals(e.getTreat())) {
//						sameDisease = true;
////						System.out.println("mm maladie : "+element.getTreat());
//						if (!element.getName().equals(e.getName())) {
//							element.setName(e.getName());
//						}
//						if (!element.getSymptoms().equals(e.getSymptoms())) {
//							element.setSymptoms(e.getSymptoms());
//						}
//						if (!element.getSynonyms().equals(e.getSynonyms())) {
//							element.setSynonyms(e.getSynonyms());
//						}
//						if (!element.getCause().equals(e.getCause())) {
//							element.setCause(e.getCause());
//						}
//					}
//					if (!element.getName().equals(e.getName()) && !element.getTreat().equals(e.getTreat())) {
////						System.out.println("pas meme");
//						l.add(e);
//					}
//				}
//				else{
////					System.out.println("ajout");
//					l.add(e);
//				}
//				sameDisease = false; 
//				sameMedic = false;
//			}
//		}
//		list.addAll(l);
		return mergeTest(e, list);
	}
	
	private ArrayList<Element> mergeTest(Element e, ArrayList<Element> list){
		ArrayList<Element> l = new ArrayList<Element>();
		if (list.size() == 0) {
//			System.out.println("init : "+e.getName()+", "+e.getTreat());
			l.add(e);
		}
		else {
			for (int i = 0; i < list.size(); i++) {
				//Si mm noms et mm disease
//				System.out.println(" i : "+i+", noms : "+e.getName()+" - "+list.get(i).getName());
				if (e.getName().equals(list.get(i).getName()) && e.getTreat().equals(list.get(i).getTreat())) {
					//Si mm disease (à revoir)
					if (e.getTreat().equals(list.get(i).getTreat())) {
//						System.out.println("\tadd mm disease : "+list.get(i).getTreat().toString()+" (nom : "+e.getName()+") - SIZE : "+l.size());
						Element temp = mergeElement(e, list.get(i));
						System.out.println("\t\t"+temp.toString());
						l.add(temp);
					}
					else{
//						System.out.println("\tadd mm noms != diseases : "+e.getName()+", "+e.getTreat().toString());
						l.add(e);
					}
				}else{
//					System.out.println("\tadd != noms != diseases : "+e.getName()+", "+e.getTreat().toString());
					l.add(e);
				}
			}
		}
		return l;
	}
	
	private Element mergeElement(Element e1, Element e2){
		System.out.println("coucou");
		Element e = new Element();
		e.setName(e1.getName());
		e.setTreat(e1.getTreat());
		for (int i = 0; i < e1.getSymptoms().size(); i++) {
			if (!e2.getSymptoms().contains(e1.getSymptoms().get(i))) {
				e.setSymptoms(mergeList(e1.getSymptoms().get(i), e2.getSymptoms()));
			}
		}
		for (int i = 0; i < e1.getSynonyms().size(); i++) {
			if (!e2.getSynonyms().contains(e1.getSynonyms().get(i))) {
				e.setSynonyms(mergeList(e1.getSynonyms().get(i), e2.getSynonyms()));
			}
		}
		for (int i = 0; i < e1.getDiseaseSynonyms().size(); i++) {
			if (e2.getDiseaseSynonyms().contains(e1.getDiseaseSynonyms().get(i))) {
				e.setDiseaseSynonyms(mergeList(e1.getDiseaseSynonyms().get(i), e2.getDiseaseSynonyms()));
			}
		}
		return e;
	}
	
	private ArrayList<String> mergeList(String s, ArrayList<String> list){
		ArrayList<String> l = new ArrayList<String>();
		boolean find = false;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				if (list.get(i).equals(list.get(j))) {
					find = true;
				}
			}
			if (!find) {
				l.add(list.get(i));
			}
			find = false;
		}
		return l;
	}
}
