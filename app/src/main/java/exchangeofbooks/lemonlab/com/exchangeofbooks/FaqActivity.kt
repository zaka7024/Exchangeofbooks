package exchangeofbooks.lemonlab.com.exchangeofbooks

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_faq.*

class FaqActivity : AppCompatActivity() {

   // data class FAQ(val question: String, val answer: List<String>)

    override fun onCreate(savedInstanceState: Bundle?) {
        modeLightOrNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        showQuestions()
    }

private fun showQuestions(){
    val answers=resources.getStringArray(R.array.FRN)
    val answersView:List<TextView> =
            listOf(answerNo1, answerNo2, answerNo3, answerNo4,
                    answerNo5, answerNo6, answerNo7, answerNo8,
                    answerNo9)
    val questions=resources.getStringArray(R.array.FAQ)
    val questionsView:List<TextView> =
            listOf(questionNo1, questionNo2, questionNo3, questionNo4,
                    questionNo5, questionNo6, questionNo7, questionNo8,
                    questionNo9)

    fun hideExcept(v:View){
        for(i in answersView){
            i.visibility=View.GONE
        }
        v.visibility=View.VISIBLE
    }

    fun setAnswers(){
        for ((k, i) in answersView.withIndex()) {
            i.text = answers[k]
        }
    }
    setAnswers()

    for((k, i) in questionsView.withIndex()){
        i.text=questions[k]
        i.setOnClickListener {
            when(i.id){
                R.id.questionNo1 -> hideExcept(answerNo1)
                R.id.questionNo2 -> hideExcept(answerNo2)
                R.id.questionNo3 -> hideExcept(answerNo3)
                R.id.questionNo4 -> hideExcept(answerNo4)
                R.id.questionNo5 -> hideExcept(answerNo5)
                R.id.questionNo6 -> hideExcept(answerNo6)
                R.id.questionNo7 -> {hideExcept(answerNo7)
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }
                R.id.questionNo8 -> {hideExcept(answerNo8)
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }
                R.id.questionNo9 -> {hideExcept(answerNo9)
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }
        }
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
}
