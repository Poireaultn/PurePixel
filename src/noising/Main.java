package noising;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
    	double[][] valeur = {
    		    {1, 2, 3},
    		    {4, 5, 6},
    		    {7, 8, 9}
    		};
    	Patch patch1 = new Patch(1, 3, valeur);
    	Patch patch2 = new Patch(2, 3, valeur);
    	Patch patch3 = new Patch(3, 3, valeur);
    	Patch patch4 = new Patch(4, 3, valeur);
    	
    	List<Patch> patchs = new ArrayList<>();
    	patchs.add(patch1);
    	patchs.add(patch2);
    	patchs.add(patch3);
    	patchs.add(patch4);
    	
    	List<Vector> vectors = Patch.VectorPatchs(patchs);
    	System.out.println(Arrays.toString(vectors.get(0).getValeur()));
        
    }
}

