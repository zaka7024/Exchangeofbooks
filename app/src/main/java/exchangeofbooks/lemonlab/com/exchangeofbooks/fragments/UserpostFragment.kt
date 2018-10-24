package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.PostActivity

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.current_user_post.view.*
import kotlinx.android.synthetic.main.fragment_userpost.*

class UserpostFragment : Fragment() {

    var adapter:GroupAdapter<ViewHolder> = GroupAdapter() // this is the adapter from library
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userpost, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cuurent_user_posts_recycler_view.adapter = adapter
        cuurent_user_posts_recycler_view.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        cuurent_user_posts_recycler_view.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL ))
        getUserPosts()
    }

    private fun getUserPosts(){
        var ref = FirebaseDatabase.getInstance().getReference("users_post/${CurrentUser?.id}")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                adapter.clear()
                p0.children.forEach {
                    var post = it.getValue(Post::class.java)
                    adapter.add(cuurent_user_item(post!!))
                }
            }

        })
    }
}

// left your work here

class cuurent_user_item(var post:Post):Item<ViewHolder>(){ // this is the viewhollder from adpater
    // you can access the itemview like this:
    // viewHolder.itemView. ....
    override fun getLayout(): Int {
        return R.layout.current_user_post
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if(post != null){
            viewHolder.itemView.current_user_post_text_view.text = post.text
            // hrer we will check if there is a image in firebase if not we will use default image
            if(post.post_image != null){
                Picasso.get().load(post.post_image).into(viewHolder.itemView.cuurent_user_post_image_view)
            }
        }

    }

}