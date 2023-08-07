package com.example.anuo.immodule.manager;

import android.content.Context;

import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;

import java.util.UUID;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :25/10/2019
 * Desc  :com.example.anuo.immodule.manager
 */
public class SocketManager {
    private final Context activity;
    private Socket socket;
    public static SocketManager socketManager;
    private Emitter.Listener connectCallback;
    private Emitter.Listener reconnectCallback;
    private Emitter.Listener connectErrorCallback;
    private Emitter.Listener connectTimeoutCallback;
    private Emitter.Listener pushCallback;
    private Emitter.Listener msgCallback;
    private Emitter.Listener sendCallback;
    private Emitter.Listener pingCallback;
    private Emitter.Listener pongCallback;
    private Emitter.Listener loginCallback;
    private Emitter.Listener joinRoomCallback;
    private Emitter.Listener joinGroupCallback;
    private Emitter.Listener disconnectCallback;

    public SocketManager(Context activity) {
        this.activity = activity;
    }

    public static SocketManager instance(Context context) {
        if (socketManager == null) {
            socketManager = new SocketManager(context.getApplicationContext());
        }
        return socketManager;
    }

    public void sendMsg(String socketKey, String content, Ack ack) {
        if (socket != null) {
            socket.emit(socketKey, content, ack);
        }
    }

    public void reconnectSocket() {
        if (socket != null) {
            socket.connect();
        }
    }


    public boolean isConnecting() {
        if (socket != null) {
            return socket.connected();
        }
        return false;
    }


    /**
     * 连接Socket并注册监听
     */
    public Socket connectSocket() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket = null;
        }
        IO.Options options = new IO.Options();
        options.transports = new String[]{WebSocket.NAME};
//        options.reconnectionAttempts = 2;     // 重连尝试次数
//        options.reconnectionDelay = 1000;     // 失败重连的时间间隔(ms)
//        options.timeout = 20000;              // 连接超时时间(ms)
        options.forceNew = true;
        options.rememberUpgrade = true;
        options.secure = true;
