package jerry.test.com.mvp.future.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.nextop.erebor.mid.common.glossary.Mapper;
import cn.nextop.erebor.mid.common.util.concurrent.future.FutureEx;

/**
 * Created by Jingqi Xu on 7/23/15.
 */
public class XMappedFutureEx<S, T> implements FutureEx<T> {
    //
    private final FutureEx<S> future;
    private final Mapper<S, T> mapper;

    /**
     *
     */
    public XMappedFutureEx(FutureEx<S> future, Mapper<S, T> mapper) {
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
    public FutureEx<T> setCookie(Object cookie) {
        this.future.setCookie(cookie); return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.future.cancel(mayInterruptIfRunning);
    }

    @Override
    public Listener<T> setListener(final Listener<T> listener) {
        this.future.setListener(new FutureEx.Listener<S>() {
            @Override
            public void onComplete(FutureEx<S> future) {
                listener.onComplete(XMappedFutureEx.this);
            }
        }); return null;
    }


    @Override
    public T get() throws InterruptedException, ExecutionException {
        return this.mapper.map(this.future.get());
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mapper.map(this.future.get(timeout, unit));
    }
}
