import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

/**
 * This class performs HTTP requests to the OpenWeatherMap API and parses the JSON response.
 */
public class OpenWeatherMap extends HttpRequest {
    private static final String APPID ="";
    private static final String URL = "https://api.openweathermap.org/data/2.5/";

    public OpenWeatherMap() {
        super(URL);
    }

    /**
     * Gets the current weather for a given ZIP code.
     * @param zip The given ZIP code.
     * @return The weather object.
     */
    public Weather getWeatherByZip(String zip) {
        String json;
        try {
            json = getRequest("https://api.openweathermap.org/data/2.5/weather?zip=" + zip + "&units=imperial&appid=" + APPID);
        } catch (IOException e) {
            System.out.println("IOException e");
            e.printStackTrace();
            return null;
        }

        return getWeatherObject(json);
    }

    /**
     * Gets the current weather for a given city name.
     * @param city The given city name.
     * @return The weather object.
     */
    public Weather getWeatherByCity(String city) {
        String json;
        try {
            json = getRequest("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=imperial&appid=" + APPID);
        } catch (IOException e) {
            System.out.println("IOException e");
            e.printStackTrace();
            return null;
        }

        return getWeatherObject(json);
    }

    /**
     * Parses a JSON String and returns a weather object.
     * @param json The given JSON String.
     * @return The weather object associated with the JSON data.
     */
    private Weather getWeatherObject(String json) {
        if (json.isEmpty()) return null; // invalid parameter
        JsonParser parser       = new JsonParser();
        JsonObject rootObject   = parser.parse(json).getAsJsonObject();

        JsonArray  weatherArray = rootObject.get("weather").getAsJsonArray();
        JsonObject mainObject   = rootObject.get("main").getAsJsonObject();
        Weather weather = new Weather();

        weather.setCity(rootObject.get("name").getAsString());
        for (JsonElement weatherObject : weatherArray)
            weather.getDescriptions().add(weatherObject.getAsJsonObject().get("description").getAsString());

        weather.setTemp(mainObject.get("temp").getAsString());
        weather.setTempMax(mainObject.get("temp_max").getAsString());
        weather.setTempMin(mainObject.get("temp_min").getAsString());
        weather.setHumidity(mainObject.get("humidity").getAsString());
        weather.setPressure(mainObject.get("pressure").getAsString());

        return weather;
    }
}
