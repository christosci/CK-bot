import java.io.*;
import java.util.ArrayList;
import com.google.gson.*;

/**
 * This class performs HTTP requests to the Twitter API and parses the JSON response.
 */
public class Twitter extends HttpRequest {
    private static final String getTokenURL = "https://api.twitter.com/oauth2/token";
    private static String bearerToken;
    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";

    public Twitter() {
        super("api.twitter.com");
        try {
            bearerToken = requestBearerToken(getTokenURL, CONSUMER_KEY, CONSUMER_SECRET);
        }
        catch (IOException e) {
            System.out.println("IOException e");
            e.printStackTrace();
        }
    }

    /**
     * Requests the current trends from Twitter and returns an ArrayList of names.
     * @param woeid  The Where On Earth ID of the intended location.
     * @param number The number of trends to return.
     * @return ArrayList of trend names.
     */
    public ArrayList<String> getTrends(String woeid, int number) {
        String json;
        try {
            json = getRequest("https://api.twitter.com/1.1/trends/place.json?id=" + woeid, bearerToken);
        } catch (IOException e) {
            System.out.println("IOException e");
            e.printStackTrace();
            return null;
        }

        JsonParser parser = new JsonParser();
        JsonArray rootArray = parser.parse(json).getAsJsonArray();
        JsonObject rootObject = rootArray.get(0).getAsJsonObject();
        JsonArray trendArray = rootObject.get("trends").getAsJsonArray();

        ArrayList<String> nameArray = new ArrayList<>();

        for (int i=0; i<number; i++) {
            nameArray.add(trendArray.get(i).getAsJsonObject().get("name").getAsString());
        }

        return nameArray;
    }
}

