package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.post_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.post_profile_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {

    var user_id:String? = null
    var user_profile:User? = null
    var adapter = GroupAdapter<ViewHolder>()
    var listOfFriends = ArrayList<String>()
    var isFriend:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // get data from previous activity
        user_id = intent.extras.getString(keys.USER_ID)
        // get user from database and set the data into profile
        getUser()
        //init
        wishlist_recycler_view_activity_profile.adapter = adapter
        wishlist_recycler_view_activity_profile.layoutManager = LinearLayoutManager(this@Profile,LinearLayoutManager.VERTICAL,false)
        getUserPosts()

        // // we will remove this code soon
        //getWishList()

        ratingBar.setOnRatingChangeListener { ratingBar, rating ->
            Log.i("Profile","rating value: $rating")
        }

        add_frined_btn.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("friends/${CurrentUser?.id}")

            ref.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    listOfFriends.clear()
                    if(CurrentUser != null)
                    p0.children.forEach {
                        it.children.forEach {
                            Log.i("Profile","friend : ${it.toString()}")
                            listOfFriends.add(it.getValue(String::class.java)!!)
                        }
                    }
                    Log.i("Profile","friend list size -> ${listOfFriends.size}")
                    if(listOfFriends.contains(user_id!!)){
                        Toast.makeText(this@Profile,"الصديق موجود بالفعل",Toast.LENGTH_SHORT).show()
                    }else{
                        sendFriendRequest()
                        isFriend = true
                        ref.removeEventListener(this)
                    }
                }

            })

        }

        rate_frined_btn.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("friends/${FirebaseAuth.getInstance().uid}")

            ref.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    listOfFriends.clear()
                    p0.children.forEach {
                        listOfFriends.add(it.getValue(String::class.java)!!)
                    }
                    if(listOfFriends.contains(user_id!!)){
                        ratingBar.visibility = View.VISIBLE
                    }else{
                        ratingBar.visibility = View.GONE
                    }
                }

            })
        }
    }

    fun getUser(){
        val ref = FirebaseDatabase.getInstance().getReference("users/${user_id}")
        ref.addValueEventListener(object:ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                user_profile = p0.getValue(User::class.java)
                setData(user_profile!!)
            }

        })
    }

    fun setData(user:User?){
        Picasso.get().load(user_profile?.image_profile).into(user_image_profile_activity)
        user_name_profile_activity.text = user?.username
    }

    // we will remove this code soon

    /*

    fun getWishList(){
        val ref = FirebaseDatabase.getInstance().getReference("wishlist/${user_id}")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var wishList = ArrayList<wish>()
                p0.children.forEach{
                    wishList.add(it.getValue(wish::class.java)!!)
                }
                wishList.reverse()
                wishList.forEach {
                    adapter.add(wish_item_others(it))
                }
            }

        })
    }
    */

    fun sendFriendRequest(){
        var ref = FirebaseDatabase.getInstance().getReference("friend_request/${user_id}/${CurrentUser?.id}").push()
        ref.setValue(FirebaseAuth.getInstance().uid).addOnCompleteListener {
            Log.i("Profile","friend request send")
        }
    }

    fun getUserPosts(){
        val ref = FirebaseDatabase.getInstance().getReference("users_post/$user_id")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var post_list = ArrayList<Post>()
                if(p0.exists()){
                    p0.children.forEach {
                        var post = it.getValue(Post::class.java)
                        post_list.add(post!!)
                        Log.i("Profile","post in profile text: ${post.text}")
                    }
                }
                post_list.forEach { adapter.add(post_profile_item(it)) }
            }

        })
    }
}
