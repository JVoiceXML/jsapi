/**
 * 
 */
package org.jvoicexml.jsapi2;


import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.jvoicexml.jsapi2.mock.MockAudioManager;

/**
 * Test cases for {@link BaseAudioManager}.
 * @author Dirk Schnelle-Walka
 *
 */
public final class BaseAudioManagerTest {

    @After
    public void tearDown() {
        System.setSecurityManager(null);
    }
    /**
     * Test method for {@link org.jvoicexml.jsapi2.BaseAudioManager#setMediaLocator(java.lang.String)}.
     * @throws Exception
     *         test failed
     */
    @Test
    public void testSetMediaLocator() throws Exception {
        final BaseAudioManager manager = new MockAudioManager();
        final String locator = "file://test.wav";
        manager.setMediaLocator(locator);
        Assert.assertEquals(locator, manager.getMediaLocator());
    }

//    /**
//     * Test method for {@link org.jvoicexml.jsapi2.BaseAudioManager#setMediaLocator(java.lang.String)}.
//     * @throws Exception
//     *         test failed
//     */
//    @Test(expected = SecurityException.class)
//    public void testSetMediaLocatorSecurityException() throws Exception {
//        System.setSecurityManager(new SecurityManager());
//        final BaseAudioManager manager = new MockAudioManager();
//        final String locator = "file://test.wav";
//        manager.setMediaLocator(locator);
//    }

}
