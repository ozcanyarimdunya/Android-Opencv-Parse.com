package ozcan.com.design8december3;

import com.parse.Parse;

public class ForParseInit extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "rd5cxc2U7vDFLQTFGAFwBALQjjAyEYQZjiSFlXjn", "AtSeSCxuLzwf8GzL4N2Lu96Vq6Iy9Q4ExWJEe90I");
    }

}
