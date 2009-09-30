package simplesound.pcm;

import java.io.IOException;
import java.util.Iterator;

public class NormalizedFrameIterator implements Iterator<DoubleDataFrame> {

    private final PcmMonoInputStream pmis;
    private final int frameSize;
    private final int shiftAmount;

    public NormalizedFrameIterator(PcmMonoInputStream pmis, int frameSize, int shiftAmount) {
        if (frameSize < 1)
            throw new IllegalArgumentException("Frame size must be larger than zero.");
        if (shiftAmount < 1)
            throw new IllegalArgumentException("Shift size must be larger than zero.");
        this.pmis = pmis;
        this.frameSize = frameSize;
        this.shiftAmount = shiftAmount;
    }

    public NormalizedFrameIterator(PcmMonoInputStream pmis, int frameSize) {
        if (frameSize < 1)
            throw new IllegalArgumentException("Frame size must be larger than zero.");
        this.pmis = pmis;
        this.frameSize = frameSize;
        this.shiftAmount = frameSize;
    }

    private DoubleDataFrame currentFrame;
    private int frameCounter;

    public boolean hasNext() {
        double[] data;
        try {
            if (frameCounter == 0) {
                data = pmis.readSamplesNormalized(frameSize);
                if (data.length < frameSize)
                    return false;
                currentFrame = new DoubleDataFrame(data);
            } else {
                data = pmis.readSamplesNormalized(shiftAmount);
                if (data.length < shiftAmount)
                    return false;
                double[] frameData = currentFrame.data.clone();
                System.arraycopy(data, 0, frameData, frameData.length - shiftAmount, shiftAmount);
                currentFrame = new DoubleDataFrame(frameData);
            }
        } catch (IOException e) {
            return false;
        }
        frameCounter++;
        return true;
    }

    public DoubleDataFrame next() {
        return currentFrame;
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported.");
    }


}
