package com.simon.base;


import com.simon.eunmmodel.ELoadingType;

/**
 * Created by simon on 17-8-4.
 */

public interface BaseInterface {

    void showProgress();

    void showProgress(ELoadingType type);

    void dismissProgress();
}
