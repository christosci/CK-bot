import java.util.ArrayList;

/**
 * This class is used to store relevant weather data.
 */
public class Weather {
    private String city;
    // Descriptions of weather conditions.
    private ArrayList<String> descriptions = new ArrayList<>();
    private String temp, pressure, humidity, tempMin, tempMax;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String temp_max) {
        this.tempMax = temp_max;
    }
}
