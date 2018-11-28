package kivi.ugran.com.launcher.navigationcontroller;

public enum BottomTab {
    HOME(0), REQUESTS(1), GALLERY(2), NONE(-1);
    public Integer tabId;

    BottomTab(Integer tabId) {
        this.tabId = tabId;
    }
}