//        options.path = "/chat/socket.io";
        try {
            String keyEncrpt = null;
            String uuid = UUID.randomUUID().toString();
            keyEncrpt = AESUtils.encrypt("{\"key\":\"#!@$%&^*AEUBSJXK\",\"token\":\"" + uuid + "\"}", ConfigCons.DEFAULT_IV, ConfigCons.DEFAULT_KEY);
            options.query = "key=" + keyEncrpt;
            socket = IO.socket(ConfigCons.CHAT_SERVER_URL, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        socket.io().timeout(-1);
        socket.on(Socket.EVENT_CONNECT, connectListener)
                .on(Socket.EVENT_CONNECT_ERROR, connectErrorListener)
                .on(Socket.EVENT_CONNECT_TIMEOUT, connectTimeoutListener)
                .on(Socket.EVENT_PING, pingListener)
                .on(Socket.EVENT_PONG, pongListner)
                .on(Socket.EVENT_MESSAGE, msgListner)
                .on(Socket.EVENT_DISCONNECT, disconnectListener)
                .on(ConfigCons.push_p, pushListener)
                .on(ConfigCons.reconnect_r, reconnectListener)
                .on(ConfigCons.login, loginListener)
                .on(ConfigCons.user_r, sendListener)
                .on(ConfigCons.user_join_room, joinRoomListener)
                .on(ConfigCons.user_join_group, joinGroupListener);
        socket.connect();
        return socket;
    }

    /**
     * 断开Socket连接并清除监听,清除记录的当前房间ID
     */
    public void disconnectSocket() {
        if (socket != null) {
            socket.off(Socket.EVENT_CONNECT, connectListener)
                    .off(Socket.EVENT_CONNECT_ERROR, connectErrorListener)
                    .off(Socket.EVENT_CONNECT_TIMEOUT, connectTimeoutListener)
                    .off(Socket.EVENT_PING, pingListener)
                    .off(Socket.EVENT_PONG, pongListner)
                    .off(Socket.EVENT_MESSAGE, msgListner)
                    .off(Socket.EVENT_DISCONNECT, disconnectListener)
                    .off(ConfigCons.push_p, pushListener)
                    .off(ConfigCons.reconnect_r, reconnectListener)
                    .off(ConfigCons.login, loginListener)
                    .off(ConfigCons.user_r, sendListener)
                    .off(ConfigCons.user_join_room, joinRoomListener)
                    .off(ConfigCons.user_join_group, joinGroupListener);
            socket.disconnect();
            socket = null;
        }
    }

    //--------------------------------SocketIO监听-----------------------------------

    private Emitter.Listener connectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (connectCallback != null) {
                connectCallback.call(args);
            }
        }
    };

    private Emitter.Listener reconnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (reconnectCallback != null) {
                reconnectCallback.call(args);
            }
        }
    };

    private Emitter.Listener pushListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (pushCallback != null) {
                pushCallback.call(args);
            }
        }
    };

    private Emitter.Listener connectErrorListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (connectErrorCallback != null) {
                connectErrorCallback.call(args);
            }
        }
    };

    private Emitter.Listener connectTimeoutListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (connectTimeoutCallback != null) {
                connectTimeoutCallback.call(args);
            }
        }
    };

    private Emitter.Listener pingListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (pingCallback != null) {
                pingCallback.call(args);
            }
        }
    };

    private Emitter.Listener pongListner = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (pongCallback != null) {
                pongCallback.call(args);
            }
        }
    };

    private Emitter.Listener msgListner = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (msgCallback != null) {
                msgCallback.call(args);
            }
        }
    };

    private Emitter.Listener disconnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (disconnectCallback != null) {
                disconnectCallback.call(args);
            }
        }
    };

    private Emitter.Listener loginListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (loginCallback != null) {
                loginCallback.call(args);
            }
        }
    };

    private Emitter.Listener sendListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (sendCallback != null) {
                sendCallback.call(args);
            }
        }
    };

    private Emitter.Listener joinRoomListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (joinRoomCallback != null) {
                joinRoomCallback.call(args);
            }
        }
    };

    private Emitter.Listener joinGroupListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (joinGroupCallback != null) {
                joinGroupCallback.call(args);
            }
        }
    };

    public void setConnectListener(Emitter.Listener connectListener) {
        this.connectCallback = connectListener;
    }

    public void setReconnectListener(Emitter.Listener reconnectListener) {
        this.reconnectCallback = reconnectListener;
    }

    public void setPushListener(Emitter.Listener pushListener) {
        this.pushCallback = pushListener;
    }

    public void setConnectErrorListener(Emitter.Listener connectErrorListener) {
        this.connectErrorCallback = connectErrorListener;
    }

    public void setConnectTimeoutListener(Emitter.Listener connectTimeoutListener) {
        this.connectTimeoutCallback = connectTimeoutListener;
    }

    public void setPingListener(Emitter.Listener pingListener) {
        this.pingCallback = pingListener;
    }

    public void setPongListner(Emitter.Listener pongListner) {
        this.pongCallback = pongListner;
    }

    public void setMsgListner(Emitter.Listener msgListner) {
        this.msgCallback = msgListner;
    }

    public void setDisconnectListener(Emitter.Listener disconnectListener) {
        this.disconnectCallback = disconnectListener;
    }

    public void setLoginListener(Emitter.Listener loginListener) {
        this.loginCallback = loginListener;
    }

    public void setSendListener(Emitter.Listener sendListener) {
        this.sendCallback = sendListener;
    }

    public void setJoinRoomListener(Emitter.Listener joinRoomListener) {
        this.joinRoomCallback = joinRoomListener;
    }

    public void setJoinGroupListener(Emitter.Listener joinGroupListener) {
        this.joinGroupCallback = joinGroupListener;
    }
}
