package com.hello.sandbox.fake.service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import black.android.net.wifi.BRIWifiManagerStub;
import black.android.net.wifi.BRWifiInfo;
import black.android.net.wifi.BRWifiSsid;
import black.android.os.BRServiceManager;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import java.lang.reflect.Method;

/** Created by Milk on 4/12/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class IWifiManagerProxy extends BinderInvocationStub {
  public static final String TAG = "IWifiManagerProxy";

  public IWifiManagerProxy() {
    super(BRServiceManager.get().getService(Context.WIFI_SERVICE));
  }

  @Override
  protected Object getWho() {
    return BRIWifiManagerStub.get()
        .asInterface(BRServiceManager.get().getService(Context.WIFI_SERVICE));
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(Context.WIFI_SERVICE);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("getConnectionInfo")
  public static class GetConnectionInfo extends MethodHook {
    /*
     * It doesn't have public method to set BSSID and SSID fields in WifiInfo class,
     * So the reflection framework invocation appeared.
     * commented by BlackBoxing at 2022/03/08
     * */
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      if (args != null && args.length > 1 && args[0] instanceof String) {
        if (!args[0].toString().equals(SandBoxCore.getHostPkg())) {
          args[0] = SandBoxCore.getHostPkg();
        }
      }
      WifiInfo wifiInfo = (WifiInfo) method.invoke(who, args);
      BRWifiInfo.get(wifiInfo)._set_mBSSID("ac:62:5a:82:65:c4");
      BRWifiInfo.get(wifiInfo)._set_mMacAddress("ac:62:5a:82:65:c4");
      BRWifiInfo.get(wifiInfo)
          ._set_mWifiSsid(BRWifiSsid.get().createFromAsciiEncoded("BlackBox_Wifi"));
      return wifiInfo;
    }

    public static String intIP2StringIP(int ip) {
      return (ip & 0xFF)
          + "."
          + ((ip >> 8) & 0xFF)
          + "."
          + ((ip >> 16) & 0xFF)
          + "."
          + (ip >> 24 & 0xFF);
    }

    public static int ip2Int(String ipString) {
      // 取 ip 的各段
      String[] ipSlices = ipString.split("\\.");
      int rs = 0;
      for (int i = 0; i < ipSlices.length; i++) {
        // 将 ip 的每一段解析为 int，并根据位置左移 8 位
        int intSlice = Integer.parseInt(ipSlices[i]) << 8 * i;
        // 或运算
        rs = rs | intSlice;
      }
      return rs;
    }
  }
}
