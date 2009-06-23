package simplesound.pcm;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class AudioFormatBuilder {

    private enum Channel {
        MONO(1), STEREO(2), NOT_SPECIFIED(0);

        public int audioChannels;

        Channel(int i) {
            this.audioChannels = i;
        }
    }

    public static final AudioFormat PCM_SIGNED_8_KHZ_16_BIT_MONO_BIG_ENDIAN =
            new AudioFormatBuilder().pcmSigned().sampleSizeInBits(16).sampleRate(8000.0f).mono().bigEndian().build();

    public static final AudioFormat PCM_SIGNED_8_KHZ_16_BIT_MONO_LITTLE_ENDIAN =
            new AudioFormatBuilder().pcmSigned().sampleSizeInBits(16).sampleRate(8000.0f).mono().littleEndian().build();

    public static final AudioFormat PCM_SIGNED_44_KHZ_16_BIT_STEREO_LITTLE_ENDIAN =
            new AudioFormatBuilder().pcmSigned().sampleSizeInBits(16).sampleRate(44100.0f).stereo().littleEndian().build();

    /**
     * The audio encoding technique used by this format.
     */
    private AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;

    /**
     * The number of samples played or recorded per second, for sounds that have this format.
     */
    private float sampleRate = 8000.0f;

    /**
     * The number of bits in each sample of a sound that has this format.
     */
    private int sampleSizeInBits = AudioSystem.NOT_SPECIFIED;

    /**
     * The number of audio channels in this format (1 for mono, 2 for stereo).
     */
    private Channel channel = Channel.NOT_SPECIFIED;

    /**
     * Indicates whether the audio data is stored in big-endian or little-endian order.
     */
    private boolean bigEndian = true;

    public AudioFormatBuilder pcmSigned() {
        this.encoding = AudioFormat.Encoding.PCM_SIGNED;
        return this;
    }

    public AudioFormatBuilder pcmUnsigned() {
        this.encoding = AudioFormat.Encoding.PCM_UNSIGNED;
        return this;
    }

    public AudioFormatBuilder aLaw() {
        this.encoding = AudioFormat.Encoding.ALAW;
        return this;
    }

    public AudioFormatBuilder uLaw() {
        this.encoding = AudioFormat.Encoding.ULAW;
        return this;
    }

    public AudioFormatBuilder sampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public AudioFormatBuilder sampleSizeInBits(int sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
        return this;
    }

    public AudioFormatBuilder mono() {
        this.channel = Channel.MONO;
        return this;
    }

    public AudioFormatBuilder stereo() {
        this.channel = Channel.STEREO;
        return this;
    }

    public AudioFormatBuilder bigEndian() {
        this.bigEndian = true;
        return this;
    }

    public AudioFormatBuilder littleEndian() {
        this.bigEndian = false;
        return this;
    }

    public AudioFormat build() {
        boolean signed = encoding.equals(AudioFormat.Encoding.PCM_SIGNED);
        return new AudioFormat(sampleRate, sampleSizeInBits, channel.audioChannels, signed, bigEndian);
    }


}
