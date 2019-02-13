package ru.nebolife.bot.core;

import ru.nebolife.bot.core.core.sites.NeboMobi;
import ru.nebolife.bot.core.core.works.Lift;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;
import ru.nebolife.bot.core.listeners.LiftGetAllDollarsListener;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        final NeboMobi bot = new NeboMobi("Dddx", "qwe123");
        try {
            bot.login();
            final Lift lift = bot.Lift();
            lift.info();
            System.out.println("nickName: " + bot.profile.nickName);
            System.out.println("coin: " + bot.profile.coin);
            System.out.println("gold: " + bot.profile.gold);
            System.out.println("lvl: " + bot.profile.lvl);
            System.out.println("Полученные баксики: " + bot.profile.liftAlreadyGiveDollars);
            System.out.println("Сколько всего баксов: " + bot.profile.liftCanAllGiveDollars);
            System.out.println("Людей чейчас в лифте: " + bot.profile.liftVisitors);
            System.out.println("Скорость лифта: " + bot.profile.liftL);
            System.out.println("Вместимость лифта: " + bot.profile.liftSpace);
            System.out.println("Доступно ли увеличение скорости лифта: " + bot.profile.liftIsCanUpgradeSpeed);
            System.out.println("Доступно ли увеличение вместимости лифта: " + bot.profile.liftIsCanUpgradeSpace);
            lift.payAllDollars(new GetOntInfoListener() {
                @Override
                public void response(String message) {
                    System.out.println(message);
                }
            });
            lift.lifter15(new GetOntInfoListener() {
                @Override
                public void response(String message) {
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
