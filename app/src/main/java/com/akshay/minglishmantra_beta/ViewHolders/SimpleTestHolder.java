package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class SimpleTestHolder extends RecyclerView.ViewHolder {


    public LinearLayout mcqlayoutLl;

    public SimpleTestHolder(@NonNull View itemView) {
        super(itemView);

        mcqlayoutLl =itemView.findViewById(R.id.test_single_subjectivequestion_in_mcqLayoutLl);


    }
}
