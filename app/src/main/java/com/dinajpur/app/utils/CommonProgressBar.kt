package com.dinajpur.app.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class CommonProgressBar(private val context: Context) {

    private var progressBar: ProgressBar? = null
    private var progressBarContainer: RelativeLayout? = null
    private val activity: AppCompatActivity? = context as? AppCompatActivity

    /**
     * Show the progress bar.
     */
    fun show() {
        if (progressBar == null && activity != null) {
            // Create the container for the progress bar
            progressBarContainer = RelativeLayout(context).apply {
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                //setBackgroundColor(Color.parseColor("#80000000")) // Semi-transparent black
            }

            // Create the progress bar
            progressBar = ProgressBar(context, null, android.R.attr.progressBarStyle).apply {
                isIndeterminate = true
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.CENTER_IN_PARENT)
                }
            }

            // Add the progress bar to the container
            progressBarContainer?.addView(progressBar)

            // Add the container to the root view
            activity.findViewById<View>(android.R.id.content)?.let { contentView ->
                if (contentView is ViewGroup) {
                    contentView.addView(progressBarContainer)
                } else {
                    println("Content view is not a ViewGroup!")
                }
            }
        }
        progressBar?.visibility = View.VISIBLE
    }

    /**
     * Hide the progress bar.
     */
    fun hide() {
        progressBar?.visibility = View.GONE
    }

    /**
     * Destroy the progress bar and remove it from the view hierarchy.
     */
    fun destroy() {
        progressBarContainer?.let { container ->
            activity?.findViewById<View>(android.R.id.content)?.let { contentView ->
                if (contentView is ViewGroup) {
                    contentView.removeView(container)
                }
            }
        }
        progressBar = null
        progressBarContainer = null
    }
}