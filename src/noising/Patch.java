package noising;

import java.util.ArrayList;
import java.util.List;

public class Patch {
	private int  IdPatch;
	private int Taille;
	private double[][] Valeur;
	
	
	
	public Patch(int idPatch, int taille, double[][] valeur) {
		IdPatch = idPatch;
		Taille = taille;
		Valeur = valeur;
	}
	
	
	public int getIdPatch() {
		return IdPatch;
	}
	public void setIdPatch(int idPatch) {
		IdPatch = idPatch;
	}
	public int getTaille() {
		return Taille;
	}
	public void setTaille(int taille) {
		Taille = taille;
	}
	public double[][] getValeur() {
		return Valeur;
	}
	public void setValeur(double[][] valeur) {
		Valeur = valeur;
	}
	public static List<Vector> VectorPatchs(List<Patch> patchs) {
			
			List<Vector> vectors = new ArrayList<>();
			
			for (int i=0;i< patchs.size();i++){
				
				int taille = patchs.get(i).getTaille();
				int id = patchs.get(i).getIdPatch();
				double[][] matrice = patchs.get(i).getValeur();
				double[] colonne = new double[taille*taille];
				
				for (int k=0;k<taille*taille;k++) {
					colonne[k] = matrice[k/taille][k%taille];
				}
				Vector vector = new Vector(id,taille*taille,colonne);
				vectors.add(vector);
			}
			return vectors;
	}
}
