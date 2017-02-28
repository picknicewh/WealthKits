package com.openhunme.sdk.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

//import android.util.Log;

public class ZipUtil
{
  private static final int BUFFER_SIZE = 8192;
 
  public static int unzip(File zipFile, String folderPath)
		    throws ZipException, IOException
  {
    if (!folderPath.endsWith(File.separator)) {
      folderPath = folderPath + File.separator;
    }

    ZipFile zfile = new ZipFile(zipFile);
    Enumeration<?> zList = zfile.entries();
    ZipEntry ze = null;
    byte[] buf = new byte[BUFFER_SIZE];
    while (zList.hasMoreElements()) {
      ze = (ZipEntry)zList.nextElement();

      if (ze.isDirectory()) {
        String dirstr = folderPath + ze.getName();
//        Log.i("unzip", "d " + dirstr);
        dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
        File f = new File(dirstr);
        f.mkdir();
      }
      else
      {
//    	Log.i("unzip", "f " + folderPath + " " + ze.getName());
        OutputStream os = new BufferedOutputStream(new FileOutputStream(
          getAbsoluteFile(folderPath, ze.getName())));
        InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
        int readLen = 0;
        while ((readLen = is.read(buf, 0, BUFFER_SIZE)) != -1) {
          os.write(buf, 0, readLen);
        }
        is.close();
        os.close();
      }
    }
    zfile.close();
    return 0;
  }

  public static int unzipfork(File zipFile, String inFolderPath, String outFolderPath)
    throws ZipException, IOException
  {
    if (!inFolderPath.endsWith(File.separator)) {
      inFolderPath = inFolderPath + File.separator;
    }
    if (!outFolderPath.endsWith(File.separator)) {
      outFolderPath = outFolderPath + File.separator;
    }

    ZipFile zfile = new ZipFile(zipFile);
    Enumeration<?> zList = zfile.entries();
    ZipEntry ze = null;
    byte[] buf = new byte[BUFFER_SIZE];
    while (zList.hasMoreElements()) {
      ze = (ZipEntry)zList.nextElement();

      if (ze.isDirectory()) {
        String dirstr = inFolderPath + ze.getName();
        dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
        File f = new File(dirstr);
        f.mkdir();
        dirstr = outFolderPath + ze.getName();
        dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
        f = new File(dirstr);
        f.mkdir();
      }
      else
      {
        OutputStream os;
        if (ze.getName().endsWith(".js")) {
          os = new BufferedOutputStream(new FileOutputStream(
            getAbsoluteFile(inFolderPath, ze.getName())));
        } else if (ze.getName().endsWith(".htm")) {
          os = new BufferedOutputStream(new FileOutputStream(
            getAbsoluteFile(inFolderPath, ze.getName())));
        } else if (ze.getName().endsWith(".html")) {
          os = new BufferedOutputStream(new FileOutputStream(
            getAbsoluteFile(inFolderPath, ze.getName())));
        } else if (ze.getName().endsWith(".css")) {
          os = new BufferedOutputStream(new FileOutputStream(
            getAbsoluteFile(inFolderPath, ze.getName())));
        } else {
          os = new BufferedOutputStream(new FileOutputStream(
            getAbsoluteFile(outFolderPath, ze.getName())));
        }
        InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
        int readLen = 0;
        while ((readLen = is.read(buf, 0, BUFFER_SIZE)) != -1) {
          os.write(buf, 0, readLen);
        }
        is.close();
        os.close();
      }
    }
    zfile.close();
    return 0;
  }

  public static File getAbsoluteFile(String baseDir, String relativeFileName)
  {
    String[] dirs = relativeFileName.split("/");
    File ret = new File(baseDir);
    String substr = null;
    if (dirs.length > 1) {
      for (int i = 0; i < dirs.length - 1; i++) {
        substr = dirs[i];
        try {
          substr = new String(substr.getBytes("8859_1"), "GB2312");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        ret = new File(ret, substr);
      }

      if (!ret.exists()) {
        ret.mkdirs();
      }
      substr = dirs[(dirs.length - 1)];
      try {
        substr = new String(substr.getBytes("8859_1"), "GB2312");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      ret = new File(ret, substr);
    } else {
      ret = new File(baseDir, relativeFileName);
    }
    return ret;
  }
}
