package snake.gui;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class GenerateFruit {


    private SegmentPlacement fruitPlacement;

    private Fruit currentFruitType;

    private int appleCounter = 0;

    private static final int PEAR_TIME_COUNT = 2;

    boolean pearTime() {

        if (appleCounter < PEAR_TIME_COUNT) {
            appleCounter++;
            return false;
        }

        appleCounter = 0;
        return true;
    }

}
