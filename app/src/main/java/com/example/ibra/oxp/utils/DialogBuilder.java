package com.example.ibra.oxp.utils;


import android.content.Context;
import android.widget.ImageView;

import com.example.ibra.oxp.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class DialogBuilder {
    private Context context;
    private KProgressHUD dialog;
    int counter;
    String loadingString;

    public DialogBuilder(Context context) {
        this.context = context;
        counter = 0;
        dialog = null;
    }

    public void loadingDialog(String str){
        dialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(str )
                .setCancellable(false);
        dialog.show();
    }

    public void failedDialog(String str){
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_close_white);
        dialog = KProgressHUD.create(context)
                .setCustomView(imageView)
                .setLabel("   " + str + "   ");
        dialog.show();
    }

    public void successDialog(){

    }

    public void infoDialog(){

    }

    public void nothingDialog(){

    }
    public void dismissDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
