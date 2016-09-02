package ass1;

import javax.media.opengl.GL2;

public class LineGameObject extends GameObject {
	private double x1;
	private double x2;
	private double y1;
	private double y2;
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
    public LineGameObject(GameObject parent, double[] lineColour) {
        super(parent);
        
        x1 = 0;
        x2 = 1;
        y1 = 0;
        y2 = 0;
        myLineColour = lineColour;
    }
    
    public LineGameObject(GameObject parent,  double x1, double y1,
            double x2, double y2,
            double[] lineColour) {
        super(parent);
        
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        myLineColour = lineColour;
    }

    /**
     * Get the first x axis point
     * 
     * @return
     */
    public double getX1() {        
        return x1;
    }
    
    /**
     * Get the second x axis point
     * 
     * @return
     */
    public double getX2() {        
        return x2;
    }
    
    /**
     * Get the first y axis point
     * 
     * @return
     */
    public double getY1() {        
        return y1;
    }
    
    /**
     * Get the second y axis point
     * 
     * @return
     */
    public double getY2() {        
        return y2;
    }

    /**
     * Set the line coordinate
     * 
     * @param points
     */
    public void setPoints(double x1, double y1,
            double x2, double y2) {
    	this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
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
     * TODO: Draw the line
     *
     * @see ass1.spec.GameObject#drawSelf(javax.media.opengl.GL2)
     */
    @Override
    public void drawSelf(GL2 gl) {
    	
        gl.glEnable (GL2.GL_BLEND);
        gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        
    	if(myLineColour != null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2],myLineColour[3]);
            gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        	gl.glBegin(GL2.GL_LINES);
            {	
            	gl.glVertex2d(x1,y1);
            	gl.glVertex2d(x2,y2);
            	
            }
            gl.glEnd();
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
    	}
    }
    
    /**
     * Return true if point p is on the line obj
     * @param p
     * @return
     */
    public boolean onLine(double[] p) {
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
