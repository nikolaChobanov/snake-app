package snake.gui;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import snake.player.CurrentState;
import snake.player.Direction;
import snake.player.SnakeSegment;

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

    private CurrentState currentState;

    private Snake snake;

    private Elements elements;

    private ArrayList<String> field;

    private HashSet<SnakeSegment> walls;

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
                    if (!Direction.UP.equals(snake.getDirection().getOppositeDirection())) {
                        snake.setDirection(Direction.UP);
                    }
                    break;

                case ArrowDown:
                    if (!Direction.DOWN.equals(snake.getDirection().getOppositeDirection())) {
                        snake.setDirection(Direction.DOWN);
                    }
                    break;

                case ArrowLeft:
                    if (!Direction.LEFT.equals(snake.getDirection().getOppositeDirection())) {
                        snake.setDirection(Direction.LEFT);
                    }
                    break;

                case ArrowRight:
                    if (!Direction.RIGHT.equals(snake.getDirection().getOppositeDirection())) {
                        snake.setDirection(Direction.RIGHT);
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

        SnakeSegment currentFruit = generateFruit.getFruitPlacement();
        //   int snakeSizeBefore=snake.getBody().size();
        if (currentFruit != null && (!snake.isFruitEaten())) {
            clearFieldSegmentPlacement(currentFruit);
        }

        SnakeSegment place = findPlaceForFruit();
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

    private SnakeSegment findPlaceForFruit() {

        //So apple doesn't spawn on wall
        SnakeSegment snakeSegment;
        do {
            snakeSegment = new SnakeSegment(1, fieldHeight, 1, fieldWidth);
        } while (isWall(snakeSegment.getX(), snakeSegment.getY()));

        //So apple doesn't spawn on snake
        if (snake.getBody().contains(snakeSegment)) {
            return findPlaceForFruit();
        } else {
            return snakeSegment;
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
                walls.add(new SnakeSegment(widthCounter, currHeight));
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

    private void clearFieldSegmentPlacement(SnakeSegment snakeSegment) {
        drawString(snakeSegment.getX(), snakeSegment.getY(), elements.getGrass(), null);
    }

    private boolean isWall(int x, int y) {
        return walls.contains(new SnakeSegment(x, y));
    }

    private void findSnakePlacement() {
        //SnakeSegment start = snake.getBody().getLast();
        int snakeHeight;
        int snakeWidth;

        do {
            SnakeSegment snakeSegment = new SnakeSegment(0, fieldHeight, 0, fieldWidth);
            snakeHeight = snakeSegment.getY();
            snakeWidth = snakeSegment.getX();
        } while (isWall(snakeWidth, snakeHeight) || isWall(snakeWidth + 1, snakeHeight)
                || isWall(snakeWidth + 2, snakeHeight)
                || isWall(snakeWidth - 1, snakeHeight)
                || isWall(snakeWidth - 2, snakeHeight));

        createSnake(snakeHeight, snakeWidth);

    }

    private void createSnake(int snakeHeight, int snakeWidth) {

        LinkedList<SnakeSegment> body = new LinkedList<>();

        Direction direction = Direction.values()[new Random().nextInt(Direction.values().length - 2) + 2];

        // System.out.println(direction.toString());

        //so you don't get spawned directly ahead of the wall
        if (direction.equals(Direction.LEFT)) {
            for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
                body.add(new SnakeSegment(i + snakeWidth, snakeHeight));
            }
        } else if (direction.equals(Direction.RIGHT)) {
            for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
                body.add(new SnakeSegment(snakeWidth - i, snakeHeight));
            }
        }


        snake = Snake.builder().fieldWidth(fieldWidth).fieldHeight(fieldHeight).body(body).dead(false)
                .direction(direction).walls(walls).fruit(generateFruit).build();

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
