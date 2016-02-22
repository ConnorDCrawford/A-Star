package a_star;

import simplegui.GUIListener;
import simplegui.SimpleGUI;

/**
 * A-Star
 * Created by Connor Crawford on 12/9/15.
 */
public class Visualizer implements GUIListener {

    SimpleGUI simpleGUI;
    Grid grid = null;
    Size size;

    public Visualizer() {
        simpleGUI = new SimpleGUI(800, 600);
        simpleGUI.labelButton1("Reset");
        simpleGUI.labelButton2("Do nothing");
        simpleGUI.registerToGUI(this);
        size = determineSize(simpleGUI.getSliderValue());
        grid = new Grid(simpleGUI, size, null);
        simpleGUI.print("Click on a start vertex, then click on an end vertex. Use the slider to choose number of vertices.");
        grid.getStart();

    }

    private Size determineSize(int sliderValue) {
        if (sliderValue < 34)
            return Size.SMALL;
        if (sliderValue < 67)
            return Size.MEDIUM;
        return Size.LARGE;
    }

    @Override
    public void reactToButton1() {
        simpleGUI.eraseAllDrawables();
        size = determineSize(simpleGUI.getSliderValue());
        grid = new Grid(simpleGUI, size, null);
        grid.getStart();
    }

    @Override
    public void reactToButton2() {
        // Do nothing
    }

    @Override
    public void reactToSwitch(boolean b) {

    }

    @Override
    public void reactToSlider(int i) {

    }
}
