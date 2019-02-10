package ru.nebolife.bot.core.helpers;


import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.util.HashMap;
import java.util.List;

public class FormParser {

    public Document source;
    public HashMap<String, String> postData;

    public static FormParser parse(Document source) {
        FormParser instance = new FormParser();
        instance.source = source;
        return instance;
    }

    public FormParser findByAction(String actionContains) {
        Element form = source.select("form[action*=" + actionContains + "]").first();
        List<Connection.KeyVal> formData = ((FormElement) form).formData();
        postData = new HashMap<>(formData.size());
        for (Connection.KeyVal formInput : formData) {
            postData.put(formInput.key(), formInput.value());
        }
        return this;
    }

    public FormParser input(String key, Object value) {
        postData.put(key, String.valueOf(value));
        return this;
    }

    public HashMap<String, String> build() {
        return postData;
    }
}
