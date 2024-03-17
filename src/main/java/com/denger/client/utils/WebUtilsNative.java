package com.denger.client.utils;

import by.radioegor146.nativeobfuscator.Native;
import com.sun.jna.win32.StdCallLibrary;
import net.minecraft.client.renderer.Tessellator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
@Native
public class WebUtilsNative {
    public static String agent1 = "User-Agent";
    public static String agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";


    public static InputStream visitSiteInput(String string) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        String string2 = "";
        try {

            Object object;
            URL uRL = new URL(string);
            URLConnection object2 = uRL.openConnection();
            object2.addRequestProperty(agent1, agent2);
            return object2.getInputStream();
        }
        catch (Exception exception) {
            Tessellator.getInstance().end();
        }

        return null;
    }
    public static String visitSiteText(String string) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        String string2 = "";
        try {
            Object object;
            URL uRL = new URL(string);
            Object object2 = uRL.openConnection();
            ((URLConnection)object2).addRequestProperty(agent1, agent2);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(((URLConnection)object2).getInputStream(), "UTF-8"));
            while ((object = bufferedReader.readLine()) != null) {
                arrayList.add(object);
            }
        }
        catch (Exception exception) {

        }
        for (Object object2 : arrayList) {
            string2 = String.valueOf(new StringBuilder().append(string2).append((String)object2));

        }
        return string2;
    }
}
