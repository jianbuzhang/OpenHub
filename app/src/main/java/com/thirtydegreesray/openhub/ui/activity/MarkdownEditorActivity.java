package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentViewPagerAdapter;
import com.thirtydegreesray.openhub.ui.fragment.MarkdownEditorFragment;
import com.thirtydegreesray.openhub.ui.fragment.MarkdownPreviewFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 11:43:25
 */

public class MarkdownEditorActivity extends PagerActivity {

    public static void show(@NonNull Activity activity, @StringRes int title, int requestCode) {
        show(activity, title, requestCode, null);
    }

    public static void show(@NonNull Activity activity, @StringRes int title,
                            int requestCode, @Nullable String text) {
        Intent intent = new Intent(activity, MarkdownEditorActivity.class);
        intent.putExtra("text", text);
        intent.putExtra("title", title);
        activity.startActivityForResult(intent, requestCode);
    }

    @AutoAccess String text;
    @AutoAccess @StringRes int title;
    private MarkdownEditor markdownEditor;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initActivity() {
        super.initActivity();
        pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(getString(title));

        MarkdownEditorFragment editorFragment = MarkdownEditorFragment.create(text);
        markdownEditor = editorFragment;
        MarkdownPreviewFragment previewFragment = MarkdownPreviewFragment.create(markdownEditor);
        pagerAdapter.setPagerList(Arrays.asList(
                new FragmentPagerModel(getString(R.string.write), editorFragment),
                new FragmentPagerModel(getString(R.string.preview), previewFragment)
        ));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_view_pager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_markdown_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_commit){
            commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void commit(){
        if(StringUtils.isBlank(markdownEditor.getText())){
            Toasty.warning(getActivity(), getString(R.string.comment_null_warning)).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra("text", markdownEditor.getText());
        setResult(RESULT_OK, data);
        finish();
    }

    public interface MarkdownEditor {

        String getText();

        boolean isTextChanged();

    }

}