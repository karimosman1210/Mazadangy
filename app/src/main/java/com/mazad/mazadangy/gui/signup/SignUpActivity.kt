
package com.mazad.mazadangy.gui.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mazad.mazadangy.R
import com.mazad.mazadangy.utels.StaticMethod
import com.mazad.mazadangy.utels.ToastUtel
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


private val IMAGE_PICK_CODE = 1000;
//Permission code
private val PERMISSION_CODE = 1001;

class SignUpActivity : AppCompatActivity(), SignUpInterface {
    lateinit var signUpPresenter: SignUpPresenter
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseRefrance: DatabaseReference
    lateinit var uri_load: String
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var progressDialog: ProgressDialog? = null
    // val storage = FirebaseStorage.getInstance()

    companion object {
        var antekaFav: Boolean = false
        var otherFav: Boolean = false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseDatabase = FirebaseDatabase.getInstance()
        signUpPresenter = SignUpPresenter(this)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        signUpBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                progressDialog=StaticMethod.createProgressDialog(this@SignUpActivity)
                progressDialog?.show()

                signUpData()

            }
        })


    }

    private fun signUpData() {
        var firstName = firstName.text.toString().trim()
        var lastName = lNameEt.text.toString().trim()
        var neckName = nickName.text.toString().trim()
        var email = emailTv.text.toString().trim()
        var password = passwordTv.text.toString().trim()
        var conPass = confirmPassTv.text.toString().trim()
        var phone = phoneTv.text.toString().trim()
        var interistTv = interistTv.text.toString().trim()
        if (antekatChB.isChecked) {
            antekaFav = true

        }
        if (othertChB.isChecked) {
            otherFav = true
        }
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(
                neckName
            )
            && TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(conPass) || TextUtils.isEmpty(
                phone
            )
            || TextUtils.isEmpty(interistTv)
        ) {
            ToastUtel.errorToast(this, "من فضلك املأ الفراغات")
            progressDialog?.dismiss()
        } else if (!password.equals(conPass)) {
            ToastUtel.errorToast(this, "كلمة السر غير متطابفة!")
            progressDialog?.dismiss()

        } else {
            signUpPresenter.checkSignUp(email, password, this)

        }


    }

    override fun errorSignUp() {
        ToastUtel.errorToast(this, "من فضلك ادخل البيانات بشكل صحيح ")
        progressDialog?.dismiss()

    }

    override fun sucessSignUp(uId: String) {
        databaseRefrance = firebaseDatabase.getReference("users").child(uId)
        databaseRefrance.child("firstName").setValue(firstName.text.toString().trim())
        databaseRefrance.child("lastName").setValue(lNameEt.text.toString().trim())
        databaseRefrance.child("nickname").setValue(nickName.text.toString().trim())
        databaseRefrance.child("email").setValue(emailTv.text.toString().trim())
        databaseRefrance.child("phoneNumber").setValue(phoneTv.text.toString().trim())
        databaseRefrance.child("uId").setValue(uId)
        val urlImge:String = intent.getStringExtra("imgeUrlProfile")
        databaseRefrance.child("image_profile").setValue(urlImge)
        databaseRefrance.child("interest").setValue(interistTv.text.toString().trim())
//        databaseRefrance.child("categories").child("anteka").setValue(antekaFav)
//        databaseRefrance.child("categories").child("other").setValue(otherFav)
        progressDialog?.dismiss()
        ToastUtel.showSuccessToast(applicationContext,"تم اتشاء حساب بنجاح")
        finish()
    }

    override fun noConnection() {
        ToastUtel.errorToast(this, "من فضلك تاكد من وجود انترنت ")
        progressDialog?.dismiss()

    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }



  

   


    




}


