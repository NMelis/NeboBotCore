package ru.nebolife.bot.core.listeners;

public interface LiftListener extends BaseListener {
    void onUp();
    void onTip(String typeTip, String countTip);
}
