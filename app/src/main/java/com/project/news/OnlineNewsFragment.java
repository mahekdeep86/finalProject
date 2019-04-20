package com.project.news;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.project.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineNewsFragment extends Fragment {
    private NewsAdapter mNewsAdapter;
    ListView articlesListView;
    EditText searchTermEditText;
    Button searchButton;
    SharedPreferences mSharedPreferences;

    public OnlineNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_news, container, false);
        articlesListView = view.findViewById(R.id.listViewArticles);
        searchTermEditText = view.findViewById(R.id.query_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsAdapter = new NewsAdapter(getActivity(), new ArrayList<NewsArticle>(), true);
        mSharedPreferences = getActivity().getSharedPreferences("NewsArticle", Context.MODE_PRIVATE);

        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticle newsArticle = (NewsArticle) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), NewsFeedViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", newsArticle.getUrl());
                bundle.putString("title", newsArticle.getTitle());
                bundle.putString("fullTitle", newsArticle.getFullTitle());
                bundle.putString("sectionTitle", newsArticle.getSectionTitle());
                bundle.putString("publishedOn", newsArticle.getPublishedOn());
                bundle.putString("mainImage", newsArticle.getMainImage());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        articlesListView.setAdapter(mNewsAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchTermEditText.getText().toString().equalsIgnoreCase("")) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("LastTerm", searchTermEditText.getText().toString());
                    editor.apply();
                    searchArticles(searchTermEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Enter the search term", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Trigers article search activity
     * @param term
     */
    private void searchArticles(String term) {
        try {
            term = URLEncoder.encode(term, "UTF-8");
            String url = "http://webhose.io/filterWebContent?token=82f7c0cd-3b21-49e6-8489-d5430425ef49&format=xml&sort=crawled&q=" + term;
            new ArticleSearchTask().execute(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public class ArticleSearchTask extends AsyncTask<String, Void, ArrayList<NewsArticle>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((NewsFeedActivity)getActivity()).showHideProgressBar(true);
        }

        @Override
        protected ArrayList<NewsArticle> doInBackground(String... strings) {
            URL url;
            try {
                url = new URL(strings[0]);
                // Setup HttpURLConnection class to receive data from the api
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");

                connection.setDoOutput(false);

                connection.connect();

                InputStream stream = connection.getInputStream();

                XmlPullParser myParser = Xml.newPullParser();

                myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                myParser.setInput(stream, null);
                myParser.nextTag();
                myParser.nextTag();
                ArrayList<NewsArticle> result = parseXML(myParser);
                stream.close();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsArticle> newsArticles) {
            super.onPostExecute(newsArticles);
            if (getActivity() != null){
                ((NewsFeedActivity)getActivity()).showHideProgressBar(false);
            }

            mNewsAdapter.setNewsArticleList(newsArticles);
        }
    }

    /**
     * This method parses the xml retrieved
     * @param myParser
     * @return
     */
    public ArrayList<NewsArticle> parseXML(XmlPullParser myParser) {
        ArrayList<NewsArticle> articles = new ArrayList<>();
        try {
            myParser.require(XmlPullParser.START_TAG, null, "posts");
            while (myParser.next() != XmlPullParser.END_TAG) {
                if (myParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = myParser.getName();
                // Starts by looking for the entry tag
                if (name.equals("post")) {
                    articles.add(getArticle(myParser));
                } else {
                    skip(myParser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return articles;
    }


    /**
     * This method tries to get the articles fetched online if nay
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private NewsArticle getArticle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "post");
        String uuid = "";
        String url = "";
        String title = "";
        String fullTitle = "";
        String sectionTitle = "";
        String publishedOn = "";
        String mainImage = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("thread")) {
                parser.require(XmlPullParser.START_TAG, null, "thread");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    // Starts by looking for the entry tag
                    if (name.equals("url")) {
                        url = readUrl(parser);
                    } else if (name.equals("title")) {
                        title = readTitle(parser);
                    } else if (name.equals("title_full")) {
                        fullTitle = readFullTitle(parser);
                    } else if (name.equals("uuid")) {
                        uuid = readUuid(parser);
                    } else if (name.equals("section_title")) {
                        sectionTitle = readSectionTitle(parser);
                    } else if (name.equals("published")) {
                        publishedOn = readPublishedOn(parser);
                    } else if (name.equals("main_image")) {
                        mainImage = readMainImage(parser);
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }
        return new NewsArticle(uuid, url, title, fullTitle, sectionTitle, publishedOn, mainImage);
    }

    //The following methods retrieves data from specific tags

    /**
     * Processes uuid tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readUuid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "uuid");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "uuid");
        return title;
    }

    /**
     * Processes section_title tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readSectionTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "section_title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "section_title");
        return title;
    }

    /**
     * Processes published tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readPublishedOn(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "published");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "published");
        return title;
    }

    /**
     * Processes main_image tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readMainImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "main_image");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "main_image");
        return title;
    }

    /**
     * Processes title tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    /**
     * Processes title_full tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readFullTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title_full");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title_full");
        return title;
    }

    /**
     * Processes url tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "url");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "url");
        return title;
    }

    /**
     * Processes text tags in the returned xml
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * This method skips an xml tag
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //after initializing the button proceed to get the last item search and search it
        String lastTerm = mSharedPreferences.getString("LastTerm", "");
        searchTermEditText.setText(lastTerm);
        searchArticles(lastTerm);
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "The last term searched was " + lastTerm, Snackbar.LENGTH_LONG);
        snackBar.show();
    }
}
