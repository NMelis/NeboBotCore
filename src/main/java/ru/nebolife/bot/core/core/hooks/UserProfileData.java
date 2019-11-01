package ru.nebolife.bot.core.core.hooks;

import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.models.UserProfile;


public class UserProfileData implements BaseHookInterface {
    private RequestCore requestCore;
    private UserProfile userProfile;

    public UserProfileData(RequestCore requestCore, UserProfile userProfile) {
        this.requestCore = requestCore;
        this.userProfile = userProfile;
    }

    @Override
    public void run() {
        if (requestCore.doc.title().equals("Небоскребы: онлайн игра")){
            this.main();
        }

    }

    private void main(){
        int totalCountHuman = requestCore.getElementInt("div.tower>div>div.rs>a>span>span>span", true);
        int totalFreePlace = requestCore.getElementInt("div.tower>div>div.rs>a>span>span>span", false);
        if (totalCountHuman > 0){ this.userProfile.hotel.totalCountHuman = totalCountHuman; }
        this.userProfile.hotel.totalFreePlace = totalFreePlace;

    }
}
