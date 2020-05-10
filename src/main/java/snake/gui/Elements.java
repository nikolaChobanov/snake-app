package snake.gui;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
class Elements {

    private final static String snakeElement = "*";

    final static String wallElement = "#";

    final static String apple = "o";

    private final static String pear = "d";

    private final  String grass = " ";


    String getSnakeElement() {
        return snakeElement;
    }
}
