package com.yibo.yiboapp.data;

import crazy_wrapper.Crazy.response.SessionResponse;

public interface ResponseCallback {
    void onResponse(SessionResponse<?> response);
}
