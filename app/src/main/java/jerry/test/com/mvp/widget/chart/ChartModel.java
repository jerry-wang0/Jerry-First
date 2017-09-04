package jerry.test.com.mvp.widget.chart;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import cn.nextop.erebor.mid.app.domain.storage.impl.bean.ChartSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.IndicatorSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.OrderSetting;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.app.domain.trading.model.TradingContracts;
import cn.nextop.erebor.mid.app.mvc.main.pricing.panel.EvaluatorModel;
import cn.nextop.erebor.mid.app.mvc.main.setting.panel.order.model.OrderPlacePanelModel;
import cn.nextop.erebor.mid.app.mvc.support.AbstractModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.grid.model.GridAdaptable;
import cn.nextop.erebor.mid.app.mvc.support.widget.grid.model.GridModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.model.ListAdaptable;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.model.ListModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.trading.TradingConsoleModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.ChartStyle;
import cn.nextop.erebor.mid.common.glossary.chart.ChartType;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorArea;
import cn.nextop.erebor.mid.common.glossary.enums.CurrencyCode;
import cn.nextop.erebor.mid.common.glossary.enums.Side;
import cn.nextop.erebor.mid.common.util.Collections;
import cn.nextop.erebor.mid.common.util.Comparators;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.Numbers;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.common.util.collection.map.IntHashMap;
import cn.nextop.erebor.mid.common.util.collection.map.LongHashMap;
import cn.nextop.erebor.mid.common.util.concurrent.collection.ConcurrentMultiKeyMap;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ConversionVo;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.event.ChartListener;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.event.QuoteListener;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.ChartVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.QuoteVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.event.PositionListener;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingContractVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingMarketOrderVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingOrderVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingPositionVo;
import cn.nextop.erebor.mid.rpc.service.protobuf.PricingServiceProtoV1;

import static cn.nextop.erebor.mid.app.domain.support.Formatters.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.list.glossary.RowStyle.BLEND;
import static cn.nextop.erebor.mid.common.glossary.enums.TradingPlaceType.OFFSETTING;
import static cn.nextop.erebor.mid.common.glossary.enums.TradingPlaceType.REGULAR;
import static cn.nextop.erebor.mid.rpc.MobileApiGenerator.getSymbolId;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

/**
 * Created by Jingqi Xu on 8/3/15.
 */
public class ChartModel extends AbstractModel implements ChartListener, QuoteListener, PositionListener {
    //
    private static final Comparator<Long> DESC = new Comparators.LongComparator(false);

