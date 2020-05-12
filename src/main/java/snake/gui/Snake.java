package snake.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import snake.player.Direction;

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

    Direction getHeadNodeDirection() {
        return body.getFirst().getDirection();
    }

    void setHeadNodeDirection(Direction headNodeDirection) {
       // this.headNodeDirection = headNodeDirection;
        body.getFirst().setDirection(headNodeDirection);
    }

   private Direction getTailNodeDirection() {
        return body.getLast().getDirection();
    }

   private void setTailNodeDirection(Direction tailNodeDirection) {
        // this.headNodeDirection = headNodeDirection;
        body.getLast().setDirection(tailNodeDirection);
    }

  //  private Direction headNodeDirection;

    private boolean dead;

    private int fieldWidth;

    private int fieldHeight;

    private HashSet<SegmentPlacement> walls;

    private boolean fruitEaten;

    private GenerateFruit fruit;

    private boolean pearEaten;

   // private GenerateFruit generateFruit;

    // Certain amount of space to be spawned away from a wall
   // private static final int FREE_ROAM = 1;

    private static final int SPACING_OF_SNAKE_MOVEMENT =1;

    public void killSnake() {
        dead = true;
    }

    static int getSnakeInitialSize() {
        return INITIAL_SNAKE_SEGMENT_COUNT;
    }

     SnakeSegment getSnakeTail() {
        return body.getLast();
    }

     boolean movement(SegmentPlacement currentFruit) {

        SnakeSegment head = body.getFirst();

        Direction headNodeDirection=getHeadNodeDirection();
      //  body.removeLast();

        switch (headNodeDirection) {

            case UP:
                head = new SnakeSegment(head.getX(), head.getY() - SPACING_OF_SNAKE_MOVEMENT,headNodeDirection);
                break;
            case DOWN:
                head = new SnakeSegment(head.getX(), head.getY() + SPACING_OF_SNAKE_MOVEMENT,headNodeDirection);
                break;
            case LEFT:
                head = new SnakeSegment(head.getX() - SPACING_OF_SNAKE_MOVEMENT, head.getY(),headNodeDirection);
                break;
            case RIGHT:
                head = new SnakeSegment(head.getX() + SPACING_OF_SNAKE_MOVEMENT, head.getY(),headNodeDirection);
                break;

            default:
                throw new IllegalArgumentException("Invalid command, please use arrow keys to operate");
        }


      checkIfFruitEaten(head,currentFruit);



        return checkForDeath(head);
    }

    private void checkIfFruitEaten(SnakeSegment snakeHead, SegmentPlacement currentFruit){

        if(!(snakeHead.equals(currentFruit))){
            body.removeLast();
            pearEaten=false;
            fruitEaten=false;
        }else {
            if(fruit.getCurrentFruitType().equals(Fruit.PEAR)){
               reverseDirection();
               pearEaten=true;

              // body.removeLast();
              //  body.removeFirst();
              //  snakeHead=null;
            }
            fruitEaten=true;
            fruit.setFruitPlacement(null);
        }
    }

    private void reverseDirection(){


        //body.getLast().setDirection(body.getLast().getDirection().getOppositeDirection());
        SnakeSegment prev=null;
       // setHeadNodeDirection(getHeadNodeDirection().getOppositeDirection());
        for(SnakeSegment ssg : body){
           // ssg.setDirection(ssg.getDirection().getOppositeDirection());
            if(prev!=null) {
                prev.setDirection(ssg.getDirection().getOppositeDirection());
            }
            prev =ssg;

        }
        setTailNodeDirection(getTailNodeDirection().getOppositeDirection());


        Collections.reverse(body);
        body.removeFirst();
    }


    private boolean checkForDeath(SnakeSegment snakeSegment) {

        if (walls.contains(snakeSegment) || body.contains(snakeSegment)) {
            setDead(true);
          return false;
        }else{
            if(pearEaten) {
                body.addLast(snakeSegment);
            }else{
                body.addFirst(snakeSegment);
            }

            return true;
        }
    }



}
