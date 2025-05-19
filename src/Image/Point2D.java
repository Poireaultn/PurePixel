package Image;

/**
 * Représente un point en 2 dimensions (x, y).
 * Utile pour localiser des positions dans une image ou un patch.
 * 
 * 
 *  Cette classe est utilisée pour définir des coordonnées dans un espace 2D,
 * comme les coins de sous-images ou de zones de traitement  
 * 
 * @author Charles
 * @version 1.0
 */
public class Point2D {
    protected int x;
    protected int y;

    /**
     * Constructeur d'un point 2D avec coordonnées spécifiées.
     *
     * @param x Coordonnée horizontale.
     * @param y Coordonnée verticale.
     */
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Renvoie la coordonnée x.
     *
     * @return Valeur de x.
     */
    public int getX() {
        return x;
    }

    /**
     * Modifie la coordonnée x.
     *
     * @param x Nouvelle valeur de x.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Renvoie la coordonnée y.
     *
     * @return Valeur de y.
     */
    public int getY() {
        return y;
    }

    /**
     * Modifie la coordonnée y.
     *
     * @param y Nouvelle valeur de y.
     */
    public void setY(int y) {
        this.y = y;
    }

	/**
	 * Renvoie une chaîne de caractères représentant le point.
	 *
	 * @return Représentation sous forme de chaîne.
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
	