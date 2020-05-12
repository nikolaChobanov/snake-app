package snake.utils;

import lombok.Data;
import snake.gui.GuiElements;

@Data
public class GenerateFruit {


    private SegmentCoordinates fruitPlacement;

    private GuiElements currentGuiElementsType;

    private int appleCounter = 0;

    private static final int PEAR_TIME_COUNT = 2;

    /**
     * Checks whether enough apples have gone by to place a pear
     *
     * @return if it has not false
     */
    public boolean pearTime() {

        if (appleCounter < PEAR_TIME_COUNT) {
            appleCounter++;
            return false;
        }
        appleCounter = 0;
        return true;
    }

}
