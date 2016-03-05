package brijesh.com.pixy.adapters;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by brijeshbharadwaj on 09/02/16.
 */
public class PixyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "YNc315zGvkvG6hTgPFCMA9rvG1r6irh3g5r2jdvb",
                "lD6oaWeAyVOSZNesa2GXCtCMSqdquiDPdOm63EYJ");

    }
}
