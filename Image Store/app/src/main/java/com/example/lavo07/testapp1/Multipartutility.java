package com.example.lavo07.testapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lavo07 on 7/18/2017.
 */

public class Multipartutility {
    HttpURLConnection conn;
    URL url;
    String LINE_FEED = "\r\n";
    String boundary;
    OutputStream output;
    PrintWriter writer;

    Multipartutility(String urlrequest) throws IOException {
        url=new URL(urlrequest);
        conn=(HttpURLConnection)url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        output = conn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"),
                true);
    }
    void addfile(String file_name,File file) throws IOException{
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + "foo"
                        + "\"; filename=\"" + file_name + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(file_name))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
        FileInputStream inputStream = new FileInputStream(file);
        Bitmap temp= BitmapFactory.decodeStream(inputStream);
        temp.compress(Bitmap.CompressFormat.JPEG,20,output);
        /*byte[] f_byte = new byte[40960];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(f_byte)) != -1) {
            output.write(f_byte, 0, bytesRead);
        }*/
        output.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }
    int finish() throws IOException{
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        int response = conn.getResponseCode();
        return response;
    }
}
