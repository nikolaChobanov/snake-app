package snake.gui;

import lombok.Data;
import lombok.NoArgsConstructor;
import snake.player.SnakeSegment;

@Data
@NoArgsConstructor
class GenerateFruit {


    private SnakeSegment fruitPlacement;

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
