package appli;

@SuppressWarnings("rawtypes")
public class Objet implements Comparable{
	private String nom;
	private double valeur;
	private double poids;
	private double rapport;
	private int stockage;
	
	public Objet(String n, double p, double v){
		this.nom = n;
		this.valeur = v;
		this.poids = p;
		this.rapport = v/p;
		this.stockage = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * comparaison de cet objet avec un autre objet (par rapport au rapport)
	 */
	@Override
	public int compareTo(Object o){
		if (this.rapport > ((Objet)o).getRapport()){
			return 1;
		}
		else
			if (this.rapport == ((Objet)o).getRapport()){
				return 0;
			}
			else{
				return -1;
			}
		
	}
	
	public String toString(){
		return this.nom;
	}

	public double getValeur() {
		return this.valeur;
	}

	public double getPoids() {
		return this.poids;
	}

	public int getStockage() {
		return this.stockage;
	}
	
	public double getRapport(){
		return this.rapport;
	}

	public void setStockage(int n) {
		this.stockage = n;
	}

}
