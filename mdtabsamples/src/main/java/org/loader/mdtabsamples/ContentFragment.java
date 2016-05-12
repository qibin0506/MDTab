package org.loader.mdtabsamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by loader on 2016/5/12.
 */
public class ContentFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private String mContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContent = getArguments().getString("content");
        View layout = inflater.inflate(R.layout.content_layout, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.list);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new Adapter());
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.text.setText(mContent + "_" + position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView text;
            public Holder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
