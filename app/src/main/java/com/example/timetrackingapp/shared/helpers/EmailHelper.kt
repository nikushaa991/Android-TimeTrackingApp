package com.example.timetrackingapp.shared.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.timetrackingapp.R
import com.example.timetrackingapp.domain.model.Task
import com.example.timetrackingapp.shared.secondsToTimeText

object EmailHelper {
    fun sendEmail(context: Context, tasks: List<Task>) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject))
        intent.putExtra(Intent.EXTRA_TEXT, generateBody(tasks))
        intent.data = Uri.parse("mailto:")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun generateBody(tasks: List<Task>): String {
        var body = "Task\tDescription\tTime\n"
        tasks.forEach {
            body += "${it.name}\t${it.desc}\t${it.time.secondsToTimeText()}\n"
        }
        return body
    }
}