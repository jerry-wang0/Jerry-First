package jerry.test.com.mvp.controller;


import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by jerry on 2017/05/11.
 */

//public class MyHttpClient extends AsyncTask<ReponseHandler,String,ReponseHandler> {
//
//
//    public enum Scheme {
//        http, https;
//    }
//
//    public enum Method {
//        get, post;
//    }

//    private static final String TAG = "AsyncHttpClient";
//
//    protected ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//    private Scheme schema;
//    private String host;
//    private String path;
//    private HttpResponse resp;
//    private Method method;
//    protected String responseString = null;
//
//    public MyHttpClient(final Method method, final Scheme schema,
//                           final String host, final String path) {
//        this.method = method;
//        this.schema = schema;
//        this.host = host;
//        this.path = path;
//    }
//
//    protected String getBody(HttpResponse response) {
//        if (response == null) {
//            return "";
//        }
//        if (responseString == null) {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            try {
//                response.getEntity().writeTo(outputStream);
//            } catch (IOException e) {
//                Logger.d(TAG, "getBody IO Eeror", e);
//            }
//            responseString = outputStream.toString();
//
//        }
//        return responseString;
//    }
//
//    private HttpGet prepareGet(Scheme schema, String host, String path)
//            throws URISyntaxException {
//
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme(schema.name()).encodedAuthority(host).path(path);
//
//        for (NameValuePair param : params) {
//            builder.appendQueryParameter(param.getName(), param.getValue());
//        }
//        return new HttpGet(builder.build().toString());
//    }
//
//    private HttpPost preparePost(Scheme schema, String host, String path)
//            throws URISyntaxException {
//        HttpEntity entity = null;
//        try {
//            entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//        } catch (final UnsupportedEncodingException e) {
//            throw new AssertionError(e);
//        }
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme(schema.name()).encodedAuthority(host).path(path);
//        final HttpPost post = new HttpPost(builder.toString());
//        post.addHeader(entity.getContentType());
//        post.setEntity(entity);
//
//        return post;
//    }
//
//    @Override
//    protected ReponseHandler doInBackground(
//            ReponseHandler... handler) {
//        final DefaultHttpClient httpClient = NetworkUtil.createHttpClient();
//        HttpUriRequest request = null;
//        try {
//            request = prepare(this.schema, this.host, this.path);
//            Logger.i("request URI", request.getURI().toString());
//            for (PreferenceActivity.Header header : request.getAllHeaders()) {
//                Logger.i("request Header", header.toString());
//            }
//            this.resp = doExecute(httpClient, request);
//            getBody(this.resp);
//        } catch (final IOException e) {
//            Logger.v(TAG, "HttpStatus is NOT OK. IOException", e);
//            this.resp = null;
//        } catch (final URISyntaxException e) {
//            Logger.v(TAG, "HttpStatus is NOT OK. URISyntaxException");
//            this.resp = null;
//        } catch (final NullPointerException e) {
//            Logger.v(TAG, "HttpStatus is NOT OK. NullPointerException", e);
//            this.resp = null;
//        } finally {
//            if (httpClient != null) {
//                httpClient.getConnectionManager().shutdown();
//            }
//        }
//
//        return handler[0];
//    }
//
//    protected HttpResponse doExecute(final DefaultHttpClient httpClient,
//                                     HttpUriRequest request) throws IOException, ClientProtocolException {
//        return httpClient.execute(request);
//    }
//
//    public void onPostExecute(ReponseHandler handler) {
//        if (handler != null) {
//            if (this.resp != null
//                    && this.resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                handler.onSuccess(getBody(this.resp));
//            } else {
//                handler.onFailed(getBody(this.resp));
//            }
//        }
//    }
//}
