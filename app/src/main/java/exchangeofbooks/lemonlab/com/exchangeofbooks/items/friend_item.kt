package exchangeofbooks.lemonlab.com.exchangeofbooks.items

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.ChatLogActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Notification
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.user_chat_row.view.*

class friend_item(var user: User,var context: Context, var adapter: GroupAdapter<ViewHolder>):Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_chat_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user?.image_profile).into(viewHolder.itemView.user_image_profile_chat_fragment)
        viewHolder.itemView.friend_name_chat_fragment.text = user?.username

        viewHolder.itemView.setOnClickListener {
            var intent = Intent(context,ChatLogActivity::class.java)
            intent.putExtra(keys.USER_ID,user.id)
            intent.putExtra(keys.USER_NAME,user.username)
            context.startActivity(intent)
        }
        var pop = PopupMenu(context,viewHolder.itemView.user_chat_row_settings)
        val removeFriend=context.getString(R.string.Remove_Friend)
        pop.menu.add(removeFriend)

        pop.setOnMenuItemClickListener {
            adapter.removeGroup(viewHolder.adapterPosition)
            val ref = FirebaseDatabase.getInstance().getReference("friends/${FirebaseAuth.getInstance()
                    .uid}/${user.id}")
            ref.removeValue().addOnCompleteListener {
                Log.i("ChatLogFragment","friend removed from database")
                Toast.makeText(context,"تم الحذف",Toast.LENGTH_SHORT).show()
                pushNotification()
            }

            var other_ref = FirebaseDatabase.getInstance().getReference("friends/${user.id}/${FirebaseAuth.getInstance().uid}")
            other_ref.removeValue()
            true
        }

        viewHolder.itemView.user_chat_row_settings.setOnClickListener {
            pop.show()
        }
    }

    fun pushNotification(){
        val activityRef = FirebaseDatabase.getInstance().getReference("notifications/${user.id}").push()
        var new_notification = Notification(activityRef.key!!,  " من قائىة اصدقاءه" + "${CurrentUser?.username}" + "لقد حذفك ", CurrentUser?.id!!)
        activityRef.setValue(new_notification).addOnCompleteListener {
            Log.i("Profile","new notification added")
        }
    }
}