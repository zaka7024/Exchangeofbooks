package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments


import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.CommentsActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.current_user_post.view.*
import kotlinx.android.synthetic.main.fragment_userpost.*
import kotlin.coroutines.experimental.coroutineContext

class UserpostFragment : Fragment() {

    var adapter:GroupAdapter<ViewHolder> = GroupAdapter() // this is the adapter from library
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userpost, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dragToDelete=getString(R.string.Drag_Delete)
        Toast.makeText(context,dragToDelete, Toast.LENGTH_SHORT).show()

        cuurent_user_posts_recycler_view.adapter = adapter
        cuurent_user_posts_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        cuurent_user_posts_recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        getUserPosts()
        val swipeHandlerDel = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, po: Int) {
                //when user has swiped
                //action to do
                deletePost((adapter.getItem(viewHolder.adapterPosition) as cuurent_user_item).post)
                Log.i("UserpostFragment","you delete this post: ${viewHolder.adapterPosition}")
                adapter.removeGroup(viewHolder.adapterPosition)

            }
        }
        //attaching swipe to delete to recycler view
        val itemTouchHelper = ItemTouchHelper(swipeHandlerDel)
        itemTouchHelper.attachToRecyclerView(cuurent_user_posts_recycler_view)
    }

    private fun getUserPosts(){
        if(CurrentUser == null) return
        var ref = FirebaseDatabase.getInstance().getReference("users_post/${FirebaseAuth.getInstance().uid}")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                adapter.clear()
                var post_list = ArrayList<Post>()
                if(p0.exists()){
                    p0.children.forEach {
                        var post = it.getValue(Post::class.java)
                        post_list.add(post!!)
                    }
                }

                // reverse the list

                post_list.reverse()
                post_list.forEach {
                    if(it != null && context != null)
                    adapter.add(cuurent_user_item(it,context!!))
                }
            }

        })
    }

    private fun deletePost(post: Post){
        Log.i("UserpostFragment","i will delete this post: ${post.post_id}")
        Log.i("UserpostFragment","current user id: ${CurrentUser?.id}")
        val ref = FirebaseDatabase.getInstance().getReference("posts/${post.post_id}")
        ref.removeValue().addOnCompleteListener {
            Log.i("UserpostFragment","deleted")
        }

        val user_ref = FirebaseDatabase.getInstance().getReference("users_post/${CurrentUser?.id}/${post.post_id}")
        user_ref.removeValue().addOnCompleteListener {

        }

    }

    abstract class SwipeToDeleteCallback(context: Context) :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_del)
        private val intrinsicWidth = deleteIcon!!.intrinsicWidth
        private val intrinsicHeight = deleteIcon!!.intrinsicHeight
        private val background = ColorDrawable()
        private val backgroundColor = Color.parseColor("#f44336")
        private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


        override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int,
                                          animateDx: Float, animateDy: Float): Long {
            return 0
        }


        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.3f
        override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
        ) {

            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive

            if (isCanceled) {
                clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }
            // Draw the red delete background
            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt()/4, itemView.top, itemView.right, itemView.bottom)
            background.draw(c)


            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
        }

        private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
            c?.drawRect(left, top, right, bottom, clearPaint)
        }

    }
}


class cuurent_user_item(var post:Post, var context: Context):Item<ViewHolder>(){

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

        viewHolder.itemView.setOnClickListener {
            var intent = Intent(context,CommentsActivity::class.java)
            intent.putExtra(keys.USER_POST_FROM_ID,post.from_id)
            intent.putExtra(keys.POST_ID,post.post_id)
            context.startActivity(intent)
        }
    }

}