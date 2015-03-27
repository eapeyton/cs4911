package cards.seniordesign.com.cards;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cards.seniordesign.com.cards.api.JeezAPI;
import cards.seniordesign.com.cards.api.ResponseConverter;
import cards.seniordesign.com.cards.models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class APITestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://ah-jeez.herokuapp.com")
                .setConverter(new ResponseConverter())
                .build();

        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        JeezAPI api = restAdapter.create(JeezAPI.class);
        User user = new User();
        user.setFbToken("myfbtoken" + System.currentTimeMillis());
        user.setFbId("myfbid" + System.currentTimeMillis());

        api.userLogin(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                System.out.println("Token:" + user.getFbToken());
            }

            @Override
            public void failure(RetrofitError error) {
                System.err.println(error);
            }
        });

        api.getUsers(new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                for (User user: users) {
                    System.out.println(user.getFbId());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apitest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
