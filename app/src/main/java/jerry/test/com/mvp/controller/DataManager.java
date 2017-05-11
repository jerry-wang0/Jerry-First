//package jerry.test.com.mvp.controller;
//
//import android.content.Context;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.util.logging.Logger;
//
///**
// * Created by jerry on 2017/05/11.
// */
//
//public class DataManager {
//
//    private static final String TAG = "DataManager";
//    private static final String REFER_KUCHISU_URI = "/json/user";
//    private static final String WEB_SEARCH_PATH = "/json/searchredirect";
//    private static final String KUCHISU_SERVER_HOSTNAME = "app.websearch.rakuten.co.jp";
//    private Context context;
//
//    // HTTP Request keys
//    private static final String HTTP_KEY_FOR_ACCESS_TOKEN = "access_token";
//    private static final String HTTP_KEY_FOR_TOOL_ID = "tool_id";
//    private static final String HTTP_KEY_FOR_REF = "ref";
//    private static final String HTTP_KEY_FOR_CLIENT_ID = "client_id";
//    private static final String HTTP_KEY_FOR_COL = "col";
//    private static final String HTTP_KEY_FOR_CT = "ct";
//    private static final String HTTP_KEY_FOR_QT = "qt";
//
//    // JSON keys
//    private static final String JSON_KEY_FOR_STATUS = "status";
//    private static final String JSON_KEY_FOR_TICKET_NUM_YOUR = "ticket_num_your";
//    private static final String JSON_KEY_FOR_TICKET_NUM_MAX = "ticket_num_max";
//    private static final String JSON_KEY_FOR_YAMAWAKE_TODAY = "yamawake_today";
//    private static final String JSON_KEY_FOR_YOUR_NAME = "your_name";
//    private static final String JSON_KEY_FOR_TOOL_ID = "tool_id";
//    private static final String JSON_KEY_FOR_TIME = "time";
//    private static final String JSON_KEY_FOR_KEYWORD = "keyword";
//    private static final String JSON_KEY_FOR_REDIRECT_URL = "redirect_url";
//
//    private TestData latestData = new TestData();
//
//    public DataManager(Context context) {
//        this.context = context;
//    }
//
//    /**
//     * when you want to current kuchisu data, please use this method
//     *
//     * @param accessToken
//     * @param ref
//     * @param kuchisuHandler
//     */
//    public void refreshKuchisu(String accessToken, String ref,
//                               final ReponseHandler kuchisuHandler) {
//
//        final MyHttpClient client = new MyHttpClient(MyHttpClient.Method.post,
//                MyHttpClient.Scheme.https, KUCHISU_SERVER_HOSTNAME, REFER_KUCHISU_URI);
//
//        client.setParam(HTTP_KEY_FOR_ACCESS_TOKEN, accessToken);
//        client.setParam(HTTP_KEY_FOR_TOOL_ID, "");
//        client.setParam(HTTP_KEY_FOR_REF, ref);
//        client.setParam(HTTP_KEY_FOR_CLIENT_ID, "");
//        client.execute(new ReponseHandler() {
//
//            @Override
//            public void onSuccess(String responseString) {
//                Logger.d(TAG, "onSuccess");
//                Logger.d(TAG, "response :" + responseString);
//
//                updateKuchisuData(decode(responseString));
//                if (getKuchisu().getStatus() == TestData.StatusCode.OK) {
//                    kuchisuHandler.onSuccess(getKuchisu());
//                } else {
//                    kuchisuHandler.onFailed(getKuchisu().getStatus());
//                }
//            }
//
//            @Override
//            public void onFailed(String responseString) {
//                Logger.d(TAG, "onFailed");
//                kuchisuHandler.onFailed(TestData.StatusCode.UNCONNECT_TO_SERVER);
//            }
//        });
//    }
//
//    /**
//     * when you search and get kuchisu, please call this method
//     *
//     * @param accessToken
//     * @param ref
//     * @param kuchisuHandler
//     */
//    public void acquireKuchisu(final String accessToken, final String keyword, final String ref,
//                               final ReponseHandler kuchisuHandler) {
//        final MyHttpClient client = new MyHttpClient(MyHttpClient.Method.get,
//                MyHttpClient.Scheme.https, KUCHISU_SERVER_HOSTNAME, WEB_SEARCH_PATH);
//
//        client.setParam(HTTP_KEY_FOR_TOOL_ID, "");
//        client.setParam(HTTP_KEY_FOR_QT,keyword);
//        client.setParam(HTTP_KEY_FOR_REF, ref);
//        client.setParam(HTTP_KEY_FOR_COL, "OW");
//        client.setParam(HTTP_KEY_FOR_CLIENT_ID, "");
//        client.setParam(HTTP_KEY_FOR_ACCESS_TOKEN, accessToken);
//        client.setParam(HTTP_KEY_FOR_CT, "Android");
//        client.execute(new ReponseHandler() {
//
//            @Override
//            public void onSuccess(String responseString) {
//                Logger.d(TAG, "onSuccess");
//                Logger.d(TAG, "response :" + responseString);
//                updateKuchisuData(decode(responseString));
//                kuchisuHandler.onSuccess(getKuchisu());
//            }
//
//            @Override
//            public void onFailed(String responseString) {
//                Logger.d(TAG, "onFailed");
//                kuchisuHandler.onFailed(TestData.StatusCode.UNCONNECT_TO_SERVER);
//            }
//        });
//    }
//
//    private TestData decode(String json) {
//        TestData data = new TestData();
//        Logger.d(TAG, json);
//        if (json == null || json.isEmpty()) {
//            Logger.d(TAG, "json is null or empty");
//            return new TestData();
//        }
//
//        try {
//            JSONObject root = new JSONObject(json);
//            data.setStatus(TestData.StatusCode.convert(root
//                    .getInt(JSON_KEY_FOR_STATUS)));
//            data.setTicketNumYour(root.getInt(JSON_KEY_FOR_TICKET_NUM_YOUR));
//            data.setTicketNumMax(root.getInt(JSON_KEY_FOR_TICKET_NUM_MAX));
//            data.setYamawakeToday(root.getInt(JSON_KEY_FOR_YAMAWAKE_TODAY));
//            data.setUserName(root.getString(JSON_KEY_FOR_YOUR_NAME));
//            data.setToolId(root.getInt(JSON_KEY_FOR_TOOL_ID));
//            data.setKeyWord(root.getString(JSON_KEY_FOR_KEYWORD));
//            data.setRedirectUrl(root.getString(JSON_KEY_FOR_REDIRECT_URL));
//
//            // when you call refresh, the json not contains "time"
//            if (root.has(JSON_KEY_FOR_TIME)) {
//                data.setTime(root.getString(JSON_KEY_FOR_TIME));
//            }
//        } catch (JSONException e) {
//            Logger.d(TAG, "json decode error : " + json, e);
//            return new TestData();
//        }
//        return data;
//    }
//
//    public void updateKuchisuData(TestData data) {
//        this.latestData = data;
//    }
//
//    public TestData getKuchisu() {
//        return latestData;
//    }
//}
