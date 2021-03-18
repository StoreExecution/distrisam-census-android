package com.storexecution;

import com.storexecution.cocacola.model.Fridge;

public interface FridgeDialogCallbackInterface {

    public void setFridge(Fridge fridge, int index);

    public void deleteFridge(int index);
}
