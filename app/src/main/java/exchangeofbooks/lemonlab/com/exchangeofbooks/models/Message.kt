package exchangeofbooks.lemonlab.com.exchangeofbooks.models

class Message(var id:String,var from_id:String,var to_id:String,var text:String) {
    constructor():this("","","","")
}