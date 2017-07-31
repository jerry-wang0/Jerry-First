package jerry.test.com.mvp.controller;

/**
 * Created by jerry on 2017/05/11.
 */

public interface ReponseHandler {
    public void onSuccess(String responseBody);
    public void onFailed(String responseBody);
}
