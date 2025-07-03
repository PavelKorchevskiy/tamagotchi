package tamagotchi.pet;

import tamagotchi.model.Reaction;

import static tamagotchi.util.Utils.pathWithHappiness;

public class PixelBeaver extends Beaver {
    private static final String PATH = "/photo/beaver_pixel/";
    private static final String PATH_EATS = "/photo/beaver_pixel/eats_";
    private static final String PATH_DEATH = "/photo/beaver_pixel/dead.jpg";
    private static final String PATH_PLAY = "/photo/beaver_pixel/played_";
    private static final String PATH_SAD = "/photo/beaver_pixel/sad_";

    @Override
    public Reaction feed() {
        happiness = Math.min(happiness + 1, 4);
        return new Reaction(pathWithHappiness(PATH_EATS, happiness), FED);
    }

    @Override
    public Reaction play() {
        happiness = Math.min(happiness + 1, 4);
        return new Reaction(pathWithHappiness(PATH_PLAY, happiness), PLAYED);
    }

    @Override
    public Reaction punish() {
        happiness--;
        if (happiness < 1) {
            return new Reaction(PATH_DEATH,
                    DEAD_PUNISHED);
        }
        return new Reaction(pathWithHappiness(PATH_SAD, happiness), PUNISHED);
    }

    @Override
    public Reaction abandon() {
        happiness--;
        if (happiness < 1) {
            return new Reaction(PATH_DEATH, DEAD_ABANDONED);
        }
        return null;
    }
}
