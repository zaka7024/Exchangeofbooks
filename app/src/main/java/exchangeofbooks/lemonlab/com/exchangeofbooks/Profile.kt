package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DividerItemDecoration
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
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Notification
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {

    var user_id:String? = null
    var user_profile:User? = null
    var adapter = GroupAdapter<ViewHolder>()
    var listOfFriends = ArrayList<String>()
    var isFriend:Boolean = false
    var views:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // get data from previous activity
        user_id = intent.extras.getString(keys.USER_ID)
        // get user from database and set the data into profile
        getUser()
        //init
        wishlist_recycler_view_activity_profile.adapter = adapter
        wishlist_recycler_view_activity_profile.layoutManager = LinearLayoutManager(this@Profile,LinearLayoutManager.VERTICAL,false)
        wishlist_recycler_view_activity_profile.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL ))
        getUserPosts()

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
                        val alreadyFriends=getString(R.string.alreadyFriends)
                        Toast.makeText(this@Profile,alreadyFriends,Toast.LENGTH_SHORT).show()
                    }else{
                        sendFriendRequest()
                        isFriend = true
                        ref.removeEventListener(this)
                    }
                }

            })

        }

        report_person_btn.setOnClickListener {
            Toast.makeText(this@Profile,"Soon",Toast.LENGTH_SHORT).show()
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


    fun sendFriendRequest(){

        if(user_id == CurrentUser?.id){
            Toast.makeText(this,"لا يمكنك إرسال طلب مصادقة إلى نفسك 😊",Toast.LENGTH_SHORT).show()
            return
        }

        // check if the current user is already friend

        val friendRef = FirebaseDatabase.getInstance().getReference("friends/${CurrentUser?.id}")

        friendRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var isFriend = false
                p0.children.forEach {
                    if(it.key == user_id){
                        isFriend = true
                    }
                }
                Log.i("Profile","is friend : ${isFriend}")
                if(isFriend == false){
                    // if not send request
                    val checkRef = FirebaseDatabase.getInstance().getReference("friend_request/${user_id}")
                    var event = checkRef.addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            var canSendRequest:Boolean = true
                            if(p0.exists()){

                                p0.children.forEach {
                                    if(CurrentUser?.id!=null && it.key == CurrentUser?.id){
                                        canSendRequest = false
                                    }
                                }
                            }
                            // if no any send request is exists send one
                            Log.i("Profile","can send friend request : ${canSendRequest}")

                            if(canSendRequest){
                                var ref = FirebaseDatabase.getInstance().getReference("friend_request/${user_id}/${CurrentUser?.id}").push()
                                ref.setValue(FirebaseAuth.getInstance().uid).addOnCompleteListener {
                                    Log.i("Profile","friend request send")
                                    Toast.makeText(this@Profile,"لقد تم إرسال الطلب",Toast.LENGTH_SHORT).show()
                                    pushNotification()
                                }
                            }else{ // there is an friend request in the database
                                Toast.makeText(this@Profile,"لقد تم إرسال الطلب مسبقًا",Toast.LENGTH_SHORT).show()
                            }
                            checkRef.removeEventListener(this)
                        }

                    })


                }else{ // if the user is already friend
                    Toast.makeText(this@Profile,"هذا الشخص صديق لديك بالفعل",Toast.LENGTH_SHORT).show()
                }

                friendRef.removeEventListener(this)
            }

        })

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
                        views += post!!.views.size
                        post_list.add(post)
                        Log.i("Profile","post in profile text: ${post.text}")
                    }
                    user_post_progresbar_profile_activity.visibility = View.GONE
                    wishlist_recycler_view_activity_profile.visibility = View.VISIBLE
                    post_count_text_view.text = post_list.size.toString()
                    view_value_text_view.text = views.toString()
                }
                post_list.reverse()
                post_list.forEach { adapter.add(post_profile_item(it,this@Profile)) }
            }

        })
    }

    private fun modeLightOrNight(){
        val shrPr=this.getSharedPreferences("mode", Context.MODE_PRIVATE) ?: return
        val mode=shrPr.getString(getString(R.string.mode), "")
        if(mode=="ni"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
    }

    fun pushNotification(){
        val activityRef = FirebaseDatabase.getInstance().getReference("notifications/${user_id}").push()
        var new_notification = Notification(activityRef.key!!,  "${CurrentUser?.username}" + "لديك طلب مصادقة من: ", CurrentUser?.id!!)
        activityRef.setValue(new_notification).addOnCompleteListener {
            Log.i("Profile","new notification added")
        }
    }
}
