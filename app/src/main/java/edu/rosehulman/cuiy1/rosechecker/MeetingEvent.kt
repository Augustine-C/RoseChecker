package edu.rosehulman.cuiy1.rosechecker

data class MeetingEvent (var state : EventState,
                         var numPeople : Int,
                         var members : String = "",
                         var agenda: String = ""){
}