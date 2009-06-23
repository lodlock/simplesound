package simplesound.pcm;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

class IOs {
    /**
     * closes the <code>closeables</code> silently, meaning that if the Closeable is null,
     * or if it throws an exception during close() operation it only creates a system error output,
     * does not throw an exception.
     * this is especially useful when you need to close one or more resources in finally blocks.
     * This method should only be called in finalize{} blocks or wherever it really makes sense.
     *
     * @param closeables zero or more closeable.
     */
    public static void closeSilently(Closeable... closeables) {
        // if closeables is null, return silently.
        if (closeables == null) return;

        for (Closeable closeable : closeables) {
            try {
                if (closeable != null)
                    closeable.close();
            }
            catch (IOException e) {
                System.err.println("IO Exception during closing stream (" + closeable + ")." + e);
            }
        }
    }

    /**
     * converts an input stream data to byte array. careful with memory usage here. 
     * @param is , an input stream
     * @return a byte array representing the stream data.
     * @throws IOException          if an error occurs during the read or write of the streams.
     * @throws NullPointerException if input stream is null
     */
    public static byte[] readAsByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (is == null)
                throw new NullPointerException("input stream cannot be null.");
            int b;
            byte[] buffer = new byte[4096];
            while ((b = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, b);
            }
            return baos.toByteArray();
        } finally {
            closeSilently(is, baos);
        }
    }
}
