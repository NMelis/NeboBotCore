package ru.nebolife.bot.core.helpers;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.listeners.CheckInstance;

public class CheckInfo {
    private RequestCore requestCore;
    private CheckInstance checkInstance;

    public CheckInfo(RequestCore requestCore, CheckInstance checkInstance) {
        this.requestCore = requestCore;
        this.checkInstance = checkInstance;
        this.requestCore.baseUrl = "http://nebo-bot.s3-website.eu-west-3.amazonaws.com/";
    }

    public void isUserBaned(String nickname) {
        this.requestCore.go("banedUsers.json");
        JSONObject Jobject = new JSONObject(this.requestCore.doc.body().text());
        JSONArray users = Jobject.getJSONArray("list");
        for (int i = 0; i < users.length(); i++) {
            if (users.get(i).toString().toLowerCase().equals(nickname.toLowerCase())) {
                checkInstance.onResponse(true);
                return;
            }
        }
        checkInstance.onResponse(false);

    }

    public void isUseVkrBaned(String userVkId) {
        this.requestCore.go("banedVkUsers.json");
        JSONObject Jobject = new JSONObject(this.requestCore.doc.body().text());
        JSONArray users = Jobject.getJSONArray("list");
        for (int i = 0; i < users.length(); i++) {
            if (users.get(i).toString().equals(userVkId)) {
                checkInstance.onResponse(true);
                return;
            }
        }
        checkInstance.onResponse(false);
    }

    public void isCityBaned(String cityName) {
        this.requestCore.go("banedCities.json");
        JSONObject Jobject = new JSONObject(this.requestCore.doc.body().text());
        JSONArray users = Jobject.getJSONArray("list");
        for (int i = 0; i < users.length(); i++) {
            if (users.get(i).toString().toLowerCase().equals(cityName.toLowerCase())) {
                checkInstance.onResponse(true);
                return;
            }
        }
        checkInstance.onResponse(false);
    }
}
