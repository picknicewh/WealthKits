package com.openhunme.sdk.data.bean;

import com.google.myjson.annotations.SerializedName;

public class UpdateManifestBean
{
  @SerializedName("status")
  public String status;

  @SerializedName("soft_version")
  public String softVersion;

  @SerializedName("bundle_model")
  public String bundleModel;

  @SerializedName("bundle_version")
  public String bundleVersion;

  @SerializedName("bundle_install_path")
  public String bundleInstallPath;

  @SerializedName("bundle_md5")
  public String bundleMd5;

  @SerializedName("upgrade_note")
  public String upgradeNote;

  @SerializedName("upgrade_type")
  public String upgradeType;

  @SerializedName("release_status")
  public String releaseStatus;
}
