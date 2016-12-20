package appli;

//structure pour stocker la valeur de retour de la fonction de répartition du quicksort
public class TabEtPivot { 
	private Objet[] tab;
	private int pivot;
	
	public Objet[] getTab(){
		return this.tab;
	}
	
	public int getPivot(){
		return this.pivot;
	}
	
	public void setTab(Objet[] tab){
		this.tab = tab;
	}
	
	public void setPivot(int p){
		this.pivot = p;
	}

}
