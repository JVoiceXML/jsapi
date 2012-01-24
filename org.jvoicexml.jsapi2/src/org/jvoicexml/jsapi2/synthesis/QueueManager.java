/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 296 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2012 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.jsapi2.synthesis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.speech.AudioException;
import javax.speech.AudioSegment;
import javax.speech.Engine;
import javax.speech.EngineStateException;
import javax.speech.synthesis.PhoneInfo;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerEvent;

import org.jvoicexml.jsapi2.AudioFormat;
import org.jvoicexml.jsapi2.BaseAudioManager;

/**
 * The {@link QueueManager} basically accepts the speech segments to
 * synthesized, appends them to a corresponding queue and hands them to the
 * synthesizer to convert those pieces into audio chunks. These chunks are added
 * to the play queue to be delivered via the configured media locator.
 *
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version $Revision: 296 $
 */
public class QueueManager {
    /** Reference to the synthesizer. */
    private BaseSynthesizer synthesizer;
    /** Queued play items. */
    private SynthesisQueue synthThread;
    /** Synthesized play items. */
    private PlayQueue playThread;
    /** <code>true</code> if the queue manager is terminated. */
    private boolean done;

    /** Id of the last queued item. */
    private int queueId;
    private boolean cancelFirstItem;
    private final Object cancelLock;

    /**
     * Constructs a new object.
     * @param synth the synthesizer whose queue is managed here.
     */
    public QueueManager(final BaseSynthesizer synth) {
        synthesizer = synth;
        queueId = 0;
        cancelFirstItem = false;
        cancelLock = new Object();

        synthThread = new SynthesisQueue();
        playThread = new PlayQueue();

        synthThread.start();
        playThread.start();
    }

    /**
     * Terminates the queue manager.
     */
    public final void terminate() {
        synthThread.terminate();
    }

    /**
     * Add a speakable item to be spoken to the output queue. Fires the
     * appropriate queue events.
     *
     * @param speakable the speakable item to add
     * @param listener a listener to notify about events of this item
     * @return queue id.
     */
    public final int appendItem(final Speakable speakable,
            final SpeakableListener listener) {
        return synthThread.appendItem(speakable, listener, null);
    }

    /**
     * Add a speakable item to be spoken to the output queue. Fires the
     * appropriate queue events.
     *
     * @param speakable the speakable item to add
     * @param listener a listener to notify about events of this item
     * @param text the text to be spoken
     * @return queue id.
     */
    public final int appendItem(final Speakable speakable,
            final SpeakableListener listener, final String text) {
        return synthThread.appendItem(speakable, listener, text);
    }

    /**
     * Add an item to be spoken to the output queue. Fires the appropriate queue
     * events
     *
     * @param item
     *                the item to add to the queue
     */
    public int appendItem(AudioSegment audioSegment, SpeakableListener listener) {
        return synthThread.appendItem(audioSegment, listener);
    }

    /**
     * Utility method to associate the given audio segment with the
     * queued item with the given id.
     * @param id id of the queue item
     * @param audioSegment the new audio segment
     */
    public void setAudioSegment(final int id, final AudioSegment audioSegment) {
        if (audioSegment != null) {
            playThread.setAudioSegment(id, audioSegment);
        }
    }

    public void setWords(final int itemId, final String[] words) {
        playThread.setWords(itemId, words);
    }

    public void setWordsStartTimes(final int itemId, final float[] starttimes) {
        playThread.setWordsStartTimes(itemId, starttimes);
    }

    public void setPhonesInfo(final int itemId, final PhoneInfo[] phonesinfo) {
        playThread.setPhonesInfo(itemId, phonesinfo);
    }

    /**
     * Determines if the input queue is empty.
     *
     * @return true if the queue is empty; otherwise false
     */
    public boolean isQueueEmpty() {
        return synthThread.isQueueEmpty() && playThread.isQueueEmpty();
    }

    /**
     * Cancel the current item.
     * @return <code>true</code> if an item was canceled
     */
    protected boolean cancelItem() throws EngineStateException {
        if (playThread.isQueueEmpty()) {
            return synthThread.cancelItem();
        } else {
            final BaseAudioManager manager =
                (BaseAudioManager) synthesizer.getAudioManager();
            final OutputStream out = manager.getOutputStream();
            try {
                out.close();
            } catch (IOException e) {
                throw new EngineStateException(e.getMessage());
            }
            return playThread.cancelItemAtTopOfQueue();
        }
    }

