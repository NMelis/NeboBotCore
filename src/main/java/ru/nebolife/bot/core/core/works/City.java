package ru.nebolife.bot.core.core.works;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.GetOntInfoListener;

import java.util.ArrayList;
import java.util.List;

public class City {
    RequestCore requestCore;
    public List<String> invitedUsers = new ArrayList<String>();
    int passInviteReinviteUsers = 0;

    public enum Position {
        NewUser,
        Citizen,
        Businessman,
        Advisor,
        ViceMayor,
    }

    public class InviterUser{
        String nickname;
        int lvl;
        int days;
        boolean want;
        String linkProfile;

        public InviterUser(String nickname, int lvl, int days, String linkProfile) {
            this.nickname = nickname;
            this.lvl = lvl;
            this.days = days;
            this.linkProfile = linkProfile;
        }
    }

    public City(RequestCore requestCore) {
        this.requestCore = requestCore;
    }

    public void getInfo() throws StopBotException {
        this.requestCore.go("city");
        this.requestCore.profile.city.name = this.requestCore.getElementText("div.main>div>div.nfl.ny>div>span>strong", true);
        this.requestCore.profile.city.lvl = this.requestCore.getElementInt("div.main>div>div.nfl.ny>div.snow>b.white", true);
        this.requestCore.profile.city.haveUsers = this.requestCore.getElementInt("div.main>div>div.nfl>b", true);
        this.requestCore.profile.city.placeForUsers = this.requestCore.getElementInt("div.main>div>div.nfl>span", true);
        if (!this.requestCore.getElementText("div.main>div>div.nfl>div.small.minor.nshd>a", true).equals("")){
            this.requestCore.profile.city.isCanInviteUsers = true;
        }
    }
    public void growUpUser(String userUrl, Position position, GetOntInfoListener listener) throws StopBotException{
        String userId = userUrl.split("/")[3];
        this.requestCore.go("/city/role/0/" + userId);
        if (!this.requestCore.doc.title().equals("Доступ закрыт")){
            String growUpLink = null;
            String growUp = "";
            try {

                if (position == Position.Citizen) {
                    growUp = "горожанин";
                    growUpLink = this.requestCore.doc.selectFirst("a:contains(горожанин)").attr("href");
                }
                if (position == Position.Businessman) {
                    growUp = "бизнесмен";
                    growUpLink = this.requestCore.doc.selectFirst("a:contains(бизнесмен)").attr("href");
                }
                if (position == Position.Advisor) {
                    growUp = "советник";
                    growUpLink = this.requestCore.doc.selectFirst("a:contains(советник)").attr("href");
                }
                if (position == Position.ViceMayor) {
                    growUp = "вице-мэр";
                    growUpLink = this.requestCore.doc.selectFirst("a:contains(вице-мэр)").attr("href");
                }
            }catch (NullPointerException error){
                listener.response("Невозможно изменить должность (Возможно у пользователя и так установлен такой должность какой нужен)");
            }
            
            if (growUpLink != null){
                this.requestCore.go(growUpLink);
                if (this.requestCore.doc.title().equals("Профиль")){
                    String userNikName = this.requestCore.getElementText("div.snow>span>strong>span.user>span>span", true);
                    listener.response("Должность '"+growUp+"' установлен для пользователя: "+userNikName);
                }
            }
        }
    }

    public void runInvite(int minLvl, int maxLvl,int minDays, int maxDays, GetOntInfoListener listener) throws StopBotException{
        while (true) {
            this.requestCore.go("online/nocity");
            ArrayList<InviterUser> users = getUsers();
            if (users.isEmpty()){
                try {
                    Thread.sleep(10000);
                    listener.response("Нету пользователей без городов в онлайне, ждем 10 секунд...");
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (InviterUser user : users) {
                if (passInviteReinviteUsers > 20) { this.invitedUsers.clear(); }
                if (this.invitedUsers.contains(user.linkProfile)) {
                    this.passInviteReinviteUsers++;
                    continue;
                }

                if (minLvl < user.lvl && user.lvl < maxLvl && minDays < user.days && user.days < maxDays) {
                    if (inviteUser(user.linkProfile)) {
                        listener.response("[Приглашен] Пользователь \"" + user.nickname + "\" (Ур: " + user.lvl + ", Дней: " + user.days + ") ");
                        this.invitedUsers.add(user.linkProfile);
                    } else {
                        listener.response("[Не успели пригласить] Пользователя \"" + user.nickname + "\" с (Ур: " + user.lvl + ", Дней: " + user.days + ")");
                    }
                } else {
                    this.invitedUsers.add(user.linkProfile);
                    listener.response("[Не подходит по критериям] Пользователь \"" + user.nickname + "\" с (Ур: " + user.lvl + ", Дней: " + user.days + ")");
                }

            }
        }
    }

    private boolean inviteUser(String userUrl) throws StopBotException {
        this.requestCore.go(userUrl);
        Element link = this.requestCore.doc.select("a[href*=:guildInviteLink::ILinkListener::]").first();
        if (link != null){
            this.requestCore.go(link.attr("href"));
            Element feedbackPanelINFO = this.requestCore.doc.select("span.feedbackPanelINFO").first();
            if (feedbackPanelINFO != null) {
                return feedbackPanelINFO.text().equals("Приглашение отправлено!");
            }
        }
        return false;
    }

    private ArrayList<InviterUser> getUsers(){
        ArrayList<InviterUser> users = new ArrayList<>();
        Elements usersElements = this.requestCore.doc.select("div.m5>div>span:nth-child(2)>span");

        for (org.jsoup.nodes.Element element: usersElements){
            try {
                Element user = element.parent().parent();
                String userNick = user.select("a>span").text();
                int userLvl = Integer.parseInt(user.select("span").get(4).select("span").text());
                int days = Integer.parseInt(user.select("span.small.minor>span").text());
                String linkProfile = user.select("a").attr("href");
                users.add(new InviterUser(userNick, userLvl, days, linkProfile));
            }catch (Exception ignored){

            }

        }

        return users;
    }

}
