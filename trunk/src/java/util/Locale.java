/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 46 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
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

package java.util;

public final class Locale {
    public static final Locale ENGLISH;

    public static final Locale FRENCH;

    public static final Locale GERMAN;

    /**
     * The default locale. Except for during bootstrapping, this should never be
     * null. Note the logic in the main constructor, to detect when
     * bootstrapping has completed.
     */
    private static Locale DEFAULT_LOCALE;

    static {
        ENGLISH = new Locale("en");
        FRENCH = new Locale("fr");
        GERMAN = new Locale("de");

        String defaultLanguage = System.getProperty("microedition.locale");
        if (defaultLanguage == null) {
            defaultLanguage = "en";

        }
        DEFAULT_LOCALE = new Locale(defaultLanguage);
    }

    private String language;

    private String country;

    private String variant;

    /**
     * Convert new iso639 codes to the old ones.
     * 
     * @param language
     *            the language to check
     * @return the appropriate code
     */
    private String convertLanguage(String language) {
        if (language.equals(""))
            return language;
        language = language.toLowerCase();
        int index = "he,id,yi".indexOf(language);
        if (index != -1)
            return "iw,in,ji".substring(index, index + 2);
        return language;
    }

    public Locale(String language, String country, String variant) {
        this.language = language;
        this.country = country;
        this.variant = variant;
    }

    public Locale(String language, String country) {
        this(language, country, "");
    }

    public Locale(String language) {
        this(language, "", "");
    }

    public static Locale getDefault() {
        return DEFAULT_LOCALE;
    }

    public static Locale[] getAvailableLocales() {
        return new Locale[] { ENGLISH, FRENCH, GERMAN };
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getVariant() {
        return variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Locale)) {
            return false;
        }
        
        Locale otherLocale = (Locale) obj;

        String otherLanguage = otherLocale.getLanguage();
        if (!language.equals(otherLanguage)) {
            return false;
        }

        String otherCountry = otherLocale.getCountry();
        if (!country.equals(otherCountry)) {
            return false;
        }
        
        String otherVariant = otherLocale.getVariant();
        return country.equals(otherCountry);
    }

    public final String toString() {
        if ((language.length() == 0) && (country.length() == 0)) {
            return "";
        }

        StringBuffer str = new StringBuffer();
        str.append(language);
        str.append('_').append(country);
        if (variant.length() != 0) {
            str.append('_').append(variant);
        }

        return str.toString();
    }

}
