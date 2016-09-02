package ass1;

import javax.media.opengl.GL2;

/**
 * A game object that has a circular shape.
 * 
 * This class extend GameObject to draw circular shapes.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class CircularGameObject extends GameObject {

	private double myRadius;
    private double[] myFillColour;
    private double[] myLineColour;
    
    double numVertices = 32; //default number of polygon side to draw a circle

    /**
     * Create a circular game object and add it to the scene tree
     * 
     * The circle radius is specified as a double
     * 
     * The line and fill colours can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param radius The radius of the circle
     * @param fillColour The fill colour in [r, g, b, a] form
     * @param lineColour The outlien colour in [r, g, b, a] form
     */
    public CircularGameObject(GameObject parent, double radius,
            double[] fillColour, double[] lineColour) {
        super(parent);

        myRadius = radius;
        myFillColour = fillColour;
        myLineColour = lineColour;
    }
    
    /**
     * Create a circular game object with fixed radius 1 and add it to the scene tree
     * 
     * The line and fill colours can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param fillColour The fill colour in [r, g, b, a] form
     * @param lineColour The outlien colour in [r, g, b, a] form
     */
    public CircularGameObject(GameObject parent,
            double[] fillColour, double[] lineColour) {
        super(parent);

        myRadius = 1;
        myFillColour = fillColour;
        myLineColour = lineColour;
    }

    /**
     * Get the circle radius
     * 
     * @return
     */
    public double getRadius() {        
        return myRadius;
    }

    /**
     * Set the circle radius
     * 
     * @param points
     */
    public void setRadius(double radius) {
        myRadius = radius;
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
     * TODO: Draw the Circle
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
            	double angle = 0;
                double angleIncrement = 2*Math.PI/numVertices;
                for(int i=0; i < numVertices; i++){
                	angle = i* angleIncrement;
                	double x = myRadius * Math.cos(angle);
                	double y = myRadius * Math.sin(angle);
                	gl.glVertex2d(x, y);
                }
            }
            gl.glEnd();
    	}
        
    	if(myLineColour != null) {
            gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
            gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2],myLineColour[3]);
        	gl.glBegin(GL2.GL_POLYGON);
            {	
            	double angle = 0;
                double angleIncrement = 2*Math.PI/numVertices;
                for(int i=0; i < numVertices; i++){
                	angle = i* angleIncrement;
                	double x = myRadius * Math.cos(angle);
                	double y = myRadius * Math.sin(angle);
                	gl.glVertex2d(x, y);
                }
            }
            gl.glEnd();
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
    	}
    }
    
    /**
     * Return true if point p is inside the or on the boundary of circle obj
     * @param p
     * @return
     */
    public boolean inCircle(double[] p) {
    	//Loading up value for finding global point
    	double[] position = getGlobalPosition();
    	double scale = getGlobalScale();
    	
    	//Changing local point to global point
    	double[] center = position;
    	double radius = myRadius * scale;
    	if((p[0] - center[0])*(p[0] - center[0]) + (p[1] - center[1])*(p[1] - center[1]) <= radius*radius) {
    		return true;
    	} else {
    		return false;
    	}
    }


}
