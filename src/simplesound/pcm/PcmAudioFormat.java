package simplesound.pcm;

/**
 * Represents paramters for raw pcm audio sample data.
 * Channels represents mono or stereo data. mono=1, stereo=2
 */
public class PcmAudioFormat {

    private final int sampleRate;
    private final int sampleSizeInBits;
    private final int bytesRequiredPerSample;
    private final int channels;
    private final boolean bigEndian;
    private final boolean signed;

    private PcmAudioFormat(int sampleRate, int sampleSizeInBits, int channels, boolean bigEndian, boolean signed) {

        if (sampleRate < 1)
            throw new IllegalArgumentException("sampleRate cannot be less than one. But it is:" + sampleRate);
        this.sampleRate = sampleRate;

        if (sampleSizeInBits < 2 || sampleSizeInBits > 31) {
            throw new IllegalArgumentException("sampleSizeInBits must be between (including) 2-31. But it is:" + sampleSizeInBits);
        }
        this.sampleSizeInBits = sampleSizeInBits;

        if (channels < 1 || channels > 2) {
            throw new IllegalArgumentException("channels must be 1 or 2. But it is:" + channels);
        }
        this.channels = channels;

        this.bigEndian = bigEndian;
        this.signed = signed;
        if (sampleSizeInBits % 8 == 0)
            bytesRequiredPerSample = sampleSizeInBits / 8;
        else
            bytesRequiredPerSample = sampleSizeInBits / 8 + 1;
    }

    /**
     * This is a builder class. By default it generates little endian, signed.
     */
    public static class Builder {
        private int _sampleRate;
        private int _sampleSizeInBits;
        private int _channels = 1;
        private boolean _bigEndian = false;
        private boolean _signed = true;

        public Builder sampleRate(int sampleRate) {
            this._sampleRate = sampleRate;
            return this;
        }

        public Builder channels(int channels) {
            this._channels = channels;
            return this;
        }

        public Builder bigEndian() {
            this._bigEndian = true;
            return this;
        }

        public Builder unsigned() {
            this._signed = false;
            return this;
        }

        public Builder sampleSizeInBits(int sampleSizeInBits) {
            this._sampleSizeInBits = sampleSizeInBits;
            return this;
        }

        public PcmAudioFormat build() {
            return new PcmAudioFormat(_sampleRate, _sampleSizeInBits, _channels, _bigEndian, _signed);
        }
    }

    public static class WavFormatBuilder {
        private int _sampleRate;
        private int _sampleSizeInBits;
        private int _channels = 1;

        public WavFormatBuilder sampleRate(int sampleRate) {
            this._sampleRate = sampleRate;
            return this;
        }

        public WavFormatBuilder channels(int channels) {
            this._channels = channels;
            return this;
        }

        public WavFormatBuilder sampleSizeInBits(int sampleSizeInBits) {
            this._sampleSizeInBits = sampleSizeInBits;
            return this;
        }

        public PcmAudioFormat build() {
            if (_sampleSizeInBits == 8)
                return new PcmAudioFormat(_sampleRate, _sampleSizeInBits, _channels, false, false);
            else
                return new PcmAudioFormat(_sampleRate, _sampleSizeInBits, _channels, false, true);
        }
    }

    public static PcmAudioFormat wavMono16Bit(int sampleRate) {
        return new PcmAudioFormat(sampleRate, 16, 1, false, true);
    }

    /**
     * Generates audio format data for Wav audio format. returning PCM format is little endian. 
     *
     * @param sampleRate       sample rate
     * @param sampleSizeInBits bit amount per sample
     * @param channels         channel count. can be 1 or 2
     * @return a RawAudioFormat suitable for wav format.
     */
    public static PcmAudioFormat wavFormat(int sampleRate, int sampleSizeInBits, int channels) {
        if (sampleSizeInBits == 8)
            return new PcmAudioFormat(sampleRate, sampleSizeInBits, channels, false, false);
        else
            return new PcmAudioFormat(sampleRate, sampleSizeInBits, channels, false, true);
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    /**
     * returns the required bytes for the sample bit size. Such that, if 4 or 8 bit samples are used.
     * it returns 1, if 12 bit used 2 returns.
     *
     * @return required byte amount for the sample size in bits.
     */
    public int getBytePerSample() {
        return bytesRequiredPerSample;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

    /**
     * finds the byte location of a given time. if time is negative, exception is thrown.
     *
     * @param second second information
     * @return the byte location in the samples.
     */
    public int findSampleLocation(double second) {

        if (second < 0)
            throw new IllegalArgumentException("Time information cannot be negative.");

        int loc = (int) (second * sampleRate * bytesRequiredPerSample);

        if (loc % bytesRequiredPerSample != 0) {
            loc += (bytesRequiredPerSample - loc % bytesRequiredPerSample);
        }
        return loc;
    }

    public boolean isSigned() {
        return signed;
    }

    public String toString() {
        return "[ Sample Rate:" + sampleRate + " , SampleSizeInBits:" + sampleSizeInBits + ", channels:" + channels + " ]";
    }
}
