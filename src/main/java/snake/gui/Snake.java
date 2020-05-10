package snake.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import snake.player.Direction;
import snake.player.SegmentPlacement;

import java.util.HashSet;
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

    private HashSet<SegmentPlacement> walls;

    // Certain amount of space to be spawned away from a wall
    private static final int FREE_ROAM = 1;

    public void killSnake() {
        dead = true;
    }

    static int getSnakeInitialSize() {
        return INITIAL_SNAKE_SEGMENT_COUNT;
    }

     SegmentPlacement getSnakeTail() {
        return body.getLast();
    }

     boolean movement(SegmentPlacement currentFruit) {

        SegmentPlacement head = body.getFirst();

      //  body.removeLast();

        switch (direction) {
            case UP:
                head = new SegmentPlacement(head.getX(), head.getY() - 1);
                break;
            case DOWN:
                head = new SegmentPlacement(head.getX(), head.getY() + 1);
                break;
            case LEFT:
                head = new SegmentPlacement(head.getX() - 1, head.getY());
                break;
            case RIGHT:
                head = new SegmentPlacement(head.getX() + 1, head.getY());
                break;

            default:
                throw new IllegalArgumentException("Invalid command, please use arrow keys to operate");
        }

       checkIfFruitEaten(head,currentFruit);



        return checkForDeath(head);
    }

    private void checkIfFruitEaten(SegmentPlacement segmentPlacement, SegmentPlacement currentFruit){

        if(!(segmentPlacement.equals(currentFruit))){
            body.removeLast();
        }
    }


    private boolean checkForDeath(SegmentPlacement segmentPlacement) {

        if (walls.contains(segmentPlacement) || body.contains(segmentPlacement)) {
            setDead(true);
          return false;
        }else{
            body.addFirst(segmentPlacement);
            return true;
        }
    }



}
