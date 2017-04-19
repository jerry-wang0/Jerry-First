package jerry.test.com.mvp.widget.login;

import jerry.test.com.mvp.presenter.AbstractPresenter;
import jerry.test.com.mvp.view.View;

/**
 * Created by wangguanlin on 2017/04/17.
 */

public class LoginPresenter extends AbstractPresenter {

    public LoginPresenter(View view){
        super(view);
    }

    @Override
    public void onCreate() {
        model = new LoginModel();
    }
}
