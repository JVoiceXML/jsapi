/**
 * 
 */
package javax.speech;


/**
 * A <code>null</code> implementation of a security check. This class only
 * exists to create a hook for implementations of security checks for the
 * Java Speech API. Since CLDC 1.0 does not know anything about
 * {@link java.security.Permission} this approach is necessary. 
 * This class should never be in the classpath of a
 * runtime.
 * <p>
 * Implementations are requested to keep the signature of this method and
 * perform a security check with the help of a class
 * <code>SpeechPermissiony</code> that needs to be written and integrated into
 * the implementation
 * </p>.
 * <pre>
 * package javax.speech;
 * 
 * import javax.security.Permission;
 * 
 * public class SpeechPermission extends Permission {
 *    ...
 * }
 * </pre>
 * 
 * @author Dirk Schnelle-Walka
 * 
 */
public class JavaSpeechSecurity {
    /**
     * Must be overriden by implementations.
     * @param name permission to check for
     * @throws SecurityException
     *         always
     */
    public static void checkPermission(final String name) throws SecurityException {
        throw new SecurityException("null implementation rejected '" + name
                + "'");
    }
}
