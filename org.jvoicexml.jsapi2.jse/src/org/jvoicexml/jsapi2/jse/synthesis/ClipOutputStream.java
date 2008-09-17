/**
 * 
 */
package org.jvoicexml.jsapi2.jse.synthesis;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author DS01191
 *
 */
public class ClipOutputStream extends OutputStream {
    private PipedOutputStream out;
    private PipedInputStream inPipe;
    private AudioInputStream in;

    private Clip clip;
    
    public ClipOutputStream() {
        out = new PipedOutputStream();
        try {
            inPipe = new PipedInputStream(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        try {
            if (in == null) {
                clip = AudioSystem.getClip();
                in = AudioSystem.getAudioInputStream(inPipe);
            }
            clip.open(in);
            clip.start();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.flush();
    }

}
