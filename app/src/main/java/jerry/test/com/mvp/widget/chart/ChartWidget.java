package jerry.test.com.mvp.widget.chart;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

import java.math.BigDecimal;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.ChartSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.OrderSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.app.domain.trading.TradingUtils;
import cn.nextop.erebor.mid.app.domain.trading.domain.TradingValidator;
import cn.nextop.erebor.mid.app.domain.trading.domain.Validation;
import cn.nextop.erebor.mid.app.mvc.Controller;
import cn.nextop.erebor.mid.app.mvc.View;
import cn.nextop.erebor.mid.app.mvc.main.pricing.panel.EvaluatorPanel;
import cn.nextop.erebor.mid.app.mvc.main.pricing.panel.TradePanelMini;
import cn.nextop.erebor.mid.app.mvc.main.setting.GraphView;
import cn.nextop.erebor.mid.app.mvc.main.setting.dialog.OrderPlaceDialog;
import cn.nextop.erebor.mid.app.mvc.main.setting.dialog.OrderPlaceDialog_;
import cn.nextop.erebor.mid.app.mvc.main.setting.panel.order.model.OrderPlacePanelModel;
import cn.nextop.erebor.mid.app.mvc.main.trading.dialog.AddOrderPostDialog;
import cn.nextop.erebor.mid.app.mvc.main.trading.dialog.AddOrderPostDialog_;
import cn.nextop.erebor.mid.app.mvc.main.trading.dialog.AddOrderPrevDialog;
import cn.nextop.erebor.mid.app.mvc.main.trading.dialog.AddOrderPrevDialog_;
import cn.nextop.erebor.mid.app.mvc.main.trading.dialog.model.AddOrderPostModel;
import cn.nextop.erebor.mid.app.mvc.main.trading.dialog.model.AddOrderPrevModel;
import cn.nextop.erebor.mid.app.mvc.support.AbstractWidget;
import cn.nextop.erebor.mid.app.mvc.support.widget.dialog.grid.GridDialog;
import cn.nextop.erebor.mid.app.mvc.support.widget.dialog.grid.GridDialog_;
import cn.nextop.erebor.mid.app.mvc.support.widget.dialog.list.ListDialog;
import cn.nextop.erebor.mid.app.mvc.support.widget.dialog.list.ListDialog_;
import cn.nextop.erebor.mid.app.mvc.support.widget.grid.model.GridAdaptable;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.model.ListAdaptable;
import cn.nextop.erebor.mid.app.mvc.support.widget.text.DecimalPicker;
import cn.nextop.erebor.mid.app.mvc.support.widget.trading.TradingConsole;
import cn.nextop.erebor.mid.app.mvc.support.widget.utility.Views;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartListener;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.ChartStyle;
import cn.nextop.erebor.mid.common.glossary.chart.ChartType;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;
import cn.nextop.erebor.mid.common.glossary.enums.Side;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.collection.map.IntHashMap;
import cn.nextop.erebor.mid.common.util.collection.map.LongHashMap;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.event.ChartListener;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.event.QuoteListener;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.ChartVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.QuoteVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.event.PositionListener;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingOrderVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingPositionVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.result.AddOrderResult;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static cn.nextop.erebor.mid.app.domain.support.Formatters.formatPipsEx;
import static cn.nextop.erebor.mid.app.domain.trading.domain.Validation.OVERFLOW;
import static cn.nextop.erebor.mid.app.domain.trading.domain.Validation.REQUIRED;
import static cn.nextop.erebor.mid.app.domain.trading.domain.Validation.UNDERFLOW;
import static cn.nextop.erebor.mid.app.mvc.Widget.Hint.REFRESH;
import static cn.nextop.erebor.mid.app.mvc.Widget.Hint.SHOW;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_CANDLE_COLOR_ASC;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_CANDLE_COLOR_DESC;
import static cn.nextop.erebor.mid.common.util.Objects.cast;
import static cn.nextop.erebor.mid.rpc.MobileApiGenerator.getSymbolId;

