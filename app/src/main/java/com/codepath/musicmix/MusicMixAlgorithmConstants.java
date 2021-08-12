package com.codepath.musicmix;

public interface MusicMixAlgorithmConstants {

    // Question 1 option constants
    public static final String OPTION_HAPPY = "Happy";
    public static final String OPTION_ANGRY = "Angry";
    public static final String OPTION_SAD = "Sad";
    public static final String OPTION_NERVOUS = "Nervous";

    public static final String[] KEYWORDS_HAPPY = new String[] {"happy", "sunshine", "good", "fun", "danc", "sun", "dream", "beautiful", "live", "excited"};
    public static final String[] KEYWORDS_ANGRY = new String[] {"break", "hate", "hell", "angry", "never", "kill", "anger", "misery", "revenge", "mad"};
    public static final String[] KEYWORDS_SAD = new String[] {"sad", "hurt", "loved", "wreck", "goodbye", "heart", "cry", "tear", "burn", "broken"};
    public static final String[] KEYWORDS_NERVOUS = new String[] {"nervous", "save", "fake", "breath", "lonely", "lost", "calm%20down", "stress", "numb", "mind"};

    // Question 2 option constants
    public static final String OPTION_RELAXING = "Relaxing";
    public static final String OPTION_PARTY = "Going out / Partying";
    public static final String OPTION_EXERCISING = "Exercising";
    public static final String OPTION_WORKING = "Working";

    // Question 3 option constants
    public static final String OPTION_INSTRUMENTALS = "Instrumentals";
    public static final String OPTION_ELECTRONIC = "Electronic/digital sounds";
    public static final String OPTION_VOCAL = "Vocal";

    // Question 4 option constants
    public static final String OPTION_MOTIVATIONAL = "Motivational";
    public static final String OPTION_CALMING = "Calming";
    public static final String OPTION_CHEERFUL = "Cheerful";
    public static final String OPTION_SORROWFUL = "Sorrowful";

    // Algorithm constants
    public static final double RELAXING_DANCEABILITY = 0.3;
    public static final double PARTYING_DANCEABILITY = 0.7;
    public static final double EXERCISING_DANCEABILITY_LOW = 0.5;
    public static final double EXERCISING_DANCEABILITY_HIGH = 0.7;
    public static final double WORKING_DANCEABILITY_LOW = 0.3;
    public static final double WORKING_DANCEABILITY_HIGH = 0.5;

    public static final double INSTRUMENTAL_INSTRUMENTALNESS = 0.5;
    public static final double ELECTRONIC_ACOUSTICNESS = 0.3;
    public static final double VOCAL_INSTRUMENTALNESS = 0.4;

    public static final double MOTIVATIONAL_TEMPO = 116.0;
    public static final double CALMING_LOUDNESS = -10.0;
    public static final double CHEERFUL_VALENCE = 0.6;
    public static final double SORROWFUL_VALENCE = 0.4;

    public static final double ENERGY_0 = 0.0;
    public static final double ENERGY_1 = 0.2;
    public static final double ENERGY_2 = 0.4;
    public static final double ENERGY_3 = 0.6;
    public static final double ENERGY_4 = 0.8;
    public static final double ENERGY_5 = 1.0;


}
