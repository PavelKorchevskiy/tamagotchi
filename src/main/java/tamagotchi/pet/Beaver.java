package tamagotchi.pet;

import tamagotchi.model.Reaction;

public class Beaver extends Tamagotchi {

  private static final String PATH = "/photo/beaver_";
  private static final String PATH_EATS = "/photo/beaver_eats_";
  private static final String PATH_DEATH = "/photo/beaver_death.jpg";

  @Override
  public Reaction feed() {
    happiness = Math.min(happiness + 1, 4);
    return new Reaction(PATH_EATS + happiness + ".jpg", "Вы покормили своего бобра!");
  }

  @Override
  public Reaction play() {
    happiness = Math.min(happiness + 1, 4);
    return new Reaction(PATH + happiness+ ".jpg", "Вы поиграли с бобром!");
  }

  @Override
  public Reaction punish() {
    happiness--;
    if (happiness < 1) {
      return new Reaction(PATH_DEATH,
          "Сегодня мы прощаемся с замечательным бобром. Он был любящим отцом, добрым братом и отличным строителем платин. Пусть его душа мирно покоится на небесах. \n\n Может следующему тамогочи повезет больше.");
    }
    return new Reaction(PATH + happiness + ".jpg", "Вы наказали своего бобра");
  }

  @Override
  public Reaction abandon() {
    happiness--;
    if (happiness < 1) {
      return new Reaction(PATH_DEATH,
              "Сегодня из-за одиночества, стресса и голода скончался бобр. Он был любящим отцом, добрым братом и отличным строителем платин. Пусть его душа мирно покоится на небесах. \n\n Может следующему тамогочи повезет больше.");
    }
    return null;
  }
}
