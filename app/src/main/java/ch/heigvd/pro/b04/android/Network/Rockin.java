package ch.heigvd.pro.b04.android.Network;

public class Rockin {
    private static RockinAPI rockinAPI;

    public static RockinAPI api() {
        if (rockinAPI == null) {
            rockinAPI = RetrofitClient.getRetrofitInstance().create(RockinAPI.class);
        }

        return rockinAPI;
    }
}
