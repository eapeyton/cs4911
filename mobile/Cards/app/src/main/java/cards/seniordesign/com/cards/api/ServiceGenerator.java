package cards.seniordesign.com.cards.api;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class ServiceGenerator {

    private static final String BASE_URL = "http://ah-jeez.herokuapp.com";

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static JeezAPIInterface createService(final String token) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new JeezConverter());
                //.setLogLevel(RestAdapter.LogLevel.FULL);

        if (token != null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Content-Type", "application/json");
                    request.addHeader("Authorization", "Token " + token);
                }
            });
        }

        RestAdapter adapter = builder.build();
        return adapter.create(JeezAPIInterface.class);
    }
}