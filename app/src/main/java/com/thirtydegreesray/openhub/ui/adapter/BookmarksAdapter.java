package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.BookmarkExt;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/11/22 16:15:52
 */

public class BookmarksAdapter extends BaseAdapter<BaseViewHolder, BookmarkExt> {

    @Inject
    public BookmarksAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        if(viewType == 0){
            return R.layout.layout_item_user;
        } else {
            return R.layout.layout_item_repository;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        if(viewType == 0){
            return new UserViewHolder(itemView);
        } else {
            return new RepoViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if("user".equals(data.get(position).getType())){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        BookmarkExt model = data.get(position);
        if(getItemViewType(position) == 0){
            User user = model.getUser();
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            GlideApp.with(fragment)
                    .load(user.getAvatarUrl())
                    .placeholder(R.mipmap.logo)
                    .into(userViewHolder.avatar);
            userViewHolder.name.setText(user.getLogin());
            userViewHolder.name.setTextColor(ViewUtils.getAccentColor(context));
        } else {
            Repository repository = data.get(position).getRepository();
            RepoViewHolder repoViewHolder = (RepoViewHolder) holder;
            repoViewHolder.tvRepoName.setText(repository.getName());
            repoViewHolder.tvLanguage.setText(StringUtils.isBlank(repository.getLanguage()) ? "" : repository.getLanguage());
            repoViewHolder.tvRepoDescription.setText(repository.getDescription());
            repoViewHolder.tvStarNum.setText(String.valueOf(repository.getStargazersCount()));
            repoViewHolder.tvForkNum.setText(String.valueOf(repository.getForksCount()));
            repoViewHolder.tvOwnerName.setText(repository.getOwner().getLogin());
            GlideApp.with(fragment)
                    .load(repository.getOwner().getAvatarUrl())
                    .placeholder(R.mipmap.logo)
                    .into(repoViewHolder.ivUserAvatar);
            repoViewHolder.forkMark.setVisibility(repository.isFork() ? View.VISIBLE : View.GONE);
        }

    }

    class UserViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.name) TextView name;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class RepoViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_user_avatar) ImageView ivUserAvatar;
        @BindView(R.id.tv_repo_name) TextView tvRepoName;
        @BindView(R.id.tv_language) TextView tvLanguage;
        @BindView(R.id.tv_repo_description) TextView tvRepoDescription;
        @BindView(R.id.tv_star_num) TextView tvStarNum;
        @BindView(R.id.tv_fork_num) TextView tvForkNum;
        @BindView(R.id.tv_owner_name) TextView tvOwnerName;
        @BindView(R.id.fork_mark) View forkMark;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.iv_user_avatar)
        public void onUserClick(){
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                ProfileActivity.show((Activity) context, ivUserAvatar,
                        data.get(getAdapterPosition()).getRepository().getOwner().getLogin(),
                        data.get(getAdapterPosition()).getRepository().getOwner().getAvatarUrl());
            }
        }
    }

}