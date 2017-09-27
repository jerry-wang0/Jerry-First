package jerry.test.com.mvp.widget.fragment;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.nextop.erebor.mid.common.glossary.Copyable;
import cn.nextop.erebor.mid.common.glossary.enums.ProductStatus;
import cn.nextop.erebor.mid.common.util.Collections;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;

/**
 * Created by Jingqi Xu on 8/3/15.
 */
public class SymbolSetting implements Copyable<SymbolSetting>, Serializable {
    //
    private byte scale;
    private String name;
    private long imageId;
    private short symbolId;
    private String mobileVersion;
    private boolean visible = true;
    private BigDecimal contractSize;
    private transient Bitmap symbolIcon;
    private BigDecimal tradingVolumeStep;
    private ProductStatus status = ProductStatus.ACTIVE;

    /**
     *
     */
    public SymbolSetting() {
    }

    public SymbolSetting(ProductVo product) {
        refresh(product);
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("name", name)
        .append("scale", scale)
        .append("status", status)
        .append("visible", visible)
        .append("imageId", imageId)
        .append("symbolId", symbolId)
        .append("contractSize", contractSize)
        .append("mobileVersion", mobileVersion)
        .append("tradingVolumeStep", tradingVolumeStep)
        .toString();
    }

    /**
     *
     */
    @Override
    public SymbolSetting copy() {
        SymbolSetting r = new SymbolSetting();
        r.name = this.name;
        r.scale = this.scale;
        r.status = this.status;
        r.imageId = this.imageId;
        r.visible = this.visible;
        r.symbolId = this.symbolId;
        r.symbolIcon = this.symbolIcon;
        r.contractSize = this.contractSize;
        r.mobileVersion = this.mobileVersion;
        r.tradingVolumeStep = this.tradingVolumeStep;
        return r;
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getScale() {
        return scale;
    }

    public void setScale(byte scale) {
        this.scale = scale;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public short getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(short symbolId) {
        this.symbolId = symbolId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Bitmap getSymbolIcon() {
        return symbolIcon;
    }

    public void setSymbolIcon(Bitmap symbolIcon) {
        this.symbolIcon = symbolIcon;
    }

    public String getMobileVersion() {
        return mobileVersion;
    }

    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }

    public BigDecimal getContractSize() {
        return contractSize;
    }

    public void setContractSize(BigDecimal contractSize) {
        this.contractSize = contractSize;
    }

    public BigDecimal getTradingVolumeStep() {
        return tradingVolumeStep;
    }

    public void setTradingVolumeStep(BigDecimal tradingVolumeStep) {
        this.tradingVolumeStep = tradingVolumeStep;
    }

    /**
     *
     */
    public final boolean isActive() {
        return status != null && status.isActive();
    }

    public static SymbolSetting first(LinkedHashMap<Short, SymbolSetting> symbols) {
        for(SymbolSetting s : symbols.values()) if(s.isActive()) return s;
        return null;
    }

    public static final List<SymbolSetting> filter(final List<SymbolSetting> symbols) {
        final List<SymbolSetting> r = new ArrayList<>(symbols.size());
        for(SymbolSetting symbol : symbols) if(symbol.isActive()) r.add(symbol);
        return r;
    }

    public static LinkedHashMap<Short, SymbolSetting> map(final List<SymbolSetting> symbols) {
        if(Collections.isEmpty(symbols)) return new LinkedHashMap<>(1);
        final LinkedHashMap<Short, SymbolSetting> r = Maps.newLinkedHashMap(symbols.size());
        for(final SymbolSetting symbol : symbols) r.put(symbol.getSymbolId(), symbol); return r;
    }

    public static LinkedHashMap<Short, SymbolSetting> filter(LinkedHashMap<Short, SymbolSetting> symbols) {
        final LinkedHashMap<Short, SymbolSetting> r = Maps.newLinkedHashMap(symbols.size());
        for(SymbolSetting symbol : symbols.values()) if(symbol.isActive()) r.put(symbol.getSymbolId(), symbol);
        return r;
    }

    public void refresh(ProductVo product) {
        if(product == null) return; this.name = product.getName();
        this.status = product.getStatus(); this.scale = product.getScale().byteValue(); this.contractSize = product.getContractSize();
        this.imageId = product.getImageId(); this.symbolId = product.getSymbolId(); this.tradingVolumeStep = product.getTradingVolumeStep();
    }
}
