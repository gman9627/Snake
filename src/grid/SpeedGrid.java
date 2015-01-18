package grid;

import grid.Grid.Location;

import java.awt.Color;

public class SpeedGrid extends Grid{

	public SpeedGrid(int row, int col, int speed) {
		super(row, col, speed);
		this.scoreMultiplier = 9;
		Location loc = new Location((row / 2 + 1), (col / 2 + 1));
		this.addSnake(loc);
		this.setEatLoction(1);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		SnakeLine snake = getSnakeLine(1);
		Location next = this.getNeighborPoint(snake.getFirst(), snake.getDirection());
		if(!isValid(next)) setGameOver(1); 
		else if(this.get(next) == 0) {
			snake.addFirst(next);
			snake.removeLast();
		} 
		else if(snake.contains(next)) setGameOver(1); 
		else {
			snake.addFirst(next);
			Location last1 = snake.getLast();
			snake.removeLast();
			Location last2 = snake.getLast();
			int direction = this.getDirectionToward(last2, last1);
			snake.addLast(last1);
			snake.addLast(last2);
			snake.addLast(this.getNeighborPoint(last2, direction));
			this.setEatLoction(1);
			snake.adjustScore();
		}
	}

	@Override
	public Color getColor(int num) {
		if(num == 0) return Color.white;
		else return Color.gray;
	}
	
	private int getDirectionToward(Location loc1, Location loc2) {
		int dx = loc2.col - loc1.col;
		int dy = loc2.row - loc1.row;  
	    int angle = (int)Math.toDegrees(Math.atan2(-dy, dx));
	    int compassAngle = 90 - angle;  
	    compassAngle += 22;
	    if (compassAngle < 0) 
	    	compassAngle += 360;
	    return compassAngle / 45 * 45;
		  
	}

}
