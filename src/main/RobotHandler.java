package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JFrame;

import main.Map.ObjectType;

public class RobotHandler extends JFrame {
	
	private static final long serialVersionUID = 2280874288993963333L;
	
	static RobotHandler monitor;
	static Map map;
	
	static InputStream inputStream;
	static DataInputStream dataInputStream;
			
	public RobotHandler() {
		super("Robot Handler");
		int width = (int) (Map.gridWidth * Map.rowCount + Map.lineWidth * (Map.rowCount - 1));
		int height = (int) (Map.gridWidth * Map.colnCount + Map.lineWidth * (Map.colnCount - 1));
		setSize(width, height);
		setVisible( true );
		
		map = new Map();
	}
	
	public static void main(String[] args) throws Exception	{
		monitor = new RobotHandler();
		
		monitor.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		monitor.setResizable(false);
		
		String ip = "10.0.1.1";
		
		@SuppressWarnings("resource")
		Socket socket = new Socket(ip, 1029);
		System.out.println("Connected!");
		
		inputStream = socket.getInputStream();
		dataInputStream = new DataInputStream(inputStream);
		
		int index = 0;
		while(true) {
			int value = dataInputStream.readInt();
			if (value == -1) {
				if (index == 0) {
					readMap: while(true) {
						value = dataInputStream.readInt();
						switch (value) {
							case -1:
								break readMap;
							case 0:
								map.gridValues[index] = ObjectType.UKNOWN;
								break;
							case 1:
								map.gridValues[index] = ObjectType.EXPLORED;
								break;
							case 2:
								map.gridValues[index] = ObjectType.OBSTACLE;
								break;
							case 3:
								map.gridValues[index] = ObjectType.STATION;
								break;
							case 4:
								map.gridValues[index] = ObjectType.TARGET;
								break;
						}
						index++;
					}
					index = 0;
					monitor.repaint();
				}
			}
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		displayMap(map, g2D);
	}

	public void displayMap(Map map, Graphics2D g) {
		g.setPaint(Color.WHITE);
		Map.gridWidth = (monitor.getWidth() - Map.rowCount + 1) / Map.rowCount;
		g.fillRect(0, 0, monitor.getWidth(), monitor.getHeight());
		
		g.setPaint(Color.BLACK);
		g.setStroke(new BasicStroke(Map.lineWidth));
		
		for (float i = 1;i < Map.rowCount;i++)
			g.draw(new Line2D.Double(0, i * Map.gridWidth + (i - 1) * Map.lineWidth,
					monitor.getWidth(), i * Map.gridWidth + (i - 1) * Map.lineWidth));
		
		for (float i = 1;i < Map.colnCount;i++)
			g.draw(new Line2D.Double(
					i * Map.gridWidth + (i - 1) * Map.lineWidth, 0,
					i * Map.gridWidth + (i - 1) * Map.lineWidth, monitor.getHeight()));
		
		for (int r = 0;r < Map.rowCount;r++)
			for (int c = 0;c < Map.colnCount;c++) {
				g.setPaint(ObjectType.getColor(map.gridValues[r * 6 + c]));
				g.fillRect(
						(int)(c * Map.gridWidth + c * Map.lineWidth),
						(int)(r * Map.gridWidth + r * Map.lineWidth),
						(int)Map.gridWidth, (int)Map.gridWidth);
			}
		
		g.setPaint(ObjectType.getColor(ObjectType.AGENT));
		g.fillOval(map.agent.x, map.agent.y, 10, 10);
	}
}

