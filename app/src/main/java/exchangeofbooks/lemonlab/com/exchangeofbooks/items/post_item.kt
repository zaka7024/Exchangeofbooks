package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.CommentsActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.Profile
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.post_row_layout.view.*
import kotlin.coroutines.experimental.coroutineContext

class post_item(var post:Post?, var context:Context?):Item<ViewHolder>() {
    var user:User? = null

    override fun getLayout(): Int {
        return R.layout.post_row_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        // get user from database

        val database = FirebaseDatabase.getInstance().getReference("users/${post?.from_id}")

        database.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java)
                viewHolder.itemView.post_username.text = user?.username
                if(user?.image_profile == null){ // we will use defult image

                }else{
                    Picasso.get().load(user?.image_profile).into( viewHolder.itemView.post_image_profile)
                }

            }

        })

        viewHolder.itemView.book_category_post_row.text = post?.category

        viewHolder.itemView.pots_post_textview.text = post?.text
        // load image from cash or picasso
        //Log.i("post_item",post?.post_image) // temp
        if(post?.post_image == "null"){
            viewHolder.itemView.post_image_view.setImageDrawable(context!!.resources.getDrawable(R.drawable.default_post_image))
        }else{
            Picasso.get().load(post?.post_image).into(viewHolder.itemView.post_image_view)
        }

        // event lestiner

        viewHolder.itemView.tkae_book_main_activity.setOnClickListener {
            var intent = Intent(context,CommentsActivity::class.java)
            intent.putExtra(keys.USER_POST_FROM_ID,post?.from_id)
            intent.putExtra(keys.POST_ID,post?.post_id)
            context?.startActivity(intent)
        }

        // open user profile activity
        viewHolder.itemView.post_image_profile.setOnClickListener {
            var _post = viewHolder as Post
            var intent = Intent(context, Profile::class.java)
            intent.putExtra(keys.USER_ID,post?.from_id)
            context?.startActivity(intent)
        }
    }
}