package a_star;

import simplegui.SGMouseListener;
import simplegui.SimpleGUI;

/**
 * A-Star
 * Created by Connor Crawford on 12/9/15.
 */
public class Vertex implements SGMouseListener {
    private int row, col, x, y;
    Vertex[] adjacent = new Vertex[8];
    Vertex[] edges = new Vertex[8];
    int num_adjacent, num_edges = 0;
    SimpleGUI simpleGUI = null;
    boolean isClosed = false;
    double g_distance = Double.MAX_VALUE;
    double h_distance = Double.MAX_VALUE;
    Grid grid = null;

    public Vertex(SimpleGUI simpleGUI, Grid grid, int row, int col, int length) {
        this.simpleGUI = simpleGUI;
        simpleGUI.registerToMouse(this);
        this.grid = grid;
        this.row = row;
        this.col = col;
        this.x = row*(simpleGUI.getWidth()-20)/(length-1) + 10;
        this.y = col*(simpleGUI.getHeight()-20)/(length-1) + 10;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    boolean isAdjacent(Vertex vertex) {
        for (Vertex v : adjacent)
            if (v.equals(vertex))
                return true;
        return false;
    }

    void draw() {
        simpleGUI.drawDot(x, y, 4);
        for (Vertex v : edges) {
            if (v != null)
                simpleGUI.drawLine(x, y, v.x, v.y);
        }
    }

    @Override
    public void reactToMouseClick(int i, int i1) {
        if (i < x+9 && i > x-9) {
            if (i1 < y+9 && i1 >y-9) {
                if (grid.waitingForStart) {
                    grid.start = this;
                    grid.waitingForStart = false;
                    grid.waitingforEnd = true;
                }
                if (grid.waitingforEnd) {
                    if (grid.start != this) {
                        grid.end = this;
                        grid.waitingforEnd = false;
                        grid.findPath();
                    }
                }
            }

        }
    }

}
