package ru.nebolife.bot.core.models;

public class City {
    public String name;
    public boolean isCanInviteUsers = false;
    public int lvl;
    public int haveUsers = 0;
    public int placeForUsers = 0;

    public int howFreePlace(){
        return placeForUsers - haveUsers;
    }
}
