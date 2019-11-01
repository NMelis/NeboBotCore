package ru.nebolife.bot.core;

import ru.nebolife.bot.core.core.sites.NeboMobi;
import ru.nebolife.bot.core.helpers.StopBotException;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws StopBotException, IOException {

        final NeboMobi bot = new NeboMobi("Dddx", "qwe123");
        bot.logEnabled(false);
        bot.login();


    }
}
