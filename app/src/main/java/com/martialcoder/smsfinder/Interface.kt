package com.martialcoder.smsfinder

interface Interface {
    fun empty(number: String?):Boolean
    fun validate(number: String?):Boolean
    fun days(day: String?):Boolean
    fun fetch(number: String?, day: String?)
    interface View{
        fun mobile(message:String)
        fun emptyDays(message:String)
        fun invalid(message:String)
        fun sucess(message:String)
        fun failure(message:String)
    }
}