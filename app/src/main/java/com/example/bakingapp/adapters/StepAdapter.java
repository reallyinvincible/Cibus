package com.example.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.interfaces.StepSelectInterface;
import com.example.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final List<Step> stepList;
    private final StepSelectInterface mStepSelectInterface;

    public StepAdapter(List<Step> stepList, StepSelectInterface stepSelectInterface) {
        this.stepList = stepList;
        this.mStepSelectInterface = stepSelectInterface;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_steps, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, int position) {
        Step data = stepList.get(position);
        holder.stepNumber.setText("Step " + String.valueOf(position + 1));
        holder.stepDescription.setText(data.getShortDescription());

        holder.stepCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepSelectInterface.onStepSelect(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step_number)
        TextView stepNumber;

        @BindView(R.id.tv_step_description)
        TextView stepDescription;

        @BindView(R.id.cv_step_card)
        CardView stepCard;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
