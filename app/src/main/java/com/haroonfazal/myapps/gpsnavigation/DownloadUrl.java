package com.haroonfazal.myapps.gpsnavigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadUrl
{
    public String readUrl(String myurl) throws IOException
    {
        HttpURLConnection httpURLConnection=null;
        String data = "";
        InputStream inputStream = null;

        try
        {
            URL url = new URL(myurl);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line="";
            while((line= bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            data = sb.toString();
            bufferedReader.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }


return data;

    }
}
