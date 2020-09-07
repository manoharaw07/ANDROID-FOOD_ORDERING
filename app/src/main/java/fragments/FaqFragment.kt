package fragments

import adapter.FaqRecyclerAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import modal.Faq
import com.example.manohar.R

class FaqFragment : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FaqRecyclerAdapter
    lateinit var faqList:List<Faq>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_faq, container, false)
        recyclerView=view.findViewById(R.id.faqRecycler)
        //  progressBar=view.findViewById(R.id.progressBar)
        //  progressLayout=view.findViewById(R.id.progressLayout)
        layoutManager= LinearLayoutManager(activity as Context)

        faqList= listOf(
            Faq("how are you", "im fine"),
            Faq(
                "Q.1 How will the training be delivered?",
                "A.1 You will be taught using pre-recorded videos and text tutorials. The training has quizzes, assignments, and tests to help you learn better. At the end of the training, you will attempt a project to get hands-on practice of what you learn during your training."
            ),
            Faq(
                "Q.2 What will be the timings of the training?",
                "A.2 As this is a purely online training program, you can choose to learn at any time of the day. We will recommend a place to be followed throughout the program, but the actual timing and learning hours can be decided by students according to their convenience."
            ),
            Faq(
                "Q.3 What is the duration of training?",
                "A.3 The training duration is of 6 weeks."
            ),
            Faq(
                "Q.4 How much time should I spend everyday?",
                "A.4 We recommend spending 10-12 hours per week. However, the actual learning hours can be decided by you as per your convenience."
            ),
            Faq(
                "Q.5 When can I start my training?",
                "A.5 You can choose your preferred batch date while signing up for the training program."
            )
        )

        recyclerAdapter = FaqRecyclerAdapter(activity as Context, faqList)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager




        return view
    }

}
