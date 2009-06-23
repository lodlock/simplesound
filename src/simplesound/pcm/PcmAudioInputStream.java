package simplesound.pcm;

import java.io.*;

public class PcmAudioInputStream extends InputStream implements Closeable {

    final PcmAudioFormat format;
    final DataInputStream dis;

    public PcmAudioInputStream(PcmAudioFormat format, InputStream is) {
        this.format = format;
        this.dis = new DataInputStream(is);
    }

    public int read() throws IOException {
        return dis.read();
    }

    public long skip(long size) throws IOException {
        return dis.skip(size);
    }

    public int[] readSamplesAsIntArray(int amount) throws IOException {
        byte[] bytez = new byte[amount * format.getBytePerSample()];
        int readAmount = dis.read(bytez);
        return Bytes.toIntArray(bytez, readAmount, format.getBytePerSample(), format.isBigEndian());
    }

    public int[] readAll() throws IOException {
        byte[] all = IOs.readAsByteArray(dis);
        return Bytes.toIntArray(all, all.length, format.getBytePerSample(), format.isBigEndian());
    }

    public byte[] readSamplesByteArray(int amount) throws IOException {
        byte[] bytez = new byte[amount * format.getBytePerSample()];
        int readCount = dis.read(bytez);
        if (readCount != bytez.length) {
            byte[] result = new byte[readCount];
            System.arraycopy(bytez, 0, result, 0, readCount);
            return result;
        } else
            return bytez;
    }

    public int[] readSamplesAsIntArray(int frameStart, int frameEnd) throws IOException {
        skipSamples(frameStart * format.getBytePerSample());
        return readSamplesAsIntArray(frameEnd - frameStart);
    }

    public int skipSamples(int skipAmount) throws IOException {
        long actualSkipped = dis.skip(skipAmount * format.getBytePerSample());
        return (int) actualSkipped / format.getBytePerSample();
    }

    public double[] readSamplesNormalized(int amount) throws IOException {
        int[] original = readSamplesAsIntArray(amount);
        double[] normalized = new double[original.length];
        int max = 0x7fffffff >>> (31 - format.getSampleSizeInBits());
        for (int i = 0; i < normalized.length; i++) {
            normalized[i] = max / original[i];
        }
        return normalized;
    }

    public double[] readSamplesNormalized() throws IOException {
        int[] original = readAll();
        double[] normalized = new double[original.length];
        int max = 0x7fffffff >>> (31 - format.getSampleSizeInBits());
        for (int i = 0; i < normalized.length; i++) {
            normalized[i] = max / original[i];
        }
        return normalized;
    }

    public void close() throws IOException {
        dis.close();
    }

    public short[] readSamplesShortArray(int amount) throws IOException {
        byte[] bytez = readSamplesByteArray(amount);
        return Bytes.toShortArray(bytez, bytez.length, format.isBigEndian());
    }

}
