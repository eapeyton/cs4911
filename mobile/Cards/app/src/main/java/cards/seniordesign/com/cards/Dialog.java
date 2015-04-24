package cards.seniordesign.com.cards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import cards.seniordesign.com.cards.game.Game;

/**
 * Created by Eric on 4/10/15.
 */
public class Dialog {

    public static void showError(Activity activity, String error) {
        AlertDialog dialog =  new AlertDialog.Builder(activity)
                .setMessage(error)
                .setNeutralButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .create();
        dialog.show();
    }

    public static void showNotification(Activity activity, String notification) {
        showError(activity, notification);
    }

    public static void showGameNotification(Activity activity, String notification) {
        final TextView textView = (TextView) activity.findViewById(R.id.notification);

        if (textView == null) {
            throw new RuntimeException(activity + " does not include the notification layout.");
        }

        textView.setGravity(Gravity.CENTER);
        textView.setText(notification);
        textView.setVisibility(View.VISIBLE);

        AlphaAnimation fadeInOut = new AlphaAnimation(0.0f, 1.0f);
        fadeInOut.setDuration(1500);
        fadeInOut.setRepeatMode(Animation.REVERSE);
        fadeInOut.setRepeatCount(1);
        fadeInOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        textView.startAnimation(fadeInOut);
    }

    public static void showDelayedGameNotification(final Activity activity, final String notification, final int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showGameNotification(activity, notification);
            }
        }, delay);
    }

    public static void showDelayedGameNotification(final Activity activity, final String notification) {
        showDelayedGameNotification(activity, notification, 500);
    }
}
