package noising;

public class Vector {
	
	private int  IdVector;
	private int taille;
	private double[] valeur;
	
	
	public Vector(int idVector, int taille, double[] valeur) {
		IdVector = idVector;
		this.taille = taille;
		this.valeur = valeur;
	}
	
	public int getIdVector() {
		return IdVector;
	}
	public void setIdVector(int idVector) {
		IdVector = idVector;
	}
	public int getTaille() {
		return taille;
	}
	public void setTaille(int taille) {
		this.taille = taille;
	}
	public double[] getValeur() {
		return valeur;
	}
	public void setValeur(double[] valeur) {
		this.valeur = valeur;
	}

	
}
