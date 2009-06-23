package simplesound.pcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WavFileReader {

    private final File file;
    private final RiffHeaderData riffHeaderData;

    public WavFileReader(File file) throws IOException {
        this.file = file;
        riffHeaderData = new RiffHeaderData(file);
    }

    public PcmAudioInputStream getStream() throws IOException {
        PcmAudioInputStream asis = new PcmAudioInputStream(
                riffHeaderData.getFormat(),
                new FileInputStream(file));
        long amount = asis.skip(RiffHeaderData.PCM_RIFF_HEADER_SIZE);
        if (amount < RiffHeaderData.PCM_RIFF_HEADER_SIZE)
            throw new IllegalArgumentException("cannot skip necessary amount of bytes from underlying stream.");
        return asis;
    }

    public short[] getSamplesAsShorts(int frameStart, int frameEnd) throws IOException {
        PcmAudioInputStream stream = getStream();
        try {
            stream.skipSamples(frameStart);
            return stream.readSamplesShortArray(frameEnd - frameStart);
        } finally {
            stream.close();
        }
    }

    public byte[] getSamplesAsBytes(int frameStart, int frameEnd) throws IOException {
        PcmAudioInputStream stream = getStream();
        try {
            stream.skipSamples(frameStart);
            return stream.readSamplesByteArray(frameEnd - frameStart);
        } finally {
            stream.close();
        }
    }

    public int[] getAllSamples() throws IOException {
        PcmAudioInputStream stream = getStream();
        try {
            return stream.readAll();
        } finally {
            stream.close();
        }
    }

    public int[] getSamplesAsInts(int frameStart, int frameEnd) throws IOException {
        PcmAudioInputStream stream = getStream();
        try {
            stream.skipSamples(frameStart);
            return stream.readSamplesAsIntArray(frameEnd - frameStart);
        } finally {
            stream.close();
        }
    }

    public int[] getSamplesSecondsInterval(double startSecond, double endSecond) throws IOException {
        if (endSecond < startSecond)
            throw new IllegalArgumentException("End cannot be earlier than start. Start:" + startSecond +
                    " End:" + endSecond);
        int startLoc = riffHeaderData.findSampleLocation(startSecond);
        int endLoc = riffHeaderData.findSampleLocation(endSecond);

        if (startLoc == -1 || endLoc == -1)
            throw new IllegalArgumentException("One or two parameter is out of the sample limit. Start:"
                    + startLoc +
                    " End:" + endLoc);
        return getSamplesAsInts(startLoc, endLoc);
    }

    public RiffHeaderData getHeader() {
        return riffHeaderData;
    }

    public File getFile() {
        return file;
    }
}
