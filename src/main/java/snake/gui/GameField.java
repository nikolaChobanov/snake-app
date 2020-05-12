package snake.gui;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import snake.player.Direction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameField {

    //30
    private static int fieldWidth;
    //10
    private static int fieldHeight;

    private static SwingTerminal terminal;

    private static Screen screen;

  //  private CurrentState currentState;

    private Snake snake;

    private Elements elements;

    private ArrayList<String> field;

    private HashSet<SegmentPlacement> walls;

    private GenerateFruit generateFruit;


    private GameField(String fieldFileName) {

        generateFruit = new GenerateFruit();
        walls = new HashSet<>();
        field = new ArrayList<>();
        constructField(fieldFileName);

    }

    private void createGuiScreen() {

        elements = new Elements();
        terminal = new SwingTerminal(fieldWidth, fieldHeight);
        screen = new Screen(terminal);
        // screen.setCursorPosition(null);
        screen.startScreen();
    }


    private Key readDirectionInput() {
        return terminal.readInput();
    }

    private void keyboardInput() {
        Key k = readDirectionInput();


        if (k != null) {

            switch (k.getKind()) {
                case ArrowUp:
                    if (!Direction.UP.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.UP);
                    }
                    break;

                case ArrowDown:
                    if (!Direction.DOWN.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.DOWN);
                    }
                    break;

                case ArrowLeft:
                    if (!Direction.LEFT.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.LEFT);
                    }
                    break;

                case ArrowRight:
                    if (!Direction.RIGHT.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.RIGHT);
                    }
                    break;


                default:
                    break;
            }
        }
    }

    private void playGame() {

        int turnCounter = 20;

        while (!(snake.isDead())) {


            turnCounter++;

            if (turnCounter >= 20) {
                spawnFruit();
                turnCounter = 0;
            }
            //  screen.refresh();

            keyboardInput();


            aTurn();


            try {
                Thread.sleep(300);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    private void aTurn() {


        SnakeSegment snakeTail = snake.getSnakeTail();

        clearFieldSegmentPlacement(snakeTail);

        // if(counter % selected_speed == 0)
        if (snake.movement(generateFruit.getFruitPlacement())) {

            SnakeSegment newHead = snake.getBody().getFirst();
            drawString(newHead.getX(), newHead.getY(), elements.getSnakeElement(), Terminal.Color.CYAN);
        } else {

            for (SnakeSegment sp : snake.getBody()) {
                clearFieldSegmentPlacement(sp);
            }

            int x = fieldWidth / 2;
            int y = fieldHeight / 2;
            drawString(x, y, "GAME", Terminal.Color.CYAN);
            drawString(x, ++y, "OVER", Terminal.Color.CYAN);
        }
        screen.refresh();

    }


    private void spawnFruit() {

        boolean isItPearTime = generateFruit.pearTime();

        SegmentPlacement currentFruit = generateFruit.getFruitPlacement();
        //   int snakeSizeBefore=snake.getBody().size();
        if (currentFruit != null && (!snake.isFruitEaten())) {
            clearFieldSegmentPlacement(currentFruit);
        }

        SegmentPlacement place = findPlaceForFruit();
        if (!isItPearTime) {
            generateFruit.setCurrentFruitType(Fruit.APPLE);
            generateFruit.setFruitPlacement(place);
            drawString(place.getX(), place.getY(), Fruit.APPLE.getSign(), Terminal.Color.RED);
        } else {
            generateFruit.setCurrentFruitType(Fruit.PEAR);
            generateFruit.setFruitPlacement(place);
            drawString(place.getX(), place.getY(), Fruit.PEAR.getSign(), Terminal.Color.MAGENTA);
        }
    }

    private SegmentPlacement findPlaceForFruit() {

        //So apple doesn't spawn on wall
        SegmentPlacement segmentPlacement;
        do {
            segmentPlacement = new SegmentPlacement(1, fieldHeight, 1, fieldWidth);
        } while (isWall(segmentPlacement.getX(), segmentPlacement.getY()));

        //So apple doesn't spawn on snake
        if (snake.getBody().contains(segmentPlacement)) {
            return findPlaceForFruit();
        } else {
            return segmentPlacement;
        }

    }


    private File accessFile(String fileName) {
        return new File(
                Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile()
        );

    }

    private void markWalls(String line, int currHeight) {
        int widthCounter = 0;
        String[] splitStr = line.split("");
        for (String ch : splitStr) {

            if (ch.equals(Elements.wallElement)) {
                walls.add(new SegmentPlacement(widthCounter, currHeight));
            }
            widthCounter++;
        }
    }


    private void constructField(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(accessFile(fileName)))) {

            //StringBuffer sb = new StringBuffer();

            fieldWidth = Integer.parseInt(br.readLine());
            fieldHeight = Integer.parseInt(br.readLine());

            createGuiScreen();

            String line;
            int heightCounter = 0;

            while ((line = br.readLine()) != null) {
                markWalls(line, heightCounter);
                field.add(line);
                drawString(0, heightCounter++, line, Terminal.Color.YELLOW);
            }


            //   screen.readInput();
            findSnakePlacement();
            screen.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawString(int x, int y, String string, Terminal.Color color) {
        screen.putString(x, y, string, color, null);
    }

    private void clearFieldSegmentPlacement(SegmentPlacement snakeSegment) {
        drawString(snakeSegment.getX(), snakeSegment.getY(), elements.getGrass(), null);
    }

    private boolean isWall(int x, int y) {
        return walls.contains(new SegmentPlacement(x, y));
    }

    private void findSnakePlacement() {
        //SnakeSegment start = snake.getBody().getLast();
        int snakeHeight;
        int snakeWidth;

        do {
            SegmentPlacement segmentPlacement = new SegmentPlacement(0, fieldHeight, 0, fieldWidth);
            snakeHeight = segmentPlacement.getY();
            snakeWidth = segmentPlacement.getX();
        } while (isWall(snakeWidth, snakeHeight) || isWall(snakeWidth + 1, snakeHeight)
                || isWall(snakeWidth + 2, snakeHeight)
                || isWall(snakeWidth - 1, snakeHeight)
                || isWall(snakeWidth - 2, snakeHeight));

        createSnake(snakeHeight, snakeWidth);

    }

    private void createSnake(int snakeHeight, int snakeWidth) {

        LinkedList<SnakeSegment> body = new LinkedList<>();

        Direction direction = Direction.values()[new Random().nextInt(Direction.values().length - 2) + 2];

        // System.out.println(headNodeDirection.toString());

        //so you don't get spawned directly ahead of the wall
        if (direction.equals(Direction.LEFT)) {
            for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
                body.add(new SnakeSegment(i + snakeWidth, snakeHeight,direction));
            }
        } else if (direction.equals(Direction.RIGHT)) {
            for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
                body.add(new SnakeSegment(snakeWidth - i, snakeHeight,direction));
            }
        }


        snake = Snake.builder().fieldWidth(fieldWidth).fieldHeight(fieldHeight).body(body).dead(false)
               .walls(walls).fruit(generateFruit).build();
        snake.setHeadNodeDirection(direction);

        for (SnakeSegment sp : snake.getBody()) {
            drawString(sp.getX(), sp.getY(), elements.getSnakeElement(), Terminal.Color.CYAN);
        }

    }

    public static void main(String[] args) {

        String filename = "Field1.txt";
        String filename2 = "FieldWalls.txt";

        GameField field = new GameField(filename);

        field.playGame();

    }
}
