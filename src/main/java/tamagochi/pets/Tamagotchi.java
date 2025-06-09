package tamagochi.pets;

import tamagochi.model.Reaction;

public class Tamagotchi {

  protected int happiness = 4;

  public Reaction feed() {
    happiness = Math.min(happiness + 1, 4);
    return null;
  }

  public Reaction play() {
    happiness = Math.min(happiness + 1, 10);
    return null;
  }

  public Reaction punish() {
    happiness = Math.max(happiness - 1, 0);
    return null;
  }

  public boolean isAlive() {
    return happiness > 0;
  }
}
