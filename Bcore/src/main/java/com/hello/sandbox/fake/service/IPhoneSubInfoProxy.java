package com.hello.sandbox.fake.service;

import black.android.telephony.BRTelephonyManager;
import java.lang.reflect.Method;
import com.hello.sandbox.fake.hook.ClassInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import com.hello.sandbox.utils.MethodParameterUtils;

/** Created by BlackBox on 2022/2/26. */
public class IPhoneSubInfoProxy extends ClassInvocationStub {
  public static final String TAG = "IPhoneSubInfoProxy";

  public IPhoneSubInfoProxy() {
    if (BRTelephonyManager.get()._check_sServiceHandleCacheEnabled() != null) {
      BRTelephonyManager.get()._set_sServiceHandleCacheEnabled(true);
    }
    if (BRTelephonyManager.get()._check_getSubscriberInfoService() != null) {
      BRTelephonyManager.get().getSubscriberInfoService();
    }
  }

  @Override
  protected Object getWho() {
    return BRTelephonyManager.get().sIPhoneSubInfo();
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    BRTelephonyManager.get()._set_sIPhoneSubInfo(proxyInvocation);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodParameterUtils.replaceFirstAppPkg(args);
    return super.invoke(proxy, method, args);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("getLine1NumberForSubscriber")
  public static class getLine1NumberForSubscriber extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      return null;
    }
  }
}
