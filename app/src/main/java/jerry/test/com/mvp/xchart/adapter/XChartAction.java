package jerry.test.com.mvp.xchart.adapter;


public interface XChartAction {

    /**
     *
     */
    Type getType();

    boolean isSetup();

    boolean isReadonly();

    boolean apply(XChartContext context);

    XChartAction merge(XChartAction action);

    /**
     *
     */
    enum Type { MOVE, ZOOM, SETUP, RESET, SELECT, RESIZE, REFRESH,ACTIVATE,PICK,REMOVELINE; }
}
