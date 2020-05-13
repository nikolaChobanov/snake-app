package snake.gui;

import lombok.Getter;

@Getter
public enum GuiElements {

    SNAKE("*"),
    WALL("#"),
    APPLE("o"),
    PEAR("d"),
    GRASS(" ");

    private String sign;

    GuiElements(String sign) {
        this.sign = sign;
    }
}
