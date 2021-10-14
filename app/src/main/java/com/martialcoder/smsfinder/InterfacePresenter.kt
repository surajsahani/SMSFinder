package com.martialcoder.smsfinder

interface InterfacePresenter {
    fun Empty(number: String?):Boolean
    fun Validate(number: String?):Boolean
    fun Days(day: String?):Boolean
    fun fetch(number: String?, day: String?)
    interface View{
        fun Mobile(message:String)
        fun Days(message:String)
        fun invalid(message:String)
        fun Sucess(message:String)
        fun Failure(message:String)
    }
}