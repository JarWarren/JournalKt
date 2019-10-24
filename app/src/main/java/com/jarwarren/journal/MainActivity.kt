package com.jarwarren.journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

// MARK: Main Activity

class MainActivity : AppCompatActivity() {

    // override func viewDidLoad()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // finds and sets the "storyboard" file named "activity_main.xml"
        setContentView(R.layout.activity_main)

        // loads all previously created entries into memory
        WRREntryController.loadEntries()

        // I have access to entriesRecyclerView because it's the ID in activity_main.xml
        // recycler view needs a LayoutManager and an Adapter(data source).
        entriesRecyclerView.apply {

            // unlike "self" in Swift, "this" does not escape the scope
            // "this@MainActivity" allows us to get the activity, instead of the recyclerView
            layoutManager = LinearLayoutManager(this@MainActivity)

            adapter = WRREntryAdapter()

            // adds separator. orientation seems to be an enum where 1 is horizontal
            // should be recyclerView's context, not MainActivity
            val separatorLine = DividerItemDecoration(context,1)
            addItemDecoration(separatorLine)
        }

        newEntryFAB.setOnClickListener {
            val detailActivity = Intent(this, DetailActivity::class.java)
            startActivity(detailActivity)
        }
    }

    // need to look into android activity lifecycle methods
    // onResume seems comparable but not identical to viewWillAppear()
    override fun onResume() {
        super.onResume()
        entriesRecyclerView.adapter?.notifyDataSetChanged()
    }
}

// DataSource class for EntriesRecyclerView. IRL might have been declared in a separate file.
// Declared as an Adapter which holds "EntryHolder" cells. The custom cell I have defined.
class WRREntryAdapter :  RecyclerView.Adapter<WRREntryAdapter.EntryHolder>() {

    // the awakeFromNib() for cells
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {

        // draws the rect for the cell, just as awakeFromNib() would normally in UIKit.
        // pass in a resource (nib/storyboard) as well as a context in order to assign its hierarchy
        val cell: View = LayoutInflater.from(parent.context).inflate(R.layout.holder_row_entries, parent, false)
        return EntryHolder(cell)
    }

    // tableView numberOfRows
    override fun getItemCount(): Int {

        // just like swift, but no need to say shared because the class itself is a singleton
        return WRREntryController.entries.size
    }

    // tableView cellForRowAt. gets called every time a cell is queued/reused
    // "holder" is the cell, "position" is the indexPath.row
    override fun onBindViewHolder(holder: EntryHolder, position: Int) {

        // very similar to Swift
        holder.titleView.text = WRREntryController.entries[position].title.toUpperCase()
        holder.bodyView.text = WRREntryController.entries[position].body
        holder.cellPosition = position
    }


    // Custom cell for each entry. Initialized by passing in a view.
    class EntryHolder(cell: View) : RecyclerView.ViewHolder(cell), View.OnClickListener {

        // "outlets"
        var titleView: TextView = cell.findViewById(R.id.titleTextView)
        var bodyView: TextView = cell.findViewById(R.id.bodyTextView)
        var cellPosition: Int = 0

        // the cell will not actually fire "onClick" unless you set an onClick listener
        init {
            cell.setOnClickListener(this)
        }


        // didSelect cell
        override fun onClick(v: View?) {

            val detailActivityIntent = Intent(v?.context, DetailActivity::class.java)
            val entry = WRREntryController.entries[cellPosition]
            detailActivityIntent.putExtra("entry", entry)
            v?.context?.startActivity(detailActivityIntent)
        }
    }
}