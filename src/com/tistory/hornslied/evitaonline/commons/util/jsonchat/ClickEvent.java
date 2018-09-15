package com.tistory.hornslied.evitaonline.commons.util.jsonchat;

/**
 * @author Avis Network
 */
public enum ClickEvent {
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    OPEN_URL("open_url"),
    CHANGE_PAGE("change_page");
    
    private String _minecraftString;
    
    ClickEvent(String minecraftString) {
        _minecraftString = minecraftString;
    }
    
    @Override
    public String toString() {
        return _minecraftString;
    }
}
