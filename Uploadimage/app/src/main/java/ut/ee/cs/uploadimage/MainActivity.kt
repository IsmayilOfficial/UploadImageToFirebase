package ut.ee.cs.uploadimage

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

import java.net.URI

class MainActivity : AppCompatActivity() {
    internal lateinit var ch: Button
    internal var up: Button? = null
    internal lateinit var img: ImageView
    internal lateinit var mStrageRef: StorageReference
    var imguri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mStrageRef = FirebaseStorage.getInstance().getReference("Images")
        ch = findViewById<View>(R.id.button) as Button

        img = findViewById<View>(R.id.imageView) as ImageView
        ch.setOnClickListener { chooseImage() }


    }

    private fun chooseImage() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 1)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri = data!!.data
            img.setImageURI(imageUri)

            val Ref = mStrageRef.child(System.currentTimeMillis().toString() + "." + getExtension(imageUri))
            Ref.putFile(imageUri!!)
                    .addOnSuccessListener {
                        // Get a URL to the uploaded content
                        //
                        Toast.makeText(this@MainActivity, "Image uploaded successfully", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        // Handle unsuccessful uploads
                        // ...
                    }
        }
    }


    private fun getExtension(uri: Uri?): String? {
        val cr = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri!!))
    }
}
