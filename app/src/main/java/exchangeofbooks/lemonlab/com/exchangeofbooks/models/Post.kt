package exchangeofbooks.lemonlab.com.exchangeofbooks.models

class Post(var post_id:String,var from_id:String, var text:String,var likes:String,var comments:String,var post_image:String) {
    constructor():this("","","","","","")
}