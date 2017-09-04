package jerry.test.com.mvp.xchart.render;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public interface XChartArea {

    /**
     *
     */
    enum Type { A, B, X, AY, BY; }

    /**
     *
     */
    boolean isVisible();

    boolean containsX(float x);

    boolean containsY(float y);

    boolean contains(float x, float y);

    float getCenterX(); float getCenterY();

    float getMarginX(); float getMarginY();

    float getNetWidth(); float getNetHeight();

    void setMarginX(float margin); void setMarginY(float margin);

    float getX(); float getY(); float getWidth(); float getHeight();

    float getLeft(); float getTop(); float getRight(); float getBottom();
}
