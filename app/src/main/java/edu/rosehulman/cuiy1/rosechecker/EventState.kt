package edu.rosehulman.cuiy1.rosechecker

data class EventState(var name:String,
                      var location : String,
                      var time: Int,
                      var duration: Int,
                      var isFinished: Boolean,
                      var importance: Int) {
}