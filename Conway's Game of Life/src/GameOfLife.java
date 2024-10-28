import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;



public class GameOfLife extends Application{
	
	
	
	int rows = 20;
	int columns = 20;
	int cellSize = 25;
	int width = rows * cellSize;
	int height = columns * cellSize;
	
	@Override
	public void start(Stage stage) throws Exception {
		Random randomGen = new Random();
		
		
		//Create canvas
		Canvas canvas = new Canvas(width, height);
		stage.setTitle("Game Of Life");
		// Make the scene work
        GraphicsContext gc =  canvas.getGraphicsContext2D(); 
		Group group = new Group(canvas);
		Scene scene = new Scene(group, width + 200 , height+ 200);
		stage.setScene(scene);
		
		
		
		//lets start the program baby
		
		int generations = 0;
		int[][] currentGen = new int[rows][columns];
		int[][] nextGen = new int[rows][columns];
		Rectangle[][] rectangles = new Rectangle[rows][columns];
		
		setSeed(currentGen, randomGen);
		createRectangles(rectangles, width, height, rows, columns, currentGen, cellSize);
		
		
		drawGrid(gc, width, height, rows, columns);
		drawCells(group, rectangles);
		
		
		
		// Create a timeline for changing colors every second
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> changeColors(rectangles, currentGen, nextGen)));
        timeline.setCycleCount(1000);
        timeline.play();
        
        stage.show();
	
		
		
		
		
		
		
		
	}
	
	
	//Draws the Grid 
	public static void drawGrid(GraphicsContext gc, int width, int height, int rows, int columns) {
		for (int x = 0; x <= width; x += width/columns) {
			gc.moveTo(x, 0);
			gc.lineTo(x, height);
            gc.stroke();
		}
		for (int y = 0; y <= height; y += height/rows) {
			gc.moveTo(0, y);
			gc.lineTo(width, y);
			gc.stroke();
		}
	}
	
	//Sets the seed for the inital state of the game
	public static void setSeed(int[][] grid, Random generator) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j ++) {
				grid[i][j] = generator.nextInt(2);
			}
		}
	}
	
	
	//Create the rectangles given the state of the seed
	public static void createRectangles(Rectangle[][] rectangles, int width, int height, int rows, int columns, int[][] gen, int cellSize) {
		for (int x = 0; x < rectangles.length; x++) {
			for (int y = 0; y < rectangles[x].length; y++) {
				rectangles[x][y] = new Rectangle(x * cellSize, y * cellSize, width / columns, height / rows);
				if (gen[x][y] == 1) {
					rectangles[x][y].setFill(Color.BLACK);
				}else {
					rectangles[x][y].setFill(Color.WHITE);
				}
					
			}
		}
		
	}
	
	
	public static void drawCells(Group group, Rectangle[][] rectangles) {
			for (int x = 0; x < rectangles.length; x++) {
				for (int y = 0; y <rectangles[x].length; y++) {
					group.getChildren().add(rectangles[x][y]);
				}
			}
		
	}
	
	
	public static void createNewGen(int[][] current, int [][]next) {
		
		for (int x = 0; x < current.length; x++) {
	        System.arraycopy(current[x], 0, next[x], 0, current[x].length);
		}
		
		
		for (int x = 0; x < current.length; x++) {
			for (int y = 0; y < current[x].length; y ++) {
				
				//Game of Life logic
				
				//Check neightbors depending on placement
				int aliveNeighborCount = 0;
				
				
				if (x == 0 && y == 0) {
					if (current[x + 1][y] == 1) {  //Top left corner
						aliveNeighborCount++;
					} if (current[x][y + 1] == 1) {
						aliveNeighborCount++;
					} if (current[x + 1][y + 1] == 1) {
						aliveNeighborCount++;
					}
				}else if (x == current.length - 1 && y== 0) { //Top right corner
					if (current[x - 1][y] == 1) {
						aliveNeighborCount++;
					} if (current[x][y + 1] == 1) {
						aliveNeighborCount++;
					} if (current[x - 1][y + 1] == 1) {
						aliveNeighborCount++;
					}
				}else if (x == 0 && y == current[x].length - 1) { //Bottom left corner
					if (current[x][y - 1] == 1) {
						aliveNeighborCount++;
					} if (current[x + 1][y] == 1) {
						aliveNeighborCount++;
					} if (current[x + 1][y - 1] == 1) {
						aliveNeighborCount++;
					}
				}else if (x == current.length - 1 && y == current[x].length - 1) { //Botton right corner
					if (current[x - 1][y] == 1) {
						aliveNeighborCount++;
					} if (current[x][y - 1] == 1) {
						aliveNeighborCount++;
					} if (current[x - 1][y - 1] == 1) {
						aliveNeighborCount++;
					}
					
				}else if (y == 0) { //Top row no corners
					for(int i = x - 1; i < x + 2; i++ ) {
						for (int j = y; j < y + 2; j++) {
							if (current[i][j] == 1 && (i != x || j != y)) {
								aliveNeighborCount++;
							}
						}
					}
				}else if(x == 0) { //Left Column
					for (int i = x; i < x + 2; i++) {
						for (int j = y - 1; j < y + 2; j++) {
							if (current[i][j] == 1 && (i != x || j != y)) {
								aliveNeighborCount++;
							}
						}
					}
				}else if (y == current[x].length - 1) { //Bottom Row
					for (int i = x - 1; i < x + 2; i++) {
						for (int j = y - 1; j < y + 1; j++) {
							if (current[i][j] == 1 && (i != x || j != y)) {
								aliveNeighborCount++;
							}
						}
					}
				}else if (x == current.length - 1) {// Right Column
					for (int i = x - 1; i < x + 1; i++) {
						for (int j = y - 1; j < y + 2; j++) {
							if(current[i][j] == 1 && (i != x || j != y)){
								aliveNeighborCount++;
							}
						}
					}
				}else { //All the other non edge cases
					for (int i = x - 1; i < x + 2; i++) {
						for (int j = y - 1; j < y + 2; j++) {
							if(current[i][j] == 1 && (i != x || j != y)) {
								aliveNeighborCount++;
							}
						}
					}
				}
				
				
				
				
				
				
				
				//Change value based on neighbor amount
				if (current[x][y] == 1) {
					if (aliveNeighborCount < 2 || aliveNeighborCount > 3) { 
						next[x][y] = 0;
					}
				}else {
					if(aliveNeighborCount == 3) {
						next[x][y] = 1;
					}
				}
				
				
				
				
				
				
				}
		}
		
		//swap next generation to current
		for (int x = 0; x < current.length; x++) {
	        System.arraycopy(next[x], 0, current[x], 0, current[x].length);
		}
		
		
		
		
	}
	
	public void changeColors(Rectangle[][] rectangles, int[][] current, int[][] next) {
        for (int x = 0; x < rectangles.length; x++) {
            for (int y = 0; y < rectangles[x].length; y++) {
                if (current[x][y] == 0) {
                	rectangles[x][y].setFill(Color.WHITE);
                }else {
                	rectangles[x][y].setFill(Color.BLACK);
                }
            }
        }
        
        
        createNewGen(current, next);
	}
	
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}