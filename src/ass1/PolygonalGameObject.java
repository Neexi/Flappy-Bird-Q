package ass1;

import javax.media.opengl.GL2;

/**
 * A game object that has a polygonal shape.
 * 
 * This class extend GameObject to draw polygonal shapes.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class PolygonalGameObject extends GameObject {

    private double[] myPoints;
    private double[] myFillColour;
    private double[] myLineColour;

    /**
     * Create a polygonal game object and add it to the scene tree
     * 
     * The polygon is specified as a list of doubles in the form:
     * 
     * [ x0, y0, x1, y1, x2, y2, ... ]
     * 
     * The line and fill colours can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param points A list of points defining the polygon
     * @param fillColour The fill colour in [r, g, b, a] form
     * @param lineColour The outlien colour in [r, g, b, a] form
     */
    public PolygonalGameObject(GameObject parent, double points[],
            double[] fillColour, double[] lineColour) {
        super(parent);
        
        myPoints = points;
        myFillColour = fillColour;
        myLineColour = lineColour;
    }

    /**
     * Get the polygon
     * 
     * @return
     */
    public double[] getPoints() {        
        return myPoints;
    }

    /**
     * Set the polygon
     * 
     * @param points
     */
    public void setPoints(double[] points) {
        myPoints = points;
    }

    /**
     * Get the fill colour
     * 
     * @return
     */
    public double[] getFillColour() {
        return myFillColour;
    }

    /**
     * Set the fill colour.
     * 
     * Setting the colour to null means the object should not be filled.
     * 
     * @param fillColour The fill colour in [r, g, b, a] form 
     */
    public void setFillColour(double[] fillColour) {
        myFillColour = fillColour;
    }

    /**
     * Get the outline colour.
     * 
     * @return
     */
    public double[] getLineColour() {
        return myLineColour;
    }

    /**
     * Set the outline colour.
     * 
     * Setting the colour to null means the outline should not be drawn
     * 
     * @param lineColour
     */
    public void setLineColour(double[] lineColour) {
        myLineColour = lineColour;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * TODO: Draw the polygon
     * 
     * if the fill colour is non-null, fill the polygon with this colour
     * if the line colour is non-null, draw the outline with this colour
     * 
     * @see ass1.spec.GameObject#drawSelf(javax.media.opengl.GL2)
     */
    @Override
    public void drawSelf(GL2 gl) {
    	
        gl.glEnable (GL2.GL_BLEND);
        gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        
    	if(myFillColour != null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
    		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
        	gl.glBegin(GL2.GL_POLYGON);
            {	
            	int index = 0;
            	while(index < myPoints.length) {
    	        	gl.glVertex2d(myPoints[index],myPoints[index+1]);
    	        	index = index+2;
            	}
            }
            gl.glEnd();
    	}
        
    	if(myLineColour != null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2],myLineColour[3]);
            gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        	gl.glBegin(GL2.GL_POLYGON);
            {	
            	int index = 0;
            	while(index < myPoints.length) {
    	        	gl.glVertex2d(myPoints[index],myPoints[index+1]);
    	        	index = index+2;
            	}
            }
            gl.glEnd();
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
    	}
    }
    
 // Extra method
	/**
     * Return true if point p is inside or on the boundary of the convex polygon obj
     * @param obj
     * @param p
     * @return
     */
	public boolean inPolygon(double[] p) {
	 	int crossing = 0; //Number of crossing of point p in polygon
	 	
	 	//Loading up value for finding global point
		double[] position = getGlobalPosition();
    	double rotation = Math.toRadians(MathUtil.normaliseAngle(getGlobalRotation()));
    	double scale = getGlobalScale();
    	
    	//Finding the global point
    	double[] x = new double[myPoints.length/2];
    	double[] y = new double[myPoints.length/2];
    	int index = 0;
    	int array = 0;
    	while(index < myPoints.length) {
    		x[array] = position[0] +
        			(myPoints[index] * Math.cos(rotation) -
					myPoints[index+1] * Math.sin(rotation)) 
	    			* scale;
    		y[array] = position[1] +
        			(myPoints[index] * Math.sin(rotation) +
					myPoints[index+1] * Math.cos(rotation)) 
	    			* scale;
    		array++;
    		index = index+2;
    	}
    	index = 0;
    	while(index < array - 1) {
    		double intercept = xIntercept(p[1], x[index], y[index], x[index+1], y[index+1]);
    		if(onLine(p,x[index],y[index],x[index+1],y[index+1])) {
    			return true;
    		}
    		if((p[1] < Math.max(y[index], y[index+1]) && p[1] > Math.min(y[index], y[index+1])) || //the point y coordinate is between line y coordinates
    				(y[index] != y[index+1] && p[1] == Math.min(y[index], y[index+1])) && 
    				p[0] <= intercept) { //the point y coordinate is lower than axis of line y coordinates
    			crossing++;
    		}
    		index++;
    	}
    	
    	//Between first point and last point
    	double intercept = xIntercept(p[1], x[0], y[0], x[index], y[index]);
    	if(onLine(p,x[0],y[0],x[index],y[index])) {
			return true;
		}
    	if(((p[1] < Math.max(y[0], y[index]) && p[1] > Math.min(y[0], y[index])) || //the point y coordinate is between line y coordinates
				(y[0] != y[index] && p[1] == Math.min(y[0], y[index]))) && 
				p[0] <= xIntercept(p[1], x[0], y[0], x[index], y[index])) { //the point y coordinate is equal to lower axis of line y coordinates
			if(p[0] == intercept) {
    			return true;
    		}
    		crossing++;
		}
    	
    	if(crossing % 2 == 1) {
    		return true;
    	} else {
    		return false;
    	}
    }
	
	/**
	 * finding the x intercept of a line with point (x1,y1) and (x2,y2)
	 * @param y
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double xIntercept(double y, double x1, double y1, double x2, double y2) {
		//From equation y-y1 = (y2-y1/x2-x1)(x-x1)
		//Then x = (y-y1)/(y2-y1/x2-x1) + x1
		if(x1 != x2) {
			return (y-y1)/(y2-y1/x2-x1) + x1;
		} else {
			return x1;
		}
	}
	
	/**
	 * Return true if point p is on the line obj
	 * @param p
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
    public boolean onLine(double[] p, double x1, double y1, double x2, double y2) {
    	//Loading up value for finding global point
    	double[] position = getGlobalPosition();
    	double rotation = Math.toRadians(MathUtil.normaliseAngle(getGlobalRotation()));
    	double scale = getGlobalScale();
    	
    	//Changing local point to global point
    	double x1Global = position[0] +
    			(x1 * Math.cos(rotation) -
    			y1 * Math.sin(rotation)) 
    			* scale;
    	double y1Global = position[1] +
    			(x1 * Math.sin(rotation) +
    			y1 * Math.cos(rotation)) 
    			* scale;
    	double x2Global = position[0] +
    			(x2 * Math.cos(rotation) -
    			y2 * Math.sin(rotation)) 
    			* scale;
    	double y2Global = position[1] +
    			(x2 * Math.sin(rotation) +
    			y2 * Math.cos(rotation)) 
    			* scale;

    	if(Math.abs(
    			p[1] - y1Global - 
    			((y2Global-y1Global)/(x2Global-x1Global)) * (p[0] - x1Global)) 
    			== 0) {
    		
    		if(p[0] >= Math.min(x1Global,x2Global) && p[0] <= Math.max(x1Global,x2Global)) {
    			if(p[1] >= Math.min(y1Global,y2Global) && p[1] <= Math.max(y1Global,y2Global)) {
    				return true;
    			} else {
    				return false;
    			}
    		} else {
    			return false;
    		}
    	} else {
    		return false;
    	}
    }

}
