package grid;

import grid.Grid.Location;
import grid.Grid.SnakeLine;

import java.awt.Color;

public class TwoPlayerGrid extends Grid {

	public TwoPlayerGrid(int row, int col, int speed) {
		super(row, col, speed);
	}
	
	@Override
	public void newGame() {
		super.newGame();
		Location loc[] = { new Location((row / 2 + 1), (col / 4 + 1)),
				new Location((row / 2 + 1), col - (col / 4) + 1) };
		for (int x = 2; x >= 1; x--) {
			addSnake(loc[x - 1]);
			this.setEatLoction(x);
		}
	}

	@Override
	public void act() {
		for(int x = 1; x <= 2; x++) {
			SnakeLine snake = getSnakeLine(x);
			Location next = this.getNeighborPoint(snake.getFirst(), snake.getDirection());
			if(!isValid(next)) setGameOver(x); 
			else if(this.get(next) == 0) {
				snake.addFirst(next);
				snake.removeLast();
			} 
			else if(get(next) != snake.getNum() || snake.contains(next)) setGameOver(x); 
			else {
				snake.addFirst(next);
				this.setEatLoction(x);
				snake.adjustScore();
			}
		}
		
	}

	@Override
	public Color getColor(int num) {
		// TODO Auto-generated method stub
		if(num == 0) return Color.white;
		if(num == 1) return Color.blue;
		else return Color.red;
	}

}
