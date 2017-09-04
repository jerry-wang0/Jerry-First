package jerry.test.com.mvp.xchart.adapter.impl.action;

import java.util.ArrayList;
import java.util.Collection;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.common.util.collection.map.LongHashMap;

/**
 * Created by Jingqi Xu on 8/28/15.
 */
public abstract class AbstractAction implements XChartAction {
    //
    protected final Type type;

    /**
     *
     */
    public AbstractAction(Type type) {
        this.type = type;
    }

    /**
     *
     */
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean isSetup() {
        return false;
    }

    @Override
    public boolean isReadonly() {
        return false;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("type", type).toString();
    }

    /**
     *
     */
    @Override
    public XChartAction merge(XChartAction action) {
        return null; // NOP
    }

    /**
     *
     */
    protected Collection<? extends XChart> merge(Collection<? extends XChart> c1, Collection<? extends XChart> c2) {
        if(c1 == null && c2 == null) return new ArrayList<>(0);
        if(c1 == null) return c2; else if(c2 == null) return c1;
        final LongHashMap<XChart> r = Maps.newLongHashMap(c1.size() + c2.size());
        for(XChart c : c1) r.put(c.getId(), c); for(XChart c : c2) r.put(c.getId(), c); return r.values();
    }
}
