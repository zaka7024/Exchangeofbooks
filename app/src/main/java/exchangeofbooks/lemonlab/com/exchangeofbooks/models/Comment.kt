package exchangeofbooks.lemonlab.com.exchangeofbooks.models

class Comment(var id:String,var text:String,var from_id:String,var to_post_id:String,var time:String) {
    constructor():this("","","","","")
}
