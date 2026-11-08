package com.example.contactapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class AddEditContactActivity : AppCompatActivity() {
    private lateinit var db: ContactDbHelper
    private var contactId: Long = -1L
    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        db = ContactDbHelper(this)

        nameInput = findViewById(R.id.nameInput)
        phoneInput = findViewById(R.id.phoneInput)
        emailInput = findViewById(R.id.emailInput)

        contactId = intent.getLongExtra(EXTRA_ID, -1L)
        val editing = contactId != -1L
        findViewById<MaterialToolbar>(R.id.toolbar).apply {
            title = if (editing) "Edit contact" else "New contact"
            setNavigationOnClickListener { finish() }
        }

        if (editing) {
            nameInput.setText(intent.getStringExtra(EXTRA_NAME))
            phoneInput.setText(intent.getStringExtra(EXTRA_PHONE))
            emailInput.setText(intent.getStringExtra(EXTRA_EMAIL))
        }

        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener { saveContact() }
    }

    private fun saveContact() {
        val name = nameInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val email = emailInput.text.toString().trim()

        if (name.isBlank()) {
            nameInput.error = "Name is required"
            nameInput.requestFocus()
            return
        }
        if (phone.isBlank()) {
            phoneInput.error = "Phone number is required"
            phoneInput.requestFocus()
            return
        }

        val contact = Contact(id = contactId, name = name, phone = phone, email = email)
        val saved = if (contactId == -1L) db.insertContact(contact) > 0 else db.updateContact(contact) > 0
        if (saved) {
            Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Could not save contact", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_ID = "contact_id"
        const val EXTRA_NAME = "contact_name"
        const val EXTRA_PHONE = "contact_phone"
        const val EXTRA_EMAIL = "contact_email"
    }
}
