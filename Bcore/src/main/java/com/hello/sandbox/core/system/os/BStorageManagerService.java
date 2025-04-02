package com.hello.sandbox.core.system.os;

import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.storage.StorageVolume;
import black.android.os.storage.BRStorageManager;
import black.android.os.storage.BRStorageVolume;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.core.env.BEnvironment;
import com.hello.sandbox.core.system.ISystemService;
import com.hello.sandbox.core.system.user.BUserHandle;
import com.hello.sandbox.fake.provider.FileProvider;
import com.hello.sandbox.proxy.ProxyManifest;
import com.hello.sandbox.utils.compat.BuildCompat;
import java.io.File;

/** Created by Milk on 4/10/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class BStorageManagerService extends IBStorageManagerService.Stub implements ISystemService {
  private static final BStorageManagerService sService = new BStorageManagerService();

  public static BStorageManagerService get() {
    return sService;
  }

  public BStorageManagerService() {}

  @Override
  public StorageVolume[] getVolumeList(int uid, String packageName, int flags, int userId)
      throws RemoteException {
    if (BRStorageManager.get().getVolumeList(0, 0) == null) {
      return null;
    }
    try {
      StorageVolume[] storageVolumes =
          BRStorageManager.get().getVolumeList(BUserHandle.getUserId(Process.myUid()), 0);
      if (storageVolumes == null) return null;
      for (StorageVolume storageVolume : storageVolumes) {
        BRStorageVolume.get(storageVolume)._set_mPath(BEnvironment.getExternalUserDir(userId));
        if (BuildCompat.isPie()) {
          BRStorageVolume.get(storageVolume)
              ._set_mInternalPath(BEnvironment.getExternalUserDir(userId));
        }
      }
      return storageVolumes;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Uri getUriForFile(String file) throws RemoteException {
    return FileProvider.getUriForFile(
        SandBoxCore.getContext(), ProxyManifest.getProxyFileProvider(), new File(file));
  }

  @Override
  public void systemReady() {}
}
