package org.tensorflow.lite.examples.classification;

public class HomePagePlantModel {
    private String plant_name;
    private String img_url;
    private String secondary_text;
    private String supporting_text;

    public HomePagePlantModel(String plant_name, String img_url, String secondary_text, String supporting_text) {
        this.plant_name = plant_name;
        this.img_url = img_url;
        this.secondary_text = secondary_text;
        this.supporting_text = supporting_text;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSecondary_text() {
        return secondary_text;
    }

    public void setSecondary_text(String secondary_text) {
        this.secondary_text = secondary_text;
    }

    public String getSupporting_text() {
        return supporting_text;
    }

    public void setSupporting_text(String supporting_text) {
        this.supporting_text = supporting_text;
    }
}
