/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2008 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.jse.synthesis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.speech.AudioSegment;
import javax.speech.EngineMode;
import javax.speech.synthesis.PhoneInfo;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerEvent;

import org.jvoicexml.jsapi2.jse.BaseAudioManager;
import org.jvoicexml.jsapi2.jse.BaseEngine;

/**
 * The {@link QueueManager} basically accepts the speech segments to 
 * synthesized, appends them to a corresponding queue and hands them to the
 * synthesizer to convert those pieces into audio chunks. These chunks are added
 * to the play queue to be delivered via the configured media locator.
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class QueueManager implements Runnable {

    private BaseSynthesizer synthesizer;
    private Thread synthThread;
    private Thread playThread;
    private boolean done;
    private Vector<QueueItem> queue;
    private Vector<QueueItem> playQueue;
    private int queueID;
    private Boolean cancelFirstItem;

    public QueueManager(BaseSynthesizer synthesizer) {
        this.synthesizer = synthesizer;
        queue = new Vector<QueueItem>();
        playQueue = new Vector<QueueItem>();
        queueID = 0;
        cancelFirstItem = false;

        final String name;
        final EngineMode mode = synthesizer.getEngineMode();
        if (mode == null) {
            name = "unnamed";
        } else {
            name = mode.getEngineName();
        }
        synthThread = new Thread(this, "QueueManager_synthesizer_"  + name);
        synthThread.setDaemon(true);
        Runnable playRunnable = new Runnable() {
            public void run() {
                playItems();
            }
        };
        playThread = new Thread(playRunnable, "QueueManager_play_" + name);
        playThread.setDaemon(true);

        synthThread.start();
        playThread.start();
    }

    public void terminate() {
        synchronized (queue) {
            done = true;
            queue.notify();
        }
    }

    /**
     * Add an item to be spoken to the output queue. Fires the appropriate queue
     * events
     *
     * @param item
     *                the item to add to the queue
     */
    public int appendItem(Speakable speakable, SpeakableListener listener) {
        boolean topOfQueueChanged;
        synchronized (queue) {
            queueID += 1;
            QueueItem item = new QueueItem(queueID, speakable, listener);

            topOfQueueChanged = isQueueEmpty();
            queue.addElement(item);
            queue.notifyAll();
        }

        long[] states = synthesizer.setEngineState(
                topOfQueueChanged ? Synthesizer.QUEUE_EMPTY
                        : Synthesizer.QUEUE_NOT_EMPTY,
                Synthesizer.QUEUE_NOT_EMPTY);
        synthesizer.postSynthesizerEvent(states[0], states[1],
                SynthesizerEvent.QUEUE_UPDATED, topOfQueueChanged);

        return queueID;
    }

    /**
     * Add an item to be spoken to the output queue. Fires the appropriate queue
     * events
     *
     */
    public int appendItem(Speakable speakable, SpeakableListener listener,
            String text) {
        boolean topOfQueueChanged;
        synchronized (queue) {
            queueID += 1;
            QueueItem item = new QueueItem(queueID, speakable, listener, text);

            topOfQueueChanged = isQueueEmpty();
            queue.addElement(item);
            queue.notifyAll();
        }

        long[] states = synthesizer.setEngineState(
                topOfQueueChanged ? Synthesizer.QUEUE_EMPTY
                        : Synthesizer.QUEUE_NOT_EMPTY,
                Synthesizer.QUEUE_NOT_EMPTY);
        synthesizer.postSynthesizerEvent(states[0], states[1],
                SynthesizerEvent.QUEUE_UPDATED, topOfQueueChanged);

        return queueID;
    }

    /**
     * Add an item to be spoken to the output queue. Fires the appropriate queue
     * events
     *
     * @param item
     *                the item to add to the queue
     */
    public int appendItem(AudioSegment audioSegment, SpeakableListener listener) {
        boolean topOfQueueChanged;
        synchronized (queue) {
            ++queueID;
            final QueueItem item = new QueueItem(queueID, audioSegment, listener);

            topOfQueueChanged = isQueueEmpty();
            queue.addElement(item);
            queue.notifyAll();
        }

        long[] states = synthesizer.setEngineState(
                topOfQueueChanged ? Synthesizer.QUEUE_EMPTY
                        : Synthesizer.QUEUE_NOT_EMPTY,
                Synthesizer.QUEUE_NOT_EMPTY);
        synthesizer.postSynthesizerEvent(states[0], states[1],
                SynthesizerEvent.QUEUE_UPDATED, topOfQueueChanged);

        return queueID;
    }

    private void playItems() {
        final int BUFFER_LENGTH = 1024;

        QueueItem item;
        int playIndex = 0;
        int wordIndex = 0;
        int wordStart = 0;
        int phonemeIndex = 0;
        double timeNextPhone = 0;
        long nextTimeStamp = 0;

        // AudioFormat audioFormat =
        // ((BaseAudioManager)synthesizer.getAudioManager()).getEngineAudioFormat();
        AudioFormat audioFormat = null;
        float sampleRate;
        final byte[] buffer = new byte[BUFFER_LENGTH];

        while (!done) {
            item = getQueueItemToPlay();

            //Update audio descriptor
            final BaseAudioManager manager =
                (BaseAudioManager) synthesizer.getAudioManager();
            audioFormat = manager.getTargetAudioFormat();
            sampleRate = audioFormat.getSampleRate();

            final Object source = item.getSource();
            final int id = item.getId();
            synthesizer.postSpeakableEvent(new SpeakableEvent(source,
                    SpeakableEvent.TOP_OF_QUEUE, id), item.getListener());

            while (synthesizer.testEngineState(Synthesizer.PAUSED)) {
                synthesizer.postSpeakableEvent(
                        new SpeakableEvent(
                                source, SpeakableEvent.SPEAKABLE_PAUSED, id),
                                item.getListener());

                try {
                    synthesizer.waitEngineState(BaseEngine.RESUMED);
                    synthesizer.postSpeakableEvent(new SpeakableEvent(source,
                            SpeakableEvent.SPEAKABLE_RESUMED,
                            id), item.getListener());
                } catch (InterruptedException ex1) {
                    return;
                }
            }

            //long startStreaming = System.currentTimeMillis();
            long totalBytesRead = 0;
            synthesizer.postSpeakableEvent(new SpeakableEvent(item.getSource(),
                    SpeakableEvent.SPEAKABLE_STARTED, item.getId()), item
                    .getListener());

            playIndex = 0;
            wordIndex = 0;
            wordStart = 0;
            phonemeIndex = 0;
            timeNextPhone = 0;
            int bytesRead = 0;

            long bps = audioFormat.getChannels();
            bps *= audioFormat.getSampleRate();
            bps *= (audioFormat.getSampleSizeInBits() / 8);

            try {
                while (true) {
                    final AudioSegment segment = item.getAudioSegment();
                    if ((segment == null) || !segment.isGettable()) {
                        throw new SecurityException(
                            "The platform does not allow to access the input "
                                + "stream!");
                    }
                    final InputStream inputStream = segment.openInputStream();
                    bytesRead = inputStream.read(buffer);
                    if (bytesRead < 0)  {
                        break;
                    }

                    totalBytesRead += bytesRead;
                    synchronized (cancelFirstItem) {
                        if (cancelFirstItem) {
                            synthesizer.postSpeakableEvent(new SpeakableEvent(
                                    item.getSource(),
                                    SpeakableEvent.SPEAKABLE_CANCELLED, item
                                            .getId()), item.getListener());
                            break;
                        }
                    }

                    while (synthesizer.testEngineState(Synthesizer.PAUSED)) {
                        synthesizer.postSpeakableEvent(new SpeakableEvent(item
                                .getSource(), SpeakableEvent.SPEAKABLE_PAUSED,
                                item.getId()), item.getListener());
                        try {
                            synthesizer.waitEngineState(BaseEngine.RESUMED);
                            synthesizer.postSpeakableEvent(new SpeakableEvent(
                                    item.getSource(),
                                    SpeakableEvent.SPEAKABLE_RESUMED, item
                                            .getId()), item.getListener());

                        } catch (InterruptedException ex1) {
                        }
                    }

                    synchronized (cancelFirstItem) {
                        if (cancelFirstItem) {
                            synthesizer.postSpeakableEvent(new SpeakableEvent(
                                    item.getSource(),
                                    SpeakableEvent.SPEAKABLE_CANCELLED, item
                                            .getId()), item.getListener());
                            break;
                        }
                    }

                    while (wordIndex < item.getWords().length
                            && (item.getWordsStartTime()[wordIndex] * sampleRate
                                <= playIndex * bytesRead)) {
                        synthesizer.postSpeakableEvent(new SpeakableEvent(item
                                .getSource(), SpeakableEvent.WORD_STARTED, item
                                .getId(), item.getWords()[wordIndex],
                                wordStart, wordStart
                                        + item.getWords()[wordIndex].length()),
                                item.getListener());
                        wordStart += item.getWords()[wordIndex].length() + 1;
                        wordIndex++;
                    }

                    while (phonemeIndex < item.getPhonesInfo().length
                            && timeNextPhone * sampleRate < playIndex
                                    * bytesRead) {
                        synthesizer.postSpeakableEvent(new SpeakableEvent(item
                                .getSource(), SpeakableEvent.PHONEME_STARTED,
                                item.getId(), item.getWords()[wordIndex - 1],
                                item.getPhonesInfo(), phonemeIndex), item
                                .getListener());
                        timeNextPhone +=
                            (double) item.getPhonesInfo()[phonemeIndex]
                                .getDuration() / (double) 1000;
                        phonemeIndex++;
                    }

                    playIndex++;

                  /**************************  long sleepTime = 0;
                  while ((sleepTime = nextTimeStamp
                            - System.currentTimeMillis()) > 0) {
                        try {
                            Thread.currentThread().sleep(sleepTime / 2);
                        } catch (InterruptedException ex) {
                        }
                    }*/

                    final OutputStream out = manager.getOutputStream();
                    out.write(buffer, 0, bytesRead);
                    // update next timestamp
                    long dataTime = (long) Math.ceil(1000 * bytesRead / bps);
                    long systm;
                    if (nextTimeStamp - (systm = System.currentTimeMillis()) < -dataTime) {
                        nextTimeStamp = systm + dataTime;
                    } else {
                        nextTimeStamp += dataTime;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Flush audio in the stream
            try {
                final OutputStream out = manager.getOutputStream();
                if (out != null) {
                    out.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (!cancelFirstItem) {

                // Delay the event sending by the remaining time audio length
                /*
                 * long audioTime = ((long)((totalBytesRead * 1000) /
                 * (audioFormat.getSampleRate() *
                 * audioFormat.getSampleSizeInBits()/8))); long endStreaming =
                 * System.currentTimeMillis(); long procTime = endStreaming -
                 * startStreaming; long sleepTime = audioTime - procTime; if
                 * (sleepTime > 0) { try {
                 * Thread.currentThread().sleep(sleepTime); } catch
                 * (InterruptedException ex2) { } }
                 */
                synthesizer.postSpeakableEvent(new SpeakableEvent(item
                        .getSource(), SpeakableEvent.SPEAKABLE_ENDED, item
                        .getId()), item.getListener());

                synchronized (playQueue) {
                    playQueue.remove(item);
                }
            }

            cancelFirstItem = false;

            if (isQueueEmpty()) {
                long[] states = synthesizer.setEngineState(
                        Synthesizer.QUEUE_NOT_EMPTY, Synthesizer.QUEUE_EMPTY);
                synthesizer.postSynthesizerEvent(states[0], states[1],
                        SynthesizerEvent.QUEUE_EMPTIED, true);
            } else {
                long[] states = synthesizer.setEngineState(
                        Synthesizer.QUEUE_NOT_EMPTY,
                        Synthesizer.QUEUE_NOT_EMPTY);
                synthesizer.postSynthesizerEvent(states[0], states[1],
                        SynthesizerEvent.QUEUE_UPDATED, true);
            }
        }
    }

    /**
     * Gets the next item from the queue and outputs it.
     */
    public void run() {
        long lastFocusEvent = Synthesizer.DEFOCUSED;

        while (!done) {
            final QueueItem item = getQueueItem();
            if (item != null) {
                if (lastFocusEvent == Synthesizer.DEFOCUSED) {
                    long[] states = synthesizer.setEngineState(
                            Synthesizer.DEFOCUSED, Synthesizer.FOCUSED);
                    synthesizer.postSynthesizerEvent(states[0], states[1],
                            SynthesizerEvent.ENGINE_FOCUSED, true);
                    lastFocusEvent = Synthesizer.FOCUSED;
                }

                // transfer item from the queue to the play queue
                removeQueueItem(item);

                synchronized (playQueue) {
                    playQueue.add(item);
                    playQueue.notifyAll();
                }

                // Synthesize item
                final BaseSynthesizerProperties properties =
                    (BaseSynthesizerProperties)
                        synthesizer.synthesizerProperties;
                properties.commitPropertiesChanges();

                final Object itemSource = item.getSource();
                if (itemSource instanceof String) {
                    synthesizer.handleSpeak(item.getId(),
                            (String) itemSource);
                } else if (itemSource instanceof Speakable) {
                    synthesizer.handleSpeak(item.getId(),
                            (Speakable) itemSource);
                } else {
                    throw new RuntimeException(
                            "WTF! It could only be text or speakable....");
                }
            }
        }
    }

    /**
     * Determines if the input queue is empty.
     *
     * @return true if the queue is empty; otherwise false
     */
    public boolean isQueueEmpty() {
        synchronized (queue) {
            synchronized (playQueue) {
                return queue.size() == 0 && playQueue.size() == 0;
            }
        }

    }

    /**
     * Returns an enumeration of the queue
     *
     * @return the enumeration queue
     */
    /*
     * public Enumeration enumerateQueue() { synchronized (queue) { return
     * queue.elements(); } }
     */

    /**
     * Cancel the current item.
     */
    protected boolean cancelItem() {
        if (playQueue.size() != 0) {
            QueueItem item = playQueue.get(0);
            if (item.getAudioSegment() == null) {
                synthesizer.handleCancel();
                synthesizer.postSpeakableEvent(new SpeakableEvent(item
                        .getSource(), SpeakableEvent.SPEAKABLE_CANCELLED, item
                        .getId()), item.getListener());
                playQueue.remove(0);

                return true;
            } else {
                playQueue.remove(0);
                synchronized (cancelFirstItem) {
                    cancelFirstItem = true;
                }
                return true;
            }

        } else {
            if (queue.size() != 0) {
                QueueItem item = queue.get(0);
                synthesizer.postSpeakableEvent(new SpeakableEvent(item
                        .getSource(), SpeakableEvent.SPEAKABLE_CANCELLED, item
                        .getId()), item.getListener());
                queue.remove(0);
                return true;
            }
        }
        /*
         * Speakable item = null; synchronized (queue) { audio.cancel(); if
         * (queue.size() != 0) { item = (Speakable) queue.remove(0); if (item !=
         * null) { // item.postSpeakableCancelled(); item.cancelled();
         * queueDrained(); } } }
         */
         return false; //No element removed from queue or playQueue
    }

    /**
     * Cancel all items in the queue.
     */
    public boolean cancelAllItems() {
        synthesizer.handleCancelAll();
        boolean found = false;

        if (playQueue.size() > 0) {
            cancelItem(); // cancel and remove first item
            while (playQueue.size() > 0) {
                QueueItem item = playQueue.get(0);
                synthesizer.postSpeakableEvent(new SpeakableEvent(item
                        .getSource(), SpeakableEvent.SPEAKABLE_CANCELLED, item
                        .getId()), item.getListener());
                playQueue.remove(0);
                found = true;
            }
        }

        while (queue.size() > 0) {
            QueueItem item = queue.get(0);
            synthesizer.postSpeakableEvent(new SpeakableEvent(item.getSource(),
                    SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()), item
                    .getListener());
            queue.remove(0);
            found = true;
        }
        /*
         * Speakable item = null; Vector copy;
         *
         * synchronized (queue) { audio.cancel(); copy = (Vector) queue.clone();
         * queue.clear(); queueDrained(); } for (Iterator i = copy.iterator();
         * i.hasNext(); ) { item = (Speakable) i.next(); //
         * item.postSpeakableCancelled(); item.cancelled(); }
         */
        return found;
    }

    /**
     * Cancel the given item.
     *
     * @param source
     *                the item to cancel.
     */
    protected void cancelItem(final Object source) {
        /*
         * Speakable item = null; synchronized (queue) { int index =
         * queue.indexOf(source); if (index == 0) { cancelItem(); } else { item =
         * (Speakable) queue.remove(index); if (item != null) { //
         * item.postSpeakableCancelled(); item.cancelled(); queueDrained(); } } }
         */
    }

    protected boolean cancelItem(final int id) {
        boolean found = false;

        // search item in playqueue
        synchronized (playQueue) {
            for (int i = 0; i < playQueue.size(); ++i) {
                QueueItem item = playQueue.get(i);
                if (item.getId() == id) {
                    if (i == 0) {
                        found = cancelItem();
                    } else {
                        if (item.getAudioSegment() == null) {
                            synthesizer.handleCancel(i);
                        }
                        synthesizer.postSpeakableEvent(new SpeakableEvent(item
                                .getSource(),
                                SpeakableEvent.SPEAKABLE_CANCELLED, item
                                        .getId()), item.getListener());
                        synthesizer.postSynthesizerEvent(synthesizer
                                .getEngineState(),
                                synthesizer.getEngineState(),
                                SynthesizerEvent.QUEUE_UPDATED, false);
                        playQueue.remove(i);
                        found = true;
                    }
                }
            }
        }

        // search item in queue
        synchronized (queue) {
            for (int i = 0; i < queue.size(); ++i) {
                QueueItem item = queue.get(i);
                if (item.getId() == id) {
                    synthesizer.postSpeakableEvent(new SpeakableEvent(item
                            .getSource(), SpeakableEvent.SPEAKABLE_CANCELLED,
                            item.getId()), item.getListener());
                    queue.remove(i);
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    /**
     * Return, but do not remove, the first item on the play queue.
     *
     * @return a queue item to play
     */
    protected QueueItem getQueueItemToPlay() {
        QueueItem item = null;
        synchronized (playQueue) {
            while (playQueue.isEmpty()
                    || (playQueue.get(0).getAudioSegment()) == null && !done) {
                try {
                    playQueue.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
            if (done) {
                return null;
            }
            item = (QueueItem) playQueue.elementAt(0);

        }
        return item;
    }

    /**
     * Returns, but does not remove, the first item on the queue.
     *
     * @return the first queue item
     */
    protected QueueItem getQueueItem() {
        QueueItem item = null;
        synchronized (queue) {
            while (queue.size() == 0 && !done) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                }
            }

            if (done) {
                return null;
            }
            item = queue.elementAt(0);

        }
        return item;
    }

    /**
     * Removes the given item, posting the appropriate events. The item may have
     * already been removed (due to a cancel).
     *
     * @param item
     *                the item to remove
     */
    protected void removeQueueItem(final QueueItem item) {
        synchronized (queue) {
            boolean found = queue.removeElement(item);
            if (found) {
                queueDrained();
            }
        }
    }

    /**
     * Should be called if one or more items have been removed from the queue.
     * Generates the appropriate state changes and events.
     */
    private void queueDrained() {
        /*
         * if (queue.size() == 0) { long[] states =
         * setEngineState(synthesizer.QUEUE_NOT_EMPTY, synthesizer.QUEUE_EMPTY);
         * postQueueEmptied(states[0], states[1]); } else { long[] states =
         * setEngineState(synthesizer.QUEUE_NOT_EMPTY,
         * synthesizer.QUEUE_NOT_EMPTY); postQueueUpdated(true, states[0],
         * states[1]); }
         */
    }

    /**
     * Utility method to associate the given audio segment with the
     * queued item with the given id.
     * @param id id of the queue item
     * @param audioSegment the new audio segment
     */
    public void setAudioSegment(final int id, final AudioSegment audioSegment) {
        synchronized (playQueue) {
            for (QueueItem item : playQueue) {
                if (item.getId() == id) {
                    item.setAudioSegment(audioSegment);
                    break;
                }
            }

            playQueue.notifyAll();
        }
    }

    public void setWords(int itemId, String[] words) {
        synchronized (playQueue) {
            for (QueueItem item : playQueue) {
                if (item.getId() == itemId) {
                    item.setWords(words);
                    break;
                }
            }

            playQueue.notifyAll();
        }
    }

    public void setWordsStartTimes(int itemId, float[] starttimes) {
        synchronized (playQueue) {
            for (QueueItem q : playQueue) {
                if (q.getId() == itemId) {
                    q.setWordsStartTimes(starttimes);
                    break;
                }
            }

            playQueue.notifyAll();
        }
    }

    public void setPhonesInfo(int itemId, PhoneInfo[] phonesinfo) {
        synchronized (playQueue) {
            for (QueueItem q : playQueue) {
                if (q.getId() == itemId) {
                    q.setPhonesInfo(phonesinfo);
                    break;
                }
            }

            playQueue.notifyAll();
        }
    }

}
