package ch.heigvd.pro.b04.android.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Emoji>> queue = new MutableLiveData<>();

    private MutableLiveData<Set<Emoji>> selectedEmoji = new MutableLiveData<>();

    private MutableLiveData<String> registrationCode = new MutableLiveData<>();

    public HomeViewModel() {
    }

    public void addNewEmoji(Emoji emoji) {

        // TODO : Factorize this logic in a model class, and test it.

        List<Emoji> buffer = queue.getValue();

        if (buffer == null) buffer = new LinkedList<>();

        // We do not support duplicate emojis.
        if (buffer.contains(emoji)) return;

        buffer.add(emoji);

        if (buffer.size() == 4) {
            Iterator<Emoji> emojis = buffer.iterator();
            StringBuilder code = new StringBuilder();
            code.append("0x");
            while (emojis.hasNext()) {
                code.append(emojis.next().getHex());
            }
            buffer.clear();
            registrationCode.postValue(code.toString());

            // Use retrofit -> https://square.github.io/retrofit/
        }

        queue.postValue(buffer);
        selectedEmoji.postValue(new HashSet<>(buffer));
    }

    public LiveData<Set<Emoji>> getSelectedEmoji() {
        return this.selectedEmoji;
    }

    public LiveData<String> getRegistrationCode() {
        return this.registrationCode;
    }
}
