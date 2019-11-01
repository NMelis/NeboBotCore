package ru.nebolife.bot.core.core.hooks;

import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.models.UserProfile;

public class PrMr implements BaseHookInterface{
    private final RequestCore requestCore;
    private final Object userProfile;

    public PrMr(RequestCore requestCore, UserProfile userProfile) {
        this.requestCore = requestCore;
        this.userProfile = userProfile;
    }

    @Override
    public void run() {
    }
}
