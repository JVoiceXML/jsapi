/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
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

package javax.speech.synthesis;

import java.util.Locale;

public class Voice {
    public static int AGE_CHILD = 8;

    public static int AGE_TEENAGER = 14;

    public static int AGE_YOUNGER_ADULT = 20;
    
    public static int AGE_MIDDLE_ADULT = 40;

    public static int AGE_OLDER_ADULT = 60;

    public static int AGE_DONT_CARE = -1;
    
    public static int GENDER_FEMALE = 1;

    public static int GENDER_MALE = GENDER_FEMALE << 1;

    public static int GENDER_NEUTRAL = GENDER_MALE << 1;

    public static int GENDER_DONT_CARE = GENDER_FEMALE | GENDER_MALE 
        | GENDER_NEUTRAL;
       
    public static int VARIANT_DEFAULT = 0;

    public static int VARIANT_DONT_CARE = -1;
    
    private Locale locale;

    private String name;

    private int gender;

    private int age;

    private int variant;

    public Voice() {
    }

    public Voice(Locale locale, String name, int gender, int age, int variant) {
        this.locale = locale;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.variant = variant;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public int getVariant() {
        return variant;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }

    public boolean match(Voice require) {
        final boolean namesMatch;
        final String requiredName = require.getName();
        if ((requiredName == null) || (name == null)) {
            namesMatch = true;
        } else {
            namesMatch = name.equals(requiredName);
        }
        
        final int requiredGender = require.getGender();
        final boolean genderMatch = ((gender & requiredGender) > 0); 
        
        final boolean localeMatch;
        final Locale requiredLocale = require.getLocale();
        if ((requiredLocale == null) || (requiredLocale == null)) {
            localeMatch = true;
        } else {
            localeMatch = locale.equals(requiredLocale);
        }

        final boolean ageMatch;
        final int requiredAge = require.getAge();
        if ((age == AGE_DONT_CARE) || (requiredAge == AGE_DONT_CARE)) {
            ageMatch = true;
        } else {
            final int closestAge = getClosestAge(requiredAge);
            ageMatch = (age == closestAge);
        }

        final boolean variantMatch;
        final int requiredVariant = require.getVariant();
        if ((variant == VARIANT_DONT_CARE) 
                || (requiredVariant == VARIANT_DONT_CARE)) {
            variantMatch = true;
        } else {
            variantMatch = (variant == requiredVariant);
        }
        
        return namesMatch || genderMatch || localeMatch || ageMatch 
            || variantMatch;
    }
    
    /**
     * Determines the age closest to the given age.
     * @param age the given age.
     * @return Age defined as a constant closest to the given age.
     */
    private int getClosestAge(int age) {
        if (age <= AGE_CHILD + (AGE_CHILD + AGE_TEENAGER) / 2) {
            return AGE_CHILD;
        }
        
        if (age <= AGE_TEENAGER + (AGE_TEENAGER + AGE_YOUNGER_ADULT) / 2) {
            return AGE_TEENAGER;
        }

        if (age <= AGE_YOUNGER_ADULT 
                + (AGE_YOUNGER_ADULT + AGE_MIDDLE_ADULT) / 2) {
            return AGE_YOUNGER_ADULT;
        }

        if (age <= AGE_MIDDLE_ADULT 
                + (AGE_MIDDLE_ADULT + AGE_OLDER_ADULT) / 2) {
            return AGE_MIDDLE_ADULT;
        }
        
        return AGE_OLDER_ADULT;
    }
}
