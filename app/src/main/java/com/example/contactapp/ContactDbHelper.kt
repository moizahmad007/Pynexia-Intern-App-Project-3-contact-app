package com.example.contactapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABLE_CONTACTS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_PHONE TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL DEFAULT ''
            )""".trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        readableDatabase.query(
            TABLE_CONTACTS, null, null, null, null, null, "$COL_NAME COLLATE NOCASE ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                contacts += Contact(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL))
                )
            }
        }
        return contacts
    }

    fun insertContact(contact: Contact): Long {
        val values = ContentValues().apply {
            put(COL_NAME, contact.name.trim())
            put(COL_PHONE, contact.phone.trim())
            put(COL_EMAIL, contact.email.trim())
        }
        return writableDatabase.insert(TABLE_CONTACTS, null, values)
    }

    fun updateContact(contact: Contact): Int {
        val values = ContentValues().apply {
            put(COL_NAME, contact.name.trim())
            put(COL_PHONE, contact.phone.trim())
            put(COL_EMAIL, contact.email.trim())
        }
        return writableDatabase.update(TABLE_CONTACTS, values, "$COL_ID = ?", arrayOf(contact.id.toString()))
    }

    fun deleteContact(id: Long): Int =
        writableDatabase.delete(TABLE_CONTACTS, "$COL_ID = ?", arrayOf(id.toString()))

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTS = "contacts"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_PHONE = "phone"
        private const val COL_EMAIL = "email"
    }
}
