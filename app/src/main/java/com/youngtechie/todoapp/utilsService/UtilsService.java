package com.youngtechie.todoapp.utilsService;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

public class UtilsService {

  public void  hideKeyboard(View view , Activity activity){
     try{
         InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
         inputMethodManager.hideSoftInputFromWindow(view.getWindowToken() , 0);
     } catch ( Exception e){
         e.printStackTrace();
     }
    }




  public void showSnackBar(View view , String message)  {
      Snackbar.make(view , message , Snackbar.LENGTH_LONG).show();
  }
}
