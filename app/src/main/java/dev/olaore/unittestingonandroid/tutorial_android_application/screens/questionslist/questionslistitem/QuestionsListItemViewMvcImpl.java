package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.questionslistitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import dev.olaore.unittestingonandroid.R;
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.Question;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.views.BaseObservableViewMvc;


public class QuestionsListItemViewMvcImpl extends BaseObservableViewMvc<QuestionsListItemViewMvc.Listener>
        implements QuestionsListItemViewMvc {

    private final TextView mTxtTitle;

    private Question mQuestion;

    public QuestionsListItemViewMvcImpl(LayoutInflater inflater, @Nullable ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.layout_question_list_item, parent, false));

        mTxtTitle = findViewById(R.id.txt_title);
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Listener listener : getListeners()) {
                    listener.onQuestionClicked(mQuestion);
                }
            }
        });
    }

    @Override
    public void bindQuestion(Question question) {
        mQuestion = question;
        mTxtTitle.setText(question.getTitle());
    }
}
