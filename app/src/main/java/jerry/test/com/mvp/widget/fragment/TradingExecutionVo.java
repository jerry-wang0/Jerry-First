package jerry.test.com.mvp.widget.fragment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.nextop.erebor.mid.common.glossary.Copyable;
import cn.nextop.erebor.mid.common.glossary.enums.CurrencyCode;
import cn.nextop.erebor.mid.common.glossary.enums.Side;
import cn.nextop.erebor.mid.common.glossary.enums.TradeType;
import cn.nextop.erebor.mid.common.glossary.enums.TradingExecuteType;
import cn.nextop.erebor.mid.common.glossary.enums.TradingOrderType;
import cn.nextop.erebor.mid.common.glossary.enums.TradingPlaceType;
import cn.nextop.erebor.mid.common.glossary.enums.TradingTriggerType;
import cn.nextop.erebor.mid.common.util.Collections;
import cn.nextop.erebor.mid.common.util.DateTimes;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.common.util.collection.map.LongHashMap;
import cn.nextop.erebor.mid.rpc.MobileApiServiceContext;
import cn.nextop.erebor.mid.rpc.service.protobuf.TradingModelProtoV1;

/**
 * Created by Jingqi Xu on 7/24/15.
 */
public final class TradingExecutionVo implements Copyable<TradingExecutionVo>, Serializable {
    //
    private long id;
    private Side side;
    private int version;
    private short groupId;
    private int productId;
    private short symbolId;
    private long accountId;
    private long executeId;
    private long contractId;
    private Date executeTime;
    private BigDecimal spotPl;
    private BigDecimal swapPl;
    private TradeType tradeType;
    private BigDecimal commission;
    private BigDecimal executePrice;
    private BigDecimal executeVolume;
    private TradingOrderType orderType;
    private TradingPlaceType placeType;
    private CurrencyCode depositCurrency;
    private TradingTriggerType triggerType;
    private TradingExecuteType executeType;

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("id", id)
        .append("side", side)
        .append("spotPl", spotPl)
        .append("swapPl", swapPl)
        .append("version", version)
        .append("groupId", groupId)
        .append("symbolId", symbolId)
        .append("productId", productId)
        .append("orderType", orderType)
        .append("placeType", placeType)
        .append("tradeType", tradeType)
        .append("accountId", accountId)
        .append("executeId", executeId)
        .append("commission", commission)
        .append("contractId", contractId)
        .append("triggerType", triggerType)
        .append("executeTime", executeTime)
        .append("executeType", executeType)
        .append("executePrice", executePrice)
        .append("executeVolume", executeVolume)
        .append("depositCurrency", depositCurrency)
        .toString();
    }

    /**
     *
     */
    @Override
    public TradingExecutionVo copy() {
        TradingExecutionVo r = new TradingExecutionVo();
        r.setId(this.id);
        r.setSide(this.side);
        r.setSpotPl(this.spotPl);
        r.setSwapPl(this.swapPl);
        r.setVersion(this.version);
        r.setGroupId(this.groupId);
        r.setSymbolId(this.symbolId);
        r.setProductId(this.productId);
        r.setOrderType(this.orderType);
        r.setPlaceType(this.placeType);
        r.setTradeType(this.tradeType);
        r.setAccountId(this.accountId);
        r.setExecuteId(this.executeId);
        r.setCommission(this.commission);
        r.setContractId(this.contractId);
        r.setTriggerType(this.triggerType);
        r.setExecuteTime(this.executeTime);
        r.setExecuteType(this.executeType);
        r.setExecutePrice(this.executePrice);
        r.setExecuteVolume(this.executeVolume);
        r.setDepositCurrency(this.depositCurrency);
        return r;
    }

    /**
     *
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public short getGroupId() {
        return groupId;
    }

    public void setGroupId(short groupId) {
        this.groupId = groupId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public short getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(short symbolId) {
        this.symbolId = symbolId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getExecuteId() {
        return executeId;
    }

    public void setExecuteId(long executeId) {
        this.executeId = executeId;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public BigDecimal getSpotPl() {
        return spotPl;
    }

    public void setSpotPl(BigDecimal spotPl) {
        this.spotPl = spotPl;
    }

    public BigDecimal getSwapPl() {
        return swapPl;
    }

    public void setSwapPl(BigDecimal swapPl) {
        this.swapPl = swapPl;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getExecutePrice() {
        return executePrice;
    }

    public void setExecutePrice(BigDecimal executePrice) {
        this.executePrice = executePrice;
    }

    public BigDecimal getExecuteVolume() {
        return executeVolume;
    }

    public void setExecuteVolume(BigDecimal executeVolume) {
        this.executeVolume = executeVolume;
    }

    public TradingOrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(TradingOrderType orderType) {
        this.orderType = orderType;
    }

    public TradingPlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(TradingPlaceType placeType) {
        this.placeType = placeType;
    }

    public TradingTriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TradingTriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public TradingExecuteType getExecuteType() {
        return executeType;
    }

    public void setExecuteType(TradingExecuteType executeType) {
        this.executeType = executeType;
    }

    public CurrencyCode getDepositCurrency() {
        return depositCurrency;
    }

    public void setDepositCurrency(CurrencyCode depositCurrency) {
        this.depositCurrency = depositCurrency;
    }

    /**
     *
     */
    public static final TradingExecutionVo valueOf(MobileApiServiceContext context, TradingModelProtoV1.Execution execution) {
        if (execution == null) return null;
        TradingExecutionVo r = new TradingExecutionVo();
        r.setId(execution.getId());
        r.setVersion(execution.getVersion());
        r.setProductId(execution.getProductId());
        r.setAccountId(execution.getAccountId());
        r.setExecuteId(execution.getExecuteId());
        r.setGroupId((short) execution.getGroupId());
        r.setSymbolId((short) execution.getSymbolId());
        r.setSide(Side.parse((byte) execution.getSide()));
        r.setExecutePrice(new BigDecimal(execution.getPrice()));
        r.setExecuteVolume(new BigDecimal(execution.getVolume()));
        r.setTradeType(TradeType.parse((byte) execution.getTradeType()));
        r.setOrderType(TradingOrderType.parse((byte) execution.getOrderType()));
        r.setPlaceType(TradingPlaceType.parse((byte) execution.getPlaceType()));
        r.setDepositCurrency(CurrencyCode.parse(execution.getDepositCurrency()));
        if (execution.hasContractId()) r.setContractId(execution.getContractId());
        if (execution.hasSpotPl()) r.setSpotPl(new BigDecimal(execution.getSpotPl()));
        if (execution.hasSwapPl()) r.setSwapPl(new BigDecimal(execution.getSwapPl()));
        r.setExecuteType(TradingExecuteType.parse((byte) execution.getExecuteType()));
        if (execution.hasCommission()) r.setCommission(new BigDecimal(execution.getCommission()));
        r.setExecuteTime(DateTimes.toLocalTime(context.getTimeZone(), execution.getExecuteTime()));
        if (execution.hasTriggerType()) r.setTriggerType(TradingTriggerType.parse((byte) execution.getTriggerType()));
        return r;
    }

    /**
     *
     */
    public static List<TradingExecutionVo> valueOf1(MobileApiServiceContext context, List<TradingModelProtoV1.Execution> executions) {
        if (Collections.isEmpty(executions)) return new ArrayList<>(0);
        List<TradingExecutionVo> r = new ArrayList<>(executions.size());
        for (TradingModelProtoV1.Execution execution : executions) r.add(valueOf(context, execution)); return r;
    }

    public static final LongHashMap<TradingExecutionVo> valueOf2(MobileApiServiceContext context, List<TradingModelProtoV1.Execution> executions) {
        if(Collections.isEmpty(executions)) new LongHashMap<>(1);
        LongHashMap<TradingExecutionVo> r = Maps.newLongHashMap(executions.size());
        for(TradingModelProtoV1.Execution execution : executions) r.put(execution.getId(), valueOf(context, execution)); return r;
    }
}
