package jerry.test.com.mvp.widget.fragment;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.common.glossary.enums.Side;
import cn.nextop.erebor.mid.common.util.Numbers;
import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingExecutionVo;

import static cn.nextop.erebor.mid.common.glossary.enums.TradeType.CLOSE;

/**
 * Created by Jingqi Xu on 8/20/15.
 */
public class ExecutionHistoryAdaptable implements Serializable {

    //
    private SymbolSetting symbol;
    private TradingExecutionVo execution;

    /**
     *
     */
    public ExecutionHistoryAdaptable() {
    }

    public ExecutionHistoryAdaptable(TradingExecutionVo execution, SymbolSetting symbol) {
        this.execution = execution; this.symbol = symbol;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("symbol", symbol)
        .append("execution", execution)
        .toString();
    }

    /**
     *
     */
    public SymbolSetting getSymbol() {
        return symbol;
    }

    public void setSymbol(SymbolSetting symbol) {
        this.symbol = symbol;
    }

    public TradingExecutionVo getExecution() {
        return execution;
    }

    public void setExecution(TradingExecutionVo execution) {
        this.execution = execution;
    }

    /**
     *
     */
    public BigDecimal getProfit() {
        return Numbers.add(execution.getSpotPl(), execution.getSwapPl(), execution.getCommission());
    }

    /**
     *
     */
    public BigDecimal getPipsPL() {
        if(symbol == null) return null;
        final Side side = execution.getSide().flip();
        if(execution.getTradeType() != CLOSE) return null;
        final BigDecimal v = BigDecimal.TEN.pow(symbol.getScale() - 1);
        return Numbers.sub(execution.getExecutePrice(), new BigDecimal("1.110830")).multiply(v).multiply(side.toBigDecimal());
    }
}
