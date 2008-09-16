/**
 * 
 */
package org.jvoicexml.jsapi2.jse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private ByteArrayOutputStream buffer;
    
    private Clip clip;
    
    public ClipOutputStream() {
        buffer = new ByteArrayOutputStream();
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        byte[] bytes = buffer.toByteArray();
        System.out.println("*** flush " + bytes.length);
        InputStream input = new ByteArrayInputStream(bytes);
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(input);
            clip.open(in);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.flush();
    }

}
