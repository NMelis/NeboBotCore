package ru.nebolife.bot.core;

import ru.nebolife.bot.core.core.sites.NeboMobi;
import ru.nebolife.bot.core.core.works.City;
import ru.nebolife.bot.core.core.works.Lift;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetInfoListener;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;
import ru.nebolife.bot.core.listeners.LiftGetAllDollarsListener;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        final NeboMobi bot = new NeboMobi("Dddx", "qwe123");
        bot.logEnabled(false);
        try {
            bot.login();
            final City city = bot.City();
            city.getInfo();
            System.out.println(bot.profile.city.name);
            final int[] invite = {0};
            city.runInvite(10, 90, 0, 4000, new GetOntInfoListener() {
                @Override
                public void response(String message) {
                    if (message.contains("Приглашен")) invite[0]++;
                    if (invite[0] == 50) bot.stop();
                    System.out.println(message);
                }
            });

        } catch (IOException e) {
            System.out.println("Не правильный пароль или логинчик");
        } catch (StopBotException e) {
            System.out.println("Бот остановлен");
        }
    }
}
