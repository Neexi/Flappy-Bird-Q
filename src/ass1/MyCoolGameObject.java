package ass1;


public class MyCoolGameObject extends GameObject {
	//Color Value
	private double white[] = {1,1,1,1};
	private double black[] = {0,0,0,1};
	private double yellow[] = {1,1,0,1};
	private double lightYellow[] = {1,1,0.5,1};
	private double orange[] = {1,0.271,0,1};
	
	//Body Part
	private EllipticalGameObject body;
	private PolygonalGameObject topBeak;
	private PolygonalGameObject botBeak;
	private EllipticalGameObject eye;
	private CircularGameObject eyeBall;
	private CircularGameObject eyeBall2;
	private EllipticalGameObject wing;
	
	//Movement 
	private double idle = 1; //by default is idle
	private double idle_direction = 1;
	private double jump = 0;
	private double maxJump = 40;
	private double drop = 0;
	
	//World Physics
	private double jumpValue = 15;
	private double maxHeight = 0.9;
	private double minHeight = -0.8;
	private double minHeight_epsilon = 0.02;
	private double idle_move = 0.1;
	private double idle_position = 0.1;
	private double jumpPower = 0.055;
	private double gravity = -0.025;
	private double idle_speed = 0.01;
	
	/**
	 * Create a cool object
	 * 
	 * @author rpur114
	 */
	public MyCoolGameObject() {	
		super(GameObject.ROOT);
		
		//Drawing Body
		body = new EllipticalGameObject(getParent(),0.4,0.75,yellow,black);
		
		double[] tbPoints = {0.1,0,0.5,0,0.6,-0.05,0.5,-0.1,0.1,-0.1};
		double[] bbPoints = {0.1,-0.1,0.4,-0.1,0.5,-0.15,0.4,-0.2,0.1,-0.2};
		topBeak = new PolygonalGameObject(body,tbPoints, orange,black);
		botBeak = new PolygonalGameObject(body,bbPoints, orange,black);
		
		EllipticalGameObject eye = new EllipticalGameObject(body,0.2,0.75,white,black);
		eye.translate(0.2, 0.2*0.75);
		eye.rotate(-25);
		
		
		eyeBall = new CircularGameObject(eye,0.05,black,white);
		eyeBall.translate(0.075,0.005);
		
		eyeBall2 = new CircularGameObject(eyeBall,0.01,white,black);
		
		wing = new EllipticalGameObject(body,0.2,0.75,lightYellow,black);
		wing.translate(-0.3,0);
		wing.rotate(-25);
	}
	
	@Override
	public void rotate(double angle) {
        body.rotate(angle);
    }
	
	@Override
	public void scale(double factor) {
        body.scale(factor);
    }
	
	@Override
	public void translate(double dx, double dy) {
        body.translate(dx,dy);
    }
	
	public void rotateWing(double r) {
		wing.rotate(r);
	}
	
	public void boostJump() {
		idle = 0;
		jump = jumpValue;
		if(jump > maxJump) {
			jump = maxJump;
		}
	}
	
	
	public void doJump() {
		if(jump <= 0) {
			return;
		}
		drop = 0;
		double[] coor = body.getGlobalPosition();
		double height = coor[1];
		double move = 0;
		if(height < maxHeight) {
			move = jumpPower + gravity;
		}
		body.translate(0,move);
		body.setRotation(45);
		jump--;
	}
	
	public void drop() {
		if(idle == 1) {
			return;
		}
		if(jump <= 0) {
			double[] coor = body.getGlobalPosition();
			double height = coor[1];
			double move = 0;
			if(height >= minHeight) {
				move = gravity;
			}
			body.translate(0,move);
			/**
			if(drop > 10) {
				body.setRotation(-45);
			}**/
			if(body.getRotation() >= -90) {
				body.rotate(-7.5);
			}
			drop++;
		}
	}
	
	public void idleMovement() {
		if(idle != 1) {
			return;
		}
		double[] coor = body.getGlobalPosition();
		double height = coor[1];
		if(height > idle_move + idle_position) {
			idle_direction = -1;
		} else if(height < -idle_move + idle_position) {
			idle_direction = 1;
		}
		if(idle_direction == 1) {
			body.translate(0,idle_speed);
		} else if(idle_direction == -1) {
			body.translate(0,-idle_speed);
		}	
	}
	
	public double getIdle() {
		return idle;
	}
	
	/**
	 * Get surrounding rectangle coordinates of MyCoolGameObject in order of
	 * leftmost x
	 * topmost y
	 * rightmost x
	 * botmost y
	 * @return
	 */
	public double[] getSurroundingRectangle() {
		double[] position = body.getGlobalPosition();
    	double rotation = Math.toRadians(MathUtil.normaliseAngle(body.getGlobalRotation()));
    	double scale = body.getGlobalScale();
    	//local point
    	double localxLeft = -body.getRadius();
    	double localyTop = body.getRadius() * body.getRatio();
    	double localxRight = 0.6; //Rightmost point of beak TBA
    	double localyBot = -body.getRadius() * body.getRatio();;
    	
    	//global point
    	double xLeft = position[0] +
    			(localxLeft * Math.cos(rotation) -
    			localyTop * Math.sin(rotation)) 
    			* scale;
    	double yTop = position[1] +
    			(localxLeft * Math.sin(rotation) +
    					localyTop * Math.cos(rotation)) 
    			* scale;
    	double xRight = position[0] +
    			(localxRight * Math.cos(rotation) -
    			localyBot * Math.sin(rotation)) 
    			* scale;
    	double yBot = position[1] +
    			(localxRight * Math.sin(rotation) +
    			localyBot * Math.cos(rotation)) 
    			* scale;
		double[] box = {xLeft,yTop,xRight,yBot};
		return box;
	}
	
	public boolean reachedBottom() {
		double[] coor = body.getGlobalPosition();
		double height = coor[1];
		if(height <= minHeight+minHeight_epsilon) {
			return true;
		}
		return false;
	}
}
