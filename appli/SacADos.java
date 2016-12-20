package appli;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SacADos {
	private double poidsLimite;
	private Objet[] listeObjets; // les 3 algorithmes vont changer l'attribut stockage pour chaque Objet
	
	
	public SacADos(String chemin, double poidsLimite) throws FileNotFoundException{
		
		// initialisation de nbObj
		int nbObj = 0; // nombre d'objets dans le fichier
		try {
			Scanner sc1 = new Scanner(new File(chemin));
			while (sc1.hasNextLine()){
				++nbObj;
				sc1.nextLine();
			}
			sc1.close();
		}
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("Fichier introuvable");
		}
				
		this.listeObjets = new Objet[nbObj];
		this.poidsLimite = poidsLimite;
		String s;
		String[] tab = new String[3];
		int i=0;
		try {
			Scanner sc2 = new Scanner(new File(chemin));
			while (sc2.hasNextLine()){
				s = sc2.nextLine(); 
				tab = s.split("\\s+" + ";" + "\\s+");
				this.listeObjets[i] = new Objet(tab[0], new Double(tab[1]), new Double(tab[2]));
				++i;
			}
			sc2.close();
		} 
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("Fichier introuvable");
		}
	}
	
	public void résoudre(String cmd) throws Exception{
		if (cmd.equals("glouton")){
			this.glouton();
		}
		if (cmd.equals("dynamique")){
			this.dynamique();
		}
		if (cmd.equals("pse")){
			this.pse();
		}
	}
	
	//////////////////////////////////       ALGO GLOUTON        /////////////////////////////////////
	
	public void glouton(){
		 
		this.listeObjets = quickSort(this.listeObjets, 0, this.listeObjets.length-1); 
	//	this.listeObjets = triBulles(this.listeObjets);
		
		for (int i=0; i<this.listeObjets.length; ++i){
			this.listeObjets[i].setStockage(1);
			// modification de l'attribut stockage (pour connaître les objets stockés dans le sac)
			if (poidsSac(this.listeObjets)>poidsLimite){
				this.listeObjets[i].setStockage(0);
			}
		}
	}
	
	
	/*
	 * fonction qui prend un tableau pour effectuer un tri rapide dessus
	 * @return un tableau trié
	 */
	public Objet[] quickSort(Objet[] listeObjets, int premier, int dernier){
		if (premier < dernier){
			TabEtPivot infos = new TabEtPivot();
			int p = choixPivot(listeObjets, premier, dernier);
			infos = repartition(listeObjets, premier, dernier, p);
			quickSort(infos.getTab(), premier, infos.getPivot()-1); 
			quickSort(infos.getTab(), infos.getPivot()+1, dernier); 
		}
		return listeObjets;
	}
	
	/*
	 * choix du pivot
	 * @return indice du pivot
	 */
	public int choixPivot(Objet[] listeObjets, int premier, int dernier){
		return (premier + dernier) / 2;
	}
	
	/*
	 * fonction de répartition (met les éléments < pivot a gauchet et > pivot à droite
	 * @return un struct (tableau + pivot)
	 */
	public TabEtPivot repartition(Objet[] listeObjets, int premier, int dernier, int pivot){
		TabEtPivot infos = new TabEtPivot();
		listeObjets = echanger(listeObjets, pivot, dernier);
		int j = premier;
		for (int i=premier; i<dernier; ++i){
			if (listeObjets[i].compareTo(listeObjets[dernier]) > 0){ // tri décroissant
				echanger(listeObjets,i,j);
				j++;
			}
		}
		listeObjets = echanger(listeObjets, dernier, j);
		infos.setTab(listeObjets);
		infos.setPivot(j);
		return infos;
	}
	
	/*
	 * fonction pour échanger deux valeurs dans le tableau
	 */
	public Objet[] echanger(Objet[] listeObjets, int i, int j){
		Objet tmp = listeObjets[i];
		listeObjets[i] = listeObjets[j];
		listeObjets[j] = tmp;
		return listeObjets;
	}

	////////////////////////////////////       ALGO DYNAMIQUE        /////////////////////////////
	
	public void dynamique(){
		
		int[][] tab = new int[listeObjets.length][(int) ((poidsLimite*Appli.nbAMultiplier)+1)];
		
		// remplissage premiere colonne
		for (int i=0; i<=poidsLimite*Appli.nbAMultiplier; ++i){ // et non pas i<poidsLimite car taille=poidsLimite+1
			if (listeObjets[0].getPoids()*Appli.nbAMultiplier > i){
				tab[0][i]=0;
			}
			else{
				tab[0][i]=(int) (listeObjets[0].getValeur());
			}
		}
		
		// remplissage des autres lignes du tableau
		for (int i=1; i<listeObjets.length; ++i){
			for (int j=0; j<=poidsLimite*Appli.nbAMultiplier; ++j){ // et non pas i<poidsLimite car taille=poidsLimite+1
				if (listeObjets[i].getPoids()*Appli.nbAMultiplier > j){
					tab[i][j] = tab[i-1][j];
				}
				else{
					tab[i][j] = (int) (Math.max(tab[i-1][j], tab[i-1][(int) (j-(listeObjets[i].getPoids()*Appli.nbAMultiplier))]+listeObjets[i].getValeur())); 
					// pour faire toutes les combinaisons possibles
				}
			}
		}
		
		// on récupère dans la dernière ligne le poids minimal nécessaire pour faire le bénéfice optimal
		int i=listeObjets.length-1;
		int j=(int) (poidsLimite*Appli.nbAMultiplier);
		while (tab[i][j]==tab[i][j-1]){
			--j;
		}
		
		// on récupère ensuite les objets
		while(j>0){
			while(i>0 && tab[i][(int) j]==tab[i-1][(int) j]){ 
			// si sans l'objet on fait la même valeur (même nombre sur la colonne)
				--i;
			}
			
			j=j-(int) (listeObjets[i].getPoids()*Appli.nbAMultiplier); 
			// on ne prend plus en compte le poids de l'objet précédent (on retire son poids)
			if (j>=0){ 
				this.listeObjets[i].setStockage(1); 
				// modification de l'attribut stockage (pour connaître les objets stockés dans le sac)
			}
			--i;
		}
	}
	
	////////////////////////////////////////       ALGO PSE       //////////////////////////////
	
	public void pse(){
		Objet[] tabObj = new Objet[this.listeObjets.length];

		ABR arbre = new ABR(this.listeObjets, this.poidsLimite, tabObj, 0);
		
		arbre.chercherSolution();
		
		Objet[] tabSolution = arbre.getTabMeilleureValeur();
		
		for (int i=0; i<this.listeObjets.length; ++i){
			if (tabSolution[i] != null){
				this.listeObjets[i].setStockage(1);
				// modification de l'attribut stockage (pour connaître les objets stockés dans le sac)
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * fonction qui retourne le poids total pour une liste d'objets
	 */
	public double poidsSac(Objet[] listeObjets){
		double res=0.0;
		for(int i=0; i<listeObjets.length; ++i){
			if (listeObjets[i] != null){
				res += listeObjets[i].getStockage() * listeObjets[i].getPoids();
			}
		}
		return res;
	}
	
	/*
	 * fonction qui retourne la valeur totale pour une liste d'objets
	 */
	public double valeurSac(Objet[] listeObjets){
		double res=0.0;
		for(int i=0; i<listeObjets.length; ++i){
			if (listeObjets[i] != null){
				res += listeObjets[i].getStockage() * listeObjets[i].getValeur();
			}
		}
		return res;
	}
	
	/*
	 * obtention de l'objet i
	 */
	public Objet getObjet(int i){
		return this.listeObjets[i];
	}
	
	/*
	 * retourne la liste des objets pour ce SacADos
	 */
	public Objet[] getListeObjets(){
		return this.listeObjets;
	}
	
	/*
	 * retourne le poids limite pour ce SacADos
	 */
	public double getPoidsLimite(){
		return this.poidsLimite;
	}
	
	public String toString(){
		String s = "";
		s += "Poids total du sac : " + this.poidsSac(this.listeObjets) + System.lineSeparator();
		s += "Valeur totale du sac : " + this.valeurSac(this.listeObjets) + System.lineSeparator();
		for (Objet o : this.listeObjets){
			if (o.getStockage() == 1){
				s+= o.toString() + System.lineSeparator();
			}
		}
		return s;
	}
}
