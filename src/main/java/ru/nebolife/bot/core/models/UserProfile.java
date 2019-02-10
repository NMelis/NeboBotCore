package ru.nebolife.bot.core.models;

public class UserProfile {
    public String nickName;
    public String lvl;
    public String coin;
    public String gold;
    public int liftAlreadyGiveDollars = -1;
    public int liftCanAllGiveDollars = -1;
    public int liftVisitors = 0;
    public int liftL = 0;
    public int liftSpace = 0;
    public boolean liftIsCanUpgradeSpeed = false;
    public boolean liftIsCanUpgradeSpace = false;
}
