package grid;

import grid.Grid.Location;

import java.awt.Color;

public class SpeedGrid extends Grid{

	public SpeedGrid(int row, int col, int speed) {
		super(row, col, speed);
	}
	
	@Override
	public void newGame() {
		super.newGame();
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
			snake.growTail();
			this.setEatLoction(1);
			snake.adjustScore();
		}
	}

	@Override
	public Color getColor(int num) {
		if(num == 0) return Color.white;
		else return Color.gray;
	}

}
