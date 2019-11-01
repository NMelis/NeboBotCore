package ru.nebolife.bot.core.core;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nebolife.bot.core.core.hooks.*;
import ru.nebolife.bot.core.core.works.*;
import ru.nebolife.bot.core.helpers.CheckInfo;
import ru.nebolife.bot.core.helpers.NewVersionApp;
import ru.nebolife.bot.core.helpers.News;
import ru.nebolife.bot.core.helpers.StopBotException;
import ru.nebolife.bot.core.listeners.*;
import ru.nebolife.bot.core.models.UserProfile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestCore {
    public String baseUrl;
    public Document doc;
    protected OkHttpClient client;
    public UserProfile profile = new UserProfile();
    public boolean isStop = false;
    public Response currentResponse;
    private boolean logShow = false;
    private List<BaseHookInterface> hooks = new ArrayList<>();

    public RequestCore(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient.Builder().cookieJar(new CookieStore()).build();
        this.hooks.add(new PrMr(this, this.profile));
        this.hooks.add(new UserProfileData(this, this.profile));
        this.hooks.add(new HotelHook(this, this.profile));
    }

    public void stop(){
        this.isStop = true;
    }

    public void unStop(){
        this.isStop = false;
    }

    public boolean isEquals(String title){
        if (this.doc == null) return false;
        return this.doc.title().equals(title);
    }

    public void goHotel() throws StopBotException {
        this.go(this.doc.select("div.rs>a.flhdr[href*=floor/0/]").first().attr("href"));
    }

    public void go(final String path) throws StopBotException {
        if (this.isStop) { throw new StopBotException();}
        try {
            Thread.sleep(700);
            String url = new URL(new URL(this.baseUrl), path).toString();
            if (logShow) System.out.println("go to: " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            this.currentResponse = client.newCall(request).execute();
            ResponseBody body = this.currentResponse.body();
            if (body != null) {
                doc = Jsoup.parse(body.string());
                this.runHooks();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void runHooks() throws StopBotException {
        if (!this.isExistsName()) return;
        for (BaseHookInterface hook : this.hooks) {
            hook.run();
        }
    }

    public void baseGo(String _url){
        try {
            Thread.sleep(700);
            String url = new URL(_url).toString();
            System.out.println("go to: " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                doc = Jsoup.parse(body.string());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Elements getElement(String cssSelect){
        return this.doc.select(cssSelect);
    }

    public String getElementText(String cssSelect, boolean pos){
        try {
            Elements elements = this.doc.select(cssSelect);
            if (elements == null) return "";
            Element element;
            if (pos) element = elements.first();
            else element = elements.last();
            return element.text();
        }catch (Exception e){
            return "";
        }
    }

    public int getElementInt(String cssSelect, boolean pos){
        try {
            Elements elements = this.doc.select(cssSelect);
            if (elements == null) return -1;
            Element element;
            if (pos) element = elements.first();
            else element = elements.last();
            try {
                return Integer.parseInt(element.text());
            } catch (NumberFormatException e) {
                return -1;
            }
        }catch (Exception e){
            return -1;
        }

    }

    public boolean isExistsName(){
        return this.getElement("html body div.main div.cntr span span.user span span").first() != null;
    }

    protected void firstLoadInfo(){
        profile.nickName = this.getElement("html body div.main div.cntr span span.user span span").first().text();
        profile.coin = this.getElement("span.cash>span.nwr>span>span").first().text();
        profile.gold = this.getElement("span.cash>span.nwr>span>span").last().text();
        profile.lvl = this.getElement("div.main>div.cntr>span>span").last().text();

    }

    public void runManager(RevenueBuildListener revenueBuildListener,
                           DeliveryListener deliveryListener,
                           CollectRevenueListener collectRevenueListener,
                           String highcost, boolean r, boolean d, boolean b) throws StopBotException{
        if (r) new RevenueBuild(this, revenueBuildListener).run();
        if (d) new Delivery(this, deliveryListener).run();
        if (b) new CollectRevenue(this, collectRevenueListener).run(highcost);
    }

    public void revenueBuild(RevenueBuildListener revenueBuildListener)throws StopBotException{
        new RevenueBuild(this, revenueBuildListener).run();
    }

    public void buy(String highcost, CollectRevenueListener collectRevenueListener)throws StopBotException{
        new CollectRevenue(this, collectRevenueListener).run(highcost);
    }

    public void delivery(DeliveryListener deliveryListener)throws StopBotException{
        new Delivery(this, deliveryListener).run();
    }

    public Lift Lift(){
        return new Lift(this);
    }

    public void lift(LiftListener liftListener)throws StopBotException{
        new Lift(this).run(liftListener);
    }

    public void humanInHotelEvict(final String humanLink, HotelListener hotelListener) throws StopBotException{
        new Hotel(this).evict(humanLink, hotelListener);
    }

    public void payAllDollars(GetOntInfoListener getOntInfoListener)throws StopBotException{
        new Lift(this).payAllDollars(getOntInfoListener);
    }

    public void getQuestsAward(QuestsListener questsListener)throws StopBotException{
        new Quests(this, questsListener).getAwards();
    }

    public void isUserVkBaned(String userVkId, CheckInstance checkInstance)throws StopBotException{
        new CheckInfo(this, checkInstance).isUseVkrBaned(userVkId);
    }

    public void isCityBaned(String cityName, CheckInstance checkInstance)throws StopBotException{
        new CheckInfo(this, checkInstance).isCityBaned(cityName);
    }

    public void isUserBaned(String nickname, CheckInstance checkInstance)throws StopBotException{
        new CheckInfo(this, checkInstance).isUserBaned(nickname);
    }

    public void getLastNew(CheckInstanceNew checkInstanceNew)throws StopBotException{
        new News(this, checkInstanceNew).getLastNew();
    }

    public void getNews(CheckInstanceNew checkInstanceNew)throws StopBotException{
        new News(this, checkInstanceNew).getNews();
    }

    public void getLastNewVersion(float currentVersion, NewVersionAppInterface newVersionAppInterface)throws StopBotException{
        new NewVersionApp(this, newVersionAppInterface).getInfoAboutANewVersionApp(currentVersion);
    }

    public City City() {
        return new City(this);
    }

    public void logEnabled(boolean b) {
        this.logShow = b;
    }
}
