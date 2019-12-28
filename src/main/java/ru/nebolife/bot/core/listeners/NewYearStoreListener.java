package ru.nebolife.bot.core.listeners;

public interface NewYearStore extends BaseListener {
    void getPrize(String message);
    void getProduct(String message);
    void addedProduct(String message);
}
