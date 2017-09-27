package jerry.test.com.mvp.xchart.model;

import cn.nextop.erebor.mid.common.util.Strings;

public class XChartMargin {
    //
    private int size;
    private int capacity;

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("size", size)
        .append("capacity", capacity)
        .toString();
    }

    /**
     *
     */
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     *
     */
    public void resize(final int capacity) {
        if(capacity < this.capacity) return;
        this.size = this.capacity = capacity;
    }

    public final int move(final int delta) {
        if(delta < 0) {
            int x = Math.min(-delta, size);
            this.size = this.size - x; return delta + x;
        } else {
            int x = Math.min(delta, capacity - size);
            this.size = this.size + x; return delta - x;
        }
    }
}
