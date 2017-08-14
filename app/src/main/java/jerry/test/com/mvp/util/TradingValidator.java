package jerry.test.com.mvp.util;

import android.content.Context;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.domain.support.Formatters;
import cn.nextop.erebor.mid.app.domain.trading.TradingUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.text.DecimalPicker;
import cn.nextop.erebor.mid.common.glossary.TradingDate;
import cn.nextop.erebor.mid.common.glossary.enums.Side;
import cn.nextop.erebor.mid.common.glossary.enums.TradingExecuteType;
import cn.nextop.erebor.mid.common.util.DateTimes;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.QuoteVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingContractVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.result.AddOrderResult;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.result.DelOrderResult;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.result.SetOrderResult;
import cn.nextop.erebor.mid.rpc.service.protobuf.CustomerServiceProtoV1.SwitchResponse;

import static cn.nextop.erebor.mid.app.domain.support.XResources.getString;
import static cn.nextop.erebor.mid.app.domain.trading.TradingUtils.getClosableVolume;
import static cn.nextop.erebor.mid.app.domain.trading.TradingUtils.getMaxLeavingOffset;
import static cn.nextop.erebor.mid.app.domain.trading.TradingUtils.getMinLeavingOffset;
import static cn.nextop.erebor.mid.app.domain.trading.domain.Validation.OVERFLOW;
import static cn.nextop.erebor.mid.app.domain.trading.domain.Validation.REQUIRED;
import static cn.nextop.erebor.mid.app.domain.trading.domain.Validation.UNDERFLOW;
import static cn.nextop.erebor.mid.common.glossary.enums.Side.BUY;
import static cn.nextop.erebor.mid.common.glossary.enums.Side.SELL;
import static cn.nextop.erebor.mid.common.glossary.enums.TradingExecuteType.LIMIT;
import static cn.nextop.erebor.mid.common.glossary.enums.TradingExecuteType.STOP;
import static cn.nextop.erebor.mid.common.util.Numbers.add;
import static cn.nextop.erebor.mid.common.util.Numbers.gt;
import static cn.nextop.erebor.mid.common.util.Numbers.isZero;
import static cn.nextop.erebor.mid.common.util.Numbers.lt;

/**
 * Created by Jingqi Xu on 9/29/15.
 */
public final class TradingValidator {
    //
    protected static final Map<String, Integer> TRANSLATIONS = new HashMap<>(128);
    static {
        TRANSLATIONS.put("invalid", R.string.error_trading_validation_invalid);
        TRANSLATIONS.put("expired", R.string.error_trading_validation_expired);
        TRANSLATIONS.put("required", R.string.error_trading_validation_required);
        TRANSLATIONS.put("readonly", R.string.error_trading_validation_readonly);
        TRANSLATIONS.put("overflow", R.string.error_trading_validation_overflow);
        TRANSLATIONS.put("underflow", R.string.error_trading_validation_underflow);
        TRANSLATIONS.put("overlength", R.string.error_trading_validation_overlength);
        TRANSLATIONS.put("duplicated", R.string.error_trading_validation_duplicated);
        TRANSLATIONS.put("restricted", R.string.error_trading_validation_restricted);
    }

    //
    protected final Context context;

    /**
     *
     */
    public TradingValidator(Context context) {
        this.context = context;
    }

    /**
     *
     */
    public final String translate(final String validation) {
        final String v = validation.toLowerCase();
        final Integer resId = TRANSLATIONS.get(v);
        return resId == null ? v : getString(resId);
    }

    public final String translate(final AddOrderResult result) {
        if(result == null) return "";
        return Formatters.format(result.getResult(), "");
    }

    public final String translate(final DelOrderResult result) {
        if(result == null) return "";
        return Formatters.format(result.getResult(), "");
    }

    public final String translate(final SetOrderResult result) {
        if(result == null) return "";
        return Formatters.format(result.getResult(), "");
    }

    public String translate(final SwitchResponse.Result result) {
        if(result == null) return "";
        return Formatters.format(result, "");
    }

