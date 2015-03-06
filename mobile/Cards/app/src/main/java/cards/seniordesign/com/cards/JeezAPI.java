package cards.seniordesign.com.cards;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by eric on 3/5/15.
 */
public interface JeezAPI {

    @GET("/cards")
    public void getCards(Callback<List<Card>> card);
}
