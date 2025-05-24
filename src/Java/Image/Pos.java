package Java.Image;

/**
 * Représente une position rectangulaire dans une image,
 * définie par deux points : début (haut gauche) et fin (bas droit).
 * 
 * Cette classe est utilisée pour localiser des zones spécifiques
 * 
 * @author Charles
 * @version 1.0
 */
public class Pos {
    private Point2D deb;
    private Point2D fin;

    /**
     * Crée une position à partir de deux couples de coordonnées.
     *
     * @param x1 Coordonnée x du point de début.
     * @param y1 Coordonnée y du point de début.
     * @param x2 Coordonnée x du point de fin.
     * @param y2 Coordonnée y du point de fin.
     */
    public Pos(int x1, int y1, int x2, int y2) {
        this.deb = new Point2D(x1, y1);
        this.fin = new Point2D(x2, y2);
    }

    /**
     * Retourne le point de début.
     *
     * @return Point2D début.
     */
    public Point2D getDeb() {
        return deb;
    }

    /**
     * Modifie le point de début.
     *
     * @param deb Nouveau point de début.
     */
    public void setDeb(Point2D deb) {
        this.deb = deb;
    }

    /**
     * Retourne le point de fin.
     *
     * @return Point2D fin.
     */
    public Point2D getFin() {
        return fin;
    }

    /**
     * Modifie le point de fin.
     *
     * @param fin Nouveau point de fin.
     */
    public void setFin(Point2D fin) {
        this.fin = fin;
    }
}