package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    var category:String? = null
    var post_text:String? = null
    var post_image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //set data to spinner
        /*book_category_spinner.adapter = ArrayAdapter<String>(this@PostActivity,android.R.layout.simple_spinner_dropdown_item
        ,resources.getStringArray(R.array.book_category))*/

        post_btn_post_activity.setOnClickListener {
            UploadImageToStorage()
        }

        upload_image_btn_post_activity.setOnClickListener {
            PickImage()
        }

        select_image_camera_btn_post_activity.setOnClickListener {
            PickImageFromCamera()
        }
    }

    private fun PickImage(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }

    private fun PickImageFromCamera(){
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == 0 || requestCode == 1)&& resultCode == Activity.RESULT_OK && data!= null){
            post_image = data?.data
            post_image_post_activity.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver,data?.data))
        }
    }

    private fun UploadImageToStorage(){
        val ref = FirebaseStorage.getInstance().getReference("images")
        if(post_image == null) return
        val dialog = ProgressDialog(this@PostActivity)
        dialog.setTitle("Posting")
        dialog.setMessage("Pleas wait...")
        dialog.show()
        ref.putFile(post_image!!).addOnSuccessListener {
            Log.i("PostActivity", "post image uploaded to storage")
            ref.downloadUrl.addOnSuccessListener {
                Log.i("PostActivity", "post image url: ${it}")
                PushNewPost(it!!)
                dialog.hide()
            }
        }
    }

    private fun PushNewPost(post_image_uri:Uri?){
        //category = book_category_spinner.selectedItem.toString()
        post_text = post_text_post_activity.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("posts").push()
        var post = Post(ref.key!!,FirebaseAuth.getInstance().uid!!,post_text!!,"0","0",post_image_uri.toString(),"")
        ref.setValue(post).addOnCompleteListener {
            Log.i("PostActivity","post pushed")
            this@PostActivity.finish()
        }
    }


}
