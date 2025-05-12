package Image;
import java.util.*;
import java.util.Random;

public class Patch {
	private Integer IdPatch;
	private Integer Taille;
	private Double[][] Valeur;
	
	



public static int ExtractPatchs(Image img, Integer nbPatchs,Integer PatchSize){
	Random r= new Random();
	Patch newPatch = newPatchs();
	ArrayList<Patch> ListPatchs = new ArrayList<Patch>();
	Pos pos;
	for (Integer i=0; i<nbPatchs;i++) {
		pos.x = r.nextInt(img.getWidth()-1);
		pos.y = r.nextInt(img.getHeight() -1);
		System.out.println(pos.x);
		System.out.println(pos.y);
		
		
	}
	
	
	return 0;
}
}