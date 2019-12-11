public enum Command {
    TWITTER("twitter"),
    WEATHER("weather"),
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    PRESSURE("pressure");

    final String COMMAND_MESSAGE;

    Command(String COMMAND_MESSAGE) {
        this.COMMAND_MESSAGE = COMMAND_MESSAGE;
    }
}
