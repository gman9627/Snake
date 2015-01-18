package main;

import grid.Grid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import config.Configuration;

public class GamePanel extends JPanel implements KeyListener, Runnable{

	private Grid grid;
	private int numRows, numCols, cellSize;
	private Dimension dim;
	
	private boolean running;
	
	private Thread thread;
	
	public GamePanel() {
		super();
		setGrid(Configuration.getExpert());
		repaint();
		setFocusable(true);
		requestFocus();
	}
	
	public Grid getGrid() {return grid;}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
		numRows = grid.getNumRows();
		numCols = grid.getNumCols();
		this.cellSize = 8;
		dim = new Dimension((cellSize + 1) * numCols, (cellSize + 1) * numRows + 20);
		setSize(dim);
		setPreferredSize(dim);
		repaint();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
	
			int[] player1 = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT};
			int[] player2 = {KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A};
			
			this.addKeyListener(this);
			this.addKeyListener(new PlayerKeyListener(player1, 1));
			this.addKeyListener(new PlayerKeyListener(player2, 2));
			
			thread.start();
		}
	}
	
	private int colToXCoord(int col) {
	    return col * (cellSize + 1) + 1 + getInsets().left;
	}
	  
	private int rowToYCoord(int row) {
	    return row * (cellSize + 1) + 1 + getInsets().top;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		Insets insets = getInsets();
	    g2.setColor(grid.getColor(0));
	    g2.fillRect(insets.left, insets.top, dim.width, dim.height);
		
	    drawGridlines(g2);
	    fillGrid(g2);
	    drawScoreLine(g2);
	}
	
	private void drawScoreLine(Graphics2D g2) {
		g2.setColor(Color.darkGray);
		int y = numRows * (cellSize + 1);
		g2.drawLine(0, y, dim.width, y);
		int x = 5; 
		y += 15;
		for(int i = 1; i <= grid.getNumPlayers(); i++) {
			g2.setColor(grid.getColor(i));
			g2.drawString("Score: " + grid.getScore(i), x, y);
			x += 75;
		}
	}
	
	private void drawGridlines(Graphics2D g2) {
		Rectangle curClip = g2.getClip().getBounds();
	    int top = getInsets().top;
	    int left = getInsets().left;
	    
	    int miny = Math.max(0, (curClip.y - top) / (this.cellSize + 1)) * (this.cellSize + 1) + top;
	    int minx = Math.max(0, (curClip.x - left) / (this.cellSize + 1)) * (this.cellSize + 1) + left;
	    int maxy = Math.min(this.numRows, (curClip.y + curClip.height - top + this.cellSize) / (this.cellSize + 1)) * (this.cellSize + 1) + top;
	    int maxx = Math.min(this.numCols, (curClip.x + curClip.width - left + this.cellSize) / (this.cellSize + 1)) * (this.cellSize + 1) + left;
	    
	    if(grid.getOriginalSpeed() >= 200) 
	    	g2.setColor(Color.darkGray);
	    for (int y = miny; y <= maxy; y += this.cellSize + 1) {
	    	g2.drawLine(minx, y, maxx, y);
	    }
	    for (int x = minx; x <= maxx; x += this.cellSize + 1) {
	    	g2.drawLine(x, miny, x, maxy);
	    }
	}
	
	private void fillGrid(Graphics2D g2) {
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int xleft = colToXCoord(c);
				int ytop = rowToYCoord(r);
				g2.setColor(grid.getColor(grid.get(r, c)));
				g2.fillRect(xleft, ytop, cellSize, cellSize);
				//drawOccupant(g2, xleft, ytop, cell);	
			}
		}
	}
	
	@Override
	public void run() {
		
		running = true;
		
		long start;
		long elapsed;
		long wait;
		
		while(running) {
			start = System.nanoTime(); 
			
			if( !(grid.isGameOver() || grid.isPaused()) ) {			
				grid.setCanTurn(0, true);
				grid.act();
				repaint();	
			}
			
			elapsed = System.nanoTime() - start;
			wait = grid.getSpeed() - elapsed / 1000000;
			if(wait < 0) wait = 5;
			
			try {
				thread.sleep(wait);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_SPACE || key == KeyEvent.VK_P)
			grid.pause();
		else if(key == KeyEvent.VK_N) 
			setGrid(Configuration.getNewGame(grid));	
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
	class PlayerKeyListener implements KeyListener {

		private int keys[];
		private int num;
		
		public PlayerKeyListener(int[] keys, int num) {
			super();
			this.keys = keys;
			this.num = num;
		}
		
		public void keyPressed(KeyEvent e) {
			if(grid.isPaused() || grid.isGameOver() || !grid.canTurn(num)) return;
			int key = e.getKeyCode();
			int direction = grid.getDirection(num);
			if(key == keys[0]) {
				if(direction == Grid.WEST) grid.setDirection(num, direction + 90);
				else if(direction == Grid.EAST) grid.setDirection(num, direction - 90);
			}
			else if(key == keys[1]) {
				if(direction == Grid.NORTH) grid.setDirection(num, direction + 90);
				else if(direction == Grid.SOUTH) grid.setDirection(num, direction - 90);
			}
			else if(key == keys[2]) {
				if(direction == Grid.WEST) grid.setDirection(num, direction - 90);
				else if(direction == Grid.EAST) grid.setDirection(num, direction + 90);
			}
			else if(key == keys[3]) {
				if(direction == Grid.NORTH) grid.setDirection(num, direction - 90);
				else if(direction == Grid.SOUTH) grid.setDirection(num, direction + 90);
			}
			grid.setCanTurn(num, false);
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
		
	}

}
