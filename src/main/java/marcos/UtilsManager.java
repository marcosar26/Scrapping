package marcos;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilsManager {

    public static boolean doesURLExist(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();
        return responseCode == 200;
    }
}