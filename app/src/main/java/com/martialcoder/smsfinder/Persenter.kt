package com.martialcoder.smsfinder

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import java.text.SimpleDateFormat
import java.util.*

class Persenter(private val mContext:Context, private val view:InterfacePresenter.View): InterfacePresenter {
    override fun Empty(number: String?): Boolean {
        return number.isNullOrBlank()
    }

    override fun Validate(number: String?): Boolean {
        if(number!!.length < 10) {
            return true
        }
        return false
    }

    override fun Days(day: String?): Boolean {
        return day.isNullOrBlank()
    }

    override fun fetch(number: String?, day: String?) {
        when {
            Empty(number) -> {
                view.Mobile("Please Mobile")
                return
            }
            Validate(number) -> {
                view.invalid("Please enter valid")
                return
            }
            Days(day = null) -> {
                view.Days("number of days")
                return
            }
            else -> {
                var count = 0
                val filter =
                    arrayOf("+91" + number!!.trim(), Date(day)!!.time.toString().toString().trim())
                val inboxURI: Uri = Uri.parse("content://sms")
                val cursor: Cursor? = mContext.contentResolver.query(
                    inboxURI,
                    arrayOf("_id", "thread_id","address", "date", "body", "type"),
                    Telephony.Sms.ADDRESS +"=? AND" + Telephony.Sms.DATE + ">=?",
                    filter,
                    null
                )
                while (cursor!!.moveToNext()) {
                    count +=1
                }
                cursor.close()
                if (count > 0)
                    view.Sucess("$count" + "total messages")
                else
                    view.Failure("No messages")
            }
        }
    }
    private fun startDate(day: String?): Date? {
        val cal: Calendar = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -(day!!.trim().toInt()))
        val formatter = SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss")
        val selectFormat = Date(cal.timeInMillis) + "T00:00:00"
        return formatter.parse(selectFormat)

    }
    private fun Date(timeInMilli: Long):String {
        val formatter = SimpleDateFormat("dd-MM-yyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMilli
        return formatter.format(calendar.time)
    }
 }