package simplesound.pcm;

public class DoubleDataFrame {

    final double[] data;

    public DoubleDataFrame(double[] data) {
        if (data == null)
            throw new IllegalArgumentException("Data cannot be null!");
        this.data = data;
    }

    public int size() {
        return data.length;
    }

    public double[] getData() {
        return data;
    }
}
