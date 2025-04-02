package com.hello.sandbox.fake.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import black.android.app.usage.BRIStorageStatsManagerStub;
import black.android.os.BRServiceManager;
import java.lang.reflect.Method;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.utils.MethodParameterUtils;

/** Created by BlackBox on 2022/3/3. */
@TargetApi(Build.VERSION_CODES.O)
public class IStorageStatsManagerProxy extends BinderInvocationStub {

  public IStorageStatsManagerProxy() {
    super(BRServiceManager.get().getService(Context.STORAGE_STATS_SERVICE));
  }

  @Override
  protected Object getWho() {
    return BRIStorageStatsManagerStub.get()
        .asInterface(BRServiceManager.get().getService(Context.STORAGE_STATS_SERVICE));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(Context.STORAGE_STATS_SERVICE);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodParameterUtils.replaceFirstAppPkg(args);
    return super.invoke(proxy, method, args);
  }
}
