public class Pos {
    private Point2D deb;
    private Point2D fin;

    public Pos(int x1, int y1, int x2, int y2) {
        this.deb = new Point2D(x1, y1);
        this.fin = new Point2D(x2, y2);
    }

	public Point2D getDeb() {
		return deb;
	}

	public void setDeb(Point2D deb) {
		this.deb = deb;
	}

	public Point2D getFin() {
		return fin;
	}

	public void setFin(Point2D fin) {
		this.fin = fin;
	}

    
}
