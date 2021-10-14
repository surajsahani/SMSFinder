package com.martialcoder.smsfinder

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import java.text.SimpleDateFormat
import java.util.*

class Persenter(private val mContext:Context, private val view: Interface.View):
    Interface {
    override fun empty(number: String?): Boolean {
        return number.isNullOrBlank()
    }

    override fun validate(number: String?): Boolean {
        if(number!!.length < 10) {
            return true
        }
        return false
    }

    override fun days(day: String?): Boolean {
        return day.isNullOrBlank()
    }

    override fun fetch(number: String?, day: String?) {
        when {
            empty(number) -> {
                view.mobile(mContext.getString(R.string.error_empty_number))
                return
            }
            validate(number) -> {
                view.invalid(mContext.getString(R.string.error_invalid_number))
                return
            }
            days(day) -> {
                view.emptyDays(mContext.getString(R.string.error_empty_day))
                return
            }
            else -> {
                var count = 0
                val filter =
                    arrayOf("+91" + number!!.trim(), startDate(day)!!.time.toString().trim())
                val inboxURI: Uri = Uri.parse("content://sms")
                val cursor: Cursor? = mContext.contentResolver.query(
                    inboxURI,
                    arrayOf("_id", "thread_id","address","person", "date", "body", "type"),
                    Telephony.Sms.ADDRESS + "=? AND " + Telephony.Sms.DATE + ">=?",
                    filter,
                    null
                )
                while (cursor!!.moveToNext()) {
                    count +=1
                }
                cursor.close()
                if (count > 0)
                    view.sucess("$count " + mContext.getString(R.string.sms_found))
                else
                    view.failure(mContext.getString(R.string.no_sms_found))
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