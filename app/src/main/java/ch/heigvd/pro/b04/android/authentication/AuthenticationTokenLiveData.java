package ch.heigvd.pro.b04.android.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import java.util.Optional;

/**
 * An implementation of a {@link LiveData} that will inform the application about the currently set
 * authentication token. In particular, it offers some utilities to discard the current
 * authentication token if appropriate.
 */
public class AuthenticationTokenLiveData extends LiveData<Optional<String>> {

    private static final String PREFERENCE_FILE = "token.xml";
    private static final String TOKEN_PREF_KEY = "AUTH_TOKEN";
    private static final String TOKEN_DEFAULT_VALUE = null;

    private SharedPreferences.OnSharedPreferenceChangeListener listener =
            (sharedPreferences, key) -> {
                if (key.equals(TOKEN_PREF_KEY)) {
                    retrieveValue();
                }
            };

    private SharedPreferences preferences;

    public AuthenticationTokenLiveData(Context context) {
        super(Optional.empty());
        this.preferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

    @Override
    protected void onActive() {
        super.onActive();
        retrieveValue();
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void retrieveValue() {
        String token = preferences.getString(TOKEN_PREF_KEY, TOKEN_DEFAULT_VALUE);
        setValue(Optional.ofNullable(token));
    }

    /**
     * Enters a new authentication token for the application.
     *
     * @param token The newly attributed authentication token.
     */
    public void login(String token) {
        preferences.edit()
                .putString(TOKEN_PREF_KEY, token)
                .apply();
    }

    /**
     * Resets the authentication token and discards it.
     */
    public void logout() {
        preferences.edit()
                .putString(TOKEN_PREF_KEY, TOKEN_DEFAULT_VALUE)
                .apply();
    }
}
