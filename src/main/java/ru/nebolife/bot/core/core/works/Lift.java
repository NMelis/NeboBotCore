package ru.nebolife.bot.core.core.works;

import javafx.scene.paint.Stop;
import org.jsoup.nodes.Element;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.*;


public class Lift  {
    RequestCore requestCore;
    private String highСost;
    public static final boolean FIRST = true;
    public static final boolean LAST = false;

    public Lift(RequestCore requestCore) {
        this.requestCore = requestCore;
    }

    public void info() throws StopBotException {
        this.requestCore.go("/lift");
        firstInfo();
        this.requestCore.go("/lobby");
        this.requestCore.profile.liftL = this.requestCore.getElementInt("div.main>div>div>div.nfl>div>span", FIRST);
        this.requestCore.profile.liftSpace = getSpaceToLift();

        if (this.requestCore.doc.select("a[href*=:liftUpgrade:liftUpgradeBlock:liftUpgradeLink:link::ILinkListener::]").first() != null){
            this.requestCore.profile.liftIsCanUpgradeSpeed = true;
        }
        if (this.requestCore.doc.select("a[href*=:lobbyUpgrade:lobbyUpgradeBlock:lobbyUpgradeLink:link::ILinkListener::]").first() != null){
            this.requestCore.profile.liftIsCanUpgradeSpace = true;
        }

    }

    private void firstInfo(){
        this.requestCore.profile.liftAlreadyGiveDollars = this.requestCore.getElementInt("div.cntr.small>span>span", FIRST);
        this.requestCore.profile.liftAlreadyGiveDollars = this.requestCore.getElementInt("div.cntr.small>span>span", LAST);
        this.requestCore.profile.liftVisitors = this.requestCore.getElementInt("div.main>div>span.white", FIRST);
    }

    private int getSpaceToLift() {
        try{
            String element = this.requestCore.getElementText("div.main>div>div>div.nfl>div>span", LAST);
            StringBuilder nums = new StringBuilder();
            for(int i = 0; i < 3; i++){
                try{
                    char elChar = element.charAt(i);
                    if (elChar == 32) continue;
                    int a = Character.getNumericValue(elChar);
                    nums.append(a);
                }catch (NumberFormatException ignored){
                }
            }
            return Integer.parseInt(nums.toString());
        }catch (Exception e){
            return -1;
        }
    }

    public void run(LiftListener liftListener) throws StopBotException {
        this.requestCore.go("/lift");
        firstInfo();
        while (true) {
            Element upLink = this.requestCore.doc.select("span>a.tdu[href*=liftState:upLink::ILinkListener::]").first();
            Element tipsLink = this.requestCore.doc.select("span>a.tdu[href*=liftState:tipsLink:link::ILinkListener::]").first();
            if (upLink != null) {
                this.requestCore.go(upLink.attr("href"));
                liftListener.onUp();
            } else if (tipsLink != null) {
                Element gold = this.requestCore.doc.select("span.ctrl>b.amount>img[src=/images/icons/mn_gold.png]").first();
                Element coin = this.requestCore.doc.select("span.ctrl>b.amount>img[src=/images/icons/st_sold.png]").first();
                this.requestCore.go(tipsLink.attr("href"));
                if (gold != null) liftListener.onTip("gold", gold.text());
                else if (coin != null) liftListener.onTip("coin", coin.text());

            } else { liftListener.onFinish(); break;}
        }
    }

