package com.project.dictionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * References:
 * https://developer.android.com/training/basics/network-ops/xml
 */

public class AsyncRetrieve extends AsyncTask<String[], Void, ArrayList<Definition>> {
    private ProgressDialog mProgressDialog;
    private HttpURLConnection connection;
    private String stringUrl;
    private static final String REQUEST_METHOD = "GET";
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 15000;

    private AsyncResponse response;

    public interface AsyncResponse {
        void processFinish(ArrayList<Definition> output);
    }

    public AsyncRetrieve(Context context, String url, AsyncResponse response) {
        this.mProgressDialog = new ProgressDialog(context);
        this.stringUrl = url;
        this.response = response;
    }

    @Override
    protected ArrayList<Definition> doInBackground(String[]... params) {
        URL url;
        try {
            //creating the address
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {

            // Setup HttpURLConnection class to receive data from the api
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestMethod(REQUEST_METHOD);

            connection.setDoOutput(false);

            connection.connect();

            InputStream stream = connection.getInputStream();

            XmlPullParser myParser = Xml.newPullParser();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);
            myParser.nextTag();
            ArrayList<Definition> result = parseXML(myParser);
            stream.close();

            return result;

        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Definition> strings) {
        super.onPostExecute(strings);
        mProgressDialog.dismiss();
        if (response != null) {
            response.processFinish(strings);
        }
    }

    /**
     * This method retrieves data from the xml data returned
     * @param myParser
     * @return
     */
    public ArrayList<Definition> parseXML(XmlPullParser myParser) {
        ArrayList<Definition> definitions = new ArrayList<>();
        try {
            myParser.require(XmlPullParser.START_TAG, null, "entry_list");
            while (myParser.next() != XmlPullParser.END_TAG) {
                if (myParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = myParser.getName();
                // Starts by looking for the entry tag
                if (name.equals("entry")) {
                    definitions.add(readDefinition(myParser));
                } else {
                    skip(myParser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return definitions;
    }

    // Parses the contents of an entry. If it encounters a pr, hw,def, or fl tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    public Definition readDefinition(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "entry");
        String pr = null;
        String fl = null;
        String hw = null;
        String def = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("pr")) {
                pr = readPr(parser);
            } else if (name.equals("hw")) {
                hw = readHw(parser);
            }  else if (name.equals("fl")) {
                fl = readFl(parser);
            } else if (name.equals("def")) {
                def = readDef(parser);
            } else {
                skip(parser);
            }
        }
        return new Definition(hw, pr, fl, def);
    }

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

    // Processes pr tags in the feed.
    private String readPr(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pr");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pr");
        return title;
    }

    // Processes dt tags in the feed.
    private String readDt(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "dt");
        String title = readText(parser);
        while (parser.getEventType() != XmlPullParser.END_TAG){
            skip(parser);
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, "dt");
        return title;
    }

    // Processes link tags in the feed.
    private String readDef(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "def");
        StringBuilder def = new StringBuilder();
        int count = 1;
        String dt;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("dt")) {
                dt = readDt(parser);
                def.append(count).append(". ").append(dt).append("\n");
                count++;
            } else {
                skip(parser);
            }
        }
        return def.toString();
    }

    // Processes summary tags in the feed.
    private String readFl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "fl");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "fl");
        return summary;
    }
    // Processes head word tags in the feed.
    private String readHw(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "hw");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "hw");
        return summary;
    }

    // Processes text tags in the feed.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
