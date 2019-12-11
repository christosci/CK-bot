import org.jibble.pircbot.*;

import java.util.ArrayList;

public class CKbot extends PircBot {
    private Twitter twitter;
    private OpenWeatherMap openWeatherMap;

    public CKbot() {
        setName("CK_bot");
        twitter = new Twitter();
        openWeatherMap = new OpenWeatherMap();
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        Command command = getCommand(message);

        if (command == Command.TWITTER) {
            ArrayList<String> trendArray = twitter.getTrends("23424977", 5);
            sendMessage(channel, sender + ": Here are the top 5 Twitter trends in the United States:");
            for (String trend : trendArray) {
                sendMessage(channel, trend);
            }
        }
        // WEATHER
        else if (command != null) {
            String zip = message.replaceAll("[^0-9]", "");
            Weather weather;
            String weatherDescription;

            if (!zip.equals(""))
                weather = openWeatherMap.getWeatherByZip(zip);
            else {
                String city = message.substring(message.indexOf("in ") + 3);
                weather = openWeatherMap.getWeatherByCity(city);
            }
            // invalid command
            if (weather == null) {
                sendMessage(channel, sender + ": That's not a valid city or zip code, try again.");
                return;
            }

            switch (command) {
                case HUMIDITY:
                    weatherDescription = String.format("The current humidity for %s is %s%%",
                            weather.getCity(), weather.getHumidity());
                    break;
                case TEMPERATURE:
                    weatherDescription = String.format("The current temperature for %s is %s\u00b0F, with a high of %s\u00b0F and a low of %s\u00b0F",
                            weather.getCity(), weather.getTemp(), weather.getTempMax(), weather.getTempMin());
                    break;
                case PRESSURE:
                    weatherDescription = String.format("The current barometric pressure for %s is %s hPa",
                            weather.getCity(), weather.getPressure());
                    break;
                default: // WEATHER
                    StringBuilder description = new StringBuilder();
                    for (String d : weather.getDescriptions()) {
                        description.append(d).append(", ");
                    }
                    weatherDescription = String.format("The current weather for %s is %swith a temperature of %s\u00b0F",
                            weather.getCity(), description, weather.getTemp());
                    break;
            }
            sendMessage(channel, sender + ": " + weatherDescription);
        }
    }

    /**
     * Returns the Command of a given message.
     * @param message The given message.
     * @return The command associated with the message.
     */
    private Command getCommand(String message) {
        message = message.toLowerCase();
        for (Command c : Command.values()) {
            if (message.contains(c.COMMAND_MESSAGE)) {
                return c;
            }
        }
        return null;
    }
}
