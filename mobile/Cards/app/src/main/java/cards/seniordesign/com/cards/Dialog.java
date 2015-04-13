package cards.seniordesign.com.cards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

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
}
