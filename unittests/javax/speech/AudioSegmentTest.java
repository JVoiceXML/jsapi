/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate: $
 * Author:  $LastChangedBy: $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package javax.speech;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.AudioSegment}.
 * 
 * @author Dirk Schnelle
 */
public class AudioSegmentTest extends TestCase {
   
    /**
     * Test method for {@link javax.speech.AudioSegment#getMediaLocator()}.
     */
    public void testGetLocator() {
        final String locator1 = "file:///user/smith/hello.wav";
        final String markup1 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment1 = 
            new AudioSegment(locator1, markup1);
        assertEquals(locator1, segment1.getMediaLocator());
        
        byte[] bytes = new byte[256];
        final ByteArrayInputStream stream2 = new ByteArrayInputStream(bytes);
        final String locator2 = "file:///user/smith/hello2.wav";
        final String markup2 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment2 = 
            new AudioSegment(stream2, locator2, markup2);
        assertEquals(locator2, segment2.getMediaLocator());
    }

    /**
     * Test method for {@link javax.speech.AudioSegment#getMarkupText()}.
     */
    public void testGetMarkupText() {
        final String locator1 = "file:///user/smith/hello.wav";
        final String markup1 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment1 = 
            new AudioSegment(locator1, markup1);
        assertEquals(markup1, segment1.getMarkupText());
        
        byte[] bytes = new byte[256];
        final ByteArrayInputStream stream2 = new ByteArrayInputStream(bytes);
        final String locator2 = "file:///user/smith/hello2.wav";
        final String markup2 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment2 = 
            new AudioSegment(stream2, locator2, markup2);
        assertEquals(markup2, segment2.getMarkupText());
    }

    /**
     * Test method for {@link javax.speech.AudioSegment#getInputStream()}.
     */
    public void testGetInputStream() {    	
        System.setProperty("javax.speech.supports.audio.capture", "true");
    	final String locator1 = "file:///user/smith/hello.wav";
        final String markup1 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment1 = 
            new AudioSegment(locator1, markup1);
        assertNull(segment1.getInputStream());
        
        byte[] bytes = new byte[256];
        final ByteArrayInputStream stream2 = new ByteArrayInputStream(bytes);
        final String locator2 = "file:///user/smith/hello2.wav";
        final String markup2 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment2 = 
            new AudioSegment(stream2, locator2, markup2);
        assertEquals(stream2, segment2.getInputStream());

        System.setProperty("javax.speech.supports.audio.capture", "false");
    	final String locator3 = "file:///user/smith/hello.wav";
        final String markup3 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment3 = 
            new AudioSegment(locator3, markup3);
        Exception failure = null;
        try {
        	segment3.getInputStream();
        } catch (SecurityException e) {
        	failure = e;
        }
        assertNotNull(failure);
        
        byte[] bytes4 = new byte[256];
        final ByteArrayInputStream stream4 = new ByteArrayInputStream(bytes);
        final String locator4 = "file:///user/smith/hello2.wav";
        final String markup4 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment4 = 
            new AudioSegment(stream4, locator4, markup4);
        failure = null;
        try {
        	segment4.getInputStream();
        } catch (SecurityException e) {
        	failure = e;
        }
        assertNotNull(failure);
    }

    /**
     * Test method for {@link javax.speech.AudioSegment#isGettable()}.
     */
    public void testIsGettable() {
        System.setProperty("javax.speech.supports.audio.capture", "true");
    	final String locator1 = "file:///user/smith/hello.wav";
        final String markup1 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment1 = 
            new AudioSegment(locator1, markup1);
        assertTrue(segment1.isGettable());
        
        byte[] bytes = new byte[256];
        final ByteArrayInputStream stream2 = new ByteArrayInputStream(bytes);
        final String locator2 = "file:///user/smith/hello2.wav";
        final String markup2 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment2 = 
            new AudioSegment(stream2, locator2, markup2);
        assertTrue(segment2.isGettable());

        System.setProperty("javax.speech.supports.audio.capture", "false");
    	final String locator3 = "file:///user/smith/hello.wav";
        final String markup3 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment3 = 
            new AudioSegment(locator3, markup3);
        assertFalse(segment1.isGettable());
        
        byte[] bytes4 = new byte[256];
        final ByteArrayInputStream stream4 = new ByteArrayInputStream(bytes);
        final String locator4 = "file:///user/smith/hello2.wav";
        final String markup4 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment4 = 
            new AudioSegment(stream4, locator4, markup4);
        assertFalse(segment4.isGettable());
    }

}
