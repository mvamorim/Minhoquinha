import java.util.Random;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;
	
	static Direction random(){
		Random r = new Random();
		int c = r.nextInt(4);
		switch(c){
			case 0:
				return UP;
				
			case 1:
				return DOWN;
				
			case 2:
				return LEFT;
				
			case 3:
				return RIGHT;
		}
		
		return UP;
	}
}