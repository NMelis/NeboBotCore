package ru.nebolife.bot.core.listeners;

public interface CollectRevenueListener extends BaseListener{
    void onBuy(String floor, String price);
}
