package ru.nebolife.bot.core.models;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    public int totalCountHuman = 0;
    public int totalFreePlace = 0;
    public int humansWithPlus = 0;
    public int humansWithMinus;
    public int humansNineLvlWithMinus;
    public int humansNineLvlWithout;
    public int humansNineLvlWithPlus;
    public List<Human> humans = new ArrayList<Human>();
}
