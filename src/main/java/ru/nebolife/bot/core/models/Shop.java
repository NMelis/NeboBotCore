package ru.nebolife.bot.core.models;

public class Shop {
    public enum PR_MR {
        ZERO,
        FIFTY,
        HUNDRED,
        TWO_HUNDRED,
        THREE_HUNDRED
    }
    public PR_MR pr = PR_MR.ZERO;
    public PR_MR mr = PR_MR.ZERO;
}
