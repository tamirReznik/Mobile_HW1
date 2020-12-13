package com.college.mobile_hw1.general;

import org.jetbrains.annotations.NotNull;

public class Player {


    private String name;
    private double lat, lon;
    private String avatar;
    private int score;

    public Player(String name, double lat, double lon, String avatar, int score) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.avatar = avatar;
        this.score = score;
    }
    public Player(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public @NotNull String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", avatar='" + avatar + '\'' +
                ", score=" + score +
                '}';
    }
}
