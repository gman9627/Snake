package grid;

import java.awt.Color;

import config.Configuration;

public class SinglePlayerGrid extends Grid {
	
	public SinglePlayerGrid(int row, int col, int speed) {
		super(row, col, speed);
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
			this.setEatLoction(1);
			snake.adjustScore();
			if(speed > 10) speed--;
		}
	}

	@Override
	public Color getColor(int num) {
		// TODO Auto-generated method stub
		if(num == 0) return Color.white;
		else return Color.gray;
	}
}
