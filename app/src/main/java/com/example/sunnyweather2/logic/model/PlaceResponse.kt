package com.example.sunnyweather2.logic.model

data class PlaceResponse (val status:String,val place:List<Place>){
}

data class Place(val name:String,val location:Location) {

}
data class Location(val lng:String,val let:String)
