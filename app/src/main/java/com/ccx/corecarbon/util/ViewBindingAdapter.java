package com.ccx.corecarbon.util;

import android.view.View;

import androidx.databinding.BindingAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * CreatedBy Vijayakumar_KA On 03-Dec-2020 11:44 PM.
 */
public class ViewBindingAdapter {

    @BindingAdapter("onClick")
    public static void onClick(@NotNull View view, @NotNull SingleClickListener listener) {
        view.setOnClickListener(listener);
    }
}
