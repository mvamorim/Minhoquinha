
import java.awt.Graphics;
import java.util.Arrays;
import java.util.LinkedList;
import java.awt.Point;
import java.awt.Color;

public class Worm {
	
	private static final int INITIAL_SIZE = 3;
	private static boolean MOVABLE = true;
	public int x, y;
	public static Integer body[][];
	private Direction direction;
	private LinkedList<Direction> llDir = new LinkedList<Direction>();
	

	public Worm() {
		x = (DrawPanel.SIZE/2)/DrawPanel.GRID_SIZE;
		y = (DrawPanel.SIZE/2)/DrawPanel.GRID_SIZE;
		direction = Direction.random();
		body = new Integer[INITIAL_SIZE][2];
		
	}
	
	public void setDirection(Direction d) {
		
		switch(d){
			case UP:
				if(direction != Direction.DOWN){
					if(MOVABLE){
						direction = d;
						MOVABLE = false;
					}
				}
				break;
				
			case DOWN:
				if(direction != Direction.UP){
					if(MOVABLE){
						direction = d;
						MOVABLE = false;
					}					
				}
				break;
				
			case LEFT:
				if(direction != Direction.RIGHT){
					if(MOVABLE){
						direction = d;
						MOVABLE = false;
					}
				}
				break;
				
			case RIGHT:
				if(direction != Direction.LEFT){
					if(MOVABLE){
						direction = d;
						MOVABLE = false;
					}
				}
				break;
		}
		
		for(Direction di: llDir){
			System.out.println(di);
		}
		
	}
	
	public void tick(){
		move();
		moveArray();
		checkCollision();
	}
	
	public void moveArray() {
		if(!DrawPanel.PAUSED && !DrawPanel.WON){
			int i = body.length - 1;
			
			while(i > 0){
				body[i][0] = body[i - 1][0];
				body[i][1] = body[i - 1][1];
				i--;
			}
			
			body[0][0] = x;
			body[0][1] = y;
		}
	}
	
	public void grow() {
		Integer tempBody[][] = new Integer[body.length + 1][2];
		
		int i = body.length;
		while(i > 0){
			tempBody[i][0] = body[i - 1][0];
			tempBody[i][1] = body[i - 1][1];
			i--;
		}
		
		tempBody[0][0] = x;
		tempBody[0][1] = y;
		
		body = tempBody;
	}
	
	public void checkCollision() {
		for(int i = 1; i < body.length; i++) {
			if(body[i][0] == body[0][0] && body[i][1] == body[0][1]){
				DrawPanel.setPaused(true);
			}
		}
	}
	
	public Point getHeadPoint() { 
		return new Point(body[0][0], body[0][1]);
	}
	
	public void move() {

		switch(direction) {
			case UP:
				y--;
				MOVABLE = true;
				break;
				
			case DOWN:
				y++;
				MOVABLE = true;
				break;
				
			case LEFT:
				x--;
				MOVABLE = true;
				break;
				
			case RIGHT:
				x++;
				MOVABLE = true;
				break;
				
		}
		checkBounds();
		
	}
	
	private void checkBounds(){
		if(y < 0 || y >= DrawPanel.SIZE/DrawPanel.GRID_SIZE ||
		   x < 0 || x >= DrawPanel.SIZE/DrawPanel.GRID_SIZE ){
				DrawPanel.PAUSED = true;
		}		
	}
	
	public void draw(Graphics g){
		int colorB = 255;
		int colorG = 0;
		int colorR = 0;
		for(int i = 0; i < body.length; i++){
			if(body[i][0] != null && body[i][1] != null){
				g.setColor(new Color(colorR, colorG, colorB, 120));
				g.fillRect(body[i][0] * DrawPanel.GRID_SIZE, body[i][1] * DrawPanel.GRID_SIZE, DrawPanel.GRID_SIZE, DrawPanel.GRID_SIZE);
				if(colorB > 0){
					colorB -= 10;
					if(colorB < 0) colorB = 0;
				}
				if(colorG < 255){
					colorG += 10;
					if(colorG > 255) colorG = 255;
				} 
				if(colorG == 255 && colorB == 0){
					colorR += 10;
					if(colorR > 255) colorR = 255;
				}
			}
		}
	}
}