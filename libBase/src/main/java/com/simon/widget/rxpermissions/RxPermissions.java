/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simon.widget.rxpermissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class RxPermissions {

    static final Object TRIGGER = new Object();

    private static final String TAG = "RxPermissions";
    private static RxPermissions sSingleton;

    public static RxPermissions getInstance(Context ctx) {
        if (sSingleton == null) {
            sSingleton = new RxPermissions(ctx.getApplicationContext());
        }
        return sSingleton;
    }

    private Context mCtx;

    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private Map<String, PublishSubject<Permission>> mSubjects = new HashMap<>();
    private boolean mLogging;

    RxPermissions(Context ctx) {
        mCtx = ctx;
    }

    public RxPermissions setLogging(boolean logging) {
        mLogging = logging;
        return this;
    }

    private void log(String message) {
        if (mLogging) {
            Log.d(TAG, message);
        }
    }

    /**
     * Map emitted items from the source observable into {@code true} if permissions in parameters
     * are granted, or {@code false} if not.
     * <p>
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    public <T> ObservableTransformer<T, Boolean> ensure(final String... permissions) {

        return new ObservableTransformer<T, Boolean>() {
            @Override
            public ObservableSource<Boolean> apply(Observable<T> upstream) {
                return request(upstream, permissions)
                        // Transform Observable<Permission> to Observable<Boolean>
                        .buffer(permissions.length)
                        .flatMap(new Function<List<Permission>, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(@NonNull List<Permission> permissions1) throws Exception {
                                if (permissions1.isEmpty()) {
                                    // Occurs during orientation change, when the subject receives onComplete.
                                    // In that case we don't want to propagate that empty list to the
                                    // subscriber, only the onComplete.
                                    return Observable.empty();
                                }
                                // Return true if all permissions are granted.
                                for (Permission p : permissions1) {
                                    if (!p.granted) {
                                        return Observable.just(false);
                                    }
                                }
                                return Observable.just(true);
                            }
                        });
            }
        };
    }


    /**
     * Map emitted items from the source observable into {@link Permission} objects for each
     * permission in parameters.
     * <p>
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    public <T> ObservableTransformer<T, Permission> ensureEach(final String... permissions) {

        return new ObservableTransformer<T, Permission>() {
            @Override
            public ObservableSource<Permission> apply(Observable<T> upstream) {
                return request(upstream, permissions);
            }
        };
    }

    /**
     * Request permissions immediately, <b>must be invoked during initialization phase
     * of your application</b>.
     */
    public Observable<Boolean> request(final String... permissions) {
        return Observable.just(TRIGGER).compose(ensure(permissions));
    }

    /**
     * Request permissions immediately, <b>must be invoked during initialization phase
     * of your application</b>.
     */
    public Observable<Permission> requestEach(final String... permissions) {
        return Observable.just(TRIGGER).compose(ensureEach(permissions));
    }

    /**
     * @param trigger
     * @param permissions
     * @return
     */
    private Observable<Permission> request(final Observable<?> trigger,
                                           final String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("RxPermissions.request/requestEach requires at least one input permission");
        }
        return oneOf(trigger, pending(permissions))
                .flatMap(new Function<Object, Observable<Permission>>() {
                    @Override
                    public Observable<Permission> apply(Object o) throws Exception {
                        return requestImplementation(permissions);
                    }
                });
    }

    private Observable<?> pending(final String... permissions) {
        for (String p : permissions) {
            if (!mSubjects.containsKey(p)) {
                return Observable.empty();
            }
        }
        return Observable.just(TRIGGER);
    }

    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        if (trigger == null) {
            return Observable.just(TRIGGER);
        }
        return Observable.merge(trigger, pending);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private Observable<Permission> requestImplementation(final String... permissions) {

        List<Observable<Permission>> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();

        // In case of multiple permissions, we create an Observable for each of them.
        // At the end, the observables are combined to have a unique response.
        for (String permission : permissions) {
            log("Requesting permission " + permission);

            if (isGranted(permission)) {
                // Already granted, or not Android M
                // Return a granted Permission object.
                list.add(Observable.just(new Permission(permission, true, false)));
                continue;
            }

            if (isRevoked(permission)) {
                // Revoked by a policy, return a denied Permission object.
                list.add(Observable.just(new Permission(permission, false, false)));
                continue;
            }

            PublishSubject<Permission> subject = mSubjects.get(permission);
            // Create a new subject if not exists
            if (subject == null) {
                unrequestedPermissions.add(permission);
                subject = PublishSubject.create();
                mSubjects.put(permission, subject);
            }

            list.add(subject);
        }

        if (!unrequestedPermissions.isEmpty()) {
            startShadowActivity(unrequestedPermissions
                    .toArray(new String[unrequestedPermissions.size()]));
        }
        return Observable.concat(Observable.fromIterable(list));
    }

    /**
     * Invokes Activity.shouldShowRequestPermissionRationale and wraps
     * the returned value in an observable.
     * <p>
     * In case of multiple permissions, only emits true if
     * Activity.shouldShowRequestPermissionRationale returned true for
     * all revoked permissions.
     * <p>
     * You shouldn't call this method if all permissions have been granted.
     * <p>
     * For SDK &lt; 23, the observable will always emit false.
     */
    public Observable<Boolean> shouldShowRequestPermissionRationale(final Activity activity,
                                                                    final String... permissions) {
        if (!isMarshmallow()) {
            return Observable.just(false);
        }
        return Observable.just(shouldShowRequestPermissionRationale_(activity, permissions));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean shouldShowRequestPermissionRationale_(final Activity activity,
                                                          final String... permissions) {
        for (String p : permissions) {
            if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前进程名字
     *
     * @param ctx
     * @return
     */
    private String getCurrentProcessName(Context ctx) {
        try {
            final int processId = android.os.Process.myPid();
            ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
                if (processId == info.pid) {
                    return info.processName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param permissions
     */
    private void startShadowActivity(String[] permissions) {
        try {
            log("startShadowActivity " + TextUtils.join(", ", permissions));

            Class<?> temp = null;
            String processName = getCurrentProcessName(mCtx);
            log("当前运行的进程 " + processName);
            if (processName.equals(ProcessName.MainProcess)) {
                temp = ShadowActivity.class;
//            } else if (processName.equals(ProcessName.PictureDealProcess)) {
//                temp = ShadowActivity_picture.class;
            } else throw new IllegalArgumentException("当前进程没有处理：" + processName);

            Intent intent = new Intent(mCtx, temp);
            intent.putExtra("permissions", permissions);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns true if the permission is already granted.
     * <p>
     * Always true if SDK &lt; 23.
     */
    public boolean isGranted(String permission) {
        return !isMarshmallow() || isGranted_(permission);
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     * <p>
     * Always false if SDK &lt; 23.
     */
    public boolean isRevoked(String permission) {
        return isMarshmallow() && isRevoked_(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isGranted_(String permission) {
        //special grant permissions
        if (Manifest.permission.SYSTEM_ALERT_WINDOW.equals(permission)) {
            return Settings.canDrawOverlays(mCtx);
        } else if (Manifest.permission.WRITE_SETTINGS.equals(permission)) {
            return Settings.System.canWrite(mCtx);
        }
        return mCtx.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isRevoked_(String permission) {
        return mCtx.getPackageManager().isPermissionRevokedByPolicy(permission, mCtx.getPackageName());
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param shouldShowRequestPermissionRationale
     */
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        try {
            for (int i = 0, size = permissions.length; i < size; i++) {
                log("onRequestPermissionsResult  " + permissions[i]);
                // Find the corresponding subject
                PublishSubject<Permission> subject = mSubjects.get(permissions[i]);
                if (subject != null) {
                    mSubjects.remove(permissions[i]);
                    boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    subject.onNext(new Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]));
                    subject.onComplete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param requestCode
     * @param activity
     * @param permissions
     */
    void onRequestPermissionsResultFailure(int requestCode, Activity activity, String[] permissions) {
        int[] grantResults = new int[permissions.length];
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];

        for (int i = 0; i < permissions.length; i++) {
            grantResults[i] = PackageManager.PERMISSION_DENIED;
            shouldShowRequestPermissionRationale[i] = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
        }

        onRequestPermissionsResult(requestCode, permissions, grantResults, shouldShowRequestPermissionRationale);
    }
}
