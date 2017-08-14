package jerry.test.com.mvp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.app.mvc.support.widget.text.DecimalPicker;
import cn.nextop.erebor.mid.common.glossary.enums.TradeType;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingContractVo;

import static cn.nextop.erebor.mid.common.glossary.enums.TradeType.CLOSE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.HALF_UP;

/**
 * Created by Jingqi Xu on 9/21/15.
 */
public final class TradingUtils {
    //
    private static final BigDecimal MIN_SLIPPAGE = new BigDecimal("0");
    private static final BigDecimal MAX_SLIPPAGE = new BigDecimal("999");
    private static final ConcurrentMap<Integer, BigDecimal> DIVISORS = new ConcurrentHashMap<>();

    /**
     *
     */
    protected static final BigDecimal getDivisor(final BigDecimal scale) {
        final int v = scale.intValue(); BigDecimal r = DIVISORS.get(v);
        return r != null ? r : Maps.putIfAbsent(DIVISORS, v, TEN.pow(v));
    }


    /**
     *
     */
    public static BigDecimal getLots(BigDecimal volume, ProductVo product) {
        final int scale = getTradingVolumeScale(product);
        return volume.divide(product.getContractSize(), scale, HALF_UP);
    }

    /**
     *
     */
    public static BigDecimal getRemainingVolume(TradingContractVo contract) {
        return contract.getOpenVolume().subtract(contract.getClosedVolume());
    }

    public static final BigDecimal getClosableVolume(TradingContractVo contract) {
        return getRemainingVolume(contract).subtract(contract.getClosingVolume());
    }

    /**
     *
     */
    public static final BigDecimal getMinLeavingOffset(ProductVo product) {
        return product.getMinLeavingOffset().divide(getDivisor(product.getScale()));
    }

    public static final BigDecimal getMaxLeavingOffset(ProductVo product) {
        return product.getMaxLeavingOffset().divide(getDivisor(product.getScale()));
    }

    /**
     *
     */
    public static BigDecimal roundUp(final BigDecimal v1, BigDecimal v2) {
        if(v1.remainder(v2).compareTo(ZERO) == 0) return v1;
        return v1.divide(v2, 0, DOWN).multiply(v2).add(v2);
    }

    public static BigDecimal roundDown(final BigDecimal v1, BigDecimal v2) {
        if(v1.remainder(v2).compareTo(ZERO) == 0) return v1;
        return v1.divide(v2, 0, DOWN).multiply(v2);
    }

    public static final BigDecimal getMinTradingLots(final ProductVo product) {
        final int scale = getTradingVolumeScale(product);
        final BigDecimal size = product.getContractSize();
        return getMinTradingVolume(product).divide(size, scale, RoundingMode.HALF_UP);
    }

    public static final BigDecimal getMaxTradingLots(final ProductVo product) {
        final int scale = getTradingVolumeScale(product);
        final BigDecimal size = product.getContractSize();
        return getMaxTradingVolume(product).divide(size, scale, RoundingMode.HALF_UP);
    }

    public static final BigDecimal getMinTradingVolume(final ProductVo product) {
        return roundUp(product.getMinTradingVolume(), product.getTradingVolumeStep());
    }

    public static final BigDecimal getMaxTradingVolume(final ProductVo product) {
        return roundDown(product.getMaxTradingVolume(), product.getTradingVolumeStep());
    }

    /**
     *
     */
    public static final int getTradingVolumeScale(final ProductVo product) {
        BigDecimal step = product.getTradingVolumeStep(), size = product.getContractSize();
        return step.divide(size, 2, RoundingMode.HALF_UP).stripTrailingZeros().scale(); // Max 2 digits
    }

    public static final int getTradingVolumeScale(final SymbolSetting symbol) {
        BigDecimal step = symbol.getTradingVolumeStep(), size = symbol.getContractSize();
        return step.divide(size, 2, RoundingMode.HALF_UP).stripTrailingZeros().scale(); // Max 2 digits
    }

    public static final BigDecimal fxOrderMargin(BigDecimal orderRatio, BigDecimal volume, BigDecimal leverage, BigDecimal scale) {
        return volume.multiply(orderRatio).divide(leverage, scale.intValue(), HALF_UP);
    }

    public static final BigDecimal cfdOrderMargin(BigDecimal orderRatio, BigDecimal volume, BigDecimal price) {
        return volume.multiply(price).multiply(orderRatio);
    }

    public static final BigDecimal cfdIndexOrderMargin(BigDecimal orderRatio, BigDecimal volume, BigDecimal price, BigDecimal tickSize, BigDecimal scale){
        return volume.multiply(price).multiply(orderRatio).divide(tickSize, scale.intValue(), HALF_UP);
    }

    public static final BigDecimal cfdLeverageOrderMargin(BigDecimal orderRatio, BigDecimal volume, BigDecimal price, BigDecimal leverage, BigDecimal scale){
        return volume.multiply(price).multiply(orderRatio).divide(leverage, scale.intValue(), HALF_UP);
    }

    /**
     * 
     */
    public static final void setupSlippage(final DecimalPicker picker) {
        picker.setSigned(false); picker.setError(null);
        picker.setMinValue(MIN_SLIPPAGE); picker.setMaxValue(MAX_SLIPPAGE);
        picker.setScale(0); picker.setStep(BigDecimal.ONE); picker.setImage(0);
    }

    public static final void setupPrice(final DecimalPicker picker, ProductVo product) {
        if(product == null) return;
        picker.setSigned(false); picker.setError(null); picker.setMaxLength(7);
        picker.setMinValue(ZERO); picker.setScale(product.getScale().intValue());
        picker.setStep(BigDecimal.ONE.divide(BigDecimal.TEN.pow(picker.getScale())));
    }

    public static final void setupDeviation(final DecimalPicker picker, ProductVo product) {
        if(product == null) return;
        picker.setSigned(false); picker.setError(null); picker.setScale(0);
        picker.setImage(0); picker.setMaxValue(product.getMaxLeavingOffset());
        picker.setStep(BigDecimal.ONE); picker.setMinValue(product.getMinLeavingOffset());
    }

    public static final void setupVolume(DecimalPicker picker, ProductVo product, TradeType type) {
        if(product == null) return;
        picker.setSigned(false); picker.setError(null);
        final int scale = getTradingVolumeScale(product);
        picker.setScale(scale); picker.setMaxValue(getMaxTradingLots(product));
        if(type == CLOSE) picker.setMinValue(ZERO); else picker.setMinValue(getMinTradingLots(product));
        picker.setStep(product.getTradingVolumeStep().divide(product.getContractSize(), scale, HALF_UP));
    }
}
