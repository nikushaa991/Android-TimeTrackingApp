package com.example.timetrackingapp.shared

import android.content.Context
import android.view.View
import android.widget.Toast

fun showToast(context: Context, resource: Int) {
    Toast.makeText(context, context.getString(resource), Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun View.toggleVisibility(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}
