package ru.nebolife.bot.core.core;

import ru.nebolife.bot.core.helpers.StopBotException;

import java.io.IOException;

public interface BotInterface {
    Object login() throws IOException, StopBotException;
}
