package simplesound.pcm;

import java.util.Iterator;

public class IterableFrameReader implements Iterable<DoubleDataFrame> {

    private final PcmMonoInputStream pmis;
    private final int frameSize;
    private final int shiftAmount;

    public IterableFrameReader(PcmMonoInputStream pmis, int frameSize) {
        this.pmis = pmis;
        this.frameSize = frameSize;
        this.shiftAmount = frameSize;
    }

    public IterableFrameReader(PcmMonoInputStream pmis, int frameSize, int shiftAmount) {
        this.pmis = pmis;
        this.frameSize = frameSize;
        this.shiftAmount = shiftAmount;
    }

    public Iterator<DoubleDataFrame> iterator() {
        return new NormalizedFrameIterator(pmis, frameSize, shiftAmount);
    }
}
