package com.hello.sandbox.fake.provider;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;
import com.hello.sandbox.utils.compat.BuildCompat;
import java.io.File;
import java.util.List;

/** Created by Milk on 4/18/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class FileProviderHandler {

  public static Uri convertFileUri(Context context, Uri uri) {
    if (BuildCompat.isN()) {
      File file = convertFile(context, uri);
      if (file == null) return null;
      return SandBoxCore.getBStorageManager().getUriForFile(file.getAbsolutePath());
    }
    return uri;
  }

  public static File convertFile(Context context, Uri uri) {
    List<ProviderInfo> providers = BActivityThread.getProviders();
    for (ProviderInfo provider : providers) {
      try {
        File fileForUri = FileProvider.getFileForUri(context, provider.authority, uri);
        if (fileForUri != null && fileForUri.exists()) {
          return fileForUri;
        }
      } catch (Exception ignored) {
      }
    }
    return null;
  }
}
