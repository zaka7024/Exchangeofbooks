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
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*

class PostActivity : AppCompatActivity() {

    var category:String? = null
    var post_text:String? = null
    var post_image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //set data to spinner
        book_category_spinner.adapter = ArrayAdapter<String>(this@PostActivity,android.R.layout.simple_spinner_dropdown_item
        ,resources.getStringArray(R.array.book_category))

        post_btn_post_activity.setOnClickListener {
            // save post and close the activity
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
            post_image = data?.data.toString()
            post_image_post_activity.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver,data?.data))
        }else{
            post_image = "null"
        }
    }

    private fun UploadImageToStorage(){
        var uid = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$uid")
        // check id post image is not equal null then...else we will use defult image
        val dialog = ProgressDialog(this@PostActivity)
        dialog.setTitle("Posting")
        dialog.setMessage("Pleas wait...")
        dialog.show()
        if(post_image != null){
            ref.putFile(Uri.parse(post_image!!)).addOnSuccessListener {
                Log.i("PostActivity", "post image uploaded to storage")
                ref.downloadUrl.addOnSuccessListener {
                    Log.i("PostActivity", "post image url: ${it}")
                    PushNewPost(it!!.toString())
                    dialog.hide()
                }

            }
        }else if(post_image == null){
            post_image = "null"
            PushNewPost(post_image)
            dialog.hide()
        }
    }

    private fun PushNewPost(post_image_uri:String?){
        //category = book_category_spinner.selectedItem.toString()
        post_text = post_text_post_activity.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("posts").push()
        var post = Post(ref.key!!,FirebaseAuth.getInstance().uid!!,post_text!!,post_image_uri.toString(),
                book_category_spinner.selectedItem.toString())

        ref.setValue(post).addOnCompleteListener {
            Log.i("PostActivity","post pushed")

        }
        val user_ref = FirebaseDatabase.getInstance().getReference("users_post/${CurrentUser?.id}/${ref.key}")
        //post.post_id = user_ref.key!! // i do not need that
        user_ref.setValue(post).addOnCompleteListener {
            this@PostActivity.finish()
        }
    }


}
