package ru.nebolife.bot.core.core.works;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.listeners.RevenueBuildListener;


public class RevenueBuild {
    private RequestCore requestCore;
    private RevenueBuildListener listener;

    public RevenueBuild(RequestCore requestCore, RevenueBuildListener revenueBuildListener) {
        this.requestCore = requestCore;
        this.listener = revenueBuildListener;
    }

    public void run() {
        this.requestCore.go("/floors/0/5");
        while (true) {
            Elements elements = this.requestCore.doc.select("span>a.tdu[href*=floorPanel:state:action::ILinkListener]");
            if (elements.first() == null) { listener.onFinish(); break; }
            Element element = elements.first();
            String priceBuyProduct = element.parent().select("span").first().select("span").text();
            listener.onRevenue(priceBuyProduct);
            this.requestCore.go(element.attr("href"));
        }
    }
}