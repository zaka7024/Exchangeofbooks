package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.friend_request_row.view.*

class friend_request_item(var user_id:String, var adapter: GroupAdapter<ViewHolder>): Item<ViewHolder> (){
    override fun getLayout(): Int {
        return R.layout.friend_request_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$user_id")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)
                Picasso.get().load(user?.image_profile).into(viewHolder.itemView.user_image_profile_friend_request)
                viewHolder.itemView.user_name_friend_request.text = user?.username
            }

        })

        viewHolder.itemView.accept_friend_request_btn.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("friends/${CurrentUser?.id}/").push()
            val other_ref = FirebaseDatabase.getInstance().getReference("friends/${user_id}").push()

            ref.setValue(user_id).addOnCompleteListener {
                Log.i("Profile", "friend added to database ")
                deleteFriendRequest()
                adapter.removeGroup(viewHolder.adapterPosition)
            }

            other_ref.setValue(CurrentUser?.id).addOnCompleteListener {
                Log.i("Profile", "other friend added to database ")
            }
        }

        viewHolder.itemView.delete_friend_request_btn.setOnClickListener {
            deleteFriendRequest()
            adapter.removeGroup(viewHolder.adapterPosition)
        }
    }

    fun deleteFriendRequest(){
        val ref = FirebaseDatabase.getInstance().getReference("friend_request/${CurrentUser?.id}/${user_id}")
        ref.removeValue().addOnCompleteListener {
            Log.i("Profile","friend request removed from database ")
        }
    }
}