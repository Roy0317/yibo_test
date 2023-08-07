package com.example.anuo.immodule.eventbus;

public class RefreshFloatMenuEvent {

    private boolean showFloat = true;

    public RefreshFloatMenuEvent() {
    }


    public RefreshFloatMenuEvent(boolean showFloat) {
        this.showFloat = showFloat;
    }

    public boolean isShowFloat() {
        return showFloat;
    }

    public void setShowFloat(boolean showFloat) {
        this.showFloat = showFloat;
    }
}
