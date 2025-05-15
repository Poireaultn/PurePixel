package Vecteur;

public class Vecteur {
	    private int taille;
	    private double[] valeur;
	    
	    
	    //Constructeur par défaut
	    public Vecteur() {
	        this.taille = 0;
	        this.valeur = new double[0];
	    }
	    
	    //Constructeur avec paramètres
	    public Vecteur(int taille, double[] valeur) {
	        this.taille = taille;
	        this.valeur = valeur;
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
	        sb.append("Vecteur ID: ").append(", Taille: ").append(taille).append(", Valeurs: [");
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
