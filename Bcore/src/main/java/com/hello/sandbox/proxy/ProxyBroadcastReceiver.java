package com.hello.sandbox.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.entity.am.PendingResultData;
import com.hello.sandbox.proxy.record.ProxyBroadcastRecord;

/** Created by BlackBox on 2022/2/25. */
public class ProxyBroadcastReceiver extends BroadcastReceiver {
  public static final String TAG = "ProxyBroadcastReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    intent.setExtrasClassLoader(context.getClassLoader());
    ProxyBroadcastRecord record = ProxyBroadcastRecord.create(intent);
    if (record.mIntent == null) {
      return;
    }
    PendingResult pendingResult = goAsync();
    try {
      SandBoxCore.getBActivityManager()
          .scheduleBroadcastReceiver(
              record.mIntent, new PendingResultData(pendingResult), record.mUserId);
    } catch (RemoteException e) {
      pendingResult.finish();
    }
  }
}
