package org.tensorflow.lite.examples.classification

class PlantModel(private var plantName: String,
                 private var plantSpecies: String,
                 private var plantSite: String) {
    // Getter and Setter
    fun getPlantName():String{
        return plantName
    }
    fun setPlantName(plantName:String){
        this.plantName = plantName
    }
    fun getPlantSpecies():String{
        return plantSpecies
    }
    fun setPlantSpecies(plantSpecies:String){
        this.plantSpecies = plantSpecies
    }
    fun getPlantSite():String{
        return plantSite
    }
    fun setPlantSite(plantSite:String){
        this.plantSite = plantSite
    }

}