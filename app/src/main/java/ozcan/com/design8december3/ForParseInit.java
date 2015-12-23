package ozcan.com.design8december3;

import com.parse.Parse;

public class ForParseInit extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "K6ffPti3ffGlJ8Y5wV8sdad8K9Ib9VKHWlOn25Xx", "xuJrppmOc3OScLT8SYzjc70pkTMgOGTC8MGWdNF3");
    }

}
