package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCoolGameBackground extends GameObject{

	private double top = 1;
	private double rand_max = 100;
	
	private CircularGameObject dummy_grass;
	private CircularGameObject dummy_pipe;
	private List<PolygonalGameObject> grasses;
	private List<RectangularGameObject> pipesBot;
	private List<RectangularGameObject> pipesTop;
	
	private double black[] = {0,0,0,1};
	private double red[] = {1,0,0,1};
	private double dark_green[] = {0,0.482,0.047,1};
	private double light_green[] = {0.004,0.651,0.067,1};
	
	private double x_velocity = -0.015;
	
	//Grass variable
	private double grass_left_corner = -2.4;
	private double grass_bottom_corner = -1;
	
	private double grass_num = 25;
	private double grass_width = 0.2;
	private double grass_height = 0.1;
	
	//Pipe variable
	private double pipe_width = 0.4;
	private double pipe_max_height = 0.25;
	private double pipe_min_height = -0.7;
	private double pipe_start_x = 1.5;
	private double pipe_min_y = -0.3;
	private double pipe_max_y = 0.3;
	private double pipe_diff_y = 0.2;
	private double pipe_diff_y_extend = 1;
	private double pipe_y_distance = 0.7;
	private double pipe_x_distance = 1.5;
	private double pipe_y_distance_shrink = -0.2;
	private double pipe_x_distance_shrink = -1.5;
	private double pipe_pass = -0.9;
	private double pipe_num = 1000;
	private int next_pipe = 0;
	
	public MyCoolGameBackground() {
		super(GameObject.ROOT);
		
		dummy_grass = new CircularGameObject(getParent(),0,null,null);
		dummy_pipe = new CircularGameObject(getParent(),0,null,null);
		
		grasses = new ArrayList<PolygonalGameObject>();
		pipesBot = new ArrayList<RectangularGameObject>();
		pipesTop = new ArrayList<RectangularGameObject>();
		int index = 0;
		
		while(index < grass_num) {
			double[] points = {(grass_left_corner + index*grass_width),grass_bottom_corner,
					((grass_left_corner + grass_width) + index*grass_width),grass_bottom_corner,
					((grass_left_corner + 2*grass_width) + index*grass_width),grass_bottom_corner+grass_height,
					((grass_left_corner + grass_width) + index*grass_width),grass_bottom_corner+grass_height};
			double[] color = null;
			if(index % 2 == 1) {
				color = light_green;
			} else {
				color = dark_green;
			}
			PolygonalGameObject grass = new PolygonalGameObject(dummy_grass,points,color,null);
			grasses.add(grass);
			index++;
		}
		
		index = 0;
		double[] yBot = new double[(int)pipe_num];
		while(index < pipe_num) {
			double y_extend;
			if(index < rand_max) {
				y_extend = index * pipe_diff_y_extend / rand_max;
			} else {
				y_extend = pipe_diff_y_extend;
			}
			if(index > 0) {
				yBot[index] = yBot[index-1] + randDbl(-pipe_diff_y - y_extend, pipe_diff_y + y_extend);
			} else {
				yBot[index] = randDbl(pipe_min_y,pipe_max_y);
			}
			if(yBot[index] < pipe_min_height) {
				yBot[index] = pipe_min_height;
			}
			if(yBot[index] > pipe_max_height) {
				yBot[index] = pipe_max_height;
			}
			
			double x_shrink_max;
			double y_shrink_max;
			//Calculating the shrink between pipe, both x and y, random point reached at pipe 100
			if(index < rand_max) {
				x_shrink_max = index*pipe_x_distance_shrink/rand_max;
				y_shrink_max = index*pipe_y_distance_shrink/rand_max;
			} else {
				x_shrink_max = pipe_x_distance_shrink;
				y_shrink_max = pipe_y_distance_shrink;
			}
			double x_shrink = randDbl(x_shrink_max,0);
			double y_shrink = randDbl(y_shrink_max,0);
			double[] pointsBot = {pipe_start_x + (index*pipe_x_distance) + x_shrink,grass_bottom_corner+grass_height,
					pipe_start_x + pipe_width + (index*pipe_x_distance) + x_shrink,grass_bottom_corner+grass_height,
					pipe_start_x + pipe_width + (index*pipe_x_distance) + x_shrink,yBot[index],
					pipe_start_x + (index*pipe_x_distance) + x_shrink,yBot[index]};
			RectangularGameObject pipeBot = new RectangularGameObject(dummy_pipe,pointsBot,red,black);
			pipesBot.add(pipeBot);
			double[] pointsTop = {pipe_start_x + (index*pipe_x_distance) + x_shrink,yBot[index]+pipe_y_distance+y_shrink,
					pipe_start_x + pipe_width + (index*pipe_x_distance) + x_shrink,yBot[index]+pipe_y_distance+y_shrink,
					pipe_start_x + pipe_width + (index*pipe_x_distance) + x_shrink,top,
					pipe_start_x + (index*pipe_x_distance) + x_shrink,top};
			RectangularGameObject pipeTop = new RectangularGameObject(dummy_pipe,pointsTop,red,black);
			pipesTop.add(pipeTop);
			index++;
		}
	}
	
	public void moveGrass() {
		dummy_grass.translate(x_velocity,0);
		double[] pos = dummy_grass.getGlobalPosition();
		if(pos[0] <= -0.4) {
			dummy_grass.setPosition(0,0);
		}
	}
	
	public void movePipe() {
		dummy_pipe.translate(x_velocity,0);
		if(pipesBot.get(next_pipe).getRightCorner() < pipe_pass) {
			next_pipe++;
		}
	}
	
	public double randDbl(double min, double max) {
	    Random rand = new Random();
	    double randomNum = rand.nextDouble()*(max-min) + min;
	    return randomNum;
	}
	
	public int getNextPipe() {
		return next_pipe;
	}
	
	public RectangularGameObject getPipeTop(int index) {
		return pipesTop.get(index);
	}
	
public RectangularGameObject getPipeBot(int index) {
		return pipesBot.get(index);
	}

}
