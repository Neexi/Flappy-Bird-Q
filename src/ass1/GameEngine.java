package ass1;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * The GameEngine is the GLEventListener for our game.
 * 
 * Every object in the scene tree is updated on each display call.
 * Then the scene tree is rendered.
 *
 * You shouldn't need to modify this class.
 *
 * @author malcolmr
 */
public class GameEngine implements GLEventListener, KeyListener {

	private TextRenderer renderer;
	
    private Camera myCamera;
    private long myTime;
    
    
    private int next_pipe = 0;
    private double idle = 1;
    private double pause = 0;
    private double[] currentRectangle = null;

    /**
     * Construct a new game engine.
     *
     * @param camera The camera that is used in the scene.
     */
    public GameEngine(Camera camera) {
        myCamera = camera;
    }
    
    /**
     * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        // initialise myTime
        myTime = System.currentTimeMillis();
        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // ignore
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        // tell the camera and the mouse that the screen has reshaped
        GL2 gl = drawable.getGL().getGL2();

        myCamera.reshape(gl, x, y, width, height);
        
        // this has to happen after myCamera.reshape() to use the new projection
        Mouse.theMouse.reshape(gl);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // set the view matrix based on the camera position
        myCamera.setView(gl); 
        
        // update the mouse position
        Mouse.theMouse.update(gl);
        
        // update the objects
        update();

        // draw the scene tree
        GameObject.ROOT.draw(gl);   
        
        if(pause == 0 && idle == 1) {
        	renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
	        // optionally set the color
	        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
	        renderer.draw("Press space to start", 130, 500);
	        renderer.endRendering();
        } else if(pause == 0) {
	        renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
	        // optionally set the color
	        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
	        renderer.draw(String.valueOf(next_pipe), 275, 500);
	        renderer.endRendering();
        } else {
        	renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
	        // optionally set the color
	        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
	        renderer.draw("Game Over!", 180, 500);
	        renderer.draw("Your score is : "+String.valueOf(next_pipe), 140, 425);
	        renderer.endRendering();
        }
    }

    private void update() {
        
        // compute the time since the last frame
        long time = System.currentTimeMillis();
        double dt = (time - myTime) / 1000.0;
        myTime = time;
        
        // take a copy of the ALL_OBJECTS list to avoid errors 
        // if new objects are created in the update
        List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
        
        if(pause == 0) {
	        // update all objects
	        for (GameObject g : objects) {
	            g.update(dt);
	            if(g instanceof MyCoolGameObject) {
	    			((MyCoolGameObject)g).rotateWing(10);
	    			((MyCoolGameObject)g).doJump();
	    			((MyCoolGameObject)g).drop();
	    			if(((MyCoolGameObject)g).reachedBottom()) {
	    				pause = 1;
	    			}
	    			((MyCoolGameObject)g).idleMovement();
	    			currentRectangle = ((MyCoolGameObject)g).getSurroundingRectangle();
	    			if(idle == 1) {
		    			if(((MyCoolGameObject)g).getIdle() == 0) {
		    				idle = 0;
		    			}
	    			}
	    		}
	            if(g instanceof MyCoolGameBackground) {
	            	((MyCoolGameBackground)g).moveGrass();
	            	if(idle == 0) {
	            		((MyCoolGameBackground)g).movePipe();
	            	}
	            	next_pipe = ((MyCoolGameBackground)g).getNextPipe();
	            	if(((MyCoolGameBackground)g).getPipeTop(next_pipe).rectangleCollision(currentRectangle)) {
	            		pause = 1;
	            	}
	            	if(((MyCoolGameBackground)g).getPipeBot(next_pipe).rectangleCollision(currentRectangle)) {
	            		pause = 1;
	            	}
	            }
	        }
        }
    }
    
    /**
     * Return the list of object which contains point p
     * @param p
     * @return
     */
    public List<GameObject> collision(double[] p) {
    	List<GameObject> col = new ArrayList<GameObject>();
    	List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
    	int index = 0;
    	while(index < objects.size()) {
    		GameObject dummy = objects.get(index);
    		if(dummy instanceof PolygonalGameObject) { //Case for Polygon
    			if(((PolygonalGameObject)dummy).inPolygon(p)) {
    				col.add(dummy);
    			}
    		} else if(dummy instanceof CircularGameObject) { //Case for Circle
    			if(((CircularGameObject)dummy).inCircle(p)) {
    				col.add(dummy);
    			}
    		} else if(dummy instanceof LineGameObject) { //Case for Line
    			if(((LineGameObject)dummy).onLine(p)) {
    				col.add(dummy);
    			}
    		}
    		index++;
    	}
    	return col;
    }

    //Keypress
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
			for (GameObject g : objects) {
	            if(g instanceof MyCoolGameObject) {
	            	((MyCoolGameObject)g).boostJump();
	    		}
	        }
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//Disable idle
	public void disableIdle() {
		idle = 0;
	}
    
}
