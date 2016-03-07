package brijesh.com.pixy.adapters;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

import brijesh.com.pixy.R;
import brijesh.com.pixy.ui.MainActivity;
import brijesh.com.pixy.utils.ParseConstants;

/**
 * Created by brijeshbharadwaj on 09/02/16.
 */
public class PixyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "YNc315zGvkvG6hTgPFCMA9rvG1r6irh3g5r2jdvb",
                "lD6oaWeAyVOSZNesa2GXCtCMSqdquiDPdOm63EYJ");

        PushService.setDefaultPushCallback(this, MainActivity.class, R.drawable.ic_stat_ic_launcher);
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
