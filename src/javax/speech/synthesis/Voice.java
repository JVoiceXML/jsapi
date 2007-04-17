package javax.speech.synthesis;

import java.util.Locale;

public class Voice {
    public static int AGE_CHILD = 0;

    public static int AGE_DONT_CARE = 1;

    public static int AGE_MIDDLE_ADULT = 2;

    public static int AGE_OLDER_ADULT = 3;

    public static int AGE_TEENAGER = 4;

    public static int AGE_YOUNGER_ADULT = 5;

    public static int GENDER_DONT_CARE = 6;

    public static int GENDER_FEMALE = 7;

    public static int GENDER_MALE = 8;

    public static int GENDER_NEUTRAL = 9;

    public static int VARIANT_DEFAULT = 10;

    public static int VARIANT_DONT_CARE = 11;

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

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    public boolean match(Voice require) {
        return false;
    }
}
