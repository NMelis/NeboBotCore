package ru.nebolife.bot.core.core.works;

import org.jsoup.nodes.Element;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.CollectRevenueListener;

public class CollectRevenue {
    RequestCore requestCore;
    CollectRevenueListener listener;
    private String highСost;

    public CollectRevenue(RequestCore requestCore, CollectRevenueListener collectRevenueListener) {
        this.requestCore = requestCore;
        this.listener = collectRevenueListener;
    }

    public void run(String highcost) throws StopBotException {
        this.highСost = highcost;
        this.requestCore.go("/floors/0/2");
        while (true) {
            Element linkToFloor = this.requestCore.doc.select("span>a.tdu[href*=floor/0/]").first();
            if (linkToFloor == null) { listener.onFinish(); break; }
            this.requestCore.go(linkToFloor.attr("href"));
            String title = this.requestCore.doc.title();
            Element linkToBuyProduct = getSelectedProductForBuy();
            this.requestCore.go(linkToBuyProduct.attr("href"));
            this.listener.onBuy(title, "100");
        }

        


    }
    private Element getSelectedProductForBuy(){
        Element linkA = this.requestCore.doc.select("span>a.tdu[href*=:floorPanel:productA:emptyState:action:link::ILinkListener::]").first();
        Element linkB = this.requestCore.doc.select("span>a.tdu[href*=:floorPanel:productB:emptyState:action:link::ILinkListener::]").first();
        Element linkC = this.requestCore.doc.select("span>a.tdu[href*=:floorPanel:productC:emptyState:action:link::ILinkListener::]").first();
        if (highСost.equals("A")){
            if (linkA != null) return linkA;
            else if (linkB != null) return linkB;
            return linkC;
        }else if (highСost.equals("B")){
            if (linkB != null) return linkB;
            else if (linkC != null) return linkC;
            return linkA;
        }else{
            if (linkC != null) return linkC;
            else if (linkB != null) return linkB;
            return linkA;
        }
    }
    
}
