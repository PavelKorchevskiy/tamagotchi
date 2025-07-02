package tamagotchi.pet;

import tamagotchi.model.Reaction;

import static tamagotchi.util.Utils.pathWithHappiness;

public class Beaver extends Tamagotchi {

  private static final String PATH = "/photo/beaver/beaver_";
  private static final String PATH_EATS = "/photo/beaver/beaver_eats_";
  private static final String PATH_DEATH = "/photo/beaver/beaver_death.jpg";

  private static final String FED = "beaver.fed";
  private static final String PLAYED = "beaver.played";
  private static final String PUNISHED = "beaver.punished";
  private static final String DEAD_PUNISHED = "beaver.dead.punished";
  private static final String DEAD_ABANDONED = "beaver.dead.abandoned";

  @Override
  public Reaction feed() {
    happiness = Math.min(happiness + 1, 4);
    return new Reaction(pathWithHappiness(PATH_EATS, happiness), FED);
  }

  @Override
  public Reaction play() {
    happiness = Math.min(happiness + 1, 4);
    return new Reaction(pathWithHappiness(PATH, happiness), PLAYED);
  }

  @Override
  public Reaction punish() {
    happiness--;
    if (happiness < 1) {
      return new Reaction(PATH_DEATH,
          DEAD_PUNISHED);
    }
    return new Reaction(pathWithHappiness(PATH, happiness), PUNISHED);
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
