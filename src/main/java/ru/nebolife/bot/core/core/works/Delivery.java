package ru.nebolife.bot.core.core.works;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.DeliveryListener;


public class Delivery {
    private final DeliveryListener deliveryListener;
    private final RequestCore requestCore;

    public Delivery(RequestCore requestCore, DeliveryListener deliveryListener) {
        this.requestCore = requestCore;
        this.deliveryListener = deliveryListener;
    }

    public void run() throws StopBotException {
        this.requestCore.go("/floors/0/3");

        while (true) {
            Elements elements = this.requestCore.doc.select("span>a.tdu[href*=floorPanel:state:action::ILinkListener]");
            if (elements.first() == null) { deliveryListener.onFinish(); break; }
            Element element = elements.first();
            String[] priceBuyProduct = element.parent().parent().parent().parent().parent().select("div").first().select("span").get(1).text().split("\\.");
            this.requestCore.go(element.attr("href"));
            deliveryListener.onDelivery(priceBuyProduct[0]);
        }

    }
}
