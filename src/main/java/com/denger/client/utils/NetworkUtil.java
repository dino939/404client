package com.denger.client.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class NetworkUtil {
    static String agent1, agent2;

    public static InputStream getStream(String url) {
        final HttpURLConnection httpURLConnection;
        InputStream a = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.addRequestProperty(agent1, agent2);
            a = httpURLConnection.getInputStream();
        } catch (Exception e) {

        }
        return a;
    }

    public static String visitSite(String string) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        String string2 = "";
        try {
            Object object;
            URL uRL = new URL(string);
            Object object2 = uRL.openConnection();
            ((URLConnection) object2).addRequestProperty(agent1, agent2);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(((URLConnection) object2).getInputStream(), "UTF-8"));
            while ((object = bufferedReader.readLine()) != null) {
                arrayList.add(object);
            }
        } catch (Exception exception) {
            // empty catch block
        }
        for (Object object2 : arrayList) {
            string2 = String.valueOf(new StringBuilder().append(string2).append((String) object2));

        }
        return string2;
    }

    static {
        agent1 = "User-Agent";
        //agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
        agent2 = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.88 Safari/537.36";
    }
}