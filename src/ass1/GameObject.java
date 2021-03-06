package ass1;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;


/**
 * A GameObject is an object that can move around in the game world.
 * 
 * GameObjects form a scene tree. The root of the tree is the special ROOT object.
 * 
 * Each GameObject is offset from its parent by a rotation, a translation and a scale factor. 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class GameObject {

    // the list of all GameObjects in the scene tree
    public final static List<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
    
    // the root of the scene tree
    public final static GameObject ROOT = new GameObject();
    
    // the links in the scene tree
    private GameObject myParent;
    private List<GameObject> myChildren;

    // the local transformation
    //myRotation should be normalised to the range (-180..180)
    private double myRotation;
    private double myScale;
    private double[] myTranslation;
    
    // is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special private constructor for creating the root node. Do not use otherwise.
     */
    private GameObject() {
        myParent = null;
        myChildren = new ArrayList<GameObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new double[2];
        myTranslation[0] = 0;
        myTranslation[1] = 0;

        amShowing = true;
        
        ALL_OBJECTS.add(this);
    }

    /**
     * Public constructor for creating GameObjects, connected to a parent (possibly the ROOT).
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public GameObject(GameObject parent) {
        myParent = parent;
        myChildren = new ArrayList<GameObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new double[2];
        myTranslation[0] = 0;
        myTranslation[1] = 0;

        // initially showing
        amShowing = true;

        ALL_OBJECTS.add(this);
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
        for (GameObject child : myChildren) {
            child.destroy();
        }
        
        myParent.myChildren.remove(this);
        ALL_OBJECTS.remove(this);
    }

    /**
     * Get the parent of this game object
     * 
     * @return
     */
    public GameObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<GameObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public double getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(double rotation) {
        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(double angle) {
        myRotation += angle;
        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public double getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(double scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(double factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public double[] getPosition() {
        double[] t = new double[2];
        t[0] = myTranslation[0];
        t[1] = myTranslation[1];

        return t;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        myTranslation[0] = x;
        myTranslation[1] = y;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(double dx, double dy) {
        myTranslation[0] += dx;
        myTranslation[1] += dy;
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object. This method is called once per frame. 
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(double dt) {
        // do nothing
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL2 gl) {
        // do nothing
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * TODO: Complete this method
     * 
     * @param gl
     */
    public void draw(GL2 gl) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        } else {
        	gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
        	double[] translation = getGlobalPosition();
        	gl.glTranslated(translation[0], translation[1], 0);
        	gl.glRotated(getGlobalRotation(),0,0,1);
        	gl.glScaled(getGlobalScale(),getGlobalScale(),1.0);
        	drawSelf(gl);
        	int index = 0;
        	while(index < myChildren.size()) {
        		myChildren.get(index).draw(gl);
        		index++;
        	}
        }

        // TODO: draw the object and all its children recursively
        // setting the model transform appropriately 
    
        // Call drawSelf() to draw the object itself
        
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * TODO: Write this method
     * 
     * @return a point in world coordinates in [x,y] form
     */
    public double[] getGlobalPosition() {
        double[] p = new double[2];
        if(myParent != null) {
        	double[] parentP = myParent.getGlobalPosition();
        	double parentRotation = Math.toRadians(MathUtil.normaliseAngle(myParent.getGlobalRotation()));
        	double parentScale = myParent.getGlobalScale();
        	p[0] = parentP[0] + 
        			(myTranslation[0] * Math.cos(parentRotation) -
        			myTranslation[1] * Math.sin(parentRotation)) 
        			* parentScale;
        	p[1] = parentP[1] +
        			(myTranslation[0] * Math.sin(parentRotation) +
        			myTranslation[1] * Math.cos(parentRotation)) 
        			* parentScale;
        } else { //parent is a root
        	p[0] = myTranslation[0];
        	p[1] = myTranslation[1];	
        }
        return p; 
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * TODO: Write this method
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public double getGlobalRotation() {
    	if(myParent != null) {
    		return MathUtil.normaliseAngle(myRotation + myParent.getGlobalRotation());
    	} else {
    		return MathUtil.normaliseAngle(myRotation);
    	}
    }

    /**
     * Compute the object's scale in global terms
     * 
     * TODO: Write this method
     * 
     * @return the global scale of the object 
     */
    public double getGlobalScale() {
    	if(myParent != null) {
    		return myScale * myParent.getGlobalScale();
    	} else {
    		return myScale;
    	}
    }

    /**
     * Change the parent of a game object.
     * 
     * TODO: add code so that the object does not change its global position, rotation or scale
     * when it is reparented. 
     * 
     * @param parent
     */
    public void setParent(GameObject parent) {
    	double[] oldPosition = getGlobalPosition();
        double oldRotation = getGlobalRotation();
        double oldScale = getGlobalScale();
        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);
        double newRotation = getGlobalRotation();
        double newScale = getGlobalScale();
        
        double[] npp = myParent.getPosition(); //new parent position
        double npsin = Math.sin(Math.toRadians(MathUtil.normaliseAngle(myParent.getRotation()))); //new parent sin value
        double npcos = Math.cos(Math.toRadians(MathUtil.normaliseAngle(myParent.getRotation()))); //new parent cos value
        double npsc = myParent.getScale(); //new parent scale
        //Recalculating translation, rotation, and scale so the global status does not change
        myTranslation[0] = ((oldPosition[0] - npp[0]) / (npsc * npsin) + (oldPosition[1] - npp[1])  / (npsc * npcos)) /
        		((npcos/npsin) + (npsin/npcos));
        myTranslation[1] = ((oldPosition[1] - npp[1]) / (npsc * npsin) - (oldPosition[0] - npp[0]) / (npsc * npcos)) /
        		((npcos/npsin) + (npsin/npcos));
        myRotation = MathUtil.normaliseAngle(myRotation + (oldRotation - newRotation));
        myScale = myScale * (oldScale / newScale); 
    }

}
