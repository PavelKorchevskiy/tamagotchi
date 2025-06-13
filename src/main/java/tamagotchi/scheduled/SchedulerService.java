package tamagotchi.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tamagotchi.TamagotchiBot;

@Service
public class SchedulerService {

private TamagotchiBot bot;

    @Scheduled(cron = "0 12 * * *")
//    @Scheduled(fixedRate = 5000)
    public void abandonTamagotchis() {
        bot.abandon();
    }

    @Autowired
    public SchedulerService(TamagotchiBot bot) {
        this.bot = bot;
    }
}
