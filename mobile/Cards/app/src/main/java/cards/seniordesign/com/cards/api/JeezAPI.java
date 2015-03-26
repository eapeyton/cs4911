package cards.seniordesign.com.cards.api;

import java.util.List;
import java.util.UUID;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by eric on 3/5/15.
 */
public interface JeezAPI {

    public static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://ah-jeez.herokuapp.com")
            .setConverter(new ResponseConverter())
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    public static JeezAPI API = restAdapter.create(JeezAPI.class);


    @POST("/users/login")
    public void userLogin(@Body User user, Callback<User> cb);

    @DELETE("/users/{id}")
    public void deleteUser(@Header("Authorization") String token, @Path("id") UUID id, Callback<Boolean> cb);

    @GET("/users")
    public void getUsers(Callback<List<User>> cb);

    @GET("/cards")
    public void getCards(Callback<List<Card>> card);
}
