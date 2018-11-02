package exchangeofbooks.lemonlab.com.exchangeofbooks.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.MainActivity.Companion.CurrentUser
import exchangeofbooks.lemonlab.com.exchangeofbooks.PostActivity
import exchangeofbooks.lemonlab.com.exchangeofbooks.Profile

import exchangeofbooks.lemonlab.com.exchangeofbooks.R
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.friend_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.friend_request_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.post_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.User
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    var adapter:GroupAdapter<ViewHolder> = GroupAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //init
        val friends=getString(R.string.friends_title)
        (activity as AppCompatActivity).supportActionBar?.title = friends
        chat_recyclerView.adapter = adapter
        chat_recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        chat_recyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL ))
        getFriendRequest()
        getFriendsInChat()

    }
    // get user friends from firebase database
    fun getFriendsInChat(){
        val ref = FirebaseDatabase.getInstance().getReference("friends/${FirebaseAuth.getInstance().uid}")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    adapter.clear()
                    p0.children.forEach {
                        var user_id = it.key.toString()
                        if(user_id != null)
                        convertToUser(user_id.toString())
                    }
                }
            }

        })
    }

    // get user id and return user object from firebase database
    fun convertToUser(id:String){
        var ref = FirebaseDatabase.getInstance().getReference("users/$id")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)
                if(user != null && context != null)
                adapter.add(friend_item(user,context!!,adapter))
            }

        })
    }
    // get user friends request from firebase database
    fun getFriendRequest(){
        val ref = FirebaseDatabase.getInstance().getReference("friend_request/${CurrentUser?.id}")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    p0.children.forEach {
                        var id = it.key.toString()
                        Log.i("ChatFragment","friend request id: $id")
                        adapter.add(friend_request_item(id,adapter))
                    }
                }
                //ref.removeEventListener(this)
            }

        })
    }
}
