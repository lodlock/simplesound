package simplesound.pcm;

import junit.framework.Assert;
import org.jcaki.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class IterableFrameReaderTest {

    public static final double EPSILON = 0.01;

    @Test
    public void testIterableFrameReader() throws IOException {
        PcmMonoInputStream pmis = new MonoWavFileReader("wav-samples/square-0_8amp-440hz-1000sample-16bit-mono.wav").getNewStream();
        int frameCounter = 0;
        for (DoubleDataFrame ddf : new IterableFrameReader(pmis, 100, 50)) {
            frameCounter++;
            Assert.assertEquals(ddf.size(), 100);
            for (double d : ddf.getData()) {
                Assert.assertTrue("oops:" + d, d <= (0.8d + EPSILON) && d > (-0.8d - EPSILON));
            }
        }
        Assert.assertEquals(frameCounter, (1000 - 100) / 50 + 1);
        pmis.close();
    }
}
