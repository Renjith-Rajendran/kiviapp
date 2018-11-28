package kivi.ugran.com.launcher.navigationcontroller;

import kivi.ugran.com.core.Constants;

/**
 * All fragments should be identified by its tag and the Bottom Navigation tab it belongs to
 */
public enum FragmentTransactionRequest {

    /**
     * Do not use class names as tag as it could be trimmed by pro-guard
     */
    HOME_ROOT_FRAGMENT(Constants.ChildFragmentTags.HOME_ROOT_FRAGMENT_TAG, 0),
    REQUEST_ROOT_FRAGMENT(Constants.ChildFragmentTags.REQUEST_ROOT_FRAGMENT_TAG, 1),
    GALLERY_ROOT_FRAGMENT(Constants.ChildFragmentTags.GALLERY_ROOT_FRAGMENT_TAG, 2),
    EMPTY("", -1);

    public Integer tabId;
    public String tag;

    FragmentTransactionRequest(String tag, Integer tabId) {
        this.tag = tag;
        this.tabId = tabId;
    }
}
