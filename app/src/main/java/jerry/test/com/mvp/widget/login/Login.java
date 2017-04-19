package jerry.test.com.mvp.widget.login;

import android.os.Bundle;

import jerry.test.com.mvp.MainActivity;
import jerry.test.com.mvp.presenter.AbstractPresenter;
import jerry.test.com.mvp.view.View;

/**
 * Created by wangguanlin on 2017/04/17.
 */

public class Login extends MainActivity implements View{

    private AbstractPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPresenter(this);
        presenter.Create();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.Pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.Destroy();
    }

    @Override
    public void Show() {

    }

    @Override
    public void Hide() {

    }

    @Override
    public void Update() {

    }

    @Override
    public void Destory() {

    }
}
