package jerry.test.com.mvp.future.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.nextop.erebor.mid.common.glossary.Mapper;
import cn.nextop.erebor.mid.common.util.concurrent.future.CompleteFutureEx;
import cn.nextop.erebor.mid.common.util.concurrent.future.FutureEx;

/**
 * Created by Jingqi Xu on 7/23/15.
 */
public class XCompleteMappedFutureEx<T> implements CompleteFutureEx {
    //
    private final FutureEx<T> future;
    private final Mapper<T, Void> mapper;

    /**
     *
     */
    public XCompleteMappedFutureEx(FutureEx<T> future) {
        this.future = future; this.mapper = null;
    }

    public XCompleteMappedFutureEx(FutureEx<T> future, Mapper<T, Void> mapper) {
        this.future = future; this.mapper = mapper;
    }

    /**
     *
     */
    @Override
    public boolean isDone() {
        return this.future.isDone();
    }

    @Override
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override
    public <V> V getCookie() {
        return this.future.getCookie();
    }

    @Override
    public FutureEx<Void> setCookie(Object cookie) {
        this.future.setCookie(cookie); return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.future.cancel(mayInterruptIfRunning);
    }
    
    @Override
    public Listener<Void> setListener(final Listener<Void> listener) {
        this.future.setListener(new FutureEx.Listener<T>() {
            @Override
            public void onComplete(FutureEx<T> future) {
                listener.onComplete(XCompleteMappedFutureEx.this);
            }
        }); return null;
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException {
        T t = this.future.get(); return mapper == null ? null : mapper.map(t);
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        T t = this.future.get(timeout, unit); return mapper == null ? null : mapper.map(t);
    }
}
