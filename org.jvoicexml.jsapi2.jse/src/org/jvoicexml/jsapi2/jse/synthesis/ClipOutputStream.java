/**
 * 
 */
package org.jvoicexml.jsapi2.jse.synthesis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

/**
 * @author DS01191
 *
 */
public class ClipOutputStream extends OutputStream {
    private ByteArrayOutputStream buffer;

    private Clip clip;
    
    public ClipOutputStream() {
        buffer = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) throws IOException {
        buffer.write(b);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        try {
            AudioFormat format = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    16000,
                    16,
                    1,
                    2,
                    16000,
                    false);
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            clip = (Clip) AudioSystem.getLine(info);
            byte[] bytes = buffer.toByteArray();
            clip.open(format, bytes, 0, bytes.length);
            clip.start();
            while (clip.isRunning()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
