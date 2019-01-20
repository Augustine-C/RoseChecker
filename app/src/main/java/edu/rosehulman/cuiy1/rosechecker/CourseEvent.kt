package edu.rosehulman.cuiy1.rosechecker

data class CourseEvent(var state : EventState,
                       var chapterCovered : String = "",
                       var keyContent : String = "",
                       var homework : String = "",
                       var preparation : String) {

}