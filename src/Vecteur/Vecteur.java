package Vecteur;

public class Vecteur {
		private int IDVecteur;
	    private int taille;
	    private double[] valeur;
	    
	    
	    //Constructeur par défaut
	    public Vecteur() {
	        this.IDVecteur = 0;
	        this.taille = 0;
	        this.valeur = new double[0];
	    }
	    
	    //Constructeur avec paramètres
	    public Vecteur(int IDVecteur, int taille, double[] valeur) {
	        this.IDVecteur = IDVecteur;
	        this.taille = taille;
	        this.valeur = valeur;
	    }
	    
	    //Getter et Setter idVecteur
	    public int getIDVecteur() {
	        return IDVecteur;
	    }

	    public void setIDVecteur(int IDVecteur) {
	        this.IDVecteur = IDVecteur;
	    }

	    //Getter et Setter taille
	    public int getTaille() {
	        return taille;
	    }

	    public void setTaille(int taille) {
	        this.taille = taille;
	    }

	    //Getter et Setter Valeur
	    public double[] getValeur() {
	        return valeur;
	    }

	    public void setValeur(double[] valeur) {
	        this.valeur = valeur;
	    }
	    
	    
	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Vecteur ID: ").append(IDVecteur).append(", Taille: ").append(taille).append(", Valeurs: [");
	        for (int i = 0; i < valeur.length; i++) {
	            sb.append(valeur[i]);
	            if (i < valeur.length - 1) {
	                sb.append(", ");
	            }
	        }
	        sb.append("]");
	        return sb.toString();
	    }

}
