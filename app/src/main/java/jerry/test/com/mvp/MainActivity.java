package jerry.test.com.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import jerry.test.com.mvp.util.BrowserSetting;

public class MainActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Socket socket = new Socket();
        try {
            OutputStream outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BrowserSetting().setDefaultBrowser(getApplicationContext());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
