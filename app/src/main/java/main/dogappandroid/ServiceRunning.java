package main.dogappandroid;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class ServiceRunning extends Service{
    private SharedPreferences mPrefs;
    private DBHelper mHelper = new DBHelper(this);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("Stoppppppppppppppppppp", "Stopppppppppp");
        mHelper.deleteNull();
        stopSelf();
    }


}
