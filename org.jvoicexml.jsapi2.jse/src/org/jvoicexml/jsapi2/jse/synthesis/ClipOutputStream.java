/**
 * 
 */
package org.jvoicexml.jsapi2.jse.synthesis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * @author DS01191
 *
 */
public class ClipOutputStream extends OutputStream implements LineListener {
    private ByteArrayOutputStream buffer;

    private Clip clip;

    /** Synchronization of start and end play back. */
    private final Semaphore sem;

    public ClipOutputStream() {
        buffer = new ByteArrayOutputStream();
        sem = new Semaphore(1);
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


            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            clip = (Clip) AudioSystem.getLine(info);
            byte[] bytes = buffer.toByteArray();
            clip.open(format, bytes, 0, bytes.length);
            clip.addLineListener(this);
            clip.start();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            sem.acquire();
            sem.release();
        } catch (InterruptedException e) {
            return;
        }
    }

    @Override
    public void update(LineEvent event) {
        if ((event.getType() == LineEvent.Type.CLOSE)
                || (event.getType() == LineEvent.Type.STOP)) {
            sem.release();
        }
    }

}
