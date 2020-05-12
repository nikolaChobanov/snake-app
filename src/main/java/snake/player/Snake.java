package snake.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import snake.utils.GenerateFruit;
import snake.gui.GuiElements;
import snake.utils.SegmentCoordinates;
import snake.utils.SnakeSegment;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

@Data
@Getter
@Builder
@AllArgsConstructor
public class Snake {

    private static final int INITIAL_SNAKE_SEGMENT_COUNT = 3;

    private static final int SPACING_OF_SNAKE_MOVEMENT = 1;

    private LinkedList<SnakeSegment> body;

    private boolean dead;

    private HashSet<SegmentCoordinates> walls;

    private boolean pearEaten;

     Direction getHeadNodeDirection() {
        return body.getFirst().getDirection();
    }

    public void setHeadNodeDirection(Direction headNodeDirection) {
        body.getFirst().setDirection(headNodeDirection);
    }

    private Direction getTailNodeDirection() {
        return body.getLast().getDirection();
    }

    private void setTailNodeDirection(Direction tailNodeDirection) {
        body.getLast().setDirection(tailNodeDirection);
    }

    public static int getSnakeInitialSize() {
        return INITIAL_SNAKE_SEGMENT_COUNT;
    }

     SnakeSegment getSnakeTail() {
        return body.getLast();
    }

    /**
     * Calculates new space for a snake segment
     *
     * @param currentFruit - the coordinates for the currently shown fruit
     * @return - validates the new spot and applies effects to the game
     */
     boolean checkIfMoveIsValid(GenerateFruit currentFruit) {

        SnakeSegment head = body.getFirst();

        Direction headNodeDirection = getHeadNodeDirection();
        //  body.removeLast();

        switch (headNodeDirection) {

            case UP:
                head = new SnakeSegment(head.getX(), head.getY() - SPACING_OF_SNAKE_MOVEMENT, headNodeDirection);
                break;
            case DOWN:
                head = new SnakeSegment(head.getX(), head.getY() + SPACING_OF_SNAKE_MOVEMENT, headNodeDirection);
                break;
            case LEFT:
                head = new SnakeSegment(head.getX() - SPACING_OF_SNAKE_MOVEMENT, head.getY(), headNodeDirection);
                break;
            case RIGHT:
                head = new SnakeSegment(head.getX() + SPACING_OF_SNAKE_MOVEMENT, head.getY(), headNodeDirection);
                break;

            default:
                throw new IllegalArgumentException("Invalid command, please use arrow keys to operate");
        }

        checkIfFruitEaten(head, currentFruit);

        return checkIfSurvived(head);
    }

    /**
     * Checks whether there has been a collision
     *
     * @param snakeHead    coordinates of the first element
     * @param currentFruit - coordinates for the available fruit
     */
    private void checkIfFruitEaten(SnakeSegment snakeHead, GenerateFruit currentFruit) {

        if (!(snakeHead.equals(currentFruit.getFruitPlacement()))) {
            body.removeLast();
            pearEaten = false;
        } else {
            if (currentFruit.getCurrentGuiElementsType().equals(GuiElements.PEAR)) {
                reverseDirection();
                pearEaten = true;
            }
            currentFruit.setFruitPlacement(null);
        }
    }

    /**
     * If a pear element has been eaten it
     * rotates the snake's body and begins in the oposite direction
     */
    private void reverseDirection() {


        //body.getLast().setDirection(body.getLast().getDirection().getOppositeDirection());
        SnakeSegment prev = null;
        // setHeadNodeDirection(getHeadNodeDirection().getOppositeDirection());
        for (SnakeSegment ssg : body) {
            // ssg.setDirection(ssg.getDirection().getOppositeDirection());
            if (prev != null) {
                prev.setDirection(ssg.getDirection().getOppositeDirection());
            }
            prev = ssg;

        }
        setTailNodeDirection(getTailNodeDirection().getOppositeDirection());


        Collections.reverse(body);
        body.removeFirst();
    }

    /**
     * Check whether there has been a collision with a wall
     *
     * @param snakeSegment the current head of the snake
     * @return false if there was
     */
    private boolean checkIfSurvived(SnakeSegment snakeSegment) {

        if (walls.contains(snakeSegment) || body.contains(snakeSegment)) {
            setDead(true);
            return false;
        } else {
            if (pearEaten) {
                body.addLast(snakeSegment);
            } else {
                body.addFirst(snakeSegment);
            }

            return true;
        }
    }


}
