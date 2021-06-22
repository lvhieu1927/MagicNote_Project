package com.example.magicnote1.intro;

public class ScreenItem {
    String titleName,description;
    int imgScreen;

    public ScreenItem(String title, String description, int imgScreen) {
        this.titleName = title;
        this.description = description;
        this.imgScreen = imgScreen;
    }

    public String getTitle() {
        return titleName;
    }

    public void setTitle(String title) {
        this.titleName = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgScreen() {
        return imgScreen;
    }

    public void setImgScreen(int imgScreen) {
        this.imgScreen = imgScreen;
    }
}