    //
    private int capacity;
    private boolean iterable = true;
    private ChartSetting chartSetting;
    private OrderSetting orderSetting;
    private TradingPositionVo position;
    private IndicatorSetting indicatorSetting;
    private volatile QuoteListener quoteListener;
    private volatile ChartListener chartListener;
    private AtomicLong progress = new AtomicLong(0);
    private volatile PositionListener positionListener;
    private LongHashMap<BigDecimal> pips = new LongHashMap<>();
    private LongHashMap<BigDecimal> profits = new LongHashMap<>();
    private volatile TradingContracts contracts = new TradingContracts();
    private volatile Map<Short, ProductVo> products = Maps.newHashMap(32);
    private PricingServiceProtoV1.SubscribeChartEventResponse.Result result;
    private ConcurrentMap<Short, QuoteVo> quotes = new ConcurrentHashMap<>(32);
    private LinkedHashMap<Short, SymbolSetting> symbols = Maps.newLinkedHashMap(32);
    private ConcurrentSkipListMap<Long, ChartVo> charts = new ConcurrentSkipListMap<>(DESC);
    private ConcurrentMultiKeyMap<CurrencyCode, CurrencyCode, ConversionVo> conversions = new ConcurrentMultiKeyMap<>();


    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("result", result)
        .append("symbols", symbols)
        .append("progress", progress)
        .append("products", products)
        .append("capacity", capacity)
        .append("iterable", iterable)
        .append("profits", profits)
        .append("position", position)
        .append("contracts", contracts)
        .append("conversions", conversions)
        .append("chartSetting", chartSetting)
        .append("orderSetting", orderSetting)
        .append("indicatorSetting", indicatorSetting)
        .toString();
    }

    /**
     *
     */
    @Override
    public ChartModel copy() {
        ChartModel r = new ChartModel();
        r.result = this.result;
        r.capacity = this.capacity;
        r.iterable = this.iterable;
        r.quoteListener = this.quoteListener;
        r.chartListener = this.chartListener;
        r.positionListener = this.positionListener;
        r.pips = new LongHashMap<>(this.pips);
        r.position = Objects.copy(this.position);
        r.contracts = Objects.copy(this.contracts);
        r.profits = new LongHashMap<>(this.profits);
        r.products = Maps.newHashMap(this.products);
        r.symbols = Maps.newLinkedHashMap(this.symbols);
        r.chartSetting = Objects.copy(this.chartSetting);
        r.orderSetting = Objects.copy(this.orderSetting);
        r.progress = new AtomicLong(this.progress.get());
        r.charts = new ConcurrentSkipListMap<>(this.charts);
        r.indicatorSetting = Objects.copy(this.indicatorSetting);
        r.conversions = new ConcurrentMultiKeyMap<>(this.conversions);
        return r;
    }

    /**
     *
     */
    @Override
    public void reset() {
        super.reset();
        this.result = null;
        this.position = null;
        this.quoteListener = null;
        this.positionListener = null;
        if(this.pips != null) this.pips.clear();
        if(this.quotes != null) this.quotes.clear();
        if(this.profits != null) this.profits.clear();
        if(this.symbols != null) this.symbols.clear();
        if(this.products != null) this.products.clear();
        if(this.conversions != null) this.conversions.clear();
    }

    /**
     *
     */
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isIterable() {
        return iterable;
    }

    public void setIterable(boolean iterable) {
        this.iterable = iterable;
    }

    public AtomicLong getProgress() {
        return progress;
    }

    public void setProgress(AtomicLong progress) {
        this.progress = progress;
    }

    public ChartListener getChartListener() {
        return chartListener;
    }

    public void setChartListener(ChartListener listener) {
        this.chartListener = listener;
    }

    public QuoteListener getQuoteListener() {
        return quoteListener;
    }

    public void setQuoteListener(QuoteListener listener) {
        this.quoteListener = listener;
    }

    public Map<Short, ProductVo> getProducts() {
        return products;
    }

    public void setProducts(Map<Short, ProductVo> products) {
        this.products = products;
    }

    public ChartSetting getChartSetting() {
        return chartSetting;
    }

    public void setChartSetting(ChartSetting chartSetting) {
        this.chartSetting = chartSetting;
    }

    public OrderSetting getOrderSetting() {
        return orderSetting;
    }

    public void setOrderSetting(OrderSetting orderSetting) {
        this.orderSetting = orderSetting;
    }

    public IndicatorSetting getIndicatorSetting() {
        return indicatorSetting;
    }

    public void setIndicatorSetting(IndicatorSetting setting) {
        this.indicatorSetting = setting;
    }

    public LinkedHashMap<Short, SymbolSetting> getSymbols() {
        return symbols;
    }

    public final SymbolSetting getSymbol(final Short symbolId) {
        return symbolId == null ? null : symbols.get(symbolId);
    }

    public void setSymbols(LinkedHashMap<Short, SymbolSetting> symbols) {
        this.symbols = symbols;
    }

    /**
     * Quote
     */
    public ConcurrentMap<Short, QuoteVo> getQuotes() {
        return quotes;
    }

    @Override
    public void onQuotes(final IntHashMap<QuoteVo> quotes) {
        addQuotes(quotes);
        QuoteListener listener = this.quoteListener;
        if(listener != null) listener.onQuotes(quotes);
    }

    public void setQuotes(ConcurrentMap<Short, QuoteVo> quotes) {
        this.quotes = quotes;
    }

    public final void setQuotes(final List<QuoteVo> quotes) {
        if(Collections.isEmpty(quotes)) return;
        for(final QuoteVo quote : quotes) {
            this.quotes.put(getSymbolId(quote.getProductId()), quote);
        }
    }

    public final void addQuotes(IntHashMap<QuoteVo> quotes) {
        if(Maps.isEmpty(quotes)) return;
        for(final QuoteVo quote : quotes.values()) {
            this.quotes.put(getSymbolId(quote.getProductId()), quote);
        }
    }

    public PositionListener getPositionListener() {
        return positionListener;
    }

    public void setPositionListener(PositionListener positionListener) {
        this.positionListener = positionListener;
    }

    public TradingContracts getContracts() {
        return contracts;
    }

    public void setContracts(TradingContracts contracts) {
        this.contracts = contracts;
    }

    public TradingPositionVo getPosition() {
        return position;
    }

    public void setPosition(TradingPositionVo position) {
        this.position = position;
    }

    public ConcurrentMultiKeyMap<CurrencyCode, CurrencyCode, ConversionVo> getConversions() {
        return conversions;
    }

    public void setConversions(ConcurrentMultiKeyMap<CurrencyCode, CurrencyCode, ConversionVo> conversions) {
        this.conversions = conversions;
    }

    public void setConversions(final List<ConversionVo> conversions) {
        this.conversions.clear(); if(Collections.isEmpty(conversions)) return;
        for(ConversionVo cv : conversions) this.conversions.put(cv.getSrcCurrency(), cv.getDstCurrency(), cv);
    }

    public LongHashMap<BigDecimal> getProfits() {
        return profits;
    }

    public void setProfits(LongHashMap<BigDecimal> profits) {
        this.profits = profits;
    }

    /**
     * Position
     */
    @Override
    public void onPosition(TradingPositionVo position) {
        this.position = position; super.touch();
        final PositionListener pl = this.positionListener; if(pl != null) pl.onPosition(position);
    }

    /**
     * Chart
     */
    @Override
    public void onCharts(final LongHashMap<ChartVo> charts) {
        addCharts(charts);
        ChartListener listener = this.chartListener;
        if(listener != null) listener.onCharts(charts);
    }

    public ConcurrentSkipListMap<Long, ChartVo> getCharts() {
        return charts;
    }

    public final void setCharts(final List<ChartVo> charts) {
        this.charts.clear();
        if(Collections.isEmpty(charts)) return;
        for(ChartVo chart : charts) this.charts.put(chart.getId(), chart);
    }

    public final void addCharts(final List<ChartVo> charts) {
        if(Collections.isEmpty(charts)) return;
        for(ChartVo chart : charts) this.charts.put(chart.getId(), chart);
        final int size = this.charts.size(), threshold = (int)(capacity * 1.1f);
        if(size > threshold) this.charts = Maps.compact(this.charts, DESC, capacity);
    }

    public final void addCharts(final LongHashMap<ChartVo> charts) {
        if(Maps.isEmpty(charts)) return;
        for(ChartVo chart : charts.values()) this.charts.put(chart.getId(), chart);
        final int size = this.charts.size(), threshold = (int)(this.capacity * 1.1f);
        if(size > threshold) this.charts = Maps.compact(this.charts, DESC, capacity);
    }

    /**
     * Result
     */
    public final PricingServiceProtoV1.SubscribeChartEventResponse.Result getResult() {
        return result;
    }

    public void setResult(PricingServiceProtoV1.SubscribeChartEventResponse.Result result) {
        this.result = result;
    }

    public final TradingConsoleModel toTradingConsoleModel() {
        TradingConsoleModel r = new TradingConsoleModel();
        r.setQuotes(Maps.newHashMap(this.quotes)); // Copy
        r.setOrderSetting(this.orderSetting); r.setSymbols(this.symbols);
        r.setSymbolId(this.chartSetting == null ? null : this.chartSetting.getSymbolId());
        if(r.getSymbolId() != null) r.setProduct(this.products.get(r.getSymbolId())); return r;
    }

    public final XChartConfig getChartConfig(final boolean reset) {
        final XChartConfig r = new XChartConfig();
        final ChartSetting cs = this.chartSetting;
        if(cs == null || cs.getSymbolId() <= 0) return r;
        final SymbolSetting ss = getSymbol(cs.getSymbolId()); if(ss == null) return r;
        if (reset) r.setScale(ChartScale.getDefault()); else r.setScale(cs.getScale());
        r.setType(cs.getType()); r.setStyle(cs.getStyle()); r.setDigits(ss.getScale());
        r.setIndicator(IndicatorArea.A, indicatorSetting.getIndicator(indicatorSetting.getSelection(IndicatorArea.A)));
        r.setIndicator(IndicatorArea.B, indicatorSetting.getIndicator(indicatorSetting.getSelection(IndicatorArea.B)));
        return r;
    }

    /**
     *
     */
    public TradingOrderVo toTradingOrder(final ProductVo product, final Side side, final QuoteVo quote, final BigDecimal volume) {
        final BigDecimal scale = TEN.pow(product.getScale().intValue());
        final TradingMarketOrderVo market = new TradingMarketOrderVo();
        market.setSide(side); market.setQuoteId(quote.getId());
        market.setPlaceType(orderSetting.isFIFO() ? OFFSETTING : REGULAR);
        market.setVolume(volume); market.setProductId(quote.getProductId());
        final BigDecimal sl = orderSetting.getSlDeviation(), tp = orderSetting.getTpDeviation();
        if(this.orderSetting.isClose() && sl != null) { market.setSlDeviation(sl.divide(scale));}
        if(this.orderSetting.isClose() && tp != null) { market.setTpDeviation(tp.divide(scale));}
        if(this.orderSetting.isClose() && orderSetting.getCloseExpiry() != null) market.setExpireType(orderSetting.getCloseExpiry());
        final TradingOrderVo order = new TradingOrderVo(); order.setMarket(market);  return order; // Market
    }

    /**
     *
     */
    public GridModel toSymbolFilter() {
        final GridModel r = new GridModel();
        final List<GridAdaptable> list = r.getItems();
        final Short symbolId = this.chartSetting.getSymbolId();
        final SymbolSetting index = symbolId == null ? null : symbols.get(symbolId);
        for(SymbolSetting s : symbols.values()) list.add(new GridAdaptable(s.getName(), s.getSymbolIcon(), s));
        for(int i = 0, size = list.size(); i < size; i++) if(list.get(i).getCookie() == index) r.setIndex(i); return r;
    }

    public final ListModel toChartTypeFilter() {
        final ListModel r = new ListModel();
        if (this.chartSetting == null) return r; int index = -1;
        for(ChartType e : ChartType.values()) {
            ListAdaptable a = new ListAdaptable(); a.setStyle(BLEND);
            index++; if(this.chartSetting.getType() == e) r.setIndex(index);
            a.setIcon(-1); a.setText(format(e,"")); a.setCookie(e); r.getRows().add(a);
        }
        return r;
    }

    public final ListModel toChartStyleFilter() {
        final ListModel r = new ListModel();
        if (this.chartSetting == null) return r; int index = -1;
        for(ChartStyle o : ChartStyle.values()){
            ListAdaptable a = new ListAdaptable(); a.setStyle(BLEND);
            index++; if(chartSetting.getStyle() == o) r.setIndex(index);
            a.setText(format(o, "")); a.setIcon(format(o, -1)); a.setCookie(o); r.getRows().add(a);
        }
        return r;
    }

    public final OrderPlacePanelModel toPlaceSettingModel() {
        final OrderPlacePanelModel r = new OrderPlacePanelModel(this.orderSetting);
        r.setProduct(getProducts().get(getChartSetting().getSymbolId()));return r;
    }

    public void setPlaceSetting(final OrderSetting order){
        if(order == null || orderSetting == null) return ;
        orderSetting.setFIFO(order.isFIFO()); orderSetting.setClose(order.isClose());
        orderSetting.setVolume(order.getVolume()); orderSetting.setCloseExpiry(order.getCloseExpiry());
        orderSetting.setTpDeviation(order.getTpDeviation()); orderSetting.setSlDeviation(order.getSlDeviation());
    }

    /**
     *
     */
    public final boolean hasContract(final Short symbolId, final Side side, final boolean fifo) {
        if(fifo || position == null || position.getContracts().isEmpty()) return false;
        for(TradingContractVo c : position.getContracts().values()) {
            if(side != c.getSide()) continue;
            if(symbolId != c.getSymbolId()) continue; return true;
        }
        return false;
    }

    /**
     *
     */
    public EvaluatorModel toEvaluatorModel() {
        //
        final EvaluatorModel r = new EvaluatorModel();
        final Short symbolId = chartSetting.getSymbolId();
        if(symbolId == null) return r; r.setSymbolId(symbolId);
        final ProductVo product = getProducts().get(symbolId); if(product == null) return r;
        r.setProduct(product); QuoteVo quote = quotes.get(symbolId); if(quote == null) return r;
        final int scale = product.getScale().intValue(); final BigDecimal pips = TEN.pow(scale - 1);

        if(position == null) return r;
        r.setCurrency(position.getAccount().getDepositCurrency());
        BigDecimal lot1 = ZERO, price1 = ZERO, pips1 = ZERO, profit1 = ZERO;
        BigDecimal lot_1 = ZERO, price_1 = ZERO, pips_1 = ZERO, profit_1 = ZERO;
        for(TradingContractVo c :  position.getContracts().values()) {
            if(c.getSymbolId() != symbolId) continue;
            final BigDecimal volume = c.getOpenVolume().subtract(c.getClosedVolume());
            final BigDecimal p2 = c.getSide() == Side.BUY ? quote.getBidPrice() : quote.getAskPrice();
            final BigDecimal profit = profits.get(c.getId()); final BigDecimal p1 = volume.multiply(c.getOpenPrice());
            final BigDecimal pip =  p2.subtract(c.getOpenPrice()).multiply(pips).multiply(c.getSide().toBigDecimal());

            if(c.getSide() == Side.BUY) {
                pips1 = Numbers.add(pips1, pip); price1 = Numbers.add(price1, p1);
                profit1 = Numbers.add(profit1, profit); lot1 = Numbers.add(lot1, volume);
            }
            if(c.getSide() == Side.SELL) {
                price_1 = Numbers.add(price_1, p1); pips_1 = Numbers.add(pips_1, pip);
                profit_1 = Numbers.add(profit_1, profit); lot_1 = Numbers.add(lot_1, volume);
            }
        }

        if (lot_1.compareTo(BigDecimal.ZERO) > 0) {
            r.setProfit(Side.SELL, profit_1); r.setPrice(Side.SELL, price_1.divide(lot_1, scale, HALF_UP));
            r.setPips(Side.SELL, pips_1.divide(lot_1.divide(product.getContractSize()), scale, HALF_UP)); r.setLots(Side.SELL, lot_1);
        }
        if (lot1.compareTo(BigDecimal.ZERO) > 0) {
            r.setProfit(Side.BUY, profit1);  r.setPrice(Side.BUY, price1.divide(lot1, scale, HALF_UP));
            r.setPips(Side.BUY, pips1.divide(lot1.divide(product.getContractSize()), scale, HALF_UP)); r.setLots(Side.BUY, lot1);
        }
        return r;
    }
}
