package com.example.gesturerecognizer.Common;

import android.content.Context;

import com.example.gesturerecognizer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonUtils {
    public static ArrayList<Gesture> readGestureFromJson(Context context) throws IOException, JSONException {
        ArrayList<Point> stroke = new ArrayList<>();
        ArrayList<ArrayList<Point>> strokes = new ArrayList<>();
        String jsonString = readString(context, R.raw.template);
        JSONObject jsonRoot = new JSONObject(jsonString);
        Iterator<String> keys = jsonRoot.keys();
        ArrayList<Gesture> gestureArrayList = new ArrayList<>();
        while(keys.hasNext()){
            String key = keys.next();
            if (jsonRoot.get(key) instanceof JSONArray) {
                JSONArray multiStrokeArray = jsonRoot.getJSONArray(key);
                for(int i=0;i<multiStrokeArray.length();i++){
                    JSONArray uniStrokeArray = multiStrokeArray.getJSONArray(i);
                    for(int j=0;j<uniStrokeArray.length();j++){
                        JSONArray point = uniStrokeArray.getJSONArray(j);
                        stroke.add(new Point(point.getDouble(0),point.getDouble(1)));
                    }
                    strokes.add(new ArrayList<>(stroke));
                    stroke.clear();
                }
                gestureArrayList.add(new Gesture(key,new ArrayList<>(strokes)));
                strokes.clear();
            }
        }
        return gestureArrayList;
    }

    private static String readString(Context context, int resId) throws IOException {
        InputStream inputStream = context.getResources().openRawResource(resId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String s = null;
        while((s = bufferedReader.readLine())!=null) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
