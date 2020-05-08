package snake.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import snake.player.Direction;
import snake.player.SegmentPlacement;

import java.util.LinkedList;

@Data
@Getter
@Builder
@AllArgsConstructor
public class Snake {

    private static final int INITIAL_SNAKE_SEGMENT_COUNT = 3;

    private LinkedList<SegmentPlacement> body;

    private Direction direction;

    private boolean dead;

    private int fieldWidth;

    private int fieldHeight;

    // Certain amount of space to be spawned away from a wall
    private static final int FREE_ROAM = 1;

    public void killSnake() {
        dead = true;
    }

     static int getSnakeInitialSize() {
        return INITIAL_SNAKE_SEGMENT_COUNT;
    }

}
