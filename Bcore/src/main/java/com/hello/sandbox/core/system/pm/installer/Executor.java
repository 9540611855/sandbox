package com.hello.sandbox.core.system.pm.installer;

import com.hello.sandbox.core.system.pm.BPackageSettings;
import com.hello.sandbox.entity.pm.InstallOption;

/** Created by Milk on 4/24/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public interface Executor {
  public static final String TAG = "InstallExecutor";

  int exec(BPackageSettings ps, InstallOption option, int userId);
}
