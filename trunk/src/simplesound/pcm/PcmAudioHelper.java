package simplesound.pcm;

import java.io.*;

public class PcmAudioHelper {

    /**
     * Converts a pcm encoded raw audio stream to a wav file.
     * @param af
     * @param rawSource
     * @param wavTarget
     * @throws IOException
     */
    public static void convertRawToWav(PcmAudioFormat af, File rawSource, File wavTarget) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(wavTarget));
        dos.write(new RiffHeaderData(af, 0).asByteArray());
        DataInputStream dis = new DataInputStream(new FileInputStream(rawSource));
        byte[] buffer = new byte[4096];
        int i;
        int total = 0;
        while ((i = dis.read(buffer)) != -1) {
            total += i;
            dos.write(buffer, 0, i);
        }
        dos.close();
        RiffHeaderData.modifyRiffSizeData(wavTarget, total);
    }

}
