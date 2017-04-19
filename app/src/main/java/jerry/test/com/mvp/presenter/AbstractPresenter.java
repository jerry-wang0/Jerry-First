package jerry.test.com.mvp.presenter;

import jerry.test.com.mvp.controller.AbstractController;
import jerry.test.com.mvp.model.AbstractModel;
import jerry.test.com.mvp.view.View;

/**
 * Created by wangguanlin on 2017/04/17.
 */

public abstract class AbstractPresenter implements Presenter {

    public AbstractController controller;
    public AbstractModel model;
    public View view;

    public abstract void onCreate();

    public AbstractPresenter(View view){
        this.view = view;
    }

    public AbstractPresenter(View view, AbstractModel model){
        this.view = view;
        this.model = model;
    }

    @Override
    public void Create() {
        onCreate();
        view.Show();
    }

    @Override
    public void Resume() {
        view.Update();
    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public AbstractModel getModel() {
        return model;
    }

    public void setModel(AbstractModel model) {
        this.model = model;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public AbstractController getController() {
        return controller;
    }

    public void setController(AbstractController controller) {
        this.controller = controller;
    }
}
