package com.oklab.githubjourney.data;

/**
 * Created by olgakuklina on 2017-01-16.
 */

public class StarsDataEntry {

        private final String title;
        private final String type;
        private final String description;
        private final String language;
        private final int stars;
        private final int forks;

        public StarsDataEntry(String title, String type, String description, String language, int stars, int forks) {
            this.title = title;
            this.type = type;
            this.description = description;
            this.language = language;
            this.stars = stars;
            this.forks = forks;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public String getLanguage() {
            return language;
        }

        public int getStars() {
            return stars;
        }

        public int getForks() {
            return forks;
        }
}
