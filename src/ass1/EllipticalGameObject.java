package ass1;

import javax.media.opengl.GL2;

public class EllipticalGameObject extends GameObject {
	private double myRadius;
	private double myRatio;
    private double[] myFillColour;
    private double[] myLineColour;
    
    double numVertices = 32; //default number of polygon side to draw a ellipse

    /**
     * Create a elliptical game object and add it to the scene tree
     * 
     * The ellipse radius is specified as a double
     * 
     * The line and fill colours can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param radius The x radius of the ellipse
     * @param ratio  The y to x ratio of the ellipse
     * @param fillColour The fill colour in [r, g, b, a] form
     * @param lineColour The outlien colour in [r, g, b, a] form
     */
    public EllipticalGameObject(GameObject parent, double radius, double ratio,
            double[] fillColour, double[] lineColour) {
        super(parent);

        myRadius = radius;
        myRatio = ratio;
        myFillColour = fillColour;
        myLineColour = lineColour;
    }

    /**
     * Get the ellipse radius
     * 
     * @return
     */
    public double getRadius() {        
        return myRadius;
    }

    /**
     * Set the ellipse radius
     * 
     * @param points
     */
    public void setRadius(double radius) {
        myRadius = radius;
    }
    
    /**
     * get the ellipse x y ratio
     * @return
     */
    public double getRatio() {
    	return myRatio;
    }
    
    /**
     * set the ellipse x y ratio
     * @param ratio
     */
    public void setRatio(double ratio) {
    	myRatio = ratio;
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
     * TODO: Draw the Ellipse
     * 
     * if the fill colour is non-null, fill the polygon with this colour
     * if the line colour is non-null, draw the outline with this colour
     * 
     * @see ass1.spec.GameObject#drawSelf(javax.media.opengl.GL2)
     */
    @Override
    public void drawSelf(GL2 gl) {
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glEnable (GL2.GL_BLEND);
        gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        
    	if(myFillColour != null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
    		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
    		double[] translation = super.getGlobalPosition();
        	gl.glTranslated(translation[0], translation[1], 0);
        	gl.glRotated(super.getGlobalRotation(),0,0,1);
        	gl.glScaled(super.getGlobalScale(),super.getGlobalScale(),1.0);
        	gl.glBegin(GL2.GL_POLYGON);
            {	
            	double angle = 0;
                double angleIncrement = 2*Math.PI/numVertices;
                for(int i=0; i < numVertices; i++){
                	angle = i* angleIncrement;
                	double x = myRadius * Math.cos(angle);
                	double y = myRadius * Math.sin(angle);
                	gl.glVertex2d(x, myRatio*y);
                }
            }
            gl.glEnd();
    	}
    	
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
    	if(myLineColour != null) {
            gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
            gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2],myLineColour[3]);
            double[] translation = super.getGlobalPosition();
        	gl.glTranslated(translation[0], translation[1], 0);
        	gl.glRotated(super.getGlobalRotation(),0,0,1);
        	gl.glScaled(super.getGlobalScale(),super.getGlobalScale(),1.0);
        	gl.glBegin(GL2.GL_POLYGON);
            {	
            	double angle = 0;
                double angleIncrement = 2*Math.PI/numVertices;
                for(int i=0; i < numVertices; i++){
                	angle = i* angleIncrement;
                	double x = myRadius * Math.cos(angle);
                	double y = myRadius * Math.sin(angle);
                	gl.glVertex2d(x, myRatio*y);
                }
            }
            gl.glEnd();
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
    	}
    }
}
