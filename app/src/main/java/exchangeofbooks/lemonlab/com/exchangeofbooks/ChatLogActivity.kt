package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.message_from_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.message_to_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.keys.keys
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Message
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.util.*

class ChatLogActivity : AppCompatActivity() {

    internal lateinit var user_id:String
    internal lateinit var username:String
    var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        //Chooses theme.
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        // get data from chatFragment
        user_id = intent.extras.getString(keys.USER_ID)
        username = intent.extras.getString(keys.USER_NAME)
        supportActionBar?.title = username

        //init
        chat_log_recycler_view.adapter = adapter
        chat_log_recycler_view.layoutManager = LinearLayoutManager(this@ChatLogActivity,LinearLayoutManager.VERTICAL,false)
        lestenToMessages()

        send_message_btn_chat_activity.setOnClickListener {
            sendMessage()
        }
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
    fun lestenToMessages(){
        val from_id = FirebaseAuth.getInstance().uid
        val to_id = user_id
        val ref = FirebaseDatabase.getInstance().getReference("messages/$from_id/$to_id")

        ref.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var message = p0.getValue(Message::class.java)

                if(message?.from_id == from_id){
                    adapter.add(message_from_item(message!!))
                }else{
                    adapter.add(message_to_item(message!!))
                }
                goToLastItem()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    fun sendMessage(){
        val from_id = FirebaseAuth.getInstance().uid
        val to_id = user_id
        val ref = FirebaseDatabase.getInstance().getReference("messages/$from_id/$to_id").push()
        var new_message = Message(ref.key!!,from_id!!,to_id,chat_message_edit_text_chat_activity.text.toString())
        chat_message_edit_text_chat_activity.setText("")
        ref.setValue(new_message).addOnCompleteListener {
            Log.i("ChatLogActivity","message send to firebase")
        }

        val other_ref = FirebaseDatabase.getInstance().getReference("messages/$to_id/$from_id").push()
        other_ref.setValue(new_message)
    }

    fun goToLastItem(){
        chat_log_recycler_view.smoothScrollToPosition(adapter.itemCount - 1)
    }
}
