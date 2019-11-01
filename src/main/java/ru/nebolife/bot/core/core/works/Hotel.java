package ru.nebolife.bot.core.core.works;

import org.jsoup.nodes.Element;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.HotelListener;
import ru.nebolife.bot.core.models.Human;

public class Hotel {
    RequestCore requestCore;
    private String highСost;
    public static final boolean FIRST = true;
    public static final boolean LAST = false;

    public Hotel(RequestCore requestCore) {
        this.requestCore = requestCore;
    }

    public void evict(final String humanLink, HotelListener hotelListener) throws StopBotException {
        this.requestCore.go(humanLink);

        Element evictLink = this.requestCore.doc.select("a[href*=evictLink::ILinkListener::]").first();
        this.requestCore.go(evictLink.attr("href"));
        if (this.requestCore.doc.title().equals("1 этаж")){
            hotelListener.onFinish();
        }
        else {
            hotelListener.onError();
        }

    }

}
