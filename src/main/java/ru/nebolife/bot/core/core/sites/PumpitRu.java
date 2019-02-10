package ru.nebolife.bot.core.core.sites;

import okhttp3.*;
import org.jsoup.Jsoup;
import ru.nebolife.bot.core.core.BotInterface;
import ru.nebolife.bot.core.core.RequestCore;

import java.io.IOException;


public class PumpitRu extends RequestCore implements BotInterface {
    private final String nickName;
    private final String password;

    public PumpitRu(String nickName, String password) {
        super("http://pumpit.mmska.ru");
        this.nickName = nickName;
        this.password = password;
    }

    @Override
    public PumpitRu login() throws IOException {
        this.go("/yb/5516820337/play_app/?app_id=36");
        RequestBody formBody = new FormBody.Builder()
                .add("login", this.nickName)
                .add("password", this.password)
                .add("submit", "Войти")
                .build();
        String action = this.doc.select("div.login>form[method=POST]").attr("action");
        Request request = new Request.Builder().url(this.baseUrl + action).post(formBody).build();
        Response response = this.client.newCall(request).execute();
        ResponseBody body = response.body();
        assert body != null;
        this.doc = Jsoup.parse(body.string());
        this.baseUrl = "http://nebo.pumpit.mmska.ru";
        if (!this.isExistsName()) return null;

        this.firstLoadInfo();

        return this;


    }

}
