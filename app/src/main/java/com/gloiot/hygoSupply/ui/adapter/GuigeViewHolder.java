package com.gloiot.hygoSupply.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.gloiot.hygoSupply.bean.GuigeModel;
import com.gloiot.hygoSupply.databinding.ItemShangpinGuigeNewBinding;

/**
 * 作者：Ljy on 2017/3/17.
 * 邮箱：enjoy_azad@sina.com
 */

public class GuigeViewHolder extends RecyclerView.ViewHolder {

    private final ItemShangpinGuigeNewBinding binding;
    public GuigeAdapter.OnItemClickListener clickListener;

    public GuigeViewHolder(final ItemShangpinGuigeNewBinding binding, final GuigeAdapter.OnItemClickListener clickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.clickListener = clickListener;
        binding.ivItemShangpinGuigeShanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(binding.ivItemShangpinGuigeShanchu, getLayoutPosition());
            }
        });
        binding.etItemShangpinGuigeGonghuojia.addTextChangedListener(new PriceTextWatcher(binding.etItemShangpinGuigeGonghuojia));
        binding.etItemShangpinGuigeJianyijia.addTextChangedListener(new PriceTextWatcher(binding.etItemShangpinGuigeJianyijia));
    }

    //绑定model
    public void bind(GuigeModel model) {
        binding.setModel(model);
        binding.executePendingBindings();
        if (binding.getModel().isQuickEdit()) {
            binding.ivItemGuigeGonghuojiaAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(binding.ivItemGuigeGonghuojiaAdd, getLayoutPosition());
                }
            });
            binding.ivItemGuigeJianyijiaAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(binding.ivItemGuigeJianyijiaAdd, getLayoutPosition());
                }
            });
            binding.ivItemGuigeKucunAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(binding.ivItemGuigeKucunAdd, getLayoutPosition());
                }
            });
            binding.ivItemGuigeGonghuojiaSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(binding.ivItemGuigeGonghuojiaSub, getLayoutPosition());
                }
            });
            binding.ivItemGuigeJianyijiaSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(binding.ivItemGuigeJianyijiaSub, getLayoutPosition());
                }
            });
            binding.ivItemGuigeKucunSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(binding.ivItemGuigeKucunSub, getLayoutPosition());
                }
            });

        }
    }

    public void clearFouces() {
        binding.llItemShangpinGuige.clearFocus();
    }

    class PriceTextWatcher implements TextWatcher {
        private EditText editText;

        public PriceTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //小数点后位数控制
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }
            }
            //第一位为点
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }
            //第一位为零，后面只能输入点
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}
