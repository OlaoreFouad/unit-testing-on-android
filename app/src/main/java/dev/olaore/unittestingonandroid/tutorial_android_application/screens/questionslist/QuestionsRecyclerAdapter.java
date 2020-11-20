package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist;

import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.olaore.unittestingonandroid.tutorial_android_application.questions.Question;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.ViewMvcFactory;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.questionslistitem.QuestionsListItemViewMvc;

public class QuestionsRecyclerAdapter extends RecyclerView.Adapter<QuestionsRecyclerAdapter.MyViewHolder>
        implements QuestionsListItemViewMvc.Listener {

    public interface Listener {
        void onQuestionClicked(Question question);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final QuestionsListItemViewMvc mViewMvc;

        public MyViewHolder(QuestionsListItemViewMvc viewMvc) {
            super(viewMvc.getRootView());
            mViewMvc = viewMvc;
        }

    }

    private final Listener mListener;
    private final ViewMvcFactory mViewMvcFactory;

    private List<Question> mQuestions = new ArrayList<>();

    public QuestionsRecyclerAdapter(Listener listener, ViewMvcFactory viewMvcFactory) {
        mListener = listener;
        mViewMvcFactory = viewMvcFactory;
    }

    public void bindQuestions(List<Question> questions) {
        mQuestions = new ArrayList<>(questions);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionsListItemViewMvc viewMvc = mViewMvcFactory.getQuestionsListItemViewMvc(parent);
        viewMvc.registerListener(this);
        return new MyViewHolder(viewMvc);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mViewMvc.bindQuestion(mQuestions.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    @Override
    public void onQuestionClicked(Question question) {
        mListener.onQuestionClicked(question);
    }

}
