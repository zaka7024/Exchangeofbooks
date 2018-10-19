package exchangeofbooks.lemonlab.com.exchangeofbooks.models

class User(var id:String, var username:String,var email:String,var image_profile:String,
           var frinds:ArrayList<User>?, var user_posts:ArrayList<Post>?) {
    constructor():this("","","","",null,null)
}
