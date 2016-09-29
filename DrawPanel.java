import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Font;

class DrawPanel extends JPanel
{
	/******** Parâmetros Iniciais ********/
	public static final int GRID_SIZE = 20;
	public static final int SIZE = 400;
	public static final int FREQ = 6;
	public static boolean RUNNING = true;
	public static boolean PAUSED = false;
	public static boolean WON = false;
	private boolean collided = false;

	int rows[][];    //Para criar os traços das linhas
	int columns[][]; //Para criar os traços das colunas
	int points;
	
	private int counter = 0;
	private ArrayList<Powerup> pu = new ArrayList<Powerup>();
	private ArrayList<Powerup> rpu = new ArrayList<Powerup>();
	private ArrayList<Powerup> apu = new ArrayList<Powerup>();
	
	Worm worm; 
	Thread loop;
	
	public DrawPanel()	{
		this.setPreferredSize(new Dimension(SIZE, SIZE));
		this.setVisible(true);

		setInputs();
		createLines();
		
		newGame();
		
		loop();
		loop.start();
		
	}
	
	//Define se está pausado ou nao
	public static void setPaused(boolean p){
		PAUSED = p;
	}
	
	//Cria as linhas do Grid
	private void createLines() {
		rows = new int[SIZE/GRID_SIZE][4];
		int j = 0;
		for(int i = 1; i < SIZE/GRID_SIZE; i++) {
			rows[j][0] = 0;
			rows[j][1] = i * GRID_SIZE;
			rows[j][2] = SIZE;
			rows[j][3] = i * GRID_SIZE;
			j++;
		}
		
		columns = new int[SIZE/GRID_SIZE][4];
		j = 0;
		for(int i = 1; i < SIZE/GRID_SIZE; i++) {
			columns[j][0] = i * GRID_SIZE;
			columns[j][1] = 0;
			columns[j][2] = i * GRID_SIZE;
			columns[j][3] = SIZE;
			j++;
		}
	}	
	
	//Inicia o loop do jogo
	private void loop() {
		loop = new Thread(new Runnable() {
			public void run() {
				while(RUNNING) {
					if(!PAUSED) {	
						worm.move();
						checkCollision();
						if(!collided && !WON)
							worm.moveArray();
						collided = false;
						worm.checkCollision();
					}
					
					repaint();
					Thread.yield();
					try{Thread.sleep(1000/FREQ);}catch(Exception e){}
				}
			}
		});	
	}
	
	//Checa a colisão entre powerUP e minhoca
	private void checkCollision(){
		for(Powerup p: pu){
			if(p.getX() == worm.x && p.getY() == worm.y){
				worm.grow();
				rpu.add(p);
				if(!WON) apu.add(new Powerup(SIZE));
				points++;
				collided = true;
				
			}
		}
		
		if(!WON) pu.addAll(apu);
		apu.clear();
		pu.removeAll(rpu);
	}
	
	//Retorna a minhoca
	public Worm getWorm(){
		return worm;
	}
	
	//Define os comandos de teclados para movimentar e começar um novo jogo em caso de GameOver
	public void setInputs(){
		Action up = new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				worm.setDirection(Direction.UP);
			}	
		};
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		this.getActionMap().put("up", up);
		
		Action down = new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				worm.setDirection(Direction.DOWN);
			}	
		};
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		this.getActionMap().put("down", down);
		
		Action left = new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				worm.setDirection(Direction.LEFT);
			}	
		};
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		this.getActionMap().put("left", left);
		
		Action right = new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				worm.setDirection(Direction.RIGHT);
			}	
		};
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		this.getActionMap().put("right", right);
		
		Action reset = new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				if(PAUSED){
					newGame();
				}
			}	
		};
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "reset");
		this.getActionMap().put("reset", reset);
		
	}
	
	//Chama um novo jogo
	private void newGame(){
		worm = new Worm();
		pu.clear();
		pu.add(new Powerup(SIZE));
		points = 0;
		PAUSED = false;
		WON = false;
	}
	
	public void drawGridLines(Graphics g){
		//Pinta a grade
		//TODO: Talvez desabiltiar grade?
		g.setColor(new Color(0, 0, 0, 20));
		for(int i = 0; i < SIZE/GRID_SIZE - 1; i++)	{
			g.drawLine(rows[i][0], rows[i][1], rows[i][2], rows[i][3]);
			g.drawLine(columns[i][0], columns[i][1], columns[i][2], columns[i][3]);
		}
		
	}
	
	public void drawPointMessage(Graphics g){
		if(!WON && PAUSED){
			g.setColor(new Color(0, 0, 150 , 255));
			String textPoints = "Você fez " + points + " pontos!";
			String restart = "\"Enter\" para recomeçar!";
			g.setFont(new Font("SANS SERIF", Font.PLAIN, 35)); 
			int width = g.getFontMetrics().stringWidth(textPoints);
			int height = g.getFontMetrics().getHeight();
			g.drawString(textPoints, SIZE/2 - width/2, SIZE/2);
			width = g.getFontMetrics().stringWidth(restart);
			g.drawString(restart, SIZE/2 - width/2, SIZE/2 + height);
			
		}
	}
	
	public void drawWinMessage(Graphics g){
		if(WON && PAUSED){
			g.setColor(new Color(0, 0, 150 , 255));
			String wonText = "Você ganhou com " + points + " pontos!";
			String restart = "\"Enter\" para recomeçar!";
			g.setFont(new Font("SANS SERIF", Font.PLAIN, 30)); 
			int width = g.getFontMetrics().stringWidth(wonText);
			int height = g.getFontMetrics().getHeight();
			g.drawString(wonText, SIZE/2 - width/2, SIZE/2);
			width = g.getFontMetrics().stringWidth(restart);
			g.drawString(restart, SIZE/2 - width/2, SIZE/2 + height);
			
		}
	}
	
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
		
		drawGridLines(g);
		
		//Pinta o powerUP
		for(Powerup p: pu){
			p.draw(g);
		}
		
		//Pinta a minhoca
		worm.draw(g);
		
		//Pinta o resultado quando dá GameOver
		drawPointMessage(g);
		drawWinMessage(g);
	}
}