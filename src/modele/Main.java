package modele;
import requests.XMLSearch;

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
		String medic = "Dornase alfa";
		String disease = "cystic fibrosis";
		Search search = new Search(medic, disease);
	}

}
