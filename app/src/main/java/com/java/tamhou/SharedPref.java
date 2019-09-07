package com.java.tamhou;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SharedPref {
    SharedPreferences mySharePref;
    public SharedPref(Context context) {
        mySharePref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }
    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharePref.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }
    public Boolean loadNightModeState() {
        return mySharePref.getBoolean("NightMode", false);
    }

    public void setUnusedCategories(List<Integer> unusedCategories) {
        SharedPreferences.Editor editor = mySharePref.edit();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < unusedCategories.size(); ++i)
            str.append(unusedCategories.get(i)).append(",");
        editor.putString("UnusedCategories", str.toString());
        editor.apply();
    }

    public List<Integer> loadUnusedCategories() {
        String savedString = mySharePref.getString("UnusedCategories", "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        List<Integer> unusedCategories = new ArrayList<>();
        while (st.hasMoreTokens())
            unusedCategories.add(Integer.parseInt(st.nextToken()));
        return  unusedCategories;
    }
}