/**
 * Created by Liqun Wan on 2016/6/9.
 */
@EFragment(R.layout.widget_pricing_chart)
public class ChartWidget extends AbstractWidget {

    @StringRes(R.string.setting_order_describe_on) protected String ON;
    @StringRes(R.string.setting_order_describe_off) protected String OFF;
    @StringRes(R.string.pricing_chart_low_price) protected String LOW;
    @StringRes(R.string.pricing_chart_open_price) protected String OPEN;
    @StringRes(R.string.pricing_chart_high_price) protected String HIGH;
    @StringRes(R.string.pricing_chart_close_price) protected String CLOSE;
    @ColorRes(R.color.pricing_chart_candle_asc) protected int COLOR_CANDLE_ASC;
    @ColorRes(R.color.pricing_chart_candle_desc) protected int COLOR_CANDLE_DESC;
    @StringRes(R.string.setting_order_describe) protected String DESCRIBE;

    @ViewById(R.id.item_pricing_chart_view) protected XChartView chart;
    //@ViewById(R.id.item_pricing_chart_view_redraw) protected XChartViewRedraw chartRedraw;
    @ViewById(R.id.item_pricing_chart_volume_panel)
    LinearLayout llVolume;
    @ViewById(R.id.item_pricing_chart_low_price) protected TextView tvLow;
    @ViewById(R.id.item_pricing_chart_high_price) protected TextView tvHigh;
    @ViewById(R.id.item_pricing_chart_open_price) protected TextView tvOpen;
    @ViewById(R.id.item_pricing_chart_close_price) protected TextView tvClose;
    @ViewById(R.id.item_pricing_chart_available_volume) protected TextView tvVolume;
    @ViewById(R.id.item_pricing_chart_show_filter) protected CompoundButton cbFilter;
    @ViewById(R.id.item_pricing_chart_trading_console) protected TradingConsole console;
    @ViewById(R.id.item_pricing_chart_land_setting_info) protected FrameLayout flSetting;
    @ViewById(R.id.item_pricing_trade_panel_setting_info_text) protected TextView tvSetting;
    @ViewById(R.id.item_pricing_trade_panel_setting_info_icon) protected ImageView ivSetting;
    @ViewById(R.id.item_pricing_chart_evaluator_panel) protected EvaluatorPanel evaluatorPanel;
    @ViewById(R.id.item_pricing_chart_filter_bar_portrait) protected LinearLayout llFilterBar1;
    @ViewById(R.id.item_pricing_chart_filter_bar_landscape) protected LinearLayout llFilterBar2;
    @ViewById(R.id.item_pricing_chart_trade_panel_mini) protected TradePanelMini tradePanelMini;
    @ViewById(R.id.item_pricing_chart_line_edit_pen) protected Button btnLinePen;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_e) protected Button btnLineEdit;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_magnet) protected Button btnLineM;
    @ViewById(R.id.item_pricing_chart_line_edit_group) protected LinearLayout llLineGroup;
    @ViewById(R.id.item_pricing_chart_line_edit_bar) protected RadioGroup rgLineBar;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_i) protected CompoundButton cbLineBarI;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_ii) protected CompoundButton cbLineBarII;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_h) protected CompoundButton cbLineBarH;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_v) protected CompoundButton cbLineBarV;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_p) protected CompoundButton cbLineBarP;
    @ViewById(R.id.item_pricing_chart_line_edit_bar_c) protected CompoundButton cbLineBarC;


    private DecimalPicker dpVolume;
    private TextView tvType1, tvType2;
    private ImageView ivStyle1, ivStyle2;
    private TextView tvSymbol1, tvSymbol2;
    private TextView tvOrderMode1, tvOrderMode2;
    private Button btnIndicator1, btnIndicator2;
    private LinearLayout llOrderMode1, llOrderMode2;

    //
    protected boolean showTrade;
    protected boolean showFilter;
    protected TradingValidator validator;
    protected ListDialog typeDialog = ListDialog_.builder().build();
    protected ListDialog styleDialog = ListDialog_.builder().build();
    protected GridDialog symbolDialog = GridDialog_.builder().build();
    protected OrderPlaceDialog setting = OrderPlaceDialog_.builder().build();
    protected AddOrderPrevDialog prev = AddOrderPrevDialog_.builder().build();
    protected AddOrderPostDialog post = AddOrderPostDialog_.builder().build();

    /**
     *
     */
    @Override
    public void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c); doRefresh(REFRESH);
    }

    /**
     *
     */
    @Override
    public void prevHide(View.ID target, boolean destroy) {
        super.prevHide(target, destroy);
        ((ChartModel)this.view.getModel()).setQuoteListener(null);
        ((ChartModel)this.view.getModel()).setChartListener(null);

        //
        int p = SCREEN_ORIENTATION_USER_PORTRAIT; doRefresh(REFRESH);
        if(target != GraphView.ID) activity.setRequestedOrientation(p);
    }

    @Override
    public void prevShow(View.ID source, boolean create) {
        final ChartModel model = this.view.getModel();
        final ChartController controller = view.getController();
        final Controller.Session session = controller.getSession();
        if(!session.hasLogin()) { showFilter = false; showTrade = false; };
    }

    @Override
    public void postShow(View.ID source, final boolean create) {
        super.postShow(source, create);
        final ChartModel model = this.view.getModel();
        final ChartController controller = view.getController();

        model.setPositionListener(new PositionListener() {
            @Override
            public void onPosition(final TradingPositionVo position) {
                if (!model.getStatus().isActive()) return;
                controller.evaluate(model); refresh();
            }
        });

        model.setQuoteListener(new QuoteListener() {
            @Override
            public void onQuotes(IntHashMap<QuoteVo> quotes) {
                if (!model.getStatus().isActive()) return;
                controller.evaluate(model); refresh();
                if (prev != null && prev.visible()) prev.refresh(quotes);
            }
        });
        model.setChartListener(new ChartListener() {
            @Override
            public void onCharts(LongHashMap<ChartVo> charts) {
                if (Maps.isEmpty(charts)) return;
                chart.getAdapter().refresh(charts.values());
            }
        });
    }

    /**
     *
     */
    @UiThread
    protected void refresh() {
        final ChartModel model = this.view.getModel();
        this.tradePanelMini.refresh(REFRESH, model);
        this.console.refresh(REFRESH, model.toTradingConsoleModel());
        this.evaluatorPanel.refresh(REFRESH, model.toEvaluatorModel());
    }

    /**
     *
     */
    @Override
    protected void onCreate() {
        //
        super.onCreate();

        this.evaluatorPanel.setPipsVisible(false);
        FragmentManager manager = getFragmentManager();
        this.validator = new TradingValidator(getActivity());
        this.styleDialog.setManager(manager); this.typeDialog.setManager(manager);
        this.symbolDialog.setManager(manager); this.prev.setManager(manager); this.post.setManager(manager);

        final FrameLayout flType1 = cast(llFilterBar1.findViewById(R.id.item_pricing_chart_type_filter));
        final FrameLayout flType2 = cast(llFilterBar2.findViewById(R.id.item_pricing_chart_type_filter));
        final FrameLayout flStyle1 = cast(llFilterBar1.findViewById(R.id.item_pricing_chart_style_filter));
        final FrameLayout flStyle2 = cast(llFilterBar2.findViewById(R.id.item_pricing_chart_style_filter));
        final FrameLayout flSymbol1 = cast(llFilterBar1.findViewById(R.id.item_pricing_chart_symbol_filter));
        final FrameLayout flSymbol2 = cast(llFilterBar2.findViewById(R.id.item_pricing_chart_symbol_filter));

        this.tvType1 = cast(flType1.findViewById(R.id.item_pricing_chart_filter_text));
        this.tvType2 = cast(flType2.findViewById(R.id.item_pricing_chart_filter_text));
        this.ivStyle1 = cast(flStyle1.findViewById(R.id.item_pricing_chart_filter_icon));
        this.ivStyle2 = cast(flStyle2.findViewById(R.id.item_pricing_chart_filter_icon));
        this.tvSymbol1 = cast(flSymbol1.findViewById(R.id.item_pricing_chart_filter_text));
        this.tvSymbol2 = cast(flSymbol2.findViewById(R.id.item_pricing_chart_filter_text));
        this.dpVolume = cast(this.llVolume.findViewById(R.id.item_pricing_chart_volume));
        this.btnIndicator1 = cast(llFilterBar1.findViewById(R.id.item_pricing_chart_indicator_toggle));
        this.btnIndicator2 = cast(llFilterBar2.findViewById(R.id.item_pricing_chart_indicator_toggle));
        this.llOrderMode1 = cast(llFilterBar1.findViewById(R.id.item_pricing_chart_order_mode_filter));
        this.llOrderMode2 = cast(llFilterBar2.findViewById(R.id.item_pricing_chart_order_mode_filter));
        this.tvOrderMode1 = cast(llOrderMode1.findViewById(R.id.item_pricing_chart_order_mode_filter_text));
        this.tvOrderMode2 = cast(llOrderMode2.findViewById(R.id.item_pricing_chart_order_mode_filter_text));

        //
        this.console.setListener(new TradingConsole.OnSelectListener() {
            @Override
            public void onSelect(Side s, QuoteVo q) { select(s, q);}
        });
        this.tradePanelMini.setListener(new TradePanelMini.OnSelectListener() {
            @Override
            public void onSelect(Side s, QuoteVo q) { select(s, q); }
        });

        this.cbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showFilter = isChecked; doRefresh(REFRESH);
            }
        });

        //
        this.flSetting.setOnClickListener(new Views.OnClickAdapter() {
            @Override
            protected void doOnClick(android.view.View v) { orderSetting(); }
        });

        //
        final OnClickListener symbolListener = new Views.OnClickAdapter() {
            @Override
            protected void doOnClick(android.view.View v)  { symbol(); }
        };
        flSymbol1.setOnClickListener(symbolListener); flSymbol2.setOnClickListener(symbolListener);

        //
        final OnClickListener typeListener = new Views.OnClickAdapter() {
            @Override
            protected void doOnClick(android.view.View v)  { chartType(); }
        };
        flType1.setOnClickListener(typeListener); flType2.setOnClickListener(typeListener);

        //
        final OnClickListener stylelListener = new Views.OnClickAdapter() {
            @Override
            protected void doOnClick(android.view.View v)  { chartStyle(); }
        };
        flStyle1.setOnClickListener(stylelListener); flStyle2.setOnClickListener(stylelListener);

        //
        final OnClickListener orderModeListener = new Views.OnClickAdapter(100L) {
            @Override
            protected void doOnClick(android.view.View v)  {
                showTrade= !showTrade; doRefresh(REFRESH);
            }
        };
        llOrderMode1.setOnClickListener(orderModeListener); llOrderMode2.setOnClickListener(orderModeListener);

        //
        final OnClickListener indicatorListener = new Views.OnClickAdapter() {
            @Override
            protected void doOnClick(android.view.View v)  {
                view.getManager().open(view.getId(), GraphView.ID, null, true);
            }
        };
        btnIndicator1.setOnClickListener(indicatorListener); btnIndicator2.setOnClickListener(indicatorListener);

        //
        this.dpVolume.setEnabled(true);
        this.dpVolume.setValue(new BigDecimal("0.1"));
        this.dpVolume.setListener(new DecimalPicker.OnClickListener() {
            @Override
            public BigDecimal onAdd(DecimalPicker picker, BigDecimal value, BigDecimal step) {
                return value == null ? null : TradingUtils.roundDown(value, step);
            }
            @Override
            public BigDecimal onSub(DecimalPicker picker, BigDecimal value, BigDecimal step) {
                return value == null ? null : TradingUtils.roundUp(value, step);
            }
        });

        //
        final XChartTheme theme = new XChartTheme();
        final ChartModel model = this.view.getModel();
        theme.setProperty(CHART_CANDLE_COLOR_ASC, COLOR_CANDLE_ASC);
        theme.setProperty(CHART_CANDLE_COLOR_DESC, COLOR_CANDLE_DESC);
        final ChartController controller = this.view.getController();
        this.chart.getAdapter().setup(model.getChartConfig(true), theme);
        this.chart.getAdapter().setWidget(this);
        //this.chart.getAdapter().setRedraw(chartRedraw);
