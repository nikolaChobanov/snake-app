package snake.gui;

import lombok.Data;
import lombok.NoArgsConstructor;
import snake.player.SegmentPlacement;

@Data
@NoArgsConstructor
 class GenerateFruit {


    private SegmentPlacement fruit;

  // private SegmentPlacement pear;

    final  String appleSign = "o";

    private final String pearSign = "d";

    private int appleCounter=0;

    private static final int PEAR_TIME_COUNT=5;

     boolean pearTime(){

        if(appleCounter<PEAR_TIME_COUNT){
            appleCounter++;
            return false;
        }

        appleCounter=0;
        return true;
    }
/*
    private SegmentPlacement spawnFruit(int lowerBound, int upperBound){


    }*/
}
