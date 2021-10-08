package xyz.developerbab.lostandfoundapp.singleton;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.developerbab.lostandfoundapp.interfaces.RESTApiInterface;

public class RESTApiClient {

    private static final String BASE_URL = "http://lostandfound.developerbab.xyz/api/";
    private static RESTApiClient apiClient;
    private static Retrofit retrofit;

    public RESTApiClient() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized RESTApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new RESTApiClient();
        }
        return apiClient;
    }

    public RESTApiInterface getApi() {
        return retrofit.create(RESTApiInterface.class);
    }
}