package ch.heigvd.pro.b04.android.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;

public final class Persistent {
    private Persistent() {};

    /**
     * This function returns the token if it was set previously.
     * @param context The app context. Needed to access the Shared Preferences
     * @return The token value
     * @throws TokenNotSetException is thrown if no token was stored
     */
    public static String getStoredTokenOrError(Context context) throws TokenNotSetException {
        String defValue = "Error";
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.data), Context.MODE_PRIVATE);
        String token = preferences.getString(context.getString(R.string.dataToken), defValue);

        if (token.equals(defValue)) {
            throw new TokenNotSetException();
        } else {
            return token;
        }
    }

    /**
     * This function will write the token to the Shared Preferences
     * @param context The app context. Needed to access the Shared Preferences
     * @param token The token to write
     */
    public static void writeToken(Context context, String token) {
        context.getSharedPreferences(context.getString(R.string.data), Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.dataToken), token)
                .apply();
    }
}
