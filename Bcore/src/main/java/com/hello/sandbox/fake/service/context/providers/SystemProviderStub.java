package com.hello.sandbox.fake.service.context.providers;

import android.os.IInterface;
import black.android.content.BRAttributionSource;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.fake.hook.ClassInvocationStub;
import com.hello.sandbox.utils.compat.ContextCompat;
import java.lang.reflect.Method;

/** Created by Milk on 4/8/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class SystemProviderStub extends ClassInvocationStub implements BContentProvider {
  private IInterface mBase;

  @Override
  public IInterface wrapper(IInterface contentProviderProxy, String appPkg) {
    mBase = contentProviderProxy;
    injectHook();
    return (IInterface) getProxyInvocation();
  }

  @Override
  protected Object getWho() {
    return mBase;
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {}

  @Override
  protected void onBindMethod() {}

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if ("asBinder".equals(method.getName())) {
      return method.invoke(mBase, args);
    }
    if (args != null && args.length > 0) {
      Object arg = args[0];
      if (arg instanceof String) {
        args[0] = SandBoxCore.getHostPkg();
      } else if (arg.getClass().getName().equals(BRAttributionSource.getRealClass().getName())) {
        ContextCompat.fixAttributionSourceState(arg, SandBoxCore.getHostUid());
      }
    }
    return method.invoke(mBase, args);
  }
}
