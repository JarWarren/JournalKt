package com.jarwarren.journal

import android.os.Parcel
import android.os.Parcelable

// MODEL
// TODO: Test whether "data" class makes a difference to this program. Either way, I believe the model should be a data class and not simply a class.
// if you don't explicitly state "var", then each of the properties will be constants/val.
data class WRREntry(var title: String,
               var body: String) : Parcelable {

    // entry must conform to parcelable in order to be passed asExtra through intent from MainActivity to DetailActivity
    // in java, it used to be Serializable. can still be done as Serializable in Kotlin. Though Parcelable is best practice because it is faster/more performant

    // Parcelable Implementation

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WRREntry> {
        override fun createFromParcel(parcel: Parcel): WRREntry {
            return WRREntry(parcel)
        }

        override fun newArray(size: Int): Array<WRREntry?> {
            return arrayOfNulls(size)
        }
    }
}


// MODEL CONTROLLER

// keyword "object" denotes a singleton class
object WRREntryController {

    // Source of Truth

    var entries = arrayListOf<WRREntry>()


    // CRUD

    fun createEntry(title: String, body: String) {
        println("CREATE")
        val newEntry = WRREntry(title, body)
        entries.add(newEntry)

    }

    fun update(entry: WRREntry, title: String, body: String) {
        println("UPDATE")
        println(title)
        println(body)

        /*
        entries are passed by value, not reference.
        so simply editing the argument entry will have no affect on the source of truth.
        need to use argument entry to get data source version and edit it directly.
        */

        // find index of identical entry
        val index = entries.indexOf(entry)

        // create a reference to it
        var updatedEntry = entries[index]

        // update it directly
        updatedEntry.title = title
        updatedEntry.body = body
    }

    fun delete(entry: WRREntry) {
        // FIXME: either deletes all (removeAt index) or deletes none (remove element)
        // removeAt "should" be used
        println("DELETE")
        val index = entries.indexOf(entry)
        entries.removeAt(index)
    }

    // TODO: private functions for persistence
    private fun autoSave() {
        // val file = File()
    }

    fun loadEntries() {

    }
}