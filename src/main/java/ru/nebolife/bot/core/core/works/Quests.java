package ru.nebolife.bot.core.core.works;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.listeners.QuestsListener;

public class Quests {
    private RequestCore requestCore;
    private QuestsListener listener;
    private String highСost;

    public Quests(RequestCore requestCore, QuestsListener listener) {
        this.requestCore = requestCore;
        this.listener = listener;
    }

    public void getAwards(){
        this.requestCore.go("/quests");
        Elements awards = this.requestCore.doc.select("a[href*=:quest:getAwarLink::ILinkListener]");
        for (Element element: awards){
            String titleQuest = element.parent().parent().select("div>div").first().text();
            String gold = element.parent().parent().select("div.m5").last().select("div>span>span").first().text();
            String coin = element.parent().parent().select("div.m5").last().select("div>span>span").last().text();
            listener.onCatch(titleQuest, coin, gold);
            this.requestCore.go(element.attr("href"));
        }
        listener.onFinish();
    }

}
