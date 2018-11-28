package kivi.ugran.com.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.net.ParseException;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import kivi.ugran.com.core.callbacks.LoadAsJsonCallback;

public class LoadAsJsonTask extends AsyncTask<Object, Void, JsonObject> {
    String url;
    LoadAsJsonCallback callback;

    public LoadAsJsonTask(String url, LoadAsJsonCallback callback) {
        this.url = url;
        this.callback = callback;
    }

    @Override protected JsonObject doInBackground(Object... params) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(this.url);
            URLConnection urlConnection = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonObject = parser.parse(inputLine).getAsJsonObject();
            }
            return jsonObject;
        } catch (IOException | ParseException e) {

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override protected void onPostExecute(JsonObject jsonObject) {
        if (callback != null) {
            callback.callback(jsonObject);
        }
    }

    @Override protected void onCancelled() {
        super.onCancelled();
    }
}