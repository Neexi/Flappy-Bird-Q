package ass1;

public class RectangularGameObject extends PolygonalGameObject {
	
	private double xLeft;
	private double yTop;
	private double xRight;
	private double yBot;
	
	public RectangularGameObject(GameObject parent, double[] points,
			double[] fillColour, double[] lineColour) {
		super(parent, points, fillColour, lineColour);
		xLeft = points[0];
		yTop = points[5];
		xRight = points[2];
		yBot = points[1];
	}
	
	 //Limited to game only
    public double getRightCorner() {
    	double[] global_pos = getGlobalPosition();
    	double[] point_pos = getPoints();
    	return global_pos[0] + point_pos[2];
    }
    
    public boolean rectangleCollision(double[] otherRectangle) {
    	double[] global_pos = getGlobalPosition();
    	if((otherRectangle[0] <= xRight+global_pos[0] && otherRectangle[0] >= xLeft+global_pos[0]) ||
    			(otherRectangle[2] <= xRight+global_pos[0] && otherRectangle[2] >= xLeft+global_pos[0])) {
    		if((otherRectangle[1] <= yTop && otherRectangle[1] >= yBot) ||
        			(otherRectangle[3] <= yTop && otherRectangle[3] >= yBot)) {
	    		return true;
    		}
    	}
    	return false;
    }

}
