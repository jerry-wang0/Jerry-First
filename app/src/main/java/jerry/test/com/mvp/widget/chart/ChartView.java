package jerry.test.com.mvp.widget.chart;

import javax.inject.Inject;

import cn.nextop.erebor.mid.app.domain.dagger.BootstrapModules;
import cn.nextop.erebor.mid.app.mvc.Model;
import cn.nextop.erebor.mid.app.mvc.View;
import cn.nextop.erebor.mid.app.mvc.support.AbstractView;
import cn.nextop.erebor.mid.app.mvc.support.ViewConfig;
import cn.nextop.erebor.mid.app.mvc.support.ViewIdFactory;
import cn.nextop.erebor.mid.common.glossary.Nullable;


public class ChartView extends AbstractView {
    //
    public static final View.ID ID = ViewIdFactory.create("view.pricing.chart");

    //
    @Inject
    protected ChartController controller;

    /**
     */
    public ChartView() {
        super(ID);BootstrapModules.inject(this);
        this.configs.put(ViewConfig.LAZY, true);
        this.configs.put(ViewConfig.CACHEABLE, true);
        this.configs.put(ViewConfig.RESTART_ON_NETWORK_RECOVERY, true);
    }

    /**
     *
     */
    @Override
    public boolean onMenu() {
        return false;
    }

    /**
     *
     */
    @Override
    public ChartController getController() {
        return controller;
    }

    /**
     *
     */
    @Override
    protected void onCreate(Manager vm, @Nullable Model m) {
        this.model = (m != null ? (ChartModel)m : new ChartModel());
        this.widget = create(vm, ChartWidget.class.getName(), this.model);
    }
}