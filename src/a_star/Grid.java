package a_star;

import simplegui.SimpleGUI;

import java.awt.*;
import java.util.Random;

/**
 * A-Star
 * Created by Connor Crawford on 12/9/15.
 */
public class Grid {

    Vertex[][] grid;
    SimpleGUI simpleGUI;
    boolean waitingForStart = false;
    boolean waitingforEnd = false;
    Vertex start = null, end = null;

    public Grid(SimpleGUI simpleGUI, Size size, Vertex[][] grid) {
        int dim;
        if (size == Size.SMALL) dim = 10;
        else if (size == Size.MEDIUM) dim = 20;
        else dim = 30;
        this.simpleGUI = simpleGUI;
        if (grid != null) this.grid = grid;
        else {
            this.grid = new Vertex[dim][dim];
            if (simpleGUI != null)
                createRandomGrid(simpleGUI);
        }
    }

    private void createRandomGrid(SimpleGUI simpleGUI) {
        if (grid == null)
            return;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Random r = new Random();
                grid[i][j] = new Vertex(simpleGUI, this, i, j, grid.length);
                if (i-1 >= 0) {
                    addAdjacency(i, j, i-1, j);
                    if (r.nextBoolean())
                        addEdge(i, j, i-1, j);
                }
                if (j-1 >= 0) {
                    addAdjacency(i, j, i, j-1);
                    if (r.nextBoolean())
                        addEdge(i, j, i, j-1);
                }
                if (i-1 >= 0 && j-1 >= 0) {
                    addAdjacency(i, j, i-1, j-1);
                    if (r.nextBoolean())
                        addEdge(i, j, i-1, j-1);
                }
                if (i-1 >= 0 && j+1 < grid.length) {
                    addAdjacency(i, j, i-1, j+1);
                    if (r.nextBoolean())
                        addEdge(i, j, i-1, j+1);
                }
                grid[i][j].draw();
            }
        }
    }

    private void addAdjacency(int v_row, int v_col, int w_row, int w_col) {
        grid[v_row][v_col].adjacent[grid[v_row][v_col].num_adjacent++] = grid[w_row][w_col];
        grid[w_row][w_col].adjacent[grid[w_row][w_col].num_adjacent++] = grid[v_row][v_col];
    }

    void addEdge(int v_row, int v_col, int w_row, int w_col) {
        if (grid[v_row][v_col].isAdjacent(grid[w_row][w_col])) {
            grid[v_row][v_col].edges[grid[v_row][v_col].num_edges++] = grid[w_row][w_col];
            grid[w_row][w_col].edges[grid[w_row][w_col].num_edges++] = grid[v_row][v_col];
        }
    }

    int getDimension() {
        return grid.length;
    }

    public void visualizePath(SimpleGUI simpleGUI, Vertex[] path) {
        for (int i = 0; i < path.length-1; i++) {
            simpleGUI.drawLine(path[i].getX(), path[i].getY(), path[i+1].getX(), path[i+1].getY(), Color.BLUE, 1.0, 5, "Path");
        }
    }

    public void visualizeVisited(SimpleGUI simpleGUI) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].isClosed)
                    simpleGUI.drawDot(grid[i][j].getX(), grid[i][j].getY(), 8, Color.BLUE, 0.7, "Path");
            }
        }

    }

    public void getStart() {
        waitingForStart = true;
    }

    public void findPath() {
        if (start!=null && end!=null) {
            PathFinder pathFinder = new PathFinder();
            Vertex[] path = pathFinder.findPath(this, start, end);
            visualizeVisited(simpleGUI);
            visualizePath(simpleGUI, path);
        }
    }
}


