package modele;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Sides;

import org.omg.PortableServer.ForwardRequestHelper;

import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.sun.org.apache.xml.internal.serializer.ToUnknownStream;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Merger implements Runnable{
	
	public static int INCLUSIVE_MERGE = 0;
	public static int EXCLUSIVE_MERGE = 1;
	public static int OTHER_SOURCE_SCORE = 100;
	public static int SAME_SOURCE_SCORE = 1;

	private ArrayList<Element> list;
	private ArrayList<Element> list1, list2;
	private int index;
	private int mergeType;
	private String medic, disease;

	public Merger() {
		init();
	}
	
	private void init(){
		list = new ArrayList<Element>();
		list1 = new ArrayList<Element>();
		list2 = new ArrayList<Element>();
		index = 0;
		mergeType = INCLUSIVE_MERGE;
		medic = "";
		disease = "";
	}
	
	@Override
	public void run() {
		ArrayList<Element> t = new ArrayList<Element>();
		if (mergeType == INCLUSIVE_MERGE) {
			for (Element element : list1) {
				t = merge(element, list2);
				if (t.size() > 0) {
					list2.addAll(t);
				}
			}
			list2.addAll(t);
			t.clear();
		}
		else if (mergeType == EXCLUSIVE_MERGE) {
			System.out.println("exclusive merge !");
			for (Element element : list1) {
				t = exclusiveMerge(element, list2);
				if (t.size() > 0) {
					list2.addAll(t);
				}
			}
			list2.addAll(t);
			t.clear();
		}
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
		ArrayList<String> treat = new ArrayList<String>();
		ArrayList<String> synonym = new ArrayList<String>(); 
		String name;
		for (Disease di : d) {
			synonym = new ArrayList<String>();
			name = "";
			treat = new ArrayList<String>();
			treat.add(di.getName());
			for (String s: di.getTreatment()) {
				name += (di.getTreatment().indexOf(s) == di.getTreatment().size()) ? s+"\n" : s;				
			}
			if (name.contains("(") && name.contains(")")) {
//				System.out.println(name);
				int begin = name.indexOf("(")+1;
				int end = name.indexOf(")");
//				System.out.println("\t"+name.substring(0, begin-((name.charAt(begin-1) == ' ') ? 2 : 1))+"\t"+name.substring(begin, end));
				synonym.add(name.substring(begin, end));
				name = name.substring(0, begin-((name.charAt(begin-1) == ' ') ? 2 : 1));
			}
			Pattern p = Pattern.compile(";");
			String[] items = p.split(name);
			name = items[0];
			for (int i = 1; i < items.length; i++) {
				synonym.add(items[i]);		
			}
			
			//if pls disease name -> disease synonyms
//			if (treat.size() > 1) {
//				String t = treat.get(0);
//				for (int i = 1; i < treat.size(); i++) {
//					di.getSynonyms().add(treat.get(i));
//				}
//				treat.clear();
//				treat.add(t);
//			}
			e.add(new Element(name, treat, di.getCause(), di.getSymptoms(), synonym, di.getSynonyms(), di.getOrigin(), 0));
		}
		return e;
	}

	public ArrayList<Element> MedicToElement(ArrayList<Medic> medic){
		ArrayList<Element> e = new ArrayList<Element>();
		for (Medic m : medic) {
			e.add(new Element(m.getName(), m.getTreat(), new ArrayList<String>(), m.getCause(), m.getSynonyms(), new ArrayList<String>(), m.getOrigin(), 0));
		}
		return e;
	}
	
	public ArrayList<Element> getOutDuplicates(ArrayList<Element> l){
		ArrayList<Element> list = new ArrayList<Element>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<ArrayList<String>> diseases = new ArrayList<ArrayList<String>>();
		boolean find = false;
		for (Element element : l) {
			Element e = new Element();
			e.setName(element.getName());
			e.setOrigin(element.getOrigin());
			e.setScore(element.getScore());
			if (!e.getName().equals("") || element.getTreat() != null) {
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
						diseases.add(e.getTreat());
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
				else if (names.contains(e.getName())){
					for (int i = 0; i < diseases.size(); i++) {
//						System.out.println("e treat : "+e.getTreat().toString()+"\n element treat : "+element.getTreat().toString());
						if (sameTreat(element.getTreat(), diseases.get(i))) {
//							list.get(names.indexOf(e.getName())).setScore(list.get(names.indexOf(e.getName())).getScore()+SAME_SOURCE_SCORE);
						}
					}
				}
//				else{
//					System.out.println("e treat : "+e.getTreat().toString()+"\n element treat : "+element.getTreat().toString());
////					list.get(names.indexOf(e.getName())).setScore(list.get(names.indexOf(e.getName())).getScore()+SAME_SOURCE_SCORE);
//				}
			}
		}
		return list;
	}

	public ArrayList<Element> merge(Element e, ArrayList<Element> list){
		ArrayList<Element> l = new ArrayList<Element>();
		if (list.size() == 0) {
			l.add(e);
		}
		else {
			for (int i = 0; i < list.size(); i++) {
					if (e.getName().equalsIgnoreCase(list.get(i).getName()) && sameTreat(e.getTreat(), list.get(i).getTreat())) {
					return new ArrayList<Element>();
				}
				//Si seulement mm noms
				else if (e.getName().equalsIgnoreCase(list.get(i).getName()) && (!e.getName().equals("") || !list.get(i).getName().equals(""))) {
					for (String s : e.getTreat()) {
						if (!list.get(i).getTreat().contains(s)) {
							list.get(i).getDiseaseSynonyms().add(s);
						}
					}
					for (String s : e.getCause()) {
						if (!list.get(i).getCause().contains(s)) {
							list.get(i).getCause().add(s);
						}
					}
					for (String s : e.getSynonyms()) {
						if (!list.get(i).getSynonyms().contains(s)) {
							list.get(i).getSynonyms().add(s);
						}
					}
					for (String s : e.getDiseaseSynonyms()) {
						if (!list.get(i).getDiseaseSynonyms().contains(s)) {
							list.get(i).getDiseaseSynonyms().add(s);
						}
					}
					if (!e.getOrigin().equals(list.get(i).getOrigin()) && !list.get(i).getOrigin().matches(".*"+e.getOrigin()+".*")) {
						list.get(i).setOrigin(list.get(i).getOrigin()+"/"+e.getOrigin());
						list.get(i).setScore(list.get(i).getScore()+OTHER_SOURCE_SCORE);
					}
					else{
						list.get(i).setScore(list.get(i).getScore()+SAME_SOURCE_SCORE);
					}
				}
				//Si seulement mm disease
				else if (sameTreat(e.getTreat(), list.get(i).getTreat())) {
//					System.out.println("seulement mm disease");
					if (e.getName().equals("") && !e.getOrigin().equals(list.get(i).getOrigin()) && !list.get(i).getOrigin().matches(".*"+e.getOrigin()+".*")) {
						list.get(i).setOrigin(list.get(i).getOrigin()+"/"+e.getOrigin());
						list.get(i).setScore(list.get(i).getScore()+OTHER_SOURCE_SCORE);
					}
					else if(!haveElement(l, e)) {
						l.add(e);
					}
				}
				else{
					if (!haveElement(l, e)) {
//						System.out.println("\tadd != noms != diseases : "+e.getName()+", "+e.getTreat().toString());
						l.add(e);
					}else{
						Element element = l.get(index);
						if (!e.getOrigin().equals(element.getOrigin())) {
							element.setOrigin(element.getOrigin()+"/"+e.getOrigin());
							list.get(i).setScore(list.get(i).getScore()+OTHER_SOURCE_SCORE);
						}
						element.setScore(element.getScore()+SAME_SOURCE_SCORE);
						l.set(index, element);
					}
				}
			}
		}
		return l;
	}
	
	private ArrayList<Element> exclusiveMerge(Element e, ArrayList<Element> list){
		ArrayList<Element> l = new ArrayList<Element>();
		if (e.getName().toUpperCase().contains(medic.toUpperCase()) && arrayContains(disease, e.getTreat())) {
			if (!haveElement(l, e)) {
				l.add(e);
			}
		}
		
		return l;
	}
	
	private boolean sameTreat(ArrayList<String> a, ArrayList<String> b){
		ArrayList<String> temp = new ArrayList<String>();
		if (a != null && b != null) {
			if (a.size() == 0 && b.size() == 0) {
				return true;
			}
			else if (a.size() == 0 || b.size() == 0) {
				return false;
			}
//			System.out.println(a.toString()+" __ "+b.toString());
			for (int i = 0; i < a.size(); i++) {
				temp = new ArrayList<String>();
				for (int j = 0; j < b.size(); j++) {
					if (a.get(i) != null && b.get(j) != null) {
						if (a.get(i).equalsIgnoreCase(b.get(j))) {
							temp.add(b.get(j));
						}
					}else
						return false;
				}
				if (temp.size() == b.size()) {
					return true;
				}
			}
			return false;
		}else
			return false;
	}
	
	private boolean haveElement(ArrayList<Element> list, Element e){
		for (Element element : list) {
			if (element.getName().equalsIgnoreCase(e.getName())) {
				index = list.indexOf(element);
				return true;
			}
		}
		return false;
	}
	
	private boolean arrayContains(String s, ArrayList<String> list){
		for (String string : list) {
			if (string.toUpperCase().contains(s.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	
	private Element mergeElement(Element e1, Element e2){
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

	public ArrayList<Element> getList() {
		return list;
	}

	public void setList(ArrayList<Element> list) {
		this.list = list;
	}

	public ArrayList<Element> getList1() {
		return list1;
	}

	public void setList1(ArrayList<Element> list1) {
		this.list1 = list1;
	}

	public ArrayList<Element> getList2() {
		return list2;
	}

	public void setList2(ArrayList<Element> list2) {
		this.list2 = list2;
	}

	public int getMergeType() {
		return mergeType;
	}

	public void setMergeType(int mergeType) {
		this.mergeType = mergeType;
	}

	public String getMedic() {
		return medic;
	}

	public void setMedic(String medic) {
		this.medic = medic;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}
}
