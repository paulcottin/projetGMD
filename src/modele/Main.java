package modele;
import exceptions.EmptyRequest;
import exceptions.NotFoundException;
import requests.CouchDBSearch;
import requests.TextSearch;
import vues.Fenetre;

/**
 * Reste à voir 
 * l'affichage de la cause et des symptoms de la recherche par maladie
 * Le merge des deux résultats
 * Revoir les informations récupérées par dans la recherche par M
 * @author paul
 *
 */

public class Main {
	public static void main(String args[]){
		SearchHandler search = new SearchHandler();
		Fenetre fen = new Fenetre(search);
		search.setDisease("fever");
//		search.setMedic("Lepirudin");
//		search.setMode(Search.AND);
//		try {
//			try {
//				search.search();
//			} catch (NotFoundException e) {
//				e.execute();
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
