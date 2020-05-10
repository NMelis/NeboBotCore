package ru.nebolife.bot.core;

import ru.nebolife.bot.core.core.sites.NeboMobi;
import ru.nebolife.bot.core.core.sites.PumpitRu;
import ru.nebolife.bot.core.core.works.City;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws StopBotException, IOException {

        final PumpitRu bot = new PumpitRu("", "");
        bot.logEnabled(true);
        if (bot.login() == null) {
            System.out.println("Incorrect Login or Password");
            return;
        }
        final City city = bot.City();
        city.growUpUser("/tower/id/22986092", City.Position.Citizen, new GetOntInfoListener(){
            @Override
            public void response(String message) {
                System.out.println(message);
            }
        });

    }
}