//        this.chart.getAdapter().setImg(chartRedraw);
        this.chart.getAdapter().setListener(new XChartListener() {
            @Override
            public void onCursor(XChartAdapter adapter, XChart chart) {
                onChange(chart);
            }

            @Override
            public void onScale(XChartAdapter adapter, ChartScale scale) {
                model.getChartSetting().setScale(scale); // NO need to save
            }

            @Override
            public void onPickMove(XChartAdapter adapter, float x, float y) {

            }

            @Override
            public void onRemove(XChartAdapter adapter, XDrawingLine line) {

            }

            @Override
            public void onDrawComplete(XChartAdapter adapter, XDrawingLine line) {

            }

            @Override
            public void onMove(final XChartAdapter adapter, boolean iterate) {
                //
                final ChartModel model = view.getModel();
                if (!iterate || !model.isIterable()) return;
                final boolean empty = model.getCharts().isEmpty();
                final long next = empty ? Long.MAX_VALUE : model.getCharts().lastKey();
                final long prev = model.getProgress().getAndSet(next); if (prev == next) return;

                //
                controller.iterate(model, view).setListener(new RemotingCallback<Boolean>(false) {
                    @Override
                    public final Boolean call() throws Exception {
                        chart.getAdapter().refresh(model.getCharts().values()); return Boolean.TRUE;
                    }
                });
            }
        });

        final RadioGroup.OnCheckedChangeListener lineBarListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.item_pricing_chart_line_edit_bar_i:
                         chart.getAdapter().activate(XDrawingStyle.SINGLE);break;
                    case R.id.item_pricing_chart_line_edit_bar_ii:
                        chart.getAdapter().activate(XDrawingStyle.DOUBLE);break;
                    case R.id.item_pricing_chart_line_edit_bar_h:
                        chart.getAdapter().activate(XDrawingStyle.HORIZONTAL);break;
                    case R.id.item_pricing_chart_line_edit_bar_v:
                        chart.getAdapter().activate(XDrawingStyle.VERTICAL);break;
                    case R.id.item_pricing_chart_line_edit_bar_p:
                        chart.getAdapter().activate(XDrawingStyle.HORIZONMULTIPLE);break;
                    case R.id.item_pricing_chart_line_edit_bar_c:
                        chart.getAdapter().removeLine();break;
                }
            }
        };
        final OnClickListener lineEditListener = new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                switch (v.getId()){
                    case R.id.item_pricing_chart_line_edit_pen:
                        btnLinePen.setVisibility(GONE);llLineGroup.setVisibility(VISIBLE);
                        break;
                    case R.id.item_pricing_chart_line_edit_bar_e:
                        btnLinePen.setVisibility(VISIBLE);llLineGroup.setVisibility(GONE);
                        break;
                    case R.id.item_pricing_chart_line_edit_bar_magnet:
                        btnLineM.setSelected(!btnLineM.isSelected());
                        chart.getAdapter().magnet(true);
                        break;
                }
            }
        };
        this.btnLinePen.setOnClickListener(lineEditListener);
        this.btnLineEdit.setOnClickListener(lineEditListener);
        this.btnLineM.setOnClickListener(lineEditListener);
        this.rgLineBar.setOnCheckedChangeListener(lineBarListener);
    }

    /**
     *
     */
    @Override
    protected void onRefresh(final Object hint) {
        //
        this.doRefresh(hint);
        final ChartModel model = view.getModel();
        int orientation = SCREEN_ORIENTATION_UNSPECIFIED;
        this.activity.setRequestedOrientation(orientation);
        final ChartSetting setting = model.getChartSetting();
        final ChartController controller = view.getController();
        final Controller.Session session = controller.getSession();

        // Chart
        if(hint == REFRESH) {
            this.chart.getAdapter().refresh(model.getCharts().values());
        } else {
            final XChartConfig config = model.getChartConfig(true);
            this.chart.getAdapter().reset(config, model.getCharts().values());
        }

        // Console
        this.tradePanelMini.refresh(hint, model);
        this.console.refresh(hint, model.toTradingConsoleModel());
        this.evaluatorPanel.refresh(hint, model.toEvaluatorModel());
        final Short symbolId = setting == null ? null : setting.getSymbolId();
        final QuoteVo quote = symbolId == null ? null : model.getQuotes().get(symbolId);
        final boolean tradable = symbolId != null && session.hasLogin() && quote != null;
        console.setTradable(Side.BUY, tradable); console.setTradable(Side.SELL, tradable);
        this.ivSetting.setVisibility(session.hasLogin() ? VISIBLE : android.view.View.INVISIBLE);
        llOrderMode1.setEnabled(session.hasLogin()); llOrderMode2.setEnabled(session.hasLogin());
    }

    @UiThread
    protected void doRefresh(final Object hint) {
        //
        final ChartModel model = this.view.getModel();
        if(!model.getStatus().isActive()) return;
        final OrderSetting o = model.getOrderSetting();
        final ChartSetting setting = model.getChartSetting();
        if(hint == REFRESH || hint == SHOW) {
            final Configuration configuration = activity.getResources().getConfiguration();
            switch (configuration.orientation) {
                case ORIENTATION_PORTRAIT:
                    this.console.setVisibility(showTrade ? VISIBLE : GONE);
                    this.llVolume.setVisibility(showTrade ? VISIBLE : GONE);
                    this.flSetting.setVisibility(showTrade ? VISIBLE : GONE);
                    this.evaluatorPanel.setVisibility(showTrade ? VISIBLE : GONE);
                    this.llFilterBar1.setVisibility(VISIBLE); this.llFilterBar2.setVisibility(GONE);
                    this.tradePanelMini.setVisibility(GONE); this.cbFilter.setVisibility(GONE); break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    this.tradePanelMini.setVisibility(showTrade ? VISIBLE : GONE);
                    cbFilter.setChecked(showFilter); cbFilter.setVisibility(VISIBLE);
                    this.flSetting.setVisibility(cbFilter.isChecked() ? VISIBLE : GONE);
                    this.llFilterBar2.setVisibility(cbFilter.isChecked() ? VISIBLE : GONE);
                    this.llFilterBar1.setVisibility(GONE); this.llVolume.setVisibility(GONE);
                    this.console.setVisibility(GONE); this.evaluatorPanel.setVisibility(GONE); break;
            }
        } else {
            //
            if(model.getCharts().isEmpty()) { onChange(null); }
            String type = setting == null ? "-" : Formatters.format(setting.getType(), "-");
            this.tvType1.setText(type); this.tvType2.setText(type);

            //
            final Short symbolId = setting == null ? null : setting.getSymbolId();
            final SymbolSetting symbol = symbolId == null ? null : model.getSymbol(symbolId);
            if(symbol == null) {
                this.tvSymbol1.setText("-"); this.tvSymbol2.setText("-");
            } else {
                this.tvSymbol1.setText(symbol.getName()); this.tvSymbol2.setText(symbol.getName());
            }

            final int style = Formatters.format(setting.getStyle(), -1);
            if(style > 0) { ivStyle1.setImageResource(style); ivStyle2.setImageResource(style);}
        }
        tvOrderMode1.setText(showTrade ? R.string.pricing_chart_mode_market : R.string.pricing_chart_mode_default);
        tvOrderMode2.setText(showTrade ? R.string.pricing_chart_mode_market : R.string.pricing_chart_mode_default);

        final ProductVo product = model.getProducts().get(setting.getSymbolId());
        final BigDecimal scale = BigDecimal.TEN.pow(product.getScale().intValue());
        final BigDecimal pips = BigDecimal.TEN.pow(product.getScale().intValue() - 1);
        BigDecimal tp = null, sl = null;
        if(o.getTpDeviation() != null) { tp = getVaildDeviation(o.getTpDeviation(), product).divide(scale).multiply(pips);}
        if(o.getSlDeviation() != null) { sl = getVaildDeviation(o.getSlDeviation(), product).divide(scale).multiply(pips);}
        this.tvSetting.setText(String.format(DESCRIBE, (o.isFIFO()? ON : OFF)+" ", formatPipsEx(tp , OFF)+" ", formatPipsEx(sl, OFF)));

        //
        if(showTrade) {
            this.tvVolume.setText(String.format(getString(R.string.pricing_tradable_volume2), "100")); //TODO
            TradingUtils.setupVolume(dpVolume, product, null); if(o != null) this.dpVolume.setValue(o.getVolume());
        }
    }

    @UiThread
    protected void onChange(final XChart chart) {
        //
        final ChartModel model = this.view.getModel();
        final ChartSetting setting = model.getChartSetting();

        //
        if(chart == null || setting == null) {
            this.tvLow.setText(String.format(LOW, " -")); this.tvHigh.setText(String.format(HIGH, " -"));
            this.tvOpen.setText(String.format(OPEN, " -")); this.tvClose.setText(String.format(CLOSE, " -"));
            this.tvLow.setTag(null); this.tvHigh.setTag(null); this.tvOpen.setTag(null); this.tvClose.setTag(null);
        } else {
            final SymbolSetting symbol = model.getSymbol(setting.getSymbolId());

            final Float low = cast(this.tvLow.getTag());
            if(low == null || low != chart.getLow()) {
                this.tvLow.setTag(chart.getLow());
                this.tvLow.setText(String.format(LOW, " "+format(chart.getLow(), symbol.getScale())));
            }
            final Float high = cast(this.tvHigh.getTag());
            if(high == null || high != chart.getHigh()) {
                this.tvHigh.setTag(chart.getHigh());
                this.tvHigh.setText(String.format(HIGH, " "+format(chart.getHigh(), symbol.getScale())));
            }
            final Float open = cast(this.tvOpen.getTag());
            if(open == null || open != chart.getOpen()) {
                this.tvOpen.setTag(chart.getOpen());
                this.tvOpen.setText(String.format(OPEN, " "+format(chart.getOpen(), symbol.getScale())));
            }
            final Float close = cast(this.tvClose.getTag());
            if(close == null || close != chart.getClose()) {
                this.tvClose.setTag(chart.getClose());
                this.tvClose.setText(String.format(CLOSE, " "+format(chart.getClose(), symbol.getScale())));
            }
        }
    }

    /**
     *
     */
    private BigDecimal getVaildDeviation(BigDecimal dp, final ProductVo product) {
        final Validation v = validator.isDeviationValid(dp, product);
        if(v == REQUIRED) return dp;
        else if(v == OVERFLOW) return product.getMaxLeavingOffset();
        else if(v == UNDERFLOW) return product.getMinLeavingOffset();
        return dp;
    }

    /**
     *
     */
    public void select(final Side side, QuoteVo quote) {
        //
        if(quote == null) return;
        final ChartModel model = view.getModel();
        final OrderSetting setting = model.getOrderSetting();
        final Configuration c = activity.getResources().getConfiguration();
        final int productId = quote.getProductId(); short symbolId = getSymbolId(productId);
        final ProductVo product = model.getProducts().get(symbolId); if(product == null) return;
        final DecimalPicker dp = (c.orientation == ORIENTATION_PORTRAIT) ? this.dpVolume : tradePanelMini.getDpVolume();

        //
        if(dp == null) return;
        final Boolean valid = this.validator.isVolumeValid(dp, product, null, null); if(!valid) return;
        final BigDecimal size = product.getContractSize(); final BigDecimal volume = dp.getValue().multiply(size);

        final TradingOrderVo order = model.toTradingOrder(product, side, quote, volume);
        if(!setting.isPlaceConfirm()) place(order, false);
        else {
            AddOrderPrevModel m = new AddOrderPrevModel(order, product, quote);
            m.setTwoSide(model.hasContract(symbolId, side.flip(), setting.isFIFO()));
            this.prev.show(m, new AddOrderPrevDialog.OnSelectListener() {
                @Override
                public void onConfirm(final AddOrderPrevModel m1) { place(order, m1.isConfirm()); }
            });
        }
    }

    /**
     *
     */
    protected void place(final TradingOrderVo order, final boolean confirm) {
        //
        showProgress(); final ChartModel model = view.getModel();
        final ChartController controller = this.view.getController();
        controller.place(model, view, order, confirm).setListener(new RemotingCallback<Boolean>(true) {
            @Override
            public final Boolean call() throws Exception {
                final ChartModel model = view.getModel();
                final AddOrderResult r = model.delCookie("_RESULT");
                final AddOrderPostModel m1 = new AddOrderPostModel();
                m1.setProduct(model.getProducts().get(model.getChartSetting().getSymbolId()));
                m1.setValidator(validator); m1.setResult(r); post.show(m1, view, null); return true;
            }
        });
    }


    protected void orderSetting() {
        //
        if(!view.getController().getSession().hasLogin()) return;

        final ChartModel model = this.view.getModel();
        setting.show(ChartWidget.this, model.toPlaceSettingModel(), new OrderPlaceDialog.OnSelectListener() {
            @Override
            public void onConfirm(OrderPlacePanelModel m) {
            final ChartController c = view.getController();
            showProgress(); model.setPlaceSetting(m.getSetting());
            c.orderSetting(model, view).setListener(new RemotingCallback<Boolean>(false) {
                @Override
                public final Boolean call() throws Exception { onRefresh(Hint.RESTART); return true; }
            });
            }
        });
    }

    /**
     *
     */
    protected void chartStyle() {
        //
        final ChartModel model = view.getModel();
        final ChartController controller = view.getController();
        final ChartSetting setting = model.getChartSetting(); if(setting == null) return;
        styleDialog.show(model.toChartStyleFilter(), new ListDialog.OnSelectListener() {

            @Override
            public void onSelect(int position, ListAdaptable data) {
                model.getChartSetting().setStyle((ChartStyle) data.getCookie());
                controller.chartSetting(model, view).setListener(new RemotingCallback<Boolean>() {
                    @Override
                    public Boolean call() {
                        chart.getAdapter().setup(model.getChartConfig(false), null); onRefresh(Hint.RESTART);  return Boolean.TRUE; }
                });
            }
        });
    }

    /**
     *
     */
    protected void symbol() {
        //
        final ChartModel model = view.getModel();
        final ChartController controller = view.getController();
        final ChartSetting setting = model.getChartSetting(); if(setting == null) return;
        symbolDialog.show(model.toSymbolFilter(), new GridDialog.OnSelectListener() {
            @Override
            public void onSelect(int position, GridAdaptable data) {
                if (data == null) return; SymbolSetting s = data.getCookie();
                model.getChartSetting().setSymbolId(s.getSymbolId()); showProgress();
                controller.restart(model, view, false).setListener(new RemotingCallback<Boolean>() {
                    @Override
                    public final Boolean call() throws Exception { onRefresh(Hint.RESTART); return Boolean.TRUE; }
                });
            }
        });
    }

    /**
     *
     */
    protected void chartType() {
        //
        final ChartModel model = view.getModel();
        final ChartController controller = view.getController();
        final ChartSetting setting = model.getChartSetting(); if(setting == null) return;
        typeDialog.show(model.toChartTypeFilter(), new ListDialog.OnSelectListener() {
            @Override
            public void onSelect(int position, ListAdaptable data) {
                model.getChartSetting().setType((ChartType) data.getCookie()); showProgress();
                controller.restart(model, view, false).setListener(new RemotingCallback<Boolean>() {
                    @Override
                    public final Boolean call() throws Exception { onRefresh(Hint.RESTART); return Boolean.TRUE; }
                });
            }
        });
    }
}
