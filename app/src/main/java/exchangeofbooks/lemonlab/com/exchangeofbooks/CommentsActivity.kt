package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {

    var user_id:String? = null
    var post_id:String? = null
    var user_post:User? = null
    var post:Post? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        // get user id from prevus activity
        user_id = intent.extras.getString(keys.USER_POST_FROM_ID)
        post_id = intent.extras.getString(keys.POST_ID)
        // get user from data base and post
        getUser()
        getPost()

        Log.i("CommentsActivity","post id:$post_id")

    }

    private fun getUser(){
        var ref = FirebaseDatabase.getInstance().getReference("users/$user_id")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                user_post = p0.getValue(User::class.java)
                Log.i("CommentsActivity","user id:$user_id user name: ${user_post?.username}")
                // change ui value
                post_username_comments_activity.text = user_post?.username

            }

        })
    }

    private fun getPost(){
        val ref = FirebaseDatabase.getInstance().getReference("posts/$post_id")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                post = p0.getValue(Post::class.java)
                Picasso.get().load(user_post?.image_profile).into(post_image_comments_activity)
                Picasso.get().load(post?.post_image).into(post_image_view_comments_activity)
                pots_post_textview_comment_activity.text = post?.text
                Log.i("CommentsActivity","post: ${post?.text}")
                Log.i("CommentsActivity","post image: ${post?.post_image}")
            }

        })
    }
}
