package modele;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Sides;

import org.omg.PortableServer.ForwardRequestHelper;

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
		ArrayList<String> synonym;
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
			name = items[0];
			synonym = new ArrayList<String>();
			for (int i = 1; i < items.length; i++) {
				synonym.add(items[i]);
				
			}
			e.add(new Element(name, treat, di.getCause(), di.getSymptoms(), synonym, di.getSynonyms(), di.getOrigin()));
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
							if (element.getTreat().get(i).equalsIgnoreCase(element.getTreat().get(j))) {
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
							if (element.getCause().get(i).equalsIgnoreCase(element.getCause().get(j))) {
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
							if (element.getSymptoms().get(i).equalsIgnoreCase(element.getSymptoms().get(j))) {
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
							if (element.getSynonyms().get(i).equalsIgnoreCase(element.getSynonyms().get(j))) {
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
							if (element.getDiseaseSynonyms().get(i).equalsIgnoreCase(element.getDiseaseSynonyms().get(j))) {
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

	private ArrayList<Element> merge(Element e, ArrayList<Element> list){
		ArrayList<Element> l = new ArrayList<Element>();
		if (list.size() == 0) {
			System.out.println("init : "+e.getName()+", "+e.getTreat());
			l.add(e);
		}
//		else if (list.size() == 1) {
//			if (e.getName().equalsIgnoreCase(list.get(0).getName()+"\n") && sameTreat(e.getTreat(), list.get(0).getTreat())) {
//				
//			}else{
//				System.out.println("ADD : "+e.getName());
//				l.add(e);
//			}
//		}
//		else if (list.size() > 10) {
//			return new ArrayList<Element>();
//		}
		else {
			
			for (int i = 0; i < list.size(); i++) {
				System.out.println("list size : "+list.size());
				//Si mm noms et mm disease
				System.out.println(" i : "+i+", noms : "+e.getName()+" - "+list.get(i).getName()+" same treat : "+sameTreat(e.getTreat(), list.get(i).getTreat()));
				if (e.getName().equalsIgnoreCase(list.get(i).getName()+"\n") && sameTreat(e.getTreat(), list.get(i).getTreat())) {
					//Si mm disease (à revoir)
//					if (e.getTreat().equals(list.get(i).getTreat())) {
//						System.out.println("\tadd mm disease : "+list.get(i).getTreat().toString()+" (nom : "+e.getName()+") - SIZE : "+l.size());
//						Element temp = mergeElement(e, list.get(i));
//						System.out.println("\t\t"+temp.toString());
//						l.add(temp);
//					}
//					else{
//						System.out.println("\tadd mm noms != diseases : "+e.getName()+", "+e.getTreat().toString());
//						l.add(e);
//					}
					System.out.println("\t SAME : "+e.getName());
					return new ArrayList<Element>();
				}
				else{
					
					if (!haveElement(l, e)) {
						System.out.println("\tadd != noms != diseases : "+e.getName()+", "+e.getTreat().toString());
						l.add(e);
					}
				}
			}
		}
		return l;
	}
	
	private boolean sameTreat(ArrayList<String> a, ArrayList<String> b){
		ArrayList<String> temp = new ArrayList<String>();
		boolean same = false;
		for (int i = 0; i < a.size(); i++) {
			temp = new ArrayList<String>();
			for (int j = 0; j < b.size(); j++) {
				if (a.get(i).equalsIgnoreCase(b.get(j))) {
					temp.add(b.get(j));
				}
			}
			if (temp.size() == b.size()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean haveElement(ArrayList<Element> list, Element e){
		for (Element element : list) {
			if (element.getName().equalsIgnoreCase(e.getName())) {
				return true;
			}
		}
		return false;
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
