package com.codepath.musicmix.models;

import com.codepath.musicmix.MusicMixAlgorithmConstants;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel
public class Options implements MusicMixAlgorithmConstants {
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private ParseUser currentUser;

    // default constructor
    public Options() {}

    public Options(String option1, String option2, String option3, String option4, String option5, ParseUser currentUser) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.currentUser = currentUser;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public ParseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ParseUser currentUser) {
        this.currentUser = currentUser;
    }

    // testing methods
    private boolean isScale(String aScale) {
        return option5.equals(aScale);
    }

    public boolean isScale_1() {
        return isScale("1");
    }
    public boolean isScale_2() {
        return isScale("2");
    }
    public boolean isScale_3() {
        return isScale("3");
    }
    public boolean isScale_4() {
        return isScale("4");
    }
    public boolean isScale_5() {
        return isScale("5");
    }

    public String[] getOptions1Keywords() {
        String[] keywordArray;
        if (option1.equals(OPTION_HAPPY)) {
            keywordArray = KEYWORDS_HAPPY;
        } else if (option1.equals(OPTION_ANGRY)) {
            keywordArray = KEYWORDS_ANGRY;
        } else if (option1.equals(OPTION_SAD)) {
            keywordArray = KEYWORDS_SAD;
        } else {                //Nervous
            keywordArray = KEYWORDS_NERVOUS;
        }

        return keywordArray;
    }
}
