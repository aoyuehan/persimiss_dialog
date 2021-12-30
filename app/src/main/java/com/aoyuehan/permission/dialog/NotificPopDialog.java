package com.aoyuehan.permission.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aoyuehan.permission.R;
import com.aoyuehan.permission.callBack.AgreeInterface;
import com.aoyuehan.permission.utils.StringUtil;

import androidx.annotation.NonNull;

/**
 * Created by aoYueHan on 2020/6/1.
 */

public class NotificPopDialog extends Dialog {
    private Context context;
    private AgreeInterface agreeInterface;
    private TextView notificPopTitle, notificPopContent, notificPopAgree, notificPopNoAgree;
    private String title,content,left,right;

    public NotificPopDialog(@NonNull Context context, String title, String content, String left, String right, AgreeInterface agreeInterface) {
        super(context, R.style.Dialog_File_Download);
        this.context = context;
        this.title = title;
        this.content = content;
        this.agreeInterface = agreeInterface;
        this.left = left;
        this.right = right;
        initView();
    }

    private void initView() {
        setContentView(R.layout.notific_pop_layout);
        setCancelable(false);
        notificPopTitle = findViewById(R.id.notific_pop_title);
        notificPopContent = findViewById(R.id.notific_pop_content);
        notificPopAgree = findViewById(R.id.notific_pop_agree);
        notificPopNoAgree = findViewById(R.id.notific_pop_no_agree);
        notificPopTitle.setText(StringUtil.isStrEmpty(title)?"":title);
        notificPopContent.setText(StringUtil.isStrEmpty(content)?"":content);
        notificPopAgree.setText(StringUtil.isStrEmpty(right)?"":right);
        notificPopNoAgree.setText(StringUtil.isStrEmpty(left)?"":left);
        notificPopAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(agreeInterface!=null)
                    agreeInterface.agree();
            }
        });
        notificPopNoAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(agreeInterface!=null)
                    agreeInterface.noAgree();
            }
        });
    }
}
