package main.dogappandroid;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocalHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context){
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context,lang);
    }

    public static Context onAttach(Context context, String defaultLanguage){
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context,lang);
    }

    public static Context setLocale(Context context, String lang) {
        persist(context,lang);
        return updateResource(context,lang);
    }

    private static Context updateResource(Context context, String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    private static void persist(Context context, String lang){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(SELECTED_LANGUAGE,lang);
        editor.apply();
    }

    private static String getPersistedData(Context context, String language){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE,language);

    }

}
