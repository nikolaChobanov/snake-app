package snake.utils;

import lombok.Getter;
import lombok.Setter;
import snake.player.Direction;

@Getter
@Setter
public
class SnakeSegment extends SegmentCoordinates {

    private Direction direction;

    public SnakeSegment(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }
}
