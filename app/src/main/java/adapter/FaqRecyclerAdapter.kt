package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import modal.Faq
import com.example.manohar.R

class FaqRecyclerAdapter (context: Context, private var faqlist:List<Faq>):RecyclerView.Adapter<FaqRecyclerAdapter.FaqViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
    val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_faq, parent, false)
    return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
       return faqlist.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int){
        val faq=faqlist[position]
        println("faq"+faqlist)
        holder.question.text=faq.question
        holder.answer.text=faq.answer
    }

    class FaqViewHolder(view:View): RecyclerView.ViewHolder(view)
    {val question:TextView=view.findViewById(R.id.tvque)
    val answer:TextView=view.findViewById(R.id.tvans)
    }

}