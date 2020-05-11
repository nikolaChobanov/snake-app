package snake.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import snake.player.Direction;
import snake.player.SnakeSegment;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

@Data
@Getter
@Builder
@AllArgsConstructor
public class Snake {

    private static final int INITIAL_SNAKE_SEGMENT_COUNT = 3;

    private LinkedList<SnakeSegment> body;

    private Direction direction;

    private boolean dead;

    private int fieldWidth;

    private int fieldHeight;

    private HashSet<SnakeSegment> walls;

    private boolean fruitEaten;

    private GenerateFruit fruit;

   // private GenerateFruit generateFruit;

    // Certain amount of space to be spawned away from a wall
    private static final int FREE_ROAM = 1;

    public void killSnake() {
        dead = true;
    }

    static int getSnakeInitialSize() {
        return INITIAL_SNAKE_SEGMENT_COUNT;
    }

     SnakeSegment getSnakeTail() {
        return body.getLast();
    }

     boolean movement(SnakeSegment currentFruit) {

        SnakeSegment head = body.getFirst();

      //  body.removeLast();

        switch (direction) {

            case UP:
                head = new SnakeSegment(head.getX(), head.getY() - 1);
                break;
            case DOWN:
                head = new SnakeSegment(head.getX(), head.getY() + 1);
                break;
            case LEFT:
                head = new SnakeSegment(head.getX() - 1, head.getY());
                break;
            case RIGHT:
                head = new SnakeSegment(head.getX() + 1, head.getY());
                break;

            default:
                throw new IllegalArgumentException("Invalid command, please use arrow keys to operate");
        }

       checkIfFruitEaten(head,currentFruit);



        return checkForDeath(head);
    }

    private void checkIfFruitEaten(SnakeSegment snakeSegment, SnakeSegment currentFruit){

        if(!(snakeSegment.equals(currentFruit))){
            body.removeLast();

            fruitEaten=false;
        }else {
            if(fruit.getCurrentFruitType().equals(Fruit.PEAR)){
               reverseDirection();
               body.removeLast();
            }
            fruitEaten=true;
            fruit.setFruitPlacement(null);
        }
    }

    private void reverseDirection(){

        Collections.reverse(body);
        setDirection(getDirection().getOppositeDirection());
    }


    private boolean checkForDeath(SnakeSegment snakeSegment) {

        if (walls.contains(snakeSegment) || body.contains(snakeSegment)) {
            setDead(true);
          return false;
        }else{
            body.addFirst(snakeSegment);
            return true;
        }
    }



}
