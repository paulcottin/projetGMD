package modele;
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
		Search search = new Search();
		Fenetre fen = new Fenetre(search);
		
	}

}