    /**
     *
     */
    public boolean isSlippageValid(final DecimalPicker picker) {
        BigDecimal value = picker.getValue();
        if(value == null) {
            picker.setError(getString(R.string.error_trading_validation_required)); return false;
        } else if(value.compareTo(BigDecimal.ZERO) < 0) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        }
        return true;
    }

    /**
     *
     */
    public Validation isDeviationValid(BigDecimal value, ProductVo product) {
        //
        if(value == null) return null;
        if(product == null) return REQUIRED;
        if(value.compareTo(product.getMaxLeavingOffset()) > 0) return OVERFLOW;
        else if(value.compareTo(product.getMinLeavingOffset()) < 0) return UNDERFLOW;
        return null;
    }

    /**
     *
     */
    public boolean isDeviationValid(DecimalPicker picker, ProductVo product) {
        //
        BigDecimal value = picker.getValue();
        Validation result = isDeviationValid(value, product);
        if(result == null) return true;
        else if(result == REQUIRED) return false;
        else if(result == OVERFLOW) {
            picker.setError(getString(R.string.error_trading_validation_overflow)); return false;
        }
        else if(result == UNDERFLOW) {
            picker.setError(getString(R.string.error_trading_validation_underflow)); return false;
        }
        return false;
    }

    /**
     *
     */
    public boolean isTriggerTimeValid(TextView picker, String pattern) {
        //
        CharSequence value = picker.getText();
        if(value == null || value.length() == 0) {
            picker.setError(getString(R.string.error_trading_validation_required)); return false;
        }

        //
        Date date = null;
        try {
            date = DateTimes.date(new Date(DateTimes.parse(pattern, value.toString())));
        } catch(Throwable t) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        }

        //
        final Date now = DateTimes.date(new Date());
        if(date.before(now)) {
            picker.setError(getString(R.string.error_trading_validation_underflow)); return false;
        }
        return true;
    }


    /**
     *
     */
    public boolean isExpireDateValid(TextView picker, String pattern) {
        //
        CharSequence value = picker.getText();
        if(value == null || value.length() == 0) {
            picker.setError(getString(R.string.error_trading_validation_required)); return false;
        }

        //
        Date date = null;
        try {
            date = DateTimes.date(new Date(DateTimes.parse(pattern, value.toString())));
        } catch(Throwable t) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        }

        //
        if(!TradingDate.isValid(DateTimes.format("yyyyMMdd", date.getTime()))) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        } else if(date.before(DateTimes.date(new Date()))) {
            picker.setError(getString(R.string.error_trading_validation_underflow)); return false;
        }
        return true;
    }

    /**
     *
     */
    public boolean isVolumeValid(DecimalPicker picker, ProductVo product, BigDecimal volume, TradingContractVo contract) {
        //basic check
        if(product == null) return false;

        //
        BigDecimal value = picker.getValue();
        if(value == null) {
            picker.setError(getString(R.string.error_trading_validation_required)); return false;
        } else if(value.compareTo(BigDecimal.ZERO) <= 0) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        }

        //
        value = value.multiply(product.getContractSize());
        BigDecimal min = TradingUtils.getMinTradingVolume(product);
        BigDecimal max = TradingUtils.getMaxTradingVolume(product);
        if(contract != null) {
            max = add(getClosableVolume(contract), volume); if(value.compareTo(max) == 0) return true;
        }

        //
        if(value.compareTo(min) < 0) {
            picker.setError(getString(R.string.error_trading_validation_underflow)); return false;
        } else if(value.compareTo(max) > 0) {
            picker.setError(getString(R.string.error_trading_validation_overflow)); return false; //
        } else if(!isZero(value.remainder(product.getTradingVolumeStep()))) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false; // Validation of trading volume step has lowest priority
        }
        return true;
    }

    /**
     *
     */
    public boolean isPriceValid(DecimalPicker picker, ProductVo product, Side side, TradingExecuteType type, BigDecimal price) {
        //basic check
        if(product == null) return false;

        //
        BigDecimal value = picker.getValue();
        if(value == null) {
            picker.setError(getString(R.string.error_trading_validation_required)); return false;
        } else if(value.compareTo(BigDecimal.ZERO) <= 0) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        } else if(value.stripTrailingZeros().scale() > product.getScale().intValue()) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        }

        //
        final BigDecimal min = getMinLeavingOffset(product);
        final BigDecimal max = getMaxLeavingOffset(product);
        if(type == STOP && side == BUY && gt(value, price.add(max))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}
        if(type == LIMIT && side == SELL && gt(value, price.add(max))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}
        if(type == STOP && side == SELL && gt(value, price.subtract(min))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}
        if(type == LIMIT && side == BUY && gt(value, price.subtract(min))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}

        //
        if(type == STOP && side == BUY && lt(value, price.add(min))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        if(type == LIMIT && side == SELL && lt(value, price.add(min))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        if(type == STOP && side == SELL && lt(value, price.subtract(max))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        if(type == LIMIT && side == BUY && lt(value, price.subtract(max))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        return true;
    }

    /**
     *
     */
    public boolean isPriceValid(DecimalPicker picker, ProductVo product, Side side, TradingExecuteType type, QuoteVo quote) {
        //basic check
        if(quote == null) return false;
        if(product == null) return false;

        //
        BigDecimal value = picker.getValue();
        if(value == null) {
            picker.setError(getString(R.string.error_trading_validation_required)); return false;
        } else if(value.compareTo(BigDecimal.ZERO) <= 0) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        } else if(value.stripTrailingZeros().scale() > product.getScale().intValue()) {
            picker.setError(getString(R.string.error_trading_validation_invalid)); return false;
        }

        //
        final BigDecimal min = getMinLeavingOffset(product);
        final BigDecimal max = getMaxLeavingOffset(product);
        final BigDecimal price = side == BUY ? quote.getAskPrice() : quote.getBidPrice();
        if(type == STOP && side == BUY && gt(value, price.add(max))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}
        if(type == LIMIT && side == SELL && gt(value, price.add(max))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}
        if(type == STOP && side == SELL && gt(value, price.subtract(min))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}
        if(type == LIMIT && side == BUY && gt(value, price.subtract(min))) { picker.setError(getString(R.string.error_trading_validation_overflow)); return false;}

        //
        if(type == STOP && side == BUY && lt(value, price.add(min))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        if(type == LIMIT && side == SELL && lt(value, price.add(min))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        if(type == STOP && side == SELL && lt(value, price.subtract(max))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        if(type == LIMIT && side == BUY && lt(value, price.subtract(max))) { picker.setError(getString(R.string.error_trading_validation_underflow)); return false;}
        return true;
    }
}
