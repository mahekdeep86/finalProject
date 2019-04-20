package com.project.articles;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ArticleSearchTask extends AsyncTask<String, Void, ArrayList<Article>> {

    private ArticleResponse response;

    public interface ArticleResponse {
        void onResponse(ArrayList<Article> output);
    }

    public ArticleSearchTask(ArticleResponse articleResponse) {
        response = articleResponse;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        URL url;
        ArrayList<Article> articles = new ArrayList<>();
        try {
            url = new URL(strings[0]);
            // Setup HttpURLConnection class to receive data from the api
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");

            //connection.setDoOutput(false);

            connection.connect();

            InputStream stream = connection.getInputStream();
            articles.addAll(parseJson(stream));
            stream.close();
            return articles;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return articles;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        super.onPostExecute(articles);
        if (response != null) {
            response.onResponse(articles);
        }
    }

    /**
     * This method parses the data returned from online
     * @param inputStream
     * @return
     */
    private ArrayList<Article> parseJson(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuffer buffer = new StringBuffer();
        ArrayList<Article> data = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = buffer.toString();
            JSONArray jsonArray = new JSONObject(result).getJSONObject("response").getJSONArray("docs");
            data = new ArrayList<>();
            for (int i = 0; i < buffer.length(); i++) {
                Article article;
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("_id");
                String webUrl = jsonObject.getString("web_url");
                String snippet = jsonObject.getString("snippet");
                String imageUrl = ((JSONObject)jsonObject.getJSONArray("multimedia").get(0)).getString("url");
                String headline = jsonObject.getJSONObject("headline").getString("main");
                article = new Article(id, webUrl, headline, imageUrl, snippet);
                data.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
