package cards.seniordesign.com.cards.api;

/**
 * Created by Eric on 4/1/15.
 */
public class JeezAPIClient {
    private static JeezAPIInterface service;

    public static JeezAPIInterface getAPI() {
        if (service == null) {
            ServiceGenerator.createService(null);
        }
        return service;
    }

    public static void setToken(String token) {
        service = ServiceGenerator.createService(token);
    }
}
