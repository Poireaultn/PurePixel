package Image;
import java.util.*;

public class Patch {
	private Integer taille;
	private Double[][] valeur;
	private Pos pos;

	public Patch(Integer taille, Image img, int x, int y) {
		this.taille = taille;
		this.valeur = new Double[taille][taille];
		this.pos = new Pos(x, y);

		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				this.valeur[i][j] = img.getPixels()[y + i][x + j];
			}
		}
	}

	public Integer getTaille() {
		return this.taille;
	}

	public void afficherPatch() {
	    System.out.println("Patch at position (" + pos.x + ", " + pos.y + "):");
	    for (int y = 0; y < taille; y++) {
	        System.out.print("[ ");
	        for (int x = 0; x < taille; x++) {
	            System.out.print(String.format("%.2f ", valeur[y][x]));
	        }
	        System.out.println("]");
	    }
	}


	public static ArrayList<Patch> ExtractPatchs(Image img, Integer nbPatchs, Integer patchSize) {
		Random r = new Random();
		ArrayList<Patch> listPatchs = new ArrayList<>();

		for (int i = 0; i < nbPatchs; i++) {
			int x = r.nextInt(img.getWidth() - patchSize + 1);
			int y = r.nextInt(img.getHeight() - patchSize + 1);
			Patch newPatch = new Patch(patchSize, img, x, y);
			newPatch.afficherPatch();
			listPatchs.add(newPatch);
		}

		return listPatchs;
	}
}
