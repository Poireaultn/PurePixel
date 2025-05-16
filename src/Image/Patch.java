package Image;
import java.util.*;

import Vecteur.Vecteur;

public class Patch {
	private Integer id;
	private Integer taille;
	private double[][] valeur;
	private Pos pos;

	public Patch(Integer id,Integer taille, Image img, int x1, int y1, int x2 , int y2) {
		this.id = id;
		this.taille = taille;
		this.valeur = new double[taille][taille];
		this.pos = new Pos(x1,y1,x2,y2);

		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				this.valeur[i][j] = img.getPixels()[y1 + i][x1 + j];
			}
		}
	}

	public Integer getTaille() {
		return this.taille;
	}
	
	public double[][] getValeur() {
		return this.valeur;
	}
	
	public Integer getID() {
		return this.id;
	}
	
	//Affiche le patch( matrice 2D)
	public void afficherPatch() {
	    System.out.println("Patch at position (" + pos.getDeb().getX() + ", " + pos.getDeb().getY() + "):");
	    for (int y = 0; y < taille; y++) {
	        System.out.print("[ ");
	        for (int x = 0; x < taille; x++) {
	            System.out.print(String.format("%.2f ", valeur[y][x]));
	        }
	        System.out.println("]");
	    }
	}

	//Permet de construire une list de Patch à partir de l'image de base et leur taille
	public static List<Patch> ExtractPatchs(Image img, int taille){
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
		int shift = 12;  //soit le max : Collections.max(shift_possible) ; soit aléatoire : //shift_possible.get(random.nextInt(shift_possible.size()));
		double[][] pixels = img.getPixels();
		
		for (int i=0;i<=height-taille; i+=shift) {
			for (int j=0;j<=width-taille; j+=shift) {
				
				double[][] matrice = new double[taille][taille];
				for (int k=0;k<taille; k++) {
					for (int l=0;l<taille; l++) {
						matrice[l][k] = pixels[i+l][j+k];
					}
				}
				
				Patch patch = new Patch(indice,taille,matrice);
				indice +=1;
				patchs.add(patch);
				
			}
		}		
		return patchs;
	}

	public static List<Vecteur> VectorPatchs(List<Patch> patchs) {
			
			List<Vecteur> vectors = new ArrayList<>();
			
			for (int i=0;i< patchs.size();i++){
				
				int taille = patchs.get(i).getTaille();
				int id = patchs.get(i).getID();
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

	public static List<Patch> PatchVectors(List<Vecteur> vecteurs) {
		
		List<Patch> patchs = new ArrayList<>();
		
		for (int i=0;i< vecteurs.size();i++){
			
			int taille = (int) Math.sqrt(vecteurs.get(i).getTaille());
			int id = vecteurs.get(i).getID();
			double[] colonne = vecteurs.get(i).getValeur();
			double[][] matrice = new double[taille][taille];
			
			for (int k=0;k<taille;k++) {
				for (int j=0;j<taille;j++) {
					matrice[k][j]=colonne[taille*k + j];
				}
			}
			Patch patch = new Patch(id,taille,matrice);
			patchs.add(patch);
		}
		return patchs;
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
		int shift = 12;  //soit le max : Collections.max(shift_possible) ; soit aléatoire : //shift_possible.get(random.nextInt(shift_possible.size()));
		double[][] pixels = img.getPixels();
		
		for (int i=0;i<=height-taille; i+=shift) {
			for (int j=0;j<=width-taille; j+=shift) {
				
				double[][] matrice = new double[taille][taille];
				for (int k=0;k<taille; k++) {
					for (int l=0;l<taille; l++) {
						matrice[l][k] = pixels[i+l][j+k];
					}
				}
				
				Patch patch = new Patch(indice, taille,matrice);
				indice +=1;
				patchs.add(patch);
				
			}
		}		
		return patchs;
	}
	

}
