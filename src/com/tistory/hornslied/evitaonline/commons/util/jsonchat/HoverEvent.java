package com.tistory.hornslied.evitaonline.commons.util.jsonchat;

/**
 * @author Avis Network
 */
public enum HoverEvent {
    SHOW_TEXT("show_text"),
    SHOW_ITEM("show_item"),
    SHOW_ACHIEVEMENT("show_achievement");
    
    private String _minecraftString;
    
    HoverEvent(String minecraftString) {
        _minecraftString = minecraftString;
    }
    
    @Override
    public String toString() {
        return _minecraftString;
    }
}
