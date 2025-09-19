package tamagotchi.pet;

import tamagotchi.model.Reaction;

import static tamagotchi.util.Utils.pathWithHappiness;
import static tamagotchi.util.Utils.pathWithSizePng;

public class Snake extends Tamagotchi {

    private static final String PATH_EATS = "/photo/snake/eats";
    private static final String PATH_DEATH = "/photo/snake/dead";
    private static final String PATH_PLAY = "/photo/snake/played";
    private static final String PATH_SAD = "/photo/snake/sad";

    protected static final String FED = "snake.fed";
    protected static final String PLAYED = "snake.played";
    protected static final String PUNISHED = "snake.punished";
    protected static final String DEAD_PUNISHED = "snake.dead.punished";
    protected static final String DEAD_ABANDONED = "snake.dead.abandoned";


    private int size = 0;

    @Override
    public Reaction feed() {
        size = Math.min(size + 1, 3);
        return new Reaction(pathWithSizePng(PATH_EATS, size), FED);
    }

    @Override
    public Reaction play() {
        happiness = Math.min(happiness + 1, 4);
        return new Reaction(pathWithSizePng(PATH_PLAY, size), PLAYED);
    }

    @Override
    public Reaction punish() {
        happiness--;
        if (happiness < 1) {
            return new Reaction(pathWithSizePng(PATH_DEATH, size), DEAD_PUNISHED);
        }
        return new Reaction(pathWithSizePng(PATH_SAD, size), PUNISHED);
    }

    @Override
    public Reaction abandon() {
        happiness--;
        if (happiness < 1) {
            return new Reaction(pathWithSizePng(PATH_DEATH, size), DEAD_ABANDONED);
        }
        return null;
    }
}
