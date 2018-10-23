package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.comment_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Comment
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

class CommentsActivity : AppCompatActivity() {

    var user_id:String? = null
    var post_id:String? = null
    var user_post:User? = null
    var post:Post? = null
    var adapter = GroupAdapter<ViewHolder>()
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

        comments_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        comments_recycler_view.adapter = adapter




        send_comment_comments_activity.setOnClickListener {
            var comment_id = UUID.randomUUID().toString()
            var new_comment = Comment(comment_id,cooment_edittext_comments_activity.text.toString(),
                    CurrentUser!!.id,post_id!!,"today")
            adapter.add(comment_item(new_comment))
            putComment(new_comment)
        }

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
                if(post?.post_image == "null"){
                    post_image_view_comments_activity.setImageResource(R.drawable.default_post_image)
                }else{
                    Picasso.get().load(post?.post_image).into(post_image_view_comments_activity)
                }

                pots_post_textview_comment_activity.text = post?.text
                Log.i("CommentsActivity","post: ${post?.text}")
                Log.i("CommentsActivity","post image: ${post?.post_image}")
            }

        })
    }

    private fun putComment(comment:Comment){
        // create node in firebase and set data

        val ref = FirebaseDatabase.getInstance().getReference("comments/${post_id}").push()
        ref.setValue(comment).addOnCompleteListener {
            Log.i("CommentsActivity","new comment posted: ${comment?.text}")
        }
    }
}
