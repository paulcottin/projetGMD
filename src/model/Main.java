package model;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import exceptions.EmptyRequest;
import exceptions.NotFoundException;
import requests.CouchDBSearch;
import requests.TextSearch;
import view.Window;

public class Main {
	public static void main(String args[]){
		SearchHandler search = new SearchHandler();
		Window fen = new Window(search);
		search.initSynonyms();
//		search.setDisease("hirudin");
//		search.setDrug("Lepirudin");
//		search.setMode(Search.AND);
//		try {
//			try {
//				search.search();
//			} catch (NotFoundException e) {
//				e.execute();
//			} catch (CommunicationsException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
