import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * This class handles authorization and requests for the Twitter and OpenWeatherMap APIs.
 */
abstract class HttpRequest {
    final String API_ENDPOINT;

    HttpRequest(String API_ENDPOINT) {
        this.API_ENDPOINT = API_ENDPOINT;
    }

    /**
     * Requests a bearer token from the Twitter API and returns it.
     * @param endPointUrl The token request endpoint URL.
     * @param key         Consumer key
     * @param secret      Consumer secret
     * @return The bearer token.
     * @throws IOException Invalid endpoint.
     */
    String requestBearerToken(String endPointUrl, String key, String secret) throws IOException {
        HttpsURLConnection connection = null;
        String encodedCredentials = encodeKeys(key, secret);

        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", API_ENDPOINT);
            connection.setRequestProperty("User-Agent", "CK_bot");
            connection.setRequestProperty("Authorization", "Basic "
                    + encodedCredentials);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("Content-Length", "29");
            connection.setUseCaches(false);

            writeRequest(connection, "grant_type=client_credentials");

            String result = readResponse(connection);
            JsonParser parser = new JsonParser();
            JsonObject jsonResult = parser.parse(result).getAsJsonObject();

            return jsonResult.get("access_token").getAsString();
        } catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint specified.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Encodes the consumer key and secret to base 64 based on Twitter API documentation.
     * @param consumerKey    The consumer key
     * @param consumerSecret The consumer secret
     * @return The base 64 encoded consumer credentials.
     */
    String encodeKeys(String consumerKey, String consumerSecret) {
        try {
            String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
            String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");

            String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
            byte[] encodedBytes = Base64.getEncoder().encode(fullKey.getBytes());

            return new String(encodedBytes);
        } catch (UnsupportedEncodingException e) {
            return new String();
        }
    }

    /**
     * Includes the given parameters in the body of a request.
     * @param connection The HTTPS connection for the request.
     * @param textBody   The parameters to include in the request
     * @return True if parameters were successfully added, else false.
     */
    boolean writeRequest(HttpURLConnection connection, String textBody) {
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
                    connection.getOutputStream()));
            wr.write(textBody);
            wr.flush();
            wr.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads the response of a request and puts it in a String.
     * @param connection The HTTPS connection for the request.
     * @return The response String.
     */
    String readResponse(HttpsURLConnection connection) {
        try {
            StringBuilder str = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                str.append(line + System.getProperty("line.separator"));
            }
            return str.toString();
        } catch (IOException e) {
            System.out.println(e);
            return "";
        }
    }

    /**
     * Gets the response String of a request to a given endpoint URL.
     * This is for use with the Twitter API and as such, requires a bearer token.
     * @param endPointUrl The endpoint URL for the request.
     * @param bearerToken The Twitter bearer token for the request.
     * @return The response String
     * @throws IOException Invalid endpoint URL.
     */
    String getRequest(String endPointUrl, String bearerToken) throws IOException {
        HttpsURLConnection connection = null;

        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Host", API_ENDPOINT);
            connection.setRequestProperty("User-Agent", "CK_bot");
            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            connection.setUseCaches(false);

            return readResponse(connection);
        }
        catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint URL specified.", e);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Gets the response String of a request to a given endpoint URL.
     * @param endPointUrl The endpoint URL for the request.
     * @return The response String
     * @throws IOException Invalid endpoint URL.
     */
    String getRequest(String endPointUrl) throws IOException {
        HttpsURLConnection connection = null;

        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Host", API_ENDPOINT);
            connection.setRequestProperty("User-Agent", "CK_bot");
            connection.setUseCaches(false);

            return readResponse(connection);
        }
        catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint URL specified.", e);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
