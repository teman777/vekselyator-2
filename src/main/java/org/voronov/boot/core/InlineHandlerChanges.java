package org.voronov.boot.core;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class InlineHandlerChanges {
    private InlineKeyboardMarkup markup;
    private String newMsgText;
    private boolean deleteMsg = false;

    public InlineHandlerChanges(InlineKeyboardMarkup markup, String newMsgText) {
        this.markup = markup;
        this.newMsgText = newMsgText;
    }

    public InlineHandlerChanges(boolean deleteMsg) {
        this.deleteMsg = deleteMsg;
    }

    public InlineHandlerChanges(String newMsgText) {
        this.newMsgText = newMsgText;
    }

    public InlineHandlerChanges(InlineKeyboardMarkup markup) {
        this.markup = markup;
    }

    public InlineKeyboardMarkup getMarkup() {
        return markup;
    }

    public void setMarkup(InlineKeyboardMarkup markup) {
        this.markup = markup;
    }

    public String getNewMsgText() {
        return newMsgText;
    }

    public void setNewMsgText(String newMsgText) {
        this.newMsgText = newMsgText;
    }

    public boolean isDeleteMsg() {
        return deleteMsg;
    }

    public void setDeleteMsg(boolean deleteMsg) {
        this.deleteMsg = deleteMsg;
    }
}
