package simplesound.pcm;

import junit.framework.Assert;
import org.jcaki.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class IterableFrameReaderTest {

    @Test
    public void testIterableFrameReader() throws IOException {
        PcmMonoInputStream pmis = new MonoWavFileReader("wav-samples/square-0_8amp-440hz-1000sample-16bit-mono.wav").getNewStream();
        int frameCounter = 0;
        for (DoubleDataFrame ddf : new IterableFrameReader(pmis, 100, 50)) {
            frameCounter++;
            Assert.assertEquals(ddf.size(), 100);
            for (double d : ddf.getData()) {
                Assert.assertTrue("oops:" + d, d <= 0.8d);
            }
        }
        Assert.assertEquals(frameCounter, (1000 - 100) / 50 + 1);
    }

    public static void main(String[] args) throws IOException {
        Files.hexDump(new File("wav-samples/square-0_8amp-440hz-1000sample-16bit-mono.wav"),
                new File("dump.txt"),
                1000);
    }


}
