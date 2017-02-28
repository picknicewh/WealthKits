package com.openhunme.sdk.data.bean;

import com.google.myjson.annotations.SerializedName;
import java.util.List;

public class VersionManifestBean {
  @SerializedName("version")
  public String version;
  @SerializedName("model")
  public String model;
  @SerializedName("md5")
  public String md5;
  @SerializedName("md5List")
  public List<FileMD5Bean> FileMD5List;
}
