package Image;
import java.util.*;

import ACP.ACP;
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
    
    // Constructeur 2 : à partir de la matrice + position explicite
    public Patch(Integer id, Integer taille, double[][] matrice, Pos pos) {
        if (taille <= 0) throw new IllegalArgumentException("taille doit être positive");
        if (matrice.length != taille || matrice[0].length != taille)
            throw new IllegalArgumentException("Dimensions matrice incorrectes");
        this.id = id;
        this.taille = taille;
        this.valeur = matrice;
        this.pos = pos;
    }
    
    // Constructeur 3 : matrice sans position (pos=null)
    public Patch(Integer id, Integer taille, double[][] matrice) {
        this(id, taille, matrice, null);
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
    
    public Pos getPos() {
        return this.pos;
    }
    
    public void setPos(Pos pos) {
        this.pos = pos;
    }
    
    // Affiche le patch( matrice 2D)
    public void afficherPatch() {
        for (int y = 0; y < taille; y++) {
            System.out.print("[ ");
            for (int x = 0; x < taille; x++) {
                System.out.print(String.format("%.2f ", valeur[y][x]));
            }
            System.out.println("]");
        }
    }

    
    public Vecteur toVecteur() {
        double[] valeurs = new double[taille * taille];
        int index = 0;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                valeurs[index++] = this.valeur[i][j]; 
            }
        }
        return new Vecteur(this.id, valeurs.length, valeurs);
    }

    public static List<Vecteur> VectorPatchs(List<Patch> patchs) {
        List<Vecteur> vectors = new ArrayList<>();
        for (Patch p : patchs) {
            int taille = p.getTaille();
            int id = p.getID();
            double[][] matrice = p.getValeur();
            double[] colonne = new double[taille * taille];
            for (int k = 0; k < taille * taille; k++) {
                colonne[k] = matrice[k / taille][k % taille];
            }
            vectors.add(new Vecteur(id, taille * taille, colonne));
        }
        return vectors;
    }

    public static List<Patch> PatchVectors(List<Vecteur> vecteurs) {
        List<Patch> patchs = new ArrayList<>();
        for (Vecteur v : vecteurs) {
            int taille = (int) Math.sqrt(v.getTaille());
            int id = v.getID();
            double[] colonne = v.getValeur();
            double[][] matrice = new double[taille][taille];
            for (int k = 0; k < taille; k++) {
                for (int j = 0; j < taille; j++) {
                    matrice[k][j] = colonne[taille * k + j];
                }
            }
            patchs.add(new Patch(id, taille, matrice));
        }
        return patchs;
    }
    
    public static int calculerShift(Image img, int taille) {
        List<Integer> shift_possible = new ArrayList<>();
        int height = img.getHeight();
        int width = img.getWidth();

       

        for (int i = 1; i < taille; i++) { // on reste strictement < taille
            if ((height - taille) % i == 0 || (width - taille) % i == 0) {
                shift_possible.add(i);
            }
        }

        if (shift_possible.isEmpty()) {
            throw new IllegalArgumentException("Aucun shift valide trouvé strictement inférieur à la taille des patchs (" + taille + ")");
        }

        return Collections.max(shift_possible);
    }

    
    public static List<Patch> ExtractPatchs(Image img, int taille, int shift) {
        List<Patch> patchs = new ArrayList<>();
        int height = img.getHeight();
        int width = img.getWidth();
        int indice = 0;

        if (height < taille || width < taille) {
            throw new IllegalArgumentException("L'image est trop petite pour des patchs de taille " + taille);
        }

        double[][] pixels = img.getPixels();

        for (int i = 0; i <= height - taille; i += shift) {
            for (int j = 0; j <= width - taille; j += shift) {
                patchs.add(createPatch(pixels, i, j, taille, indice++));
            }
        }

        // Ajoute patchs à droite si pas couvert
        if ((width - taille) % shift != 0) {
            int j = width - taille;
            for (int i = 0; i <= height - taille; i += shift) {
                patchs.add(createPatch(pixels, i, j, taille, indice++));
            }
        }

        // Ajoute patchs en bas si pas couvert
        if ((height - taille) % shift != 0) {
            int i = height - taille;
            for (int j = 0; j <= width - taille; j += shift) {
                patchs.add(createPatch(pixels, i, j, taille, indice++));
            }
        }

        // Ajoute patch coin bas-droit si nécessaire
        if ((width - taille) % shift != 0 && (height - taille) % shift != 0) {
            patchs.add(createPatch(pixels, height - taille, width - taille, taille, indice++));
        }

        return patchs;
    }

    private static Patch createPatch(double[][] pixels, int i, int j, int taille, int indice) {
        double[][] matrice = new double[taille][taille];
        for (int k = 0; k < taille; k++) {
            for (int l = 0; l < taille; l++) {
                matrice[k][l] = pixels[i + k][j + l];
            }
        }
        return new Patch(indice, taille, matrice, new Pos(j, i, j + taille - 1, i + taille - 1));
    }


}
