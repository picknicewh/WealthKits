package com.openhunme.sdk.data.bean;

import com.google.myjson.annotations.SerializedName;

public class FileMD5Bean
{

  @SerializedName("Path")
  public String Path;

  @SerializedName("MD5")
  public String MD5;

  public FileMD5Bean()
  {
  }

  public FileMD5Bean(String path, String md5)
  {
    this.Path = path;
    this.MD5 = md5;
  }

  public int hashCode()
  {
    return String.format("%s, %s", new Object[] { this.Path, this.MD5 }).hashCode();
  }

  public boolean equals(Object o)
  {
    if (!(o instanceof FileMD5Bean)) {
      return false;
    }
    FileMD5Bean rhs = (FileMD5Bean)o;
    return (this.Path.equals(rhs.Path)) && (this.MD5.equals(rhs.MD5));
  }
}