    public void payAllDollars(GetOntInfoListener getOntInfoListener) throws StopBotException {
        this.requestCore.go("/lift");
        firstInfo();
        try {
            int alreadyGiveDollars = Integer.parseInt(this.requestCore.doc.select("div.cntr.small>span>span").first().text());
            int canAllGiveDollars = Integer.parseInt(this.requestCore.doc.select("div.cntr.small>span>span").last().text());
            if (alreadyGiveDollars == canAllGiveDollars){
                getOntInfoListener.response("Уже все баксы были получены");
                return;
            }
        }catch (NullPointerException ignored){
            getOntInfoListener.response("Похоже вы уже все баксы были получили");
            return;
        }

        this.requestCore.go("/change");
        Element payAllLink = this.requestCore.doc.select("div>span>a[href*=payAllLink:link::ILinkListener::]").first();
        if (payAllLink == null) {
            getOntInfoListener.response("Уже все баксы были получены");
            return;
        }
        this.requestCore.go(payAllLink.attr("href"));

        Element confirmLink = this.requestCore.doc.select("a[href*=:confirmLink::ILinkListener::]").first();
        if (confirmLink == null) {
            getOntInfoListener.response("Походу у вас вдруг монеты кончились");
            return;
        }
        this.requestCore.go(confirmLink.attr("href"));
        Element feedbackPanelINFO = this.requestCore.doc.select("span.feedbackPanelINFO").first();
        if (feedbackPanelINFO != null){
            getOntInfoListener.response(feedbackPanelINFO.text());
            return;
        }
        getOntInfoListener.response("Илон макс черт подери");



    }

    public void processLiftAll(GetOntInfoListener listener) throws StopBotException{
        this.requestCore.go("/lift");
        firstInfo();
        Element processLiftAllLink = this.requestCore.doc.select("a.tdu[href*=:processLiftAll:link::ILinkListener]").first();
        if (processLiftAllLink == null) {
            listener.response("Нету народов в лифте");
            return;
        }
        this.requestCore.go(processLiftAllLink.attr("href"));

        Element feedbackPanelERROR = this.requestCore.doc.select("span.feedbackPanelERROR").first();
        if (feedbackPanelERROR != null){
            listener.response("У вас нету баксов для этого");
            return;
        }

        Element feedbackPanelINFO = this.requestCore.doc.select("span.feedbackPanelINFO").first();
        if (feedbackPanelINFO != null){
            listener.response(feedbackPanelINFO.text());
        }
        Element messageElemets = this.requestCore.doc.select("div.main>div>div.ntfy.white").first();
        if (messageElemets != null) listener.response(messageElemets.text());

    }

    public void activateLift(GetOntInfoListener listener) throws StopBotException{
        this.requestCore.go("/lift");
        firstInfo();
        Element activateLiftLink = this.requestCore.doc.select("a.tdu[href*=:activateLiftLink:link::ILinkListener::]").first();
        if (activateLiftLink == null) {
            listener.response("Все баксы с лифта успешно куплены");
            return;
        }
        this.requestCore.go(activateLiftLink.attr("href"));
        Element feedbackPanelERROR = this.requestCore.doc.select("span.feedbackPanelERROR").first();
        if (feedbackPanelERROR != null){
            listener.response("У вас нету баксов для этого");
            return;
        }
        Element feedbackPanelINFO = this.requestCore.doc.select("span.feedbackPanelINFO").first();
        if (feedbackPanelINFO != null){
            listener.response(feedbackPanelINFO.text());
        }

    }

    public void lifter15(GetOntInfoListener getOntInfoListener) throws StopBotException {
        this.requestCore.go("/lift/results");
        Element price = this.requestCore.doc.select("a.tdu[href*=:lifterPanel:doLifterLink:link::ILinkListener]").first();
        if (price == null){
            getOntInfoListener.response("Пока не доступен лифтер, вам нужно сначало выкупить все баксы с лифта");
            return;
        }
        this.requestCore.go(price.attr("href"));
        Element info1 = this.requestCore.doc.select("div.main>div>div.ntfy.notify").first();
        if (info1 != null) getOntInfoListener.response(info1.text());
        Element info2 = this.requestCore.doc.select("div.main>div>div.ntfy.white").first();
        if (info2 != null) getOntInfoListener.response(info2.text());
        Element feedbackPanelERROR = this.requestCore.doc.select("span.feedbackPanelERROR").first();
        if (feedbackPanelERROR != null) getOntInfoListener.response(feedbackPanelERROR.text());
    }
}
