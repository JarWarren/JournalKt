package com.jarwarren.journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity: AppCompatActivity() {

    var entry: WRREntry? = null

    // viewDidLoad()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set the "storyboard" again
        setContentView(R.layout.activity_detail)

        /*
        adds an "up button" (back arrow) to the app bar (navigation bar)
        button appears but has no action defined unless added in the manifest
        android:parentActivityName="com.JarWarren.journal.MainActivity"
        */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       entry = intent?.extras?.getParcelable("entry") as? WRREntry
        entry?.let {
            titleTextView.setText(it.title)
            bodyTextView.setText(it.body)
        }

        // behavior for saveButton
        saveButton.setOnClickListener {

            entry?.let {

                if (titleTextView.text.isNotEmpty()) {
                    WRREntryController.update(it,titleTextView.text.toString(),bodyTextView.text.toString())
                } else {
                    WRREntryController.delete(it)
                }
                finish()
            }

            if (entry == null && titleTextView.text.isNotEmpty()) {
                WRREntryController.createEntry(titleTextView.text.toString(), bodyTextView.text.toString())
                finish()
            }
        }
    }
}