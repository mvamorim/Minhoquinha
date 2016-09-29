import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;


public class Powerup{
	private Random r = new Random();
	private int x, y;
	private int counter;
	
	public Powerup(int size){
		setPosition(size);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	private void setPosition(int size){
		x = r.nextInt(size/DrawPanel.GRID_SIZE);
		y = r.nextInt(size/DrawPanel.GRID_SIZE);
		
		for(int i= 0; i < Worm.body.length; i++){
			if(Worm.body[i][0] != null && Worm.body[i][1] != null){
				if(Worm.body[i][0] == x && Worm.body[i][1] == y){
					
					if(Worm.body.length < (DrawPanel.SIZE/DrawPanel.GRID_SIZE)
					* (DrawPanel.SIZE/DrawPanel.GRID_SIZE)){
						System.out.println(counter++ + " - " + Worm.body.length);
						setPosition(size);
						
					}else{	
						System.out.println("GANHOU!");
						DrawPanel.WON = true;
						return;
					}
				}
			}
		}		
	}
	
	public void draw(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(x * DrawPanel.GRID_SIZE, y * DrawPanel.GRID_SIZE, DrawPanel.GRID_SIZE, DrawPanel.GRID_SIZE);
	}
}