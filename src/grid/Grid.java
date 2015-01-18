package grid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import list.SnakeList;

public abstract class Grid {
	
	public static final int NORTH = 0, EAST = 90, SOUTH = 180, WEST = 270;

	private final int row, col, originalSpeed;
	private final int[][] grid;
	protected boolean gameOver, pause;
	protected int speed, scoreMultiplier;
	protected List<SnakeLine> snakeLines;
	
	public Grid(int row, int col, int speed) {
		this.row = row;
		this.col = col;
		this.speed = originalSpeed = speed;
		snakeLines = new ArrayList();
		grid = new int[row][col];
		gameOver = pause = false;
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				grid[r][c] = 0;
			}
		}
		if(getSpeed() >= 200) scoreMultiplier = 6;
		else if(getSpeed() >= 150) scoreMultiplier = 5;
		else scoreMultiplier = 4;	
		scoreMultiplier *= 2;
		
	}
	
	public abstract void act();
	public abstract Color getColor(int num);
	
	public int getNumRows() {return row;}
	public int getNumCols() {return col;}
	public int getOriginalSpeed() {return originalSpeed;}
	public int getNumPlayers() {return snakeLines.size();}
	public void setDirection(int num, int direction) {getSnakeLine(num).setDirection(direction);}
	public int getDirection(int num) {return getSnakeLine(num).getDirection();}
	public int getScore(int num) {return getSnakeLine(num).getScore();}
	public int getSpeed() {return speed;}
	public boolean canTurn(int num) {return getSnakeLine(num).canTurn();}
	public boolean isGameOver() {return gameOver;}
	public boolean isPaused() {return pause;}
	
	protected SnakeLine getSnakeLine(int num) {
		num = num - 1;
		if(num >= snakeLines.size()) num = snakeLines.size() - 1;
		return snakeLines.get(num);
	}
	protected void set(Location loc, int num) {grid[loc.row][loc.col] = num;}
	
	public boolean isValid(Location p) {
		return (0 <= p.row) && (p.row < getNumRows()) && (0 <= p.col) && (p.col < getNumCols());
	}
	
	public int get(int row, int col) {return get(new Location(row, col));}
	public int get(Location loc) {
		if(loc == null) throw new NullPointerException();
		if(!isValid(loc)) throw new IllegalStateException();
		return grid[loc.row][loc.col];
	}
	
	public void pause() {
		for(SnakeLine snake: snakeLines) snake.pause();
		pause = !pause;
	}
	
	public void setCanTurn(int num, boolean turn) {
		if(!turn) getSnakeLine(num).setCanTurn(false); 
		else 
			for(SnakeLine snake: snakeLines) snake.setCanTurn(true);
			
	}
	
	private Location getRandomOffLocation() {
		int r = (int)(Math.random() * getNumRows() * 10 % getNumRows());
		int c = (int)(Math.random() * getNumCols() * 10 % getNumCols());
		Location loc = new Location(r,c);
		if(get(loc) == 0) return loc;
		return getRandomOffLocation();
	}
	
	protected Location getNeighborPoint(Location loc, int direction) {
		while(direction < 0) direction += 360;
		while(direction >= 360) direction -= 360;
		direction = direction - (direction % 90);
		if(direction == Grid.NORTH) return new Location(loc.row - 1, loc.col);
		if(direction == Grid.EAST) return new Location(loc.row, loc.col + 1);
		if(direction == Grid.SOUTH) return new Location(loc.row + 1, loc.col);
		if(direction == Grid.WEST) return new Location(loc.row, loc.col - 1);
		return null;
	}
	
	protected void addSnake(Location loc) {
		int size = snakeLines.size();
		SnakeLine snake = new SnakeLine(size + 1);
		snakeLines.add(snake);
		for(int x = 0; x < 5; x++) {
			snake.addLast(loc);
			loc = getNeighborPoint(loc, snake.getDirection() - 180);
		}
	}
	
	protected void setGameOver(int num) {
		gameOver = true;
		for(int r = 0; r < getNumRows(); r++) {
			for(int c = 0; c < getNumCols(); c++) {
				Location loc = new Location(r,c);
				if(get(loc) == num) set(loc, 0);
			}
		}
	
	}
	
	protected void setEatLoction(int num) {
		Location loc = getRandomOffLocation();
		this.set(loc, num);
	}
	
	protected class SnakeLine extends SnakeList<Location>{
		
		private final int num;
		private int snakeDirection, score;
		private long startTime, endTime, startPauseTime, endPauseTime;
		private boolean canTurn;
		
		public SnakeLine(int num) {
			super();
			this.num = num;
			this.snakeDirection = Grid.NORTH;
			this.score = 0;
			this.canTurn = true;
			this.startTime = System.currentTimeMillis();
		}
		
		public int getDirection() {return snakeDirection;}
		public int getNum() {return num;}
		public int getScore() {return score;}
		public boolean canTurn() {return canTurn;}
		public void setCanTurn(boolean b) {canTurn = b;}
		
		public void setDirection(int d) {
			snakeDirection = d - (d % 90);
			while(snakeDirection < 0) snakeDirection += 360;
			while(snakeDirection >= 360) snakeDirection -= 360;
		}
		
		public void addFirst(Location loc) {
			super.addFirst(loc);
			set(loc, num);
		}
		
		public void addLast(Location loc) {
			super.addLast(loc);
			set(loc, num);
		}
		
		public void removeLast() {
			Location loc = getLast();
			set(loc, 0);
			super.removeLast();
		}
		
		public void pause() {
			if(!isPaused()) startPauseTime += System.currentTimeMillis();
			else endPauseTime += System.currentTimeMillis();
		}
		
		public void adjustScore() {
			endTime = System.currentTimeMillis();
			
			long elasped = (endTime - startTime) - (endPauseTime - startPauseTime);
			
			score += (size() / 4) + (50000 / (scoreMultiplier * (int)elasped));
			endPauseTime = startPauseTime = 0;
			startTime = System.currentTimeMillis();
		}	
	}
	
	class Location {
		
		public final int row, col;
		
		public Location(int r, int c) {
			this.row = r;
			this.col = c;
		}
		
		public boolean equals(Object other) {
			Location p = (Location) other;
			if(row != p.row) return false;
			return col == p.col; 
		}
	}
	
	
	
}
