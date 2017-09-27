package jerry.test.com.mvp.widget.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.recycler.RecyclerViewAdapter;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingExecutionVo;

import static cn.nextop.erebor.mid.app.domain.support.Formatters.format;
import static cn.nextop.erebor.mid.app.domain.support.Formatters.formatAmountEx;
import static cn.nextop.erebor.mid.app.domain.support.Formatters.formatPips;
import static cn.nextop.erebor.mid.app.domain.support.Formatters.formatPrice;
import static cn.nextop.erebor.mid.app.domain.support.Formatters.formatVolumeEx;
import static cn.nextop.erebor.mid.common.util.Objects.cast;

/**
 * Created by Liqun Wan on 2015/7/29.
 */
public class ExecutionHistoryDetailAdapter extends RecyclerViewAdapter<ExecutionHistoryDetailAdaptable, ExecutionHistoryDetailAdapter.Holder> {

    protected ExecutionHistoryDetail parent;

    public final static class Holder extends RecyclerView.ViewHolder {

        private TextView tvName, tvValue;

        public Holder(View v) {
            super(v);
            this.tvName = cast(v.findViewById(R.id.item_history_detail_row_name));
            this.tvValue = cast(v.findViewById(R.id.item_history_detail_row_value));
        }
    }

    public ExecutionHistoryDetailAdapter(ExecutionHistoryDetail parent){
        this.parent = parent;
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_symbol));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_trade_type));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_side));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_volume));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_price));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_time));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_pips_pl));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_spot));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_swap));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_profit));
        rows.add(new ExecutionHistoryDetailAdaptable(R.string.trading_execution_history_detail_id));
    }

    @Override
    protected void onBindView(final Holder h, final ExecutionHistoryDetailAdaptable a, final int p) {
        //
        final Integer textId = cast(h.tvName.getTag());
        if(textId == null || textId != a.getName()) {
            h.tvName.setTag(a.getName()); h.tvName.setText(a.getName());
        }

        //
        h.tvValue.setText(a.getValue());
        if(h.tvValue.getCurrentTextColor() != a.getColor()) h.tvValue.setTextColor(a.getColor());
    }

    @Override
    protected Holder onCreateView(ViewGroup vg, int viewType) {
        return new Holder(LayoutInflater.from(vg.getContext()).inflate(R.layout.widget_trading_execution_history_detail_row, vg, false));
    }

	/**
	 *
	 */
	public void refresh(final ExecutionHistoryAdaptable adaptable) {
        if(adaptable == null) return;
        final SymbolSetting s = adaptable.getSymbol();
        final TradingExecutionVo e = adaptable.getExecution();
		for(final ExecutionHistoryDetailAdaptable a : this.rows) {
			switch(a.getName()) {
                case R.string.trading_execution_history_detail_id: a.setValue(String.valueOf(e.getId())); break;
                case R.string.trading_execution_history_detail_symbol: a.setValue(format(s.getName(), "---")); break;
                case R.string.trading_execution_history_detail_trade_type: a.setValue(format(e.getTradeType(), "---")); break;
                case R.string.trading_execution_history_detail_time: a.setValue(format(e.getExecuteTime(), "yyyy/MM/dd HH:mm:ss", "---")); break;
                case R.string.trading_execution_history_detail_volume: a.setValue(formatVolumeEx(e.getExecuteVolume(), s, "---")); break;
                case R.string.trading_execution_history_detail_price: a.setValue(formatPrice(e.getExecutePrice(), s.getScale(), "---")); break;
                case R.string.trading_execution_history_detail_spot: a.setValue(formatAmountEx(e.getSpotPl(), e.getDepositCurrency(), "---")); break;
                case R.string.trading_execution_history_detail_swap: a.setValue(formatAmountEx(e.getSwapPl(), e.getDepositCurrency(), "---")); break;
                case R.string.trading_execution_history_detail_pips_pl: a.setValue(formatPips(adaptable.getPipsPL(), "---")); break;
                case R.string.trading_execution_history_detail_profit: a.setValue(formatAmountEx(adaptable.getProfit(), e.getDepositCurrency(), "---")); break;
                case R.string.trading_execution_history_detail_side: a.setValue(format(e.getSide(), "---")); a.setColor(getColor(e.getSide())); break;
            }
		}
		this.notifyDataSetChanged();
	}
}