    /**
     * Cancel all items in the queue.
     */
    public boolean cancelAllItems() {
        synthesizer.handleCancelAll();
        boolean found = false;

        // First remove all pending requests...
        while (!synthThread.isQueueEmpty()) {
            synthThread.cancelItem();
            found = true;
        }

        // ...then remove all the stuff being played back
        if (!playThread.isQueueEmpty()) {
            cancelItem(); // cancel and remove first item
            while (!playThread.isQueueEmpty()) {
                playThread.cancelItemAtTopOfQueue();
                found = true;
            }
        }

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

    /**
     * Cancels the playback of the speakable with the given id. This is 
     * done by trying to remove it from the playthread and from
     * the synthesis thread.
     * @param id the id of the speakable to cancel
     * @return <code>true</code> if the spakable could be canceled.
     */
    protected boolean cancelItem(final int id) {
        final boolean found = playThread.cancelItem(id);
        return found || synthThread.cancelItem(id);
    }

    /**
     * Returns, but does not remove, the first item on the queue.
     *
     * @return the first queue item
     */
    protected QueueItem getQueueItem() {
        return synthThread.getNextQueueItem();
    }

    /**
     * Removes the given item, posting the appropriate events. The item may have
     * already been removed (due to a cancel).
     *
     * @param item
     *                the item to remove
     */
    protected void removeQueueItem(final QueueItem item) {
        synthThread.removeQueueItem(item);
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
     * Synthesis thread.
     * @author Dirk Schnelle-Walka
     *
     */
    class SynthesisQueue extends Thread {
        /** Queued speakables. */
        private Vector queue;

        /**
         * Constructs a new object.
         */
        public SynthesisQueue() {
            queue = new Vector();
        }

        /**
         * Terminates the queue manager.
         */
        public void terminate() {
            synchronized (queue) {
                done = true;
                queue.notifyAll();
            }
        }

        /**
         * Add a speakable item to be spoken to the output queue. Fires the
         * appropriate queue events.
         *
         * @param speakable the speakable item to add
         * @param listener a listener to notify about events of this item
         * @param text the text to be spoken, maybe <code>null</code> if the
         *             speakable contains markup text
         * @return queue id.
         */
        public int appendItem(final Speakable speakable,
                final SpeakableListener listener, final String text) {
            final boolean topOfQueueChanged;
            final int addedId;
            synchronized (queue) {
                addedId = ++queueId;
                final QueueItem item;
                if (text == null) {
                    item = new QueueItem(addedId, speakable, listener);
                } else {
                    item = new QueueItem(addedId, speakable, listener, text);
                }
                topOfQueueChanged = append(item);
            }
            adaptSynthesizerState(topOfQueueChanged);
            return addedId;
        }

        /**
         * Add an item to be spoken to the output queue. Fires the appropriate
         * queue events.
         *
         * @param item
         *                the item to add to the queue
         */
        public int appendItem(final AudioSegment audioSegment,
                final SpeakableListener listener) {
            final boolean topOfQueueChanged;
            synchronized (queue) {
                ++queueId;
                final QueueItem item =
                    new QueueItem(queueId, audioSegment, listener);
                topOfQueueChanged = append(item);
            }
            adaptSynthesizerState(topOfQueueChanged);
            return queueId;
        }

        /**
         * Appends the given queue item to the end of the list
         * @param item the item to append
         * @return <code>true</code> if the appended item is the only one
         *      in the queue
         */
        private boolean append(final QueueItem item) {
            final boolean topOfQueueChanged = isQueueEmpty();
            queue.addElement(item);
            queue.notifyAll();
            return topOfQueueChanged;
        }

        /**
         * Adapts the synthesizer state after a queue item has been added
         * @param topOfQueueChanged <code>true</code> if the top of the
         *      queue changed after the item has been appended
         */
        private void adaptSynthesizerState(final boolean topOfQueueChanged) {
            final long[] states;
            if (topOfQueueChanged) {
                states = synthesizer.setEngineState(Synthesizer.QUEUE_EMPTY,
                        Synthesizer.QUEUE_NOT_EMPTY);
            } else {
                states = synthesizer.setEngineState(Synthesizer.QUEUE_NOT_EMPTY,
                        Synthesizer.QUEUE_NOT_EMPTY);
            }
            synthesizer.postSynthesizerEvent(states[0], states[1],
                    SynthesizerEvent.QUEUE_UPDATED, topOfQueueChanged);
        }

        /**
         * Determines if the input queue is empty.
         *
         * @return true if the queue is empty; otherwise false
         */
        public boolean isQueueEmpty() {
            synchronized (queue) {
                return queue.size() == 0;
            }

        }

        /**
         * Cancels the current item.
         * @return <code>true</code> if an item was removed from the queue
         */
        protected boolean cancelItem() {
            if (queue.size() != 0) {
                QueueItem item = (QueueItem) queue.elementAt(0);
                synthesizer.postSpeakableEvent(new SpeakableEvent(item
                        .getSource(), SpeakableEvent.SPEAKABLE_CANCELLED, item
                        .getId()), item.getListener());
                queue.removeElementAt(0);
                return true;
            }
            return false;
        }

        /**
         * Cancels the item with the given id.
         * @param id the id of the item to cancel
         * @return <code>true</code> if the item was cancelled
         */
        protected boolean cancelItem(final int id) {
            // search item in queue
            synchronized (queue) {
                final QueueItem item = getQueueItem(id);
                if (item == null) {
                    return false;
                }
                for (int i = 0; i < queue.size(); ++i) {
                    if (item.getId() == id) {
                        final Object source = item.getSource();
                        final SpeakableListener listener = item.getListener();
                        final SpeakableEvent event = new SpeakableEvent(
                                source, SpeakableEvent.SPEAKABLE_CANCELLED,
                                id);
                        synthesizer.postSpeakableEvent(event, listener);
                        queue.removeElementAt(i);
                        // TODO cancel the playback in the synthesizer
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * Retrieves the queue item with the given id.
         * @param id the id of the queue item to look for
         * @return found queue item, or <code>null</code> if there is no
         *      queue item with the given id
         */
        public QueueItem getQueueItem(final int id) {
            synchronized (queue) {
                for (int i=0; i< queue.size(); i++) {
                    final QueueItem item = (QueueItem) queue.elementAt(i);
                    if (item.getId() == id) {
                        return item;
                    }
                }
            }
            return null;
        }

        /**
         * Returns, but does not remove, the first item on the queue. Waits
         * until a queue item has been added if the queue is empty.
         *
         * @return the first queue item
         */
        protected QueueItem getNextQueueItem() {
            synchronized (queue) {
                while (queue.size() == 0 && !done) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        return null;
                    }
                }

                if (done) {
                    return null;
                }
                return (QueueItem) queue.elementAt(0);
            }
        }

        /**
         * Removes the given item, posting the appropriate events.
         * The item may have already been removed (due to a cancel).
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
         * Gets the next item from the queue and outputs it.
         */
        public void run() {
            long lastFocusEvent = Synthesizer.DEFOCUSED;

            while (!done) {
                final QueueItem item = getNextQueueItem();
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
                    playThread.addQueueItem(item);
                    // Synthesize it
                    synthesize(item);
                    // Notify the observers that something changed
                    playThread.itemChanged(item);
                }
            }
        }

        /**
         * Synthesizes the given queue item.
         * @param item the queue item to synthesize
         */
        private void synthesize(final QueueItem item) {
            final Object itemSource = item.getSource();
            final int id = item.getId();
            final AudioSegment segment;
            if (itemSource instanceof String) {
                final String text = (String) itemSource;
                segment = synthesizer.handleSpeak(id, text);
            } else if (itemSource instanceof Speakable) {
                final Speakable speakable = (Speakable) itemSource;
                segment = synthesizer.handleSpeak(id, speakable);
            } else {
                throw new RuntimeException(
                     "WTF! It could only be text or speakable but was "
                     + (itemSource == null ? "null" :
                         item.getClass().getName()));
            }

            setAudioSegment(id, segment);
        }
    }

    /**
     * Play back the audio coming from the synthesizer.
     * @author Dirk Schnelle-Walka
     */
    class PlayQueue extends Thread {
        /** Buffer size when reading from the audio segment input stream. */
        private static final int BUFFER_LENGTH = 1024;

        /** The items to be played back. */
        private Vector playQueue;

        /**
         * Constructs a new object.
         */
        public PlayQueue() {
            playQueue = new Vector();
        }

        /**
         * {@inheritDoc}
         */
        public void run() {

            int playIndex = 0;
            int wordIndex = 0;
            int wordStart = 0;
            int phonemeIndex = 0;
            double timeNextPhone = 0;
            long nextTimeStamp = 0;

            final byte[] buffer = new byte[BUFFER_LENGTH];

            while (!done) {
                final QueueItem item = getNextQueueItem();
                final Object source = item.getSource();
                final int id = item.getId();
                final SpeakableListener listener = item.getListener();
                postTopOfQueue(item);
                try {
                    delayUntilResumed(item);
                } catch (InterruptedException e1) {
                    return;
                }

                long totalBytesRead = 0;
                final SpeakableEvent startedEvent = new SpeakableEvent(source,
                        SpeakableEvent.SPEAKABLE_STARTED, id);
                synthesizer.postSpeakableEvent(startedEvent, listener);

                playIndex = 0;
                wordIndex = 0;
                wordStart = 0;
                phonemeIndex = 0;
                timeNextPhone = 0;
                int bytesRead = 0;
                final BaseAudioManager manager =
                    (BaseAudioManager) synthesizer.getAudioManager();
                final AudioFormat format;
                try {
                    format = manager.getAudioFormat();
                } catch (AudioException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
                }
                final float sampleRate = format.getSampleRate();
                long bps = format.getChannels();
                bps *= sampleRate;
                bps *= (format.getSampleSizeInBits() / 8);
                try {
                    while (true) {
                        final AudioSegment segment = item.getAudioSegment();
                        if ((segment == null) || !segment.isGettable()) {
                            throw new SecurityException(
                                "The platform does not allow to access the input "
                                    + "stream!");
                        }
                        final InputStream inputStream =
                                segment.openInputStream();
                        if (inputStream == null) {
                            break;
                        }
                        bytesRead = inputStream.read(buffer);
                        if (bytesRead < 0)  {
                            break;
                        }

                        totalBytesRead += bytesRead;
                        synchronized (cancelLock) {
                            if (cancelFirstItem) {
                                final SpeakableEvent cancelledEvent =
                                    new SpeakableEvent(source,
                                        SpeakableEvent.SPEAKABLE_CANCELLED, id);
                                synthesizer.postSpeakableEvent(cancelledEvent,
                                        listener);
                                break;
                            }
                        }
                        try {
                            delayUntilResumed(item);
                        } catch (InterruptedException e) {
                            return;
                        }

                        synchronized (cancelLock) {
                            if (cancelFirstItem) {
                                synthesizer.postSpeakableEvent(new SpeakableEvent(
                                        source,
                                        SpeakableEvent.SPEAKABLE_CANCELLED, id),
                                        listener);
                                break;
                            }
                        }

                        while (wordIndex < item.getWords().length
                                && (item.getWordsStartTime()[wordIndex] * sampleRate
                                    <= playIndex * bytesRead)) {
                            synthesizer.postSpeakableEvent(new SpeakableEvent(
                                    source, SpeakableEvent.WORD_STARTED, id,
                                    item.getWords()[wordIndex],
                                        wordStart, wordStart
                                        + item.getWords()[wordIndex].length()),
                                    listener);
                            wordStart += item.getWords()[wordIndex].length() + 1;
                            wordIndex++;
                        }

                        while (phonemeIndex < item.getPhonesInfo().length
                                && timeNextPhone * sampleRate < playIndex
                                        * bytesRead) {
                            synthesizer.postSpeakableEvent(new SpeakableEvent(
                                    source, SpeakableEvent.PHONEME_STARTED,
                                    id, item.getWords()[wordIndex - 1],
                                    item.getPhonesInfo(), phonemeIndex),
                                    listener);
                            timeNextPhone +=
                                (double) item.getPhonesInfo()[phonemeIndex]
                                    .getDuration() / (double) 1000;
                            phonemeIndex++;
                        }

                        playIndex++;

                        final OutputStream out = manager.getOutputStream();
                        out.write(buffer, 0, bytesRead);
                        // update next timestamp
                        long dataTime = (long) (1000 * bytesRead / bps);
                        final long system = System.currentTimeMillis();
                        if (nextTimeStamp - system < -dataTime) {
                            nextTimeStamp = system + dataTime;
                        } else {
                            nextTimeStamp += dataTime;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                // Flush audio in the stream
                try {
                    final OutputStream out = manager.getOutputStream();
                    if (out != null) {
                        out.flush();
                    }
                } catch (IOException ex) {
                    return;
                }

                if (!cancelFirstItem) {
                    synthesizer.postSpeakableEvent(new SpeakableEvent(
                            source, SpeakableEvent.SPEAKABLE_ENDED, id),
                            listener);

                    synchronized (playQueue) {
                        playQueue.removeElement(item);
                    }
                }

                synchronized(cancelLock) {
                    cancelFirstItem = false;
                }
                postEventsAfterPlay();
            }
        }

        /**
         * Posts an event that processing of the queue has started
         * @param item the top most queue item
         */
        private void postTopOfQueue(final QueueItem item) {
            final SpeakableListener listener = item.getListener();
            if (listener == null) {
                return;
            }
            final Object source = item.getSource();
            final int id = item.getId();
            synthesizer.postSpeakableEvent(new SpeakableEvent(source,
                    SpeakableEvent.TOP_OF_QUEUE, id), listener);
        }

        /**
         * Delays until the synthesizer moved into the
         * {@link Synthesizer.RESUMED} state.
         * <p>
         * If the {@link Synthesizer} is in the {@link Synthesizer.PAUSED}
         * state, first a {@link SpeakableEvent.SPEAKABLE_PAUSED} is issued.
         * Once the {@link Synthesizer} is resumed, a
         * {@link SpeakableEvent.SPEAKABLE_RESUMED} is issued.
         * </p>
         * @param item the next queue item to play
         * @throws InterruptedException
         *         if the waiting was interrupted
         */
        private void delayUntilResumed(final QueueItem item)
                throws InterruptedException {
            while (synthesizer.testEngineState(Synthesizer.PAUSED)) {
                final SpeakableListener listener = item.getListener();
                final Object source = item.getSource();
                final int id = item.getId();
                final SpeakableEvent pausedEvent = new SpeakableEvent(
                                source, SpeakableEvent.SPEAKABLE_PAUSED,
                                id);
                synthesizer.postSpeakableEvent(pausedEvent, listener);

                synthesizer.waitEngineState(Engine.RESUMED);
                final SpeakableEvent resumedEvent =
                    new SpeakableEvent(source,
                            SpeakableEvent.SPEAKABLE_RESUMED, id);
                synthesizer.postSpeakableEvent(resumedEvent, listener);
            }
        }

        /**
         * Posts an update event after a play has been performed. This is
         * either a {@link Synthesizer.QUEUE_EMPTIED} event if the
         * queue is empty or {@link Synthesizer.QUEUE_UPDATED} if there
         * are more speakables to process,
         */
        private void postEventsAfterPlay() {
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

        /**
         * Notifies the play queue that the given item has changed.
         * @param item the item that has changed
         */
        public void itemChanged(final QueueItem item) {
            synchronized (playQueue) {
                playQueue.notifyAll();
            }
        }

        /**
         * Adds the given item to the play queue.
         * @param item the item to add
         */
        public void addQueueItem(final QueueItem item) {
            synchronized (playQueue) {
                playQueue.addElement(item);
                playQueue.notifyAll();
            }
        }

        /**
         * Retrieves the queue item with the given id.
         * @param id the id of the queue item to look for
         * @return found queue item, or <code>null</code> if there is no
         *      queue item with the given id
         */
        public QueueItem getQueueItem(final int id) {
            synchronized (playQueue) {
                for (int i=0; i< playQueue.size(); i++) {
                    final QueueItem item = (QueueItem) playQueue.elementAt(i);
                    if (item.getId() == id) {
                        return item;
                    }
                }
            }
            return null;
        }

        /**
         * Return, but do not remove, the first item on the play queue.
         *
         * @return a queue item to play
         */
        protected QueueItem getNextQueueItem() {
            synchronized (playQueue) {
                while (playQueue.isEmpty()
                        || (((QueueItem) playQueue.elementAt(0)).getAudioSegment())
                            == null && !done) {
                    try {
                        playQueue.wait();
                    } catch (InterruptedException e) {
                        return null;
                    }
                }
                if (done) {
                    return null;
                }
                return (QueueItem) playQueue.elementAt(0);
            }
        }


        /**
         * Determines if the input queue is empty.
         *
         * @return <code>true</code> if the queue is empty; otherwise
         * <code>false</code>
         */
        public boolean isQueueEmpty() {
            synchronized (playQueue) {
                return playQueue.size() == 0;
            }
        }

        /**
         * Cancel the item at the top of the queue
         * @return <code>true</code> if an item was canceled
         * @exception EngineStateException
         *            if the engine is in an invalid state
         */
        protected boolean cancelItemAtTopOfQueue() throws EngineStateException {
            synchronized (playQueue) {
                if (playQueue.isEmpty()) {
                    return false;
                }
                final QueueItem item = (QueueItem) playQueue.elementAt(0);
                final AudioSegment segment = item.getAudioSegment();
                // Having an audio segment means that the handleSpeak
                // method is already being called
                if (segment == null) {
                    synthesizer.handleCancel();
                    final Object source = item.getSource();
                    final int id = item.getId();
                    final SpeakableListener listener = item.getListener();
                    synthesizer.postSpeakableEvent(new SpeakableEvent(
                            source, SpeakableEvent.SPEAKABLE_CANCELLED, id),
                            listener);
                    playQueue.removeElementAt(0);
                } else {
                    // The SpeakableEvent.SPEAKABLE_CANCELLED is posted in the
                    // play method
                    playQueue.removeElementAt(0);
                }
                synchronized (cancelLock) {
                    cancelFirstItem = true;
                }
                return true;
            }
        }

        /**
         * Cancels the playback of the speakable with the given id
         * @param id the speakable to cancle
         * @return <code>true</code> if the speakable was canceled
         */
        protected boolean cancelItem(final int id) {
            boolean found = false;

            // search item in playqueue
            synchronized (playQueue) {
                for (int i = 0; i < playQueue.size(); ++i) {
                    final QueueItem item = (QueueItem) playQueue.elementAt(i);
                    final int currentId = item.getId();
                    if (currentId == id) {
                        if (i == 0) {
                            found = cancelItemAtTopOfQueue();
                        } else {
                            if (item.getAudioSegment() == null) {
                                synthesizer.handleCancel(i);
                            }
                            synthesizer.postSpeakableEvent(new SpeakableEvent(item
                                    .getSource(),
                                    SpeakableEvent.SPEAKABLE_CANCELLED, currentId),
                                    item.getListener());
                            synthesizer.postSynthesizerEvent(synthesizer
                                    .getEngineState(),
                                    synthesizer.getEngineState(),
                                    SynthesizerEvent.QUEUE_UPDATED, false);
                            playQueue.removeElementAt(i);
                            found = true;
                        }
                    }
                }
            }
            return found;
        }

        /**
         * Utility method to associate the given audio segment with the
         * queued item with the given id.
         * @param id id of the queue item
         * @param audioSegment the new audio segment
         */
        public void setAudioSegment(final int id,
                final AudioSegment audioSegment) {
            synchronized (playQueue) {
                final QueueItem item = getQueueItem(id);
                if (item != null) {
                    item.setAudioSegment(audioSegment);
                }
                playQueue.notifyAll();
            }
        }

        public void setWords(final int id, final String[] words) {
            synchronized (playQueue) {
                final QueueItem item = getQueueItem(id);
                if (item != null) {
                    item.setWords(words);
                }
                playQueue.notifyAll();
            }
        }

        public void setWordsStartTimes(final int itemId,
                final float[] starttimes) {
            synchronized (playQueue) {
                final QueueItem item = getQueueItem(itemId);
                if (item != null) {
                    item.setWordsStartTimes(starttimes);
                }
                playQueue.notifyAll();
            }
        }

        public void setPhonesInfo(final int itemId,
                final PhoneInfo[] phonesinfo) {
            synchronized (playQueue) {
                final QueueItem item = getQueueItem(itemId);
                if (item != null) {
                    item.setPhonesInfo(phonesinfo);
                }
                playQueue.notifyAll();
            }
        }
    }
}
