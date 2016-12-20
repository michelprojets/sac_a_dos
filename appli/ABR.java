package appli;

public class ABR {
	
	private Objet[] value;
	private ABR leftTree, rightTree;
	
	private int profondeur;
	private static double borneInferieure; 
	// meilleure valeur trouvée pour l'instant (utile pour la construction ET pour solution()
	private double borneSuperieure;
	// la valeur max que pourra avoir la combinaison finale à partir d'un noeud
	
	private static Objet[] tabMeilleureValeur; 
	// tableau correspondant à la meilleure valeur trouvée dans l'arbre (par borne inférieure lors de la construction)
	
	
	/*
	 *  Constructeur récursif
	 *  Construction des combinaisons possibles ET qui ont un intérêt
	 */
	public ABR(Objet[] listeObjetsSac, double poidsLimite, Objet[] tabObj, int i){
		if (i <= listeObjetsSac.length) {
			
			// recopiage dans this.value le tableau tabObj
			this.value = new Objet[listeObjetsSac.length];
			for (int j=0; j<listeObjetsSac.length; ++j){
				if (tabObj[j] != null){
					this.value[j] = tabObj[j];
				}
			}
			
			this.profondeur = i;
			this.calculBorneSuperieure(listeObjetsSac);
			this.calculBorneInferieure();
			
			if (i != listeObjetsSac.length){
				this.leftTree = new ABR(listeObjetsSac, poidsLimite, tabObj, i+1);
			
				tabObj[i] = listeObjetsSac[i];
				if (this.poidsListeObjets(tabObj)<=poidsLimite && this.borneSuperieure>ABR.borneInferieure){
					// vérification pour raccourcir l'arbre (les combinaisons sans intérêts ne sont pas créées)
					this.rightTree = new ABR(listeObjetsSac, poidsLimite, tabObj, i+1);
				}
				tabObj[i] = null; // pour supprimer le dernier objet dans tabObj MAIS AUSSI dans this.value (car référence)
			}
			
		}
	}
	
	/*
	 * fonction récursive pour trouver la combinaison (en initialisant l'attribut statique tabMeilleureValeur)_ 
	 * à partir de la meilleure valeur trouvée dans tout le tableau (qui est obtenue avec borneInferieure en construisant l'arbre)
	 */
	public void chercherSolution(){
		if (this.valeurListeObjets() == ABR.borneInferieure){
			ABR.tabMeilleureValeur = this.value;
		}
		else {
			if (this.leftTree==null && this.rightTree==null){
				return;
			}
			if (this.leftTree==null){
				this.rightTree.chercherSolution();
			}
			if (this.rightTree==null){
				this.leftTree.chercherSolution();
			}
			if (this.rightTree!=null && this.leftTree!=null){
				this.rightTree.chercherSolution();
				this.leftTree.chercherSolution();
			}
		}	
	}
	
	public double getBorneInferieure(){
		return ABR.borneInferieure;
	}
	
	public double getBorneSuperieure(){
		return this.borneSuperieure;
	}
	
	/*
	 * mis à jour de l'attribut statique borneInferieure lorsqu'une meilleure valeur (correspondant à une combinaison) est trouvée
	 * mis à jour lors de la construction de l'arbre
	 */
	public void calculBorneInferieure(){
		if (this.valeurListeObjets() > ABR.borneInferieure){
			ABR.borneInferieure = this.valeurListeObjets();
		}
	}
	
	/*
	 * calcul pour chaque noeud (ABR) la valeur max que pourra avoir la combinaison finale à partir d'un noeud
	 */
	public void calculBorneSuperieure(Objet[] listeObjetsSac){
		double res = 0.0;
		res += this.valeurListeObjets(); // valeur totale du noeud courant
		for (int i=this.profondeur; i<listeObjetsSac.length; ++i){
			res += listeObjetsSac[i].getValeur(); // ajout des valeurs des objets restants
		}
		this.borneSuperieure = res;
	}
	
	/*
	 * retourne la valeur totale de this.value (tableau d'objets)
	 */
	public double valeurListeObjets(){
		double res=0.0;
		for(int i=0; i<this.value.length; ++i){
			if (this.value[i] != null){
				res += this.value[i].getValeur();
			}
		}
		return res;
	}
	
	/*
	 * retourne le poids total de this.value (tableau d'objets)
	 */
	public double poidsListeObjets(){
		double res=0.0;
		for(int i=0; i<this.value.length; ++i){
			if (this.value[i] != null){
				res += this.value[i].getValeur();
			}
		}
		return res;
	}
	
	/*
	 * retourne la valeur totale d'un tableau d'objets
	 */
	public double valeurListeObjets(Objet[] listeObjets){
		double res=0.0;
		for(int i=0; i<listeObjets.length; ++i){
			if (listeObjets[i] != null){
				res += listeObjets[i].getValeur();
			}
		}
		return res;
	}
	
	/*
	 * retourne le poids total d'un tableau d'objets
	 */
	public double poidsListeObjets(Objet[] listeObjets){
		double res=0.0;
		for(int i=0; i<listeObjets.length; ++i){
			if (listeObjets[i] != null){
				res += listeObjets[i].getPoids();
			}
		}
		return res;
	}

	public Objet[] getTabMeilleureValeur(){
		return ABR.tabMeilleureValeur;
	}
	
	public int getProfondeur(){
		return this.profondeur;
	}
	
}


