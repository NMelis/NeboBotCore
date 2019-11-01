package ru.nebolife.bot.core.models;

public class Human {


    public enum Need {
        Plus,
        Minus,
        Free,
    }
    public String name;
    public String job;
    public boolean isWorking = false;
    public int lvl = 1;
    public Need need = Need.Free;
    public String link;
}
