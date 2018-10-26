package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.wish_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.wish_item_others
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.wish
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {

    var user_id:String? = null
    var user_profile:User? = null
    var adapter = GroupAdapter<ViewHolder>()

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
        getWishList()

        ratingBar.setOnRatingChangeListener { ratingBar, rating ->
            Log.i("Profile","rating value: $rating")
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
}
