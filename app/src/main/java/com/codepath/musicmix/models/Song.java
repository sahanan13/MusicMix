package com.codepath.musicmix.models;

import com.codepath.musicmix.MusicMixAlgorithmConstants;

public class Song implements MusicMixAlgorithmConstants {
    private String id;
    private String name;
    private String uri;
    private String artist;
    private String songImageUrl;
    private double danceability;
    private double instrumentalness;
    private double acousticness;
    private double tempo;
    private double loudness;
    private double valence;
    private double energy;

    public Song() {} //empty constructor

    public Song(String id, String name, String uri) {
        this.name = name;
        this.id = id;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongImageUrl() { return songImageUrl; }

    public void setSongImageUrl(String songImageUrl) { this.songImageUrl = songImageUrl; }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.id = uri;
    }

    public String toString() {
        return "Name: " + name + "| id: " + id;
    }

    public double getDanceability() {
        return danceability;
    }

    public void setDanceability(double danceability) {
        this.danceability = danceability;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public double getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(double acousticness) {
        this.acousticness = acousticness;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public double getLoudness() {
        return loudness;
    }

    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    public double getValence() {
        return valence;
    }

    public void setValence(double valence) {
        this.valence = valence;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public boolean isRelaxing() {
        if (danceability < RELAXING_DANCEABILITY) {
            return true;
        }
        return false;
    }

    public boolean isParty() {
        if (danceability >= PARTYING_DANCEABILITY) {
            return true;
        }
        return false;
    }

    public boolean isExercising() {
        if (danceability >= EXERCISING_DANCEABILITY_LOW && danceability < EXERCISING_DANCEABILITY_HIGH) {
            return true;
        }
        return false;
    }

    public boolean isWorking() {
        if (danceability >= WORKING_DANCEABILITY_LOW && danceability < WORKING_DANCEABILITY_HIGH) {
            return true;
        }
        return false;
    }

    public boolean isInstrumental() {
        if (instrumentalness >= INSTRUMENTAL_INSTRUMENTALNESS) {
            return true;
        }
        return false;
    }

    public boolean isElectronic() {
        if (acousticness <= ELECTRONIC_ACOUSTICNESS) {
            return true;
        }
        return false;
    }

    public boolean isVocal() {
        if (instrumentalness <= VOCAL_INSTRUMENTALNESS) {
            return true;
        }
        return false;
    }

    public boolean isMotivational() {
        if (tempo >= MOTIVATIONAL_TEMPO) {
            return true;
        }
        return false;
    }

    public boolean isCalming() {
        if (loudness >= CALMING_LOUDNESS) {
            return true;
        }
        return false;
    }

    public boolean isCheerful() {
        if (valence >= CHEERFUL_VALENCE) {
            return true;
        }
        return false;
    }

    public boolean isSorrowful() {
        if (valence <= SORROWFUL_VALENCE) {
            return true;
        }
        return false;
    }

    public boolean isInEnergyLevels(double energyLow, double energyHigh) {
        if (energy > energyLow && energy <= energyHigh) {
            return true;
        }
        return false;
    }

}
