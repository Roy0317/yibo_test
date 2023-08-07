package com.simon.view.webview;


public interface WebViewJavascriptBridge {

     void send(String data);

     void send(String data, CallBackFunction responseCallback);
}
