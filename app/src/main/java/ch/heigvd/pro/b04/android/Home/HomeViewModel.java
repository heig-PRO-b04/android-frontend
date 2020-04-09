package ch.heigvd.pro.b04.android.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Emoji>> codeEmoji = new MutableLiveData<>(new ArrayList<>());

    public void addNewEmoji(Emoji emoji) {
        List<Emoji> list = codeEmoji.getValue();
        list.add(emoji);
        codeEmoji.postValue(list);
        // Use retrofit -> https://square.github.io/retrofit/
    }

    public void clearAll() {
        codeEmoji.postValue(new ArrayList<>());
    }

    public LiveData<List<Emoji>> getCodeEmoji() {
        return codeEmoji;
    }
}
