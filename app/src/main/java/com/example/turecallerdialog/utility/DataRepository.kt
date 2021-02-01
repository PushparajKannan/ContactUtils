package com.example.turecallerdialog.utility

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.turecallerdialog.database.dao.ContactDao
import com.example.turecallerdialog.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DataRepository(val context: Context,val database : ContactDao)
{


   suspend fun fetchContacts() {


        val data =  ArrayList<ContactModel>()

        val cursor: Cursor? = context.getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )


        cursor?.let {

            when{
                (it.count > 0)->{

                    while (cursor.moveToNext()) {
                       // val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))
                        val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        var phoneNo: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                       // val photoUri: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))



                        phoneNo = phoneNo.replace("\\s".toRegex(), "")


                        Log.e("contact", "getAllContacts: $name $phoneNo")

                        val contact = ContactModel(phoneNo,name,name,false)

                       // contact.originalName =  name
                       /// contact.phoneNumber = phoneNo
                        //contact.dummyName =""

                        if(!data.contains(contact)){
                            data.add(contact)
                        }


                    }
                }
            }

            cursor.close()

        }



       withContext(Dispatchers.IO){
           insertAllData(data)

       }












   }

    suspend fun insertData(model : ContactModel)
    {
        withContext(Dispatchers.IO){
            database.insert(model)
        }

    }

    suspend fun insertAllData(data : List<ContactModel>)
    {
        withContext(Dispatchers.IO){
            database.insertAll(data)
        }

    }
}