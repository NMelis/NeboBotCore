package ru.nebolife.bot.core.helpers;

import org.json.JSONObject;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.listeners.NewVersionAppInterface;

import java.util.HashMap;


public class NewVersionApp {
    private RequestCore requestCore;
    private NewVersionAppInterface newVersionAppInterface;

    public NewVersionApp(RequestCore requestCore, NewVersionAppInterface newVersionAppInterface) {
        this.requestCore = requestCore;
        this.newVersionAppInterface = newVersionAppInterface;
        this.requestCore.baseUrl = "http://nebo-bot.s3-website.eu-west-3.amazonaws.com/";
    }


    public void getInfoAboutANewVersionApp(float currentVersion)throws StopBotException{
        this.requestCore.go("newVersionApp.json");
        final JSONObject Jobject = new JSONObject(this.requestCore.doc.body().text());
        if (!Jobject.getBoolean("show")){ return;}
        this.requestCore.go("textNew.md");
        final String textNew = this.requestCore.doc.body().text();

        final float versionInLast = (float) Jobject.getDouble("version");
        if (versionInLast > currentVersion){
            newVersionAppInterface.onResponse(new HashMap<String, Object>(){{
                put("text", textNew);
                put("version", versionInLast);
                put("desc", Jobject.getString("desc"));
                put("required", Jobject.getBoolean("required"));
                put("links", Jobject.getJSONObject("links"));
            }});
        }
    }
}
