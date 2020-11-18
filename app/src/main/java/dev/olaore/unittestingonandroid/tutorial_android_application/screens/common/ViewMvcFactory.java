package dev.olaore.unittestingonandroid.tutorial_android_application.screens.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.navdrawer.NavDrawerHelper;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.navdrawer.NavDrawerViewMvc;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.navdrawer.NavDrawerViewMvcImpl;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toolbar.ToolbarViewMvc;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsViewMvc;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsViewMvcImpl;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.QuestionsListViewMvc;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.QuestionsListViewMvcImpl;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.questionslistitem.QuestionsListItemViewMvc;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.questionslistitem.QuestionsListItemViewMvcImpl;


public class ViewMvcFactory {

    private final LayoutInflater mLayoutInflater;
    private final NavDrawerHelper mNavDrawerHelper;

    public ViewMvcFactory(LayoutInflater layoutInflater, NavDrawerHelper navDrawerHelper) {
        mLayoutInflater = layoutInflater;
        mNavDrawerHelper = navDrawerHelper;
    }

    public QuestionsListViewMvc getQuestionsListViewMvc(@Nullable ViewGroup parent) {
        return new QuestionsListViewMvcImpl(mLayoutInflater, parent, mNavDrawerHelper, this);
    }

    public QuestionsListItemViewMvc getQuestionsListItemViewMvc(@Nullable ViewGroup parent) {
        return new QuestionsListItemViewMvcImpl(mLayoutInflater, parent);
    }

    public QuestionDetailsViewMvc getQuestionDetailsViewMvc(@Nullable ViewGroup parent) {
        return new QuestionDetailsViewMvcImpl(mLayoutInflater, parent, this);
    }

    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {
        return new ToolbarViewMvc(mLayoutInflater, parent);
    }

    public NavDrawerViewMvc getNavDrawerViewMvc(@Nullable ViewGroup parent) {
        return new NavDrawerViewMvcImpl(mLayoutInflater, parent);
    }
}
