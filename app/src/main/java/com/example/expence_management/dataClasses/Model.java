package com.example.expence_management.dataClasses;


public class Model {
   private boolean isSelected=false;

    public Model(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
