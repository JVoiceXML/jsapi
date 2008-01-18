package org.jvoicexml.jsapi2.synthesis;

import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.SynthesizerListener;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.Speakable;
import javax.speech.synthesis.SynthesisException;
import javax.speech.EngineEvent;
import javax.speech.EngineStateException;
import javax.speech.AudioSegment;
import org.jvoicexml.jsapi2.BaseEngine;
import javax.speech.synthesis.SynthesizerMode;
import java.util.Vector;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SynthesizerEvent;
import java.util.Enumeration;
import javax.speech.EngineListener;
import javax.speech.EngineException;
import javax.speech.AudioException;
import org.jvoicexml.jsapi2.BaseAudioManager;
import java.io.*;

/**
 * <p>Title: JSAPI 2.0</p>
 *
 * <p>Description: An independent reference implementation of JSR 113</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
abstract public class BaseSynthesizer extends BaseEngine implements Synthesizer {

    protected Vector speakableListeners;
    protected SynthesizerProperties synthesizerProperties;
    protected int speakableMask;
    protected QueueManager queueManager;

    public BaseSynthesizer() {
        this(null);
    }

    public BaseSynthesizer(SynthesizerMode engineMode) {
        super(engineMode);
        speakableListeners = new Vector();
        synthesizerProperties = new BaseSynthesizerProperties(this);
        speakableMask = SpeakableEvent.DEFAULT_MASK;
        setEngineMask(getEngineMask() | SynthesizerEvent.DEFAULT_MASK);
        queueManager = new QueueManager(this);
    }

    /**
     * fireEvent
     *
     * @param event EngineEvent
     */
    public void fireEvent(EngineEvent event) {
        Enumeration listeners = engineListeners.elements();
        while (listeners.hasMoreElements()) {
            EngineListener el = (EngineListener) listeners.nextElement();
            ((SynthesizerListener) el).synthesizerUpdate((SynthesizerEvent)
                    event);
        }
    }

    /**
     * postEngineEvent
     *
     * @param oldState long
     * @param newState long
     * @param eventType int
     * @todo Implement this org.jvoicexml.jsapi2.BaseEngine method
     */
    public void postEngineEvent(long oldState, long newState, int eventType) {
        final SynthesizerEvent event = new SynthesizerEvent(this,
                eventType,
                oldState,
                newState,
                null,
                false); /** @todo Change after adding the queue */

        postEngineEvent(event);
    }


    protected void postSynthesizerEvent(long oldState, long newState,
                                        int eventType,
                                        boolean changedTopOfQueue) {
        final SynthesizerEvent event = new SynthesizerEvent(this,
                eventType,
                oldState,
                newState,
                null,
                changedTopOfQueue);

        postEngineEvent(event);

    }

    protected void postSpeakableEvent(final SpeakableEvent event) {
        if ((getSpeakableMask() & event.getId()) == event.getId()) {
            try {
                speechEventExecutor.execute(new Runnable() {
                    public void run() {
                        fireSpeakableEvent(event);
                    }
                });
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Utility function to send a speakable event to all grammar
     * listeners.
     */
    public void fireSpeakableEvent(SpeakableEvent event) {
        Enumeration E;
        if (speakableListeners != null) {
            E = speakableListeners.elements();
            while (E.hasMoreElements()) {
                SpeakableListener sl = (SpeakableListener) E.nextElement();
                sl.speakableUpdate(event);
            }
        }
    }


    protected boolean isValid(long state) {
        if (testEngineState(QUEUE_EMPTY | QUEUE_NOT_EMPTY))
            return false;

        return super.isValid(state);
    }

    public void addSpeakableListener(SpeakableListener listener) {
        if (!speakableListeners.contains(listener))
            speakableListeners.addElement(listener);
    }

    public void removeSpeakableListener(SpeakableListener listener) {
        speakableListeners.removeElement(listener);
    }

    public void addSynthesizerListener(SynthesizerListener listener) {
        super.addEngineListener(listener);
    }

    public void removeSynthesizerListener(SynthesizerListener listener) {
        super.removeEngineListener(listener);
    }

    public void cancel() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        //Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        queueManager.cancelItem();
    }

    public void cancel(int id) throws IllegalArgumentException,
            EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        //Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        queueManager.cancelItem(id);
    }

    public void cancelAll() throws EngineStateException {
        checkEngineState(DEALLOCATED | DEALLOCATING_RESOURCES);

        //Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        queueManager.cancelAllItems();
    }

    public String getPhonemes(String text) throws EngineStateException {
        return "";
    }

    public SynthesizerProperties getSynthesizerProperties() {
        return synthesizerProperties;
    }

    public void setSynthesizerProperties(SynthesizerProperties synthesizerProperties) {
        this.synthesizerProperties = synthesizerProperties;
    }

    public void setSpeakableMask(int mask) {
        speakableMask = mask;
    }

    public int getSpeakableMask() {
        return speakableMask;
    }

    public int speak(AudioSegment audio, SpeakableListener listener) throws
            EngineStateException, IllegalArgumentException {
        return queueManager.appendItem(audio, listener);
    }

    public int speak(Speakable speakable, SpeakableListener listener) throws
            EngineStateException, SynthesisException, IllegalArgumentException {

        //Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        return queueManager.appendItem(speakable, listener);
    }

    public int speak(String text, SpeakableListener listener) throws
            EngineStateException {

        //Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        return queueManager.appendItem(getSpeakable(text), listener);
    }

    public int speakMarkup(String synthesisMarkup, SpeakableListener listener) throws
            EngineStateException, SynthesisException, IllegalArgumentException {

        //Wait to finalize allocation
        while (testEngineState(ALLOCATING_RESOURCES)) {
            try {
                waitEngineState(ALLOCATED);
            } catch (InterruptedException ex) {
            }
        }

        return queueManager.appendItem(parseMarkup(synthesisMarkup), listener);
    }


    /**
     * Called from the <code>allocate</code> method.  Override this in
     * subclasses.
     *
     * @see #allocate
     *
     * @throws EngineException if problems are encountered
     */
    protected boolean baseAllocate() throws EngineStateException, EngineException, AudioException {

        //Starts AudioManager
        audioManager.audioStart();

        //Procceed to real engine allocation
        boolean status = handleAllocate();
        if (status == true) {
            long states[] = setEngineState(CLEAR_ALL_STATE,
                                           ALLOCATED | RESUMED | DEFOCUSED);
            postEngineEvent(states[0], states[1], EngineEvent.ENGINE_ALLOCATED);
        }

        return status;
    }

    /**
     * Called from the <code>deallocate</code> method.  Override this in
     * subclasses.
     *
     * @throws EngineException if this <code>Engine</code> cannot be
     *   deallocated.
     */
    protected boolean baseDeallocate() throws EngineStateException, EngineException, AudioException {

        //Stops AudioManager
        audioManager.audioStop();

        //Procceed to real engine deallocation
        boolean status = handleDeallocate();
        if (status == true) {
            long states[] = setEngineState(CLEAR_ALL_STATE, DEALLOCATED);
            postEngineEvent(states[0], states[1],
                            EngineEvent.ENGINE_DEALLOCATED);
        }

        return status;
    }

    protected boolean basePause() {
        return handlePause();
    }

    /**
     * Called from the <code>resume</code> method.  Override in subclasses.
     *
     * @todo Handle grammar updates
     */
    protected boolean baseResume() {
        return handleResume();
    }


    abstract protected boolean handleAllocate();

    abstract protected boolean handleDeallocate();

    abstract protected boolean handlePause();

    abstract protected boolean handleResume();

    abstract protected boolean handleCancel();

    abstract protected boolean handleCancel(int id);

    abstract protected boolean handleCancelAll();


    protected abstract Speakable getSpeakable(String text);

    protected abstract Speakable parseMarkup(String synthesisMarkup);

    protected abstract void handleSpeak(int id, Speakable item);


    /**
     * Returns a <code>String</code> of the names of all the
     * <code>Engine</code> states in the given <code>Engine</code>
     * state.
     *
     * @param state the bitmask of states
     *
     * @return a <code>String</code> containing the names of all the
     *   states set in <code>state</code>
     */
    protected String stateToString(long state) {
        StringBuffer buf = new StringBuffer(super.stateToString(state));
        if ((state & Synthesizer.QUEUE_EMPTY) != 0)
            buf.append(" QUEUE_EMPTY ");
        if ((state & Synthesizer.QUEUE_EMPTY) != 0)
            buf.append(" QUEUE_EMPTY ");
        return buf.toString();
    }


    /**
     *
     * <p>Title: JSAPI 2.0</p>
     *
     * <p>Description: An independent reference implementation of JSR 113</p>
     *
     * <p>Copyright: Copyright (c) 2007</p>
     *
     * <p>Company: JVoiceXML group - http://jvoicexml.sourceforge.net</p>
     *
     * @author Renato Cassaca
     * @version 1.0
     */
    protected class QueueManager implements Runnable {

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
            done = false;
            queue = new Vector();
            playQueue = new Vector<QueueItem>();
            queueID = 0;
            cancelFirstItem = false;

            synthThread = new Thread(this,
                                "QueueManager_synthesizer_" +
                                synthesizer.getEngineMode().getEngineName());
            synthThread.start();

            playThread = new Thread(this,
                                "QueueManager_play_" +
                                synthesizer.getEngineMode().getEngineName());
            playThread.start();
        }

        public void terminate() {
            synchronized (queue) {
                done = true;
                queue.notify();
            }
        }

        /**
         * Add an item to be spoken to the output queue. Fires the
         * appropriate queue events
         *
         * @param item the item to add to the queue
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

            long[] states = setEngineState(topOfQueueChanged ? QUEUE_EMPTY :
                                           QUEUE_NOT_EMPTY, QUEUE_NOT_EMPTY);
            postSynthesizerEvent(states[0], states[1],
                                 SynthesizerEvent.QUEUE_UPDATED,
                                 topOfQueueChanged);

            return queueID;
        }

        /**
         * Add an item to be spoken to the output queue. Fires the
         * appropriate queue events
         *
         * @param item the item to add to the queue
         */
        public int appendItem(AudioSegment audioSegment,
                              SpeakableListener listener) {
            boolean topOfQueueChanged;
            synchronized (queue) {
                queueID += 1;
                QueueItem item = new QueueItem(queueID, audioSegment, listener);

                topOfQueueChanged = isQueueEmpty();
                queue.addElement(item);
                queue.notifyAll();
            }

            long[] states = setEngineState(topOfQueueChanged ? QUEUE_EMPTY :
                                           QUEUE_NOT_EMPTY, synthesizer.QUEUE_NOT_EMPTY);
            postSynthesizerEvent(states[0], states[1],
                                 SynthesizerEvent.QUEUE_UPDATED,
                                 topOfQueueChanged);

            return queueID;
        }

        private void playItens(){
             QueueItem item;

             while(!done){
                 item = getQueueItemToPlay();

                 postSpeakableEvent(new SpeakableEvent(item, SpeakableEvent.TOP_OF_QUEUE,
                                                       item.getId()));

                 while (testEngineState(PAUSED)) {
                     postSpeakableEvent(new SpeakableEvent(item,
                                 SpeakableEvent.
                                 SPEAKABLE_PAUSED, item.getId()));
                     try {
                         waitEngineState(BaseEngine.RESUMED);
                         postSpeakableEvent(new SpeakableEvent(item,
                                 SpeakableEvent.
                                 SPEAKABLE_RESUMED, item.getId()));
                     } catch (InterruptedException ex1) {
                     }
                 }

                 postSpeakableEvent(new SpeakableEvent(item,
                                                       SpeakableEvent.SPEAKABLE_STARTED,
                                                       item.getId()));

                 byte[] buffer = new byte[1024];
                 try {
                     while (item.getAudioSegment().getInputStream().read(
                             buffer) != -1) {

                         synchronized (cancelFirstItem) {
                             if (cancelFirstItem == true) {
                                 postSpeakableEvent(new SpeakableEvent(item,
                                         SpeakableEvent.
                                         SPEAKABLE_CANCELLED, item.getId()));
                                 break;
                             }
                         }

                         while (testEngineState(PAUSED)) {
                             postSpeakableEvent(new SpeakableEvent(item,
                                 SpeakableEvent.
                                 SPEAKABLE_PAUSED, item.getId()));
                             try {
                                 waitEngineState(BaseEngine.RESUMED);
                                 postSpeakableEvent(new SpeakableEvent(item,
                                                                       SpeakableEvent.
                                                                       SPEAKABLE_RESUMED, item.getId()));

                             } catch (InterruptedException ex1) {
                             }
                         }

                         synchronized (cancelFirstItem) {
                             if (cancelFirstItem == true) {
                                 postSpeakableEvent(new SpeakableEvent(item,
                                         SpeakableEvent.
                                         SPEAKABLE_CANCELLED, item.getId()));

                                 break;
                             }
                         }

                         ((BaseAudioManager) getAudioManager()).
                                 getOutputStream().write(buffer);
                     }
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }

                 if (!cancelFirstItem){
                     postSpeakableEvent(new SpeakableEvent(item,
                                                           SpeakableEvent.SPEAKABLE_ENDED,
                                                           item.getId()));

                     synchronized (playQueue) {
                         playQueue.remove(item);
                     }
                 }

                 cancelFirstItem = false;

                 if (isQueueEmpty() == true) {
                     long[] states = setEngineState(synthesizer.QUEUE_NOT_EMPTY,
                                                    synthesizer.QUEUE_EMPTY);
                     postSynthesizerEvent(states[0], states[1],
                                          SynthesizerEvent.QUEUE_EMPTIED,
                                          true);
                 }else{
                     long[] states = setEngineState(synthesizer.QUEUE_NOT_EMPTY,
                                                    synthesizer.QUEUE_NOT_EMPTY);
                     postSynthesizerEvent(states[0], states[1],
                                          SynthesizerEvent.QUEUE_UPDATED,
                                          true);
                 }
             }
        }

        /**
         * Gets the next item from the queue and outputs it
         */
        public void run() {
            if (Thread.currentThread().getName().compareTo(playThread.getName())==0){
                playItens();
                return ;
            }

            QueueItem item;
            int currentCommand;
            boolean queueEmptied;

            long lastFocusEvent = DEFOCUSED;
            long lastQueueEvent = QUEUE_EMPTY;


            while (!done) {
                item = getQueueItem();
                if (item != null) {
                    if (lastFocusEvent == DEFOCUSED) {
                        long[] states = setEngineState(DEFOCUSED, FOCUSED);
                        postSynthesizerEvent(states[0], states[1],
                                             SynthesizerEvent.ENGINE_FOCUSED,
                                             true);
                        lastFocusEvent = FOCUSED;
                    }

                    //Synthetize item
                    handleSpeak(item.getId(), item.getSpeakable());

                    //transfer item from queue to playqueue
                    QueueItem qi = item;
                    removeQueueItem(item);

                    synchronized(playQueue){
                        playQueue.add(qi);
                    }

                    /*if (isQueueEmpty() == true) {
                        long[] states = setEngineState(synthesizer.QUEUE_NOT_EMPTY,
                                                       synthesizer.QUEUE_EMPTY);
                        postSynthesizerEvent(states[0], states[1],
                                             SynthesizerEvent.QUEUE_UPDATED,
                                             true);

                        if (lastFocusEvent != DEFOCUSED) {
                            states = setEngineState(FOCUSED, DEFOCUSED);
                            postSynthesizerEvent(states[0], states[1],
                                                 SynthesizerEvent.
                                                 ENGINE_DEFOCUSED,
                                                 true);
                            lastFocusEvent = DEFOCUSED;
                        }

                    }*/


                }
            }
        }

        /**
         * Determines if the input queue is empty
         *
         * @return true if the queue is empty; otherwise false
         */
        public boolean isQueueEmpty() {
            synchronized (queue) {
                synchronized (playQueue){
                    return queue.size()==0 && playQueue.size()==0;
                }
            }

        }


        /**
         * Returns an enumeration of the queue
         *
         * @return the enumeration queue
         */
        /*    public Enumeration enumerateQueue() {
                synchronized (queue) {
                    return queue.elements();
                }
            }*/




        /**
         * Cancel the current item
         */
        protected void cancelItem() {
            if (playQueue.size() != 0) {
                QueueItem item = playQueue.get(0);
                if (item.getAudioSegment() == null) {
                    handleCancel();
                    postSpeakableEvent(new SpeakableEvent(item,
                        SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()));
                    playQueue.remove(0);
                }
                else {
                    playQueue.remove(0);
                    synchronized (cancelFirstItem) {
                        cancelFirstItem = true;
                    }
                }

            } else {
                if (queue.size() != 0) {
                    QueueItem item = queue.get(0);
                    postSpeakableEvent(new SpeakableEvent(item,
                            SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()));
                    queue.remove(0);
                }
            }
            /*            Speakable item = null;
                        synchronized (queue) {
                            audio.cancel();
                            if (queue.size() != 0) {
                                item = (Speakable) queue.remove(0);
                                if (item != null) {
                                    // item.postSpeakableCancelled();
                                    item.cancelled();
                                    queueDrained();
                                }
                            }
                        }*/
        }

        /**
         * Cancel all items in the queue
         */
        public void cancelAllItems() {
            handleCancelAll();

            if (playQueue.size() > 0) {
                cancelItem(); //cancel and remove first item
                while (playQueue.size()>0){
                    QueueItem item = playQueue.get(0);
                    postSpeakableEvent(new SpeakableEvent(item,SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()));
                    playQueue.remove(0);
                }
            }

            while (queue.size() > 0){
                QueueItem item = queue.get(0);
                postSpeakableEvent(new SpeakableEvent(item,SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()));
                queue.remove(0);
            }
            /*            Speakable item = null;
                        Vector copy;

                        synchronized (queue) {
                            audio.cancel();
                            copy = (Vector) queue.clone();
                            queue.clear();
                            queueDrained();
                        }
                        for (Iterator i = copy.iterator(); i.hasNext(); ) {
                            item = (Speakable) i.next();
                            // item.postSpeakableCancelled();
                            item.cancelled();
                        }*/
        }


        /**
         * Cancel the given item.
         *
         * @param source the item to cancel.
         */
        protected void cancelItem(Object source) {
            /*            Speakable item = null;
                        synchronized (queue) {
                            int index = queue.indexOf(source);
                            if (index == 0) {
                                cancelItem();
                            } else {
                                item = (Speakable) queue.remove(index);
                                if (item != null) {
                                    // item.postSpeakableCancelled();
                                    item.cancelled();
                                    queueDrained();
                                }
                            }
                        }*/
        }

        protected void cancelItem(int id) {
            //search item in playqueue
            synchronized (playQueue) {
                for (int i = 0; i < playQueue.size(); ++i) {
                    QueueItem item = playQueue.get(i);
                    if (item.getId() == id) {
                        if (i == 0) {
                            cancelItem();
                        } else {
                            if (item.getAudioSegment() == null)
                                handleCancel(i);
                            postSpeakableEvent(new SpeakableEvent(item,SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()));
                            postSynthesizerEvent(getEngineState(), getEngineState(),SynthesizerEvent.QUEUE_UPDATED,false);
                            playQueue.remove(i);
                        }
                        return;
                    }
                }
            }

            //search item in queue
            synchronized (queue) {
                for (int i = 0; i < queue.size(); ++i) {
                    QueueItem item = queue.get(i);
                    if (item.getId() == id) {
                        postSpeakableEvent(new SpeakableEvent(item,SpeakableEvent.SPEAKABLE_CANCELLED, item.getId()));
                        queue.remove(i);
                        break;
                    }
                }
            }
        }


        /**
         * Return, but do not remove, the first item on the play queue.
         *
         * @return a queue item to play
         */
        protected QueueItem getQueueItemToPlay() {
            QueueItem item = null;
            synchronized (playQueue) {
                while (((playQueue.size() == 0) || (playQueue.get(0).getAudioSegment()==null)) && !done) {
                    try {
                        playQueue.wait();
                    } catch (InterruptedException e) {
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
         * Return, but do not remove, the first item on the queue.
         *
         * @return a queue item
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
                item = (QueueItem) queue.elementAt(0);

            }
            /////////////////////////item.postTopOfQueue();
            return item;
        }

        /**
         * removes the given item, posting the appropriate
         * events. The item may have already been removed (due to a
         * cancel).
         *
         * @param item the item to remove
         */
        protected void removeQueueItem(QueueItem item) {
            boolean queueEmptied = false;
            synchronized (queue) {
                boolean found = queue.removeElement(item);
                if (found) {
                    queueDrained();
                }
            }
        }

        /**
         * Should be called iff one or more items have been removed
         * from the queue. Generates the appropriate state changes and
         * events.
         */
        private void queueDrained() {
            /*            if (queue.size() == 0) {
             long[] states = setEngineState(synthesizer.QUEUE_NOT_EMPTY,
             synthesizer.QUEUE_EMPTY);
                            postQueueEmptied(states[0], states[1]);
                        } else {
             long[] states = setEngineState(synthesizer.QUEUE_NOT_EMPTY,
             synthesizer.QUEUE_NOT_EMPTY);
                            postQueueUpdated(true, states[0], states[1]);
                        }*/
        }


        public void setAudioSegment(int id, AudioSegment audioSegment) {
            synchronized (playQueue) {
                for (QueueItem q : playQueue) {
                    if (q.getId() == id) {
                        q.setAudioSegment(audioSegment);
                        break;
                    }
                }

                playQueue.notifyAll();
            }
        }
    }


    /**
     * Set AudioSegment in a queueItem (Not JSAPI2)
     * @param itemId int
     * @param audioSegment AudioSegment
     */
    protected void setAudioSegment(int id, AudioSegment audioSegment) {
        queueManager.setAudioSegment(id, audioSegment);
    }


}
