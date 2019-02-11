package ru.nebolife.bot.core.core.sites;

import okhttp3.*;
import org.jsoup.Jsoup;
import ru.nebolife.bot.core.core.BotInterface;
import ru.nebolife.bot.core.core.RequestCore;
import ru.nebolife.bot.core.helpers.FormParser;
import ru.nebolife.bot.core.helpers.StopBotException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class NeboMobi extends RequestCore implements BotInterface {

    private final String nickName;
    private final String password;

    public NeboMobi(String nickName, String password) {
        super("http://nebo.mobi");
        this.nickName = nickName;
        this.password = password;
    }

    public NeboMobi login() throws IOException, StopBotException {
        this.go("/welcome");
        this.go("/login");
        HashMap<String, String> postData = FormParser.parse(this.doc)
                .findByAction("loginForm")
                .input("login", nickName)
                .input("password", password).build();


        FormBody.Builder f = new FormBody.Builder();
        for(Map.Entry<String, String> entry : postData.entrySet()) {
            f.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = f.build();


        String action = this.doc.select("div>div>form[method*=post]").attr("action");
        Request request = new Request.Builder().url(this.baseUrl + action).post(formBody).build();
        Response response = this.client.newCall(request).execute();
        ResponseBody body = response.body();
        assert body != null;
        this.doc = Jsoup.parse(body.string());
        if (!this.isExistsName()) return null;

        this.firstLoadInfo();

        return this;
    }

}
