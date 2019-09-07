package com.java.tamhou.ui.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetFromUrl
{

    public static String getJsonFromUrl(String url)
    {

        try
        {
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String temp = "";
            while((temp = reader.readLine()) != null)
            {
                builder.append(temp);
            }
            reader.close();
            return builder.toString();
        }catch(IOException e)
        {
            System.out.println(e.toString());
            return "";
        }
    }

}
