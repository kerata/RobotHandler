package main;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Map {
	
	ObjectType[] gridValues = new ObjectType[rowCount * colnCount];
	ArrayList<Point> samples = new ArrayList<Point>();
	Point agent;
	
	static int rowCount = 6;
	static int colnCount = 6;
	static float gridWidth = 100;
	static float lineWidth = 1;
	
	public enum ObjectType {
		EMPTY, OBSTACLE, STATION, TARGET, EXPLORED, AGENT;
		
		public static Color getColor(ObjectType type) {
			switch (type) {
				case OBSTACLE:
					return Color.BLACK;
				case STATION:
					return Color.GREEN;
				case TARGET:
					return Color.RED;
				case EXPLORED:
					return Color.YELLOW;
				case AGENT:
					return Color.BLUE;
				default:
					return Color.WHITE;
			}
		}
	}
	
	public Map() {
		for (int r = 0;r < 6;r++)
			for (int c = 0;c < 6;c++) {
				ObjectType grid = r == 5 ? ObjectType.EXPLORED : ObjectType.EMPTY;
				gridValues[r * 6 + c] = grid;
			}
		
		agent = new Point((int)(gridWidth * 3.5 + 3 * lineWidth),
				(int)(gridWidth * 5.5 + 5 * lineWidth));
	}
}