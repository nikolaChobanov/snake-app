package snake.player;

import lombok.Getter;

@Getter
public enum Direction {
    UP,DOWN,LEFT,RIGHT;

    static{
        (DOWN.oppositeDirection = UP).oppositeDirection=DOWN;
        (LEFT.oppositeDirection=RIGHT).oppositeDirection=LEFT;
    }


    private Direction oppositeDirection;


}
