package com.openhunme.sdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileUtil {
  public static boolean makeDirectories(String dir) {
    return new File(dir).mkdirs();
  }

  public static boolean isDirectoryExisted(String dir) {
    File file = new File(dir);
    if ((file.isDirectory()) && (file.exists()))
      return true;
    return false;
  }

  public static String readFileContent(File file) {
    String str = "";
    try {
      FileInputStream fis = new FileInputStream(file);
      int size = fis.available();
      byte[] buffer = new byte[size];
      fis.read(buffer);
      fis.close();
      str = new String(buffer,"UTF8");
    } catch (IOException e) {
    }
    return str;
  }
  
  public static void copyAssets(Context context, String src, String dst)
          throws IOException {
    AssetManager am = context.getAssets();
    InputStream in = am.open(src);
    File outFile = new File(dst);
    FileOutputStream out = new FileOutputStream(outFile);
	byte[] buffer = new byte[8192];
	int read;
	while((read = in.read(buffer)) != -1){
      out.write(buffer, 0, read);
	}
    in.close();
    out.flush();
    out.close();
  }

  public static void copyFile(File sourceFile, File targetFile)
          throws IOException {
    FileInputStream in = new FileInputStream(sourceFile);
    FileOutputStream out = new FileOutputStream(targetFile);
    byte[] buffer = new byte[8192];
    int read;
    while ((read = in.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
    in.close();
    out.flush();
    out.close();
  }

  public static void copyDirectory(String sourceDir, String targetDir)
          throws IOException {
    if (!isDirectoryExisted(targetDir)) {
      makeDirectories(targetDir);
    }
    if (isDirectoryExisted(sourceDir)) {
      File[] file = new File(sourceDir).listFiles();
      for (int i = 0; i < file.length; i++) {
        if (file[i].isFile()) {
          if (!targetDir.endsWith(File.separator))
            copyFile(file[i], new File(targetDir + File.separator + file[i].getName()));
          else
            copyFile(file[i], new File(targetDir + file[i].getName()));
        }
        if (file[i].isDirectory()) {
          String subSourceDir;
          String subTargetDir;
          if (!sourceDir.endsWith(File.separator))
            subSourceDir = sourceDir + File.separator + file[i].getName();
          else
            subSourceDir = sourceDir + file[i].getName();
          if (!targetDir.endsWith(File.separator))
            subTargetDir = targetDir + File.separator + file[i].getName();
          else
            subTargetDir = targetDir + file[i].getName();
          copyDirectory(subSourceDir, subTargetDir);
        }
      }
    }
  }

  public static void deleteDirectory(String targetDir)
          throws IOException {
    if (isDirectoryExisted(targetDir)) {
      File[] file = new File(targetDir).listFiles();
      for (int i = 0; i < file.length; i++) {
        if (file[i].isFile())
          file[i].delete();
        if (file[i].isDirectory()) {
          String subTargetDir;
          if (!targetDir.endsWith(File.separator))
            subTargetDir = targetDir + File.separator + file[i].getName();
          else
            subTargetDir = targetDir + file[i].getName();
          deleteDirectory(subTargetDir);
        }
      }
    }
    if (isDirectoryExisted(targetDir)) {
      new File(targetDir).delete();
    }
  }
}
