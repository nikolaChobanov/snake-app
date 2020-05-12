package snake.gui;

import lombok.Getter;
import lombok.Setter;
import snake.player.Direction;

@Getter
@Setter
class SnakeSegment extends SegmentPlacement{

    private Direction direction;


    SnakeSegment(int x, int y, Direction direction) {
        super(x, y);
        this.direction=direction;
    }
}
