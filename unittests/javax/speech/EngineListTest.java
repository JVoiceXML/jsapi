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

import java.util.Enumeration;
import java.util.Locale;

import javax.speech.recognition.RecognizerMode;
import javax.speech.synthesis.SynthesizerMode;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.EngineList}.
 * 
 * @author Dirk Schnelle
 */
public class EngineListTest extends TestCase {
	/** The engine list to test. */
	private EngineList list;

	/**
	 * {@inheritDoc}
	 */
	protected void setUp() throws Exception {
		super.setUp();

		final EngineMode[] modes = new EngineMode[] {
				new SynthesizerMode(Locale.US),
				new RecognizerMode(Locale.GERMAN),
				new EngineMode("name1", "mode1", Boolean.TRUE, Boolean.TRUE,
						Boolean.TRUE) };
		list = new EngineList(modes);
	}

	/**
	 * Test method for
	 * {@link javax.speech.EngineList#anyMatch(javax.speech.EngineMode)}.
	 */
	public void testAnyMatch() {
		final EngineMode require1 = new EngineMode("name1", "mode1",
				Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
		assertTrue(list.anyMatch(require1));

		final EngineMode require2 = new EngineMode("name2", "mode1",
				Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
		assertFalse(list.anyMatch(require2));
	}

	/**
	 * Test method for {@link javax.speech.EngineList#elementAt(int)}.
	 */
	public void testElementAt() {
		EngineMode mode1 = list.elementAt(0);
		assertNotNull(mode1);
		EngineMode mode2 = list.elementAt(1);
		assertNotNull(mode2);
		EngineMode mode3 = list.elementAt(2);
		assertNotNull(mode3);
		Exception failure = null;
		try {
			list.elementAt(4);
		} catch (ArrayIndexOutOfBoundsException e) {
			failure = e;
		}
		assertNotNull(failure);
	}

	/**
	 * Test method for {@link javax.speech.EngineList#elements()}.
	 */
	public void testElements() {
		Enumeration enumeration = list.elements();
		assertNotNull(enumeration);
		assertTrue(enumeration.hasMoreElements());
		assertNotNull(enumeration.nextElement());
		assertTrue(enumeration.hasMoreElements());
		assertNotNull(enumeration.nextElement());
		assertTrue(enumeration.hasMoreElements());
		assertNotNull(enumeration.nextElement());
		assertFalse(enumeration.hasMoreElements());
	}

	/**
	 * Test method for
	 * {@link javax.speech.EngineList#orderByMatch(javax.speech.EngineMode)}.
	 */
	public void testOrderByMatch() {
		final EngineMode require = new EngineMode("name1", "mode1",
				Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
		list.orderByMatch(require);
		final EngineMode mode = list.elementAt(0);
		assertEquals(3, list.size());
		assertEquals(require, mode);
	}

	/**
	 * Test method for
	 * {@link javax.speech.EngineList#rejectMatch(javax.speech.EngineMode)}.
	 */
	public void testRejectMatch() {
		assertEquals(3, list.size());
		final EngineMode reject = new EngineMode("name1", "mode1",
				Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
		list.rejectMatch(reject);
		assertEquals(2, list.size());
		list.rejectMatch(null);
		assertEquals(0, list.size());
	}

	/**
	 * Test method for {@link javax.speech.EngineList#removeElementAt(int)}.
	 */
	public void testRemoveElementAt() {
		assertEquals(3, list.size());
		list.removeElementAt(2);
		assertEquals(2, list.size());

		Exception failure = null;
		try {
			list.removeElementAt(2);
		} catch (ArrayIndexOutOfBoundsException e) {
			failure = e;
		}
		assertNotNull(failure);
	}

	/**
	 * Test method for
	 * {@link javax.speech.EngineList#requireMatch(javax.speech.EngineMode)}.
	 */
	public void testRequireMatch() {
		assertEquals(3, list.size());
		final EngineMode require = new EngineMode("name1", "mode1",
				Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
		list.requireMatch(require);
		assertEquals(1, list.size());
		list.requireMatch(null);
		assertEquals(1, list.size());
	}

	/**
	 * Test method for {@link javax.speech.EngineList#size()}.
	 */
	public void testSize() {
		assertEquals(3, list.size());
	}

}
