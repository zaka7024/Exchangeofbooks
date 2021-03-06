package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.current_user_profile_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import kotlin.collections.ArrayList

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
        getUserPost()
        userPostRecyclerView.visibility = View.INVISIBLE
        userPostRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        userPostRecyclerView.adapter = adapter
        userPostRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

        //to change user image profile
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

    private fun getUserPost(){
        var ref = FirebaseDatabase.getInstance().getReference("users_post/${CurrentUser?.id}")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var temp_lsit:ArrayList<Post> = ArrayList()
                adapter.clear()

                p0.children.forEach {
                    var post = it.getValue(Post::class.java)
                    temp_lsit.add(post!!)
                }

                temp_lsit.reverse()
                temp_lsit.forEach {
                    adapter.add(current_user_profile_item(it))
                }

                if(userPostRecyclerView != null && progress_bar_profile_fragment!= null){
                    userPostRecyclerView.visibility = View.VISIBLE
                    progress_bar_profile_fragment.visibility = View.GONE
                }
            }
        })
    }
}
