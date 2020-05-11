package snake.player;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
public class SnakeSegment {

    //width
    private final int x;

    //height
    private final int y;

  //  private Direction direction;


    public SnakeSegment(int heightLowerBound, int heightUpperBound, int widthLowerBound, int widthUpperBound){

        this.x= ThreadLocalRandom.current().nextInt(widthLowerBound, widthUpperBound);
        this.y=ThreadLocalRandom.current().nextInt(heightLowerBound, heightUpperBound);

    }


}
