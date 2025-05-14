package noising;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Patch {
	
	private int  IdPatch;
	private int taille;
	private double[][] valeur;
	
	
	public Patch(int idPatch, int taille, double[][] valeur) {
		IdPatch = idPatch;
		this.taille = taille;
		this.valeur = valeur;
	}
	public static List<Vecteur> VectorPatchs(List<Patch> patchs) {
			
			List<Vecteur> vectors = new ArrayList<>();
			
			for (int i=0;i< patchs.size();i++){
				
				int taille = patchs.get(i).getTaille();
				int id = patchs.get(i).getIdPatch();
				double[][] matrice = patchs.get(i).getValeur();
				double[] colonne = new double[taille*taille];
				
				for (int k=0;k<taille*taille;k++) {
					colonne[k] = matrice[k/taille][k%taille];
				}
				Vecteur vector = new Vecteur(id,taille*taille,colonne);
				vectors.add(vector);
			}
			return vectors;
	}
	public static List<Patch> ExtractPatchs(Image img, int taille){
		//Random random = new Random();
		List<Patch> patchs = new ArrayList<>();
		List<Integer> shift_possible = new ArrayList<>();
		
		int height = img.getHeight();
		int width = img.getWidth();
		int indice = 0;
		for (int i = 1; i <= Math.max(Math.sqrt(height-taille),Math.sqrt(width-taille)); i++) {
	        if ((height-taille) % i == 0 || (width-taille) % i == 0){
	            shift_possible.add(i);
	        }
	    }
		int shift = 12;//Collections.max(shift_possible) ;//shift_possible.get(random.nextInt(shift_possible.size()));
		double[][] pixels = img.getPixels();
		
		for (int i=0;i<=height-taille; i+=shift) {
			for (int j=0;j<=width-taille; j+=shift) {
				
				double[][] matrice = new double[taille][taille];
				for (int k=0;k<taille; k++) {
					for (int l=0;l<taille; l++) {
						matrice[l][k] = pixels[j+k][i+l];
					}
				}
				
				Patch patch = new Patch(indice, taille,matrice);
				indice +=1;
				patchs.add(patch);
				
			}
		}		
		return patchs;
	}
	
	
	public static void afficherPatch(Patch patch) {
		System.out.println("le Patch est d'incide : "+ patch.getIdPatch());
		for (int y = 0; y < patch.getTaille(); y++) {
			System.out.print("[ ");
			for (int x = 0; x < patch.getTaille(); x++) {
				System.out.printf("%.2f ", patch.getValeur()[y][x]);
			}
			System.out.println("]");
		}
	}
	
	
	
	public int getIdPatch() {
		return IdPatch;
	}
	public void setIdPatch(int idPatch) {
		IdPatch = idPatch;
	}
	public int getTaille() {
		return taille;
	}
	public void setTaille(int taille) {
		this.taille = taille;
	}
	public double[][] getValeur() {
		return valeur;
	}
	public void setValeur(double[][] valeur) {
		this.valeur = valeur;
	}
	
}
