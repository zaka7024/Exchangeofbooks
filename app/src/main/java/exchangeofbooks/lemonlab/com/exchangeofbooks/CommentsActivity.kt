package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
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
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.util.*

class CommentsActivity : AppCompatActivity() {

    var user_id:String? = null
    var post_id:String? = null
    var user_post:User? = null
    var post:Post? = null
    var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        //init
        val comments=getString(R.string.comments_c)
        supportActionBar?.title = comments
        // get user id from previous activity
        user_id = intent.extras.getString(keys.USER_POST_FROM_ID)
        post_id = intent.extras.getString(keys.POST_ID)
        // get user from data base and post
        getUser()
        getPost()
        lestenToComments()
        Log.i("CommentsActivity","post id:$post_id")

        comments_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        comments_recycler_view.addItemDecoration(DividerItemDecoration(this@CommentsActivity,DividerItemDecoration.VERTICAL ))
        comments_recycler_view.adapter = adapter

        Picasso.get().load(CurrentUser?.image_profile).into(post_image_comments_activity)

        send_comment_comments_activity.setOnClickListener {
            var comment_id = UUID.randomUUID().toString()
            var new_comment = Comment(comment_id,cooment_edittext_comments_activity.text.toString(),
                    CurrentUser!!.id,post_id!!,"today")
            // clear the ui and move to end of rv
            cooment_edittext_comments_activity.setText("")
            //scrollToLastItem()
            putComment(new_comment)
        }

        cooment_edittext_comments_activity.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.trim().isEmpty()){
                    send_comment_comments_activity.setTextColor(resources.getColor(R.color.irisBlue))
                }else{
                    send_comment_comments_activity.setTextColor(resources.getColor(R.color.mainColorBlue))
                }
            }

        })

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
                Log.i("CommentsActivity","post: ${post?.text}")
                Log.i("CommentsActivity","post image: ${post?.post_image}")
            }

        })
    }

    private fun putComment(comment:Comment){
        // create node in firebase and set data

        val ref = FirebaseDatabase.getInstance().getReference("comments/${post_id}").push()
        comment.id = ref.key.toString()
                ref.setValue(comment).addOnCompleteListener {
            Log.i("CommentsActivity","new comment posted: ${comment?.text}")
        }
    }

    private fun lestenToComments(){
        val ref = FirebaseDatabase.getInstance().getReference("comments/${post_id}")
        var comments_count = 0
        var temp_count =
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // get the comments from database and put it into our adapter
                var comment = p0.getValue(Comment::class.java)
                adapter.add(comment_item(comment!!,this@CommentsActivity))//
                scrollToLastItem()
            }
            /*
                val dingSound=MediaPlayer.create(this@CommentsActivity, R.raw.ding_new_comment)
                //if two comments come in the same time, one sound is played.
                //remove try & catch if you want two sounds at the same time.
                try{
                    dingSound.stop()
                    dingSound.reset()
                }catch (e:Exception){}
                dingSound.start()
             */

            override fun onChildRemoved(p0: DataSnapshot) {

            }


        })
    }

    fun scrollToLastItem(){
        comments_recycler_view.smoothScrollToPosition(adapter.itemCount - 1)
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
}
