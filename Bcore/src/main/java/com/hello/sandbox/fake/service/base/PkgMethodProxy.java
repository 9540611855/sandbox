package com.hello.sandbox.fake.service.base;

import java.lang.reflect.Method;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.utils.MethodParameterUtils;

public class PkgMethodProxy extends MethodHook {

  String mName;

  public PkgMethodProxy(String name) {
    mName = name;
  }

  @Override
  protected String getMethodName() {
    return mName;
  }

  @Override
  protected Object hook(Object who, Method method, Object[] args) throws Throwable {
    MethodParameterUtils.replaceFirstAppPkg(args);
    return method.invoke(who, args);
  }
}
