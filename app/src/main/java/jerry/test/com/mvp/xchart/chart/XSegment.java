package jerry.test.com.mvp.xchart.chart;

/**
 * Created by wanggl on 2016/12/1.
 */
public interface XSegment {
    enum SegmentType{
        kTypeReal,
        kTypeVirtual,
        kTypeInfiniteH,
        kTypeInfiniteV,
    };
    enum SegmentExtendable{
        kNone,          // No extension line
        kBothSides,     // Both sides with extension line
        kForward,       // From start to end
        kReverse,       // From end to start
    };
}
