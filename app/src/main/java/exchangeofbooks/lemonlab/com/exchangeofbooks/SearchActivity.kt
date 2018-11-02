package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mancj.materialsearchbar.MaterialSearchBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import exchangeofbooks.lemonlab.com.exchangeofbooks.items.post_profile_item
import exchangeofbooks.lemonlab.com.exchangeofbooks.models.Post
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    var suggestions = ArrayList<String>().apply {
        add("hello")
        add("ahmad")
        add("Ali")
        add("omer")
        add("japer")
        add("abed")
        add("ibrahim")
        add("ceo")
    }

    var book_name = ArrayList<String>().apply {
        add("mkc")
        add("barch")
        add("omer")
        add("how to program")
    }

    var adapter:GroupAdapter<ViewHolder> = GroupAdapter()
    var searchAdapter:GroupAdapter<ViewHolder> = GroupAdapter()
    var data:ArrayList<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        getAllPost()

        search_post_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        search_post_recycler_view.adapter = adapter
        searchBar.lastSuggestions = suggestions

        searchBar.setOnSearchActionListener(object: MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {

            }

            override fun onSearchStateChanged(enabled: Boolean) {
                if(!enabled){
                    search_post_recycler_view.adapter = adapter
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString())
            }
        })


        searchBar.addTextChangeListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var suggest = ArrayList<String>()
                for (item in book_name){
                    if(searchBar.text.toLowerCase().contains(item)){
                        suggest.add(item)
                    }
                }
                searchBar.lastSuggestions = suggest

            }

        })
    }

    fun getAllPost(){
        val ref = FirebaseDatabase.getInstance().getReference("posts")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    p0.children.forEach {
                        var post = it.getValue(Post::class.java)
                        data.add(post!!)
                        //adapter.add(post_profile_item(post,this@SearchActivity))
                    }
                }
            }

        })
    }

    fun startSearch(text:String){
        var temp:ArrayList<Post> = ArrayList()
        data.forEach {
            if(it.category == text){
                temp.add(it)
            }
        }

        temp.forEach {
            adapter.add(post_profile_item(it,this@SearchActivity))
        }
    }
}
