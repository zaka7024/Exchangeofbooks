package exchangeofbooks.lemonlab.com.exchangeofbooks.models

class Post(var post_id:String,var from_id:String, var text:String,
           var post_image:String,var category:String) {
    constructor():this("","","","","")
}