package ass1.tests;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import ass1.*;

/**
 * A simple class to view MyCoolGameObject
 *
 * @author angf
 */
public class TestMyCoolGameObject {
	private static float sky_blue[] = {0.494f,0.753f,0.933f,1};

	public static void createTestObjects(){
		
		//Should look good when we create using the default constructor
		MyCoolGameObject cgo = new MyCoolGameObject();
		
		MyCoolGameBackground bck = new MyCoolGameBackground();
		
		// Should not break if we apply transformations for example
		// If we uncommented these lines (or wrote other transformations) 
		// it should not break your object
		//cgo.rotate(180);
		
		cgo.translate(-0.8,0);
		
		//cgo.translate(-0.2,0.2);
		//cgo.rotate(45);
		cgo.scale(0.2);
		
	}
   
    /**
     * A simple test for MyCoolGameObject
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        
        // create a GLJPanel to draw on
        GLJPanel panel = new GLJPanel(glcapabilities);

        // Create a camera
        Camera camera = new Camera(GameObject.ROOT);
        camera.setBackground(sky_blue);
        createTestObjects();
        
        
        // Add the game engine
        GameEngine engine = new GameEngine(camera);
        panel.addGLEventListener(engine);
        panel.addKeyListener(engine);

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        // Put it in a window
        JFrame jFrame = new JFrame("Test Shapes");
        jFrame.add(panel);
        jFrame.setSize(600, 600);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
