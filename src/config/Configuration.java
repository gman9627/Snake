package config;

import grid.*;

public class Configuration {
	
	public static Grid getBeginner() {
		return new SinglePlayerGrid(32, 64, 200);
	}
	
	public static Grid getIntermediate() {
		return new SinglePlayerGrid(32, 64, 150);
	}
	
	public static Grid getExpert() {
		return new SinglePlayerGrid(32, 64, 100);
	}
	
	public static Grid getTwoPlayer() {
		return new TwoPlayerGrid(48, 96, 125);
	}
	
	public static Grid getSpeed() {
		return new SpeedGrid(48, 96, 35);
	}
}
