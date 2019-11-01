package ru.nebolife.bot.core.core.hooks;


import ru.nebolife.bot.core.helpers.StopBotException;

public interface BaseHookInterface {
    void run() throws StopBotException;
}
