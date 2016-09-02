package ass1.tests;

import java.util.List;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import ass1.*;

/**
 * A simple test class for assignment1
 * 
 * @author angf
 */
public class TestShapes {

	public static void createTestShapes(){
		
		///**
        // Create a polygon
        double white[] = {1,1,1,1};
        double points[] = {0,0,1,1,0,1};
        PolygonalGameObject p = new PolygonalGameObject(GameObject.ROOT,points,null,white);
        p.rotate(45);
        
        
        // Create a circle 
        double cFillCol[] = {1,0.5,0.5,1};
        CircularGameObject c = new CircularGameObject(GameObject.ROOT,cFillCol,white);
        c.setPosition(1, -1);  
        c.setScale(0.5);  
        
        //Create a line
        double lineCol [] = {0.5,1,0.5,1};
        LineGameObject l = new LineGameObject(GameObject.ROOT,0.5,0.5,1,1,lineCol);
       
        
        //Create a line that is a child of polygon p
        double lineCol2 []= {0.5,0.5,1,1};
        LineGameObject l2 = new LineGameObject(p,lineCol2);     
        l2.setPosition(-1, 0);
      
        
        //Create a circle that is a child of line l2       
        CircularGameObject c2 = new CircularGameObject(p,0.25,white,white);     
        c2.translate(-1,0);
        
        //CircularGameObject test = new CircularGameObject(GameObject.ROOT,null,white);
        //*/ 
		
		/**
		double white[] = {1,1,1,1};
		//Test this shit with all the point
        double points[] = {-0.5,-0.5,0.5,-0.5,0.5,0.5,-0.5,0.5};
        //PolygonalGameObject rectangle = new PolygonalGameObject(GameObject.ROOT,points,null,white);
        //rectangle.scale(2);
        CircularGameObject c = new CircularGameObject(GameObject.ROOT,1,white,null);
        //c.translate(0.5,0.5);
        //c.scale(0.25);
        //**/
        
	}
   
    /**
     * A simple example of how to use PolygonalGameObject, CircularGameObject and LineObject
     * 
     * and also how to put together a simple scene using the game engine.
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
        //camera.setPosition(0, 0);
        //camera.setRotation(45);
        camera.setScale(2); // scale up the camera so we can see more of the world
        
              
        createTestShapes();
        
        // Add the game engine
        GameEngine engine = new GameEngine(camera);
        double points[] = {1,0};
        List<GameObject> col = engine.collision(points);
        System.out.println(col.size());
        panel.addGLEventListener(engine);

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        // Put it in a window
        JFrame jFrame = new JFrame("Test Shapes");
        jFrame.add(panel);
        jFrame.setSize(400, 400);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
