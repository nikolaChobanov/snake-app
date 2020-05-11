package snake.gui;

import lombok.Getter;

@Getter
public enum Fruit {

    APPLE("o"),
    PEAR("d");

    private String sign;

    Fruit(String sign) {
        this.sign=sign;
    }
}
