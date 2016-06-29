package com.jcodecraeer.xrecyclerview;


interface BaseRefreshHeader {
    void onMove(float delta);

    boolean releaseAction();

    void refreshComplate();

    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;
}
