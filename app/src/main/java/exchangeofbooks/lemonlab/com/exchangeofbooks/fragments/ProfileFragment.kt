package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class ProfileFragment() : Fragment() {

    internal  lateinit var temp_imagre_uri:String
    var adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //inti
        setUserData()

        image_profile_user_profile.setOnClickListener {
            changeUserProfileIImage()
        }
    }

    private fun setUserData(){
        Picasso.get().load(CurrentUser?.image_profile).into(image_profile_user_profile)
        user_name_user_profile.text = CurrentUser?.username
    }

    private fun changeUserProfileIImage(){
        startActivityForResult(Intent(Intent.ACTION_PICK).apply { type = "image/*" }, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 1 && data!=null){
            Log.i("ProfileFragment","start change profile image")
            temp_imagre_uri = data?.data.toString()
            image_profile_user_profile.setImageURI(data?.data)
            uploadImage()
        }
    }

    private fun uploadImage(){
        var id = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$id")
        ref.putFile(Uri.parse(temp_imagre_uri)).addOnCompleteListener{
            Log.i("ProfileFragment","image profile changed")
            ref.downloadUrl.addOnSuccessListener {
                CurrentUser?.image_profile = it.toString()
                Log.i("ProfileFragment","image uri: ${CurrentUser?.image_profile}")
            }
        }
    }
}
