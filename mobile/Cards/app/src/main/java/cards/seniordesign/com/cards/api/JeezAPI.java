package cards.seniordesign.com.cards.api;

import java.util.List;

import cards.seniordesign.com.cards.models.Card;
import cards.seniordesign.com.cards.models.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by eric on 3/5/15.
 */
public interface JeezAPI {

    @POST("/users/login")
    public void userLogin(@Body User user, Callback<User> cb);

    @GET("/users")
    public void getUsers(Callback<List<User>> cb);

    @GET("/cards")
    public void getCards(Callback<List<Card>> card);
}
