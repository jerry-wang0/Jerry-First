package jerry.test.com.mvp.widget.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import cn.nextop.erebor.mid.app.domain.remoting.RemotingCallable;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.ChartSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.OrderSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.app.domain.trading.domain.TradingEvaluator;
import cn.nextop.erebor.mid.app.glossary.RemotingResult;
import cn.nextop.erebor.mid.app.mvc.Model;
import cn.nextop.erebor.mid.app.mvc.View;
import cn.nextop.erebor.mid.app.mvc.support.AbstractController;
import cn.nextop.erebor.mid.common.glossary.Triple;
import cn.nextop.erebor.mid.common.glossary.Tuple;
import cn.nextop.erebor.mid.common.glossary.chart.ChartType;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.collection.map.IntHashMap;
import cn.nextop.erebor.mid.common.util.concurrent.future.FutureEx;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.ChartVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.QuoteVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingOrderVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingPositionVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.result.AddOrderResult;
import cn.nextop.erebor.mid.rpc.service.protobuf.PricingServiceProtoV1;
import cn.nextop.erebor.mid.rpc.service.protobuf.TradingServiceProtoV1;


public class ChartController extends AbstractController {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteController.class);

    //
    protected TradingEvaluator evaluator;

    /**
     *
     */
    public ChartController() {
        super("controller.pricing.chart");
        this.evaluator = new TradingEvaluator();
    }

    /**
     *
     */
    @Override
    protected void onSuspend(final Model m, View v) throws Exception {
        this.pricingService.unsubscribeChartEvent();
        this.tradingService.unsubscribePositionEvent();
    }

    @Override
    protected void onDestroy(final Model m, View v) throws Exception {
        this.pricingService.unsubscribeChartEvent();
        this.tradingService.unsubscribePositionEvent();
    }

    /**
     *
     */
    public void evaluate(final ChartModel model) {
        final TradingPositionVo p = model.getPosition();
        final Map<Short, ProductVo> products = model.getProducts();
        final ConcurrentMap<Short, QuoteVo> quotes = model.getQuotes();
        model.setProfits(this.evaluator.evaluate(p, products, quotes, model.getConversions()));
    }

    /**
     *
     */
    public FutureEx<RemotingResult> chartSetting(final Model m, final View v) {
        final ChartModel model = Objects.cast(m);
        return super.invoke("save", m, v, new RemotingCallable<Boolean>(false) {
            @Override
            public Boolean call() throws Exception {
                final ChartSetting setting = model.getChartSetting();
                storage.setChartSetting(getSession().getAccountId(), setting);
                if(verbose) LOGGER.info("[" + name + "]save preference: {}", setting); return true;
            }
        });
    }

    /**
     *
     */
    public final FutureEx<RemotingResult> orderSetting(final Model m, final View v) {
        return super.invoke("save", m, v, new RemotingCallable<Boolean>(false) {
            @Override
            public Boolean call() throws Exception {
                final ChartModel model = Objects.cast(m);
                final OrderSetting setting = model.getOrderSetting();
                storage.setOrderSetting(getSession().getAccountId(), setting);
                if(verbose) LOGGER.info("[" + name + "]save setting: {}", setting); return true;
            }
        });
    }

    /**
     *
     */
    public FutureEx<RemotingResult> place(Model m, final View v, final TradingOrderVo order, final boolean confirm) {
        final ChartModel model = Objects.cast(m);
        return super.invoke("place", m, v, new RemotingCallable<Boolean>(true) {
            @Override
            public Boolean call() throws Exception {
                final OrderSetting setting = model.getOrderSetting();
                final boolean save = setting.isPlaceConfirm() != confirm;
                if(save) {setting.setPlaceConfirm(confirm); orderSetting(model, v);}
                final long aid = getSession().getAccountId(); order.setAccountId(aid);
                AddOrderResult r; model.setCookie("_RESULT", r = tradingService.addOrder(order).get());
                if(verbose) LOGGER.info("[" + name + "]place order: {}, result: {}", order, r); return true;
            }
        });
    }



    /**
     * If not login, server side will use default group
     * Client side MUST make sure that symbolId belongs to that group
     */
    public FutureEx<RemotingResult> iterate(final Model m, final View v) {
        final ChartModel model = Objects.cast(m);
        return super.invoke("iterate", model, v, new RemotingCallable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final int size = model.getCharts().size();
                final ChartSetting setting = model.getChartSetting();
                if (size >= model.getCapacity() || !model.isIterable()) {
                    LOGGER.info("iterate charts[abort], symbolId: {}, size: {}", setting.getSymbolId(), size); return false;
                } else {
                    final Tuple<Boolean, List<ChartVo>> r;
                    final short sid = setting.getSymbolId();
                    final int step = systemService.getConfig().getChartIterationStep();
                    final Long next = (size == 0 ? null : model.getCharts().lastKey());
                    r = pricingService.iterateCharts(sid, setting.getType(), next, step).get();
                    model.setIterable(r.getV1()); if(r.getV2() != null) model.addCharts(r.getV2());
                    if(verbose) LOGGER.info("[" + name + "]iterate charts, symbolId: {}, size: {}", sid, size); return r.getV1();
                }
            }
        });
    }

    /**
     *
     */
    @Override
    protected void onRestart(final Model m, final View v) throws Exception {
        //
        final ChartModel model = Objects.cast(m);
        final Short groupId = getGroupId(this.session);
        final long accountId = getSession().getAccountId();
        model.setOrderSetting(this.storage.getOrderSetting(accountId));
        model.setIndicatorSetting(this.storage.getIndicatorSetting(accountId));
        model.setProducts(ProductVo.map(this.companyService.getProducts(groupId).get()));
        if(model.getChartSetting() != null) this.storage.setChartSetting(accountId, model.getChartSetting());

        //
        if(model.getChartSetting() == null || model.getChartSetting().getSymbolId() == (short)0) {
            model.setIterable(false);
            model.setCharts(new ArrayList<ChartVo>(0));
            model.setResult(PricingServiceProtoV1.SubscribeChartEventResponse.Result.SUCCESS);
        } else {
            final ChartSetting setting = model.getChartSetting();
            model.setResult(PricingServiceProtoV1.SubscribeChartEventResponse.Result.INTERNAL_ERROR);
            final int ic = this.systemService.getConfig().getChartInitialCapacity();
            Triple<PricingServiceProtoV1.SubscribeChartEventResponse.Result, List<QuoteVo>, List<ChartVo>> t;
            final Tuple<TradingServiceProtoV1.SubscribePositionEventResponse.Result, TradingPositionVo> t2;
            final ChartType type = setting.getType(); final short symbolId = setting.getSymbolId();
            t = this.pricingService.subscribeChartEvent(symbolId, type, ic, model, model); model.setResult(t.getV1());
            if(t.getV1() == PricingServiceProtoV1.SubscribeChartEventResponse.Result.SUCCESS) { model.setQuotes(t.getV2()); model.setCharts(t.getV3()); model.setIterable(true); }
            else LOGGER.warn("[" + name + "]failed to subscribe charts, accountId: {}, symbolId: {}, result: {}", new Object[]{accountId, symbolId, t.getV1()});
            if(session.hasLogin()) {
                t2 = this.tradingService.subscribePositionEvent(model);
                model.setConversions(companyService.getConversions(groupId).get());
                if (t2.getV1() == TradingServiceProtoV1.SubscribePositionEventResponse.Result.SUCCESS) model.setPosition(t2.getV2());
                else LOGGER.warn("[" + this.name + "]failed to subscribe position, accountId: {}, result: {}", accountId, t2.getV1());

                // Evaluate
                model.setProfits(evaluator.evaluate(model.getPosition(), model.getProducts(), model.getQuotes(), model.getConversions()));
            }
        }
    }

    /**
     *
     */
    @Override
    protected void onCreate(final Model m, final View v) throws Exception {
        // NOP
        final ChartModel model = Objects.cast(m);
        final Short groupId = getGroupId(this.session); // Maybe null
        final long accountId = getSession().getAccountId(); // Maybe zero
        model.setOrderSetting(this.storage.getOrderSetting(accountId));
        model.setIndicatorSetting(this.storage.getIndicatorSetting(accountId));
        FutureEx<IntHashMap<ProductVo>> f1 = companyService.getProducts(groupId);
        model.setCapacity(this.systemService.getConfig().getChartMaximumCapacity());
        model.setSymbols(SymbolSetting.map(this.storage.getSymbolSettings(accountId, f1.get())));
        model.setProducts(ProductVo.map(f1.get())); model.setChartSetting(this.storage.getChartSetting(accountId, f1.get()));

        //
        if(model.getChartSetting().getSymbolId() <= (short)0) {
            model.setIterable(false);
            model.setCharts(new ArrayList<ChartVo>(0));
            model.setResult(PricingServiceProtoV1.SubscribeChartEventResponse.Result.SUCCESS);
        } else {
            final ChartSetting setting = model.getChartSetting();
            model.setResult(PricingServiceProtoV1.SubscribeChartEventResponse.Result.INTERNAL_ERROR);
            final int ic = this.systemService.getConfig().getChartInitialCapacity();
            Triple<PricingServiceProtoV1.SubscribeChartEventResponse.Result, List<QuoteVo>, List<ChartVo>> t;
            final Tuple<TradingServiceProtoV1.SubscribePositionEventResponse.Result, TradingPositionVo> t2;
            final ChartType type = setting.getType(); final short symbolId = setting.getSymbolId();
            t = this.pricingService.subscribeChartEvent(symbolId, type, ic, model, model); model.setResult(t.getV1());
            if(t.getV1() == PricingServiceProtoV1.SubscribeChartEventResponse.Result.SUCCESS) { model.setQuotes(t.getV2()); model.setCharts(t.getV3()); model.setIterable(true); }
            else LOGGER.warn("[" + name + "]failed to subscribe charts, accountId: {}, symbolId: {}, result: {}", new Object[]{accountId, symbolId, t.getV1()});
            if(session.hasLogin()) {
                t2 = this.tradingService.subscribePositionEvent(model);
                model.setConversions(companyService.getConversions(groupId).get());
                if (t2.getV1() == TradingServiceProtoV1.SubscribePositionEventResponse.Result.SUCCESS) model.setPosition(t2.getV2());
                else LOGGER.warn("[" + this.name + "]failed to subscribe position, accountId: {}, result: {}", accountId, t2.getV1());

                // Evaluate
                model.setProfits(evaluator.evaluate(model.getPosition(), model.getProducts(), model.getQuotes(), model.getConversions()));
            }

        }
    }
}
