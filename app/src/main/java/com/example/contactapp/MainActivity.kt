package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var db: ContactDbHelper
    private lateinit var adapter: ContactAdapter
    private lateinit var searchInput: EditText
    private lateinit var emptyState: TextView
    private lateinit var countLabel: TextView
    private var allContacts: List<Contact> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = ContactDbHelper(this)
        searchInput = findViewById(R.id.searchInput)
        emptyState = findViewById(R.id.emptyState)
        countLabel = findViewById(R.id.countLabel)
        val recycler = findViewById<RecyclerView>(R.id.contactsRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(
            emptyList(),
            onContactClick = { showContactDetails(it) },
            onEditClick = { openEditor(it) },
            onDeleteClick = { confirmDelete(it) }
        )
        recycler.adapter = adapter

        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener { openEditor(null) }
        searchInput.addTextChangedListener(SimpleTextWatcher { filterContacts(it) })
    }

    override fun onResume() {
        super.onResume()
        refreshContacts()
    }

    private fun refreshContacts() {
        allContacts = db.getAllContacts()
        filterContacts(searchInput.text?.toString().orEmpty())
    }

    private fun filterContacts(query: String) {
        val q = query.trim()
        val filtered = if (q.isBlank()) allContacts else allContacts.filter {
            it.name.contains(q, true) || it.phone.contains(q, true) || it.email.contains(q, true)
        }
        adapter.updateData(filtered)
        countLabel.text = "${filtered.size} ${if (filtered.size == 1) "CONTACT" else "CONTACTS"}"
        emptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        emptyState.text = if (q.isBlank()) "No contacts yet. Add your first contact." else "No contacts match “$q”."
    }

    private fun openEditor(contact: Contact?) {
        val intent = Intent(this, AddEditContactActivity::class.java)
        contact?.let {
            intent.putExtra(AddEditContactActivity.EXTRA_ID, it.id)
            intent.putExtra(AddEditContactActivity.EXTRA_NAME, it.name)
            intent.putExtra(AddEditContactActivity.EXTRA_PHONE, it.phone)
            intent.putExtra(AddEditContactActivity.EXTRA_EMAIL, it.email)
        }
        startActivity(intent)
    }

    private fun showContactDetails(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle(contact.name)
            .setMessage("Phone: ${contact.phone}\nEmail: ${contact.email.ifBlank { "Not provided" }}")
            .setPositiveButton("Close", null)
            .setNeutralButton("Edit") { _, _ -> openEditor(contact) }
            .show()
    }

    private fun confirmDelete(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Delete contact?")
            .setMessage("Remove ${contact.name} from your contacts?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                db.deleteContact(contact.id)
                refreshContacts()
            }
            .show()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}
