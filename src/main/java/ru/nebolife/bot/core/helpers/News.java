package ru.nebolife.bot.core.helpers;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.listeners.CheckInstanceNew;


public class News {
    private RequestCore requestCore;
    private CheckInstanceNew checkInstanceNew;

    public News(RequestCore requestCore, CheckInstanceNew checkInstanceNew) {
        this.requestCore = requestCore;
        this.checkInstanceNew = checkInstanceNew;
    }

    public void getLastNew() throws StopBotException{
        this.requestCore.go("lastNew.md");
        checkInstanceNew.onResponse(this.requestCore.doc.body().text());
    }

    public void getNews()throws StopBotException {
        this.requestCore.go("news.json");
        JSONObject Jobject = new JSONObject(this.requestCore.doc.body().text());
        JSONArray news = Jobject.getJSONArray("news");
        for (int i=0; i <= news.length()-1; i++){
            checkInstanceNew.onResponse(news.getString(i));
        }
    }

}
