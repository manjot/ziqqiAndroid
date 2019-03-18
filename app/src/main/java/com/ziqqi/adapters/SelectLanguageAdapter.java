package com.ziqqi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.languagemodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.SelectLanguageViewHolder> {
    private List<Payload> payloadList;
    private int selectedPosition = -1;
    private Context context;
    private OnItemClickListener listener;
    Typeface regular;

    public SelectLanguageAdapter(List<Payload> payloadList, Context context, OnItemClickListener listener) {
        this.payloadList = payloadList;
        this.context = context;
        this.listener = listener;
        regular = FontCache.get(context.getResources().getString(R.string.regular), context);

    }

    @NonNull
    @Override
    public SelectLanguageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_select_language, viewGroup, false);
        return new SelectLanguageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectLanguageViewHolder holder, int i) {
        Payload payload = payloadList.get(i);
        holder.tvLanguageCode.setText(payload.getLanguageShortname());
        holder.tvLanguage.setText(payload.getLanguageName());
        holder.rlLang.setBackground(selectedPosition == i ? context.getResources().getDrawable(R.drawable.selected_bg) : context.getResources().getDrawable(R.drawable.white_bg_ripple));
        holder.tvLanguageCode.setBackground(selectedPosition == i ? context.getResources().getDrawable(R.drawable.black_circle) : context.getResources().getDrawable(R.drawable.grey_circle));
        holder.tvLanguageCode.setTextColor(selectedPosition == i ? context.getResources().getColor(R.color.colorWhite) : context.getResources().getColor(R.color.colorBlack));
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class SelectLanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvLanguageCode, tvLanguage;
        RelativeLayout rlLang;

        public SelectLanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLanguage = itemView.findViewById(R.id.tv_language);
            tvLanguageCode = itemView.findViewById(R.id.tv_lang_code);
            rlLang = itemView.findViewById(R.id.rl_lang);
            rlLang.setOnClickListener(this);
            tvLanguageCode.setTypeface(regular);
            tvLanguage.setTypeface(regular);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }
            notifyItemChanged(selectedPosition);
            selectedPosition = getAdapterPosition();
            notifyItemChanged(selectedPosition);
            listener.onItemClick(tvLanguage.getText().toString(),null);
        }
    }
}
