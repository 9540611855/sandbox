package com.hello.sandbox.fake.service;

import black.android.os.BRINetworkManagementServiceStub;
import black.android.os.BRServiceManager;
import java.lang.reflect.Method;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import com.hello.sandbox.fake.service.base.UidMethodProxy;
import com.hello.sandbox.utils.MethodParameterUtils;

/** Created by BlackBox on 2022/3/5. */
public class INetworkManagementServiceProxy extends BinderInvocationStub {
  public static final String NAME = "network_management";

  public INetworkManagementServiceProxy() {
    super(BRServiceManager.get().getService(NAME));
  }

  @Override
  protected Object getWho() {
    return BRINetworkManagementServiceStub.get()
        .asInterface(BRServiceManager.get().getService(NAME));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(NAME);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @Override
  protected void onBindMethod() {
    super.onBindMethod();
    addMethodHook(new UidMethodProxy("setUidCleartextNetworkPolicy", 0));
    addMethodHook(new UidMethodProxy("setUidMeteredNetworkBlacklist", 0));
    addMethodHook(new UidMethodProxy("setUidMeteredNetworkWhitelist", 0));
  }

  @ProxyMethod("getNetworkStatsUidDetail")
  public static class getNetworkStatsUidDetail extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      MethodParameterUtils.replaceFirstUid(args);
      MethodParameterUtils.replaceFirstAppPkg(args);
      return method.invoke(who, args);
    }
  }
}
