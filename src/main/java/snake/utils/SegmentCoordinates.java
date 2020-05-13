package snake.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
public
class SegmentCoordinates {

    //width
    private final int x;
    //height
    private final int y;

    /**
     * Picks a random set of coordinates
     *
     * @param heightLowerBound lower and upper
     * @param heightUpperBound bounds for
     * @param widthLowerBound  randomizing
     * @param widthUpperBound  a location
     */
    public SegmentCoordinates(int heightLowerBound, int heightUpperBound, int widthLowerBound, int widthUpperBound) {

        this.x = ThreadLocalRandom.current().nextInt(widthLowerBound, widthUpperBound);
        this.y = ThreadLocalRandom.current().nextInt(heightLowerBound, heightUpperBound);
    }
}
