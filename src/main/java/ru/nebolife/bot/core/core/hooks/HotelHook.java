package ru.nebolife.bot.core.core.hooks;

import org.jsoup.nodes.Element;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.models.Human;
import ru.nebolife.bot.core.models.UserProfile;

public class HotelHook implements BaseHookInterface {
    private RequestCore requestCore;
    private UserProfile userProfile;

    public HotelHook(RequestCore requestCore, UserProfile userProfile) {
        this.requestCore = requestCore;
        this.userProfile = userProfile;
    }

    @Override
    public void run() throws StopBotException{
        if (requestCore.doc.title().equals("1 этаж")){
            this.oneFloor();
        }
    }

    private void oneFloor() throws StopBotException {
        Element linkToShoAll = requestCore.getElement("a.nshd:contains(Раскрыть список)").first();
        if (linkToShoAll != null){
            requestCore.go(linkToShoAll.attr("href"));

        }
        userProfile.hotel.humansWithPlus = requestCore.getElement("ul>li>div>span:contains((+))").size();
        userProfile.hotel.humansWithMinus = requestCore.getElement("ul>li>div>span:contains((-))").size();
        userProfile.hotel.totalFreePlace = requestCore.getElementInt("div>span.flr.txtar>b>span", true);
        userProfile.hotel.totalCountHuman = requestCore.getElementInt("div>div>span.fll.mw50.txtal>b>span", true);
        int humansNineLvlWithPlus = 0;
        int humansNineLvlWithMinus = 0;
        int humansNineLvlWithout = 0;
        int i = 0;
        userProfile.hotel.humans.clear();
        for (Element item_: requestCore.getElement("div.main>div>div>ul.rsd>li>div.rsdst")){
            Element item = item_.parent();
            if (userProfile.hotel.totalCountHuman - userProfile.hotel.totalFreePlace == i) { break;}
            Human human = new Human();
            human.lvl = Integer.parseInt(item.select("div>b>span").first().text());

            if (item.select("div>span:contains((+))").first() != null){
                human.need = Human.Need.Plus;
            }
            else if (item.select("div>span:contains((-))").first() != null){
                human.need = Human.Need.Minus;
            }else {
                human.need = Human.Need.Free;
            }

            if (human.need == Human.Need.Plus && human.lvl == 9){
                humansNineLvlWithPlus++;
            }

            if (human.need == Human.Need.Minus && human.lvl == 9){
                humansNineLvlWithMinus++;
            }
            if (human.need == Human.Need.Free && human.lvl == 9){
                humansNineLvlWithout++;
            }
            human.name = item.select("div>a>span").first().text();
            human.job = item.select("div>span>span").first().text();
            human.link = item.select("div>a").first().attr("href");
            i++;
            userProfile.hotel.humans.add(human);
        }
        userProfile.hotel.humansNineLvlWithPlus = humansNineLvlWithPlus;
        userProfile.hotel.humansNineLvlWithMinus = humansNineLvlWithMinus;
        userProfile.hotel.humansNineLvlWithout = humansNineLvlWithout;
    }
}
