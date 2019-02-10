package ru.nebolife.bot.core.listeners;

public interface QuestsListener extends BaseListener {
    void onCatch(String titleQuest, String coinAward, String goldAward);
}
