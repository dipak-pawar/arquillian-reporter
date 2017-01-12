package org.arquillian.reporter.api.utils;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Validate {

    public static boolean isEmpty(String string){
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotEmpty(String string){
        return !isEmpty(string);
    }
}
