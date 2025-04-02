package com.hello.sandbox.fake.service;

import android.content.Context;
import black.android.content.pm.BRUserInfo;
import black.android.os.BRIUserManagerStub;
import black.android.os.BRServiceManager;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import java.lang.reflect.Method;
import java.util.ArrayList;

/** Created by Milk on 4/8/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class IUserManagerProxy extends BinderInvocationStub {
  public IUserManagerProxy() {
    super(BRServiceManager.get().getService(Context.USER_SERVICE));
  }

  @Override
  protected Object getWho() {
    return BRIUserManagerStub.get()
        .asInterface(BRServiceManager.get().getService(Context.USER_SERVICE));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(Context.USER_SERVICE);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("getApplicationRestrictions")
  public static class GetApplicationRestrictions extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      args[0] = SandBoxCore.getHostPkg();
      return method.invoke(who, args);
    }
  }

  @ProxyMethod("getProfileParent")
  public static class GetProfileParent extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      Object blackBox =
          BRUserInfo.get()
              ._new(BActivityThread.getUserId(), "sandbox", BRUserInfo.get().FLAG_PRIMARY());
      return blackBox;
    }
  }

  @ProxyMethod("getUsers")
  public static class getUsers extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      return new ArrayList<>();
    }
  }
}
