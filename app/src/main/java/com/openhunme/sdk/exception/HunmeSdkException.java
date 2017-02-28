package com.openhunme.sdk.exception;

public class HunmeSdkException extends Exception
{
  private static final long serialVersionUID = 1197246990404953840L;
  private int errcode = -99;
  private Exception innerException;

  public HunmeSdkException()
  {
  }

  public HunmeSdkException(String message)
  {
    super(message);
    this.innerException = this;
  }

  public HunmeSdkException(int errcode, String meesage) {
    super(meesage);
    this.errcode = errcode;
    this.innerException = this;
  }

  public HunmeSdkException(int errcode, String meesage, Exception innerException) {
    super(meesage);
    this.errcode = errcode;
    this.innerException = innerException;
  }

  public HunmeSdkException(String meesage, Exception innerException) {
    super(meesage);
    this.innerException = innerException;
  }

  public HunmeSdkException(Exception exception) {
    super(exception);
    this.innerException = this;
  }

  public int getErrcode() {
    return this.errcode;
  }

  public Exception getInnerException() {
    return this.innerException;
  }

  public String toString()
  {
    return super.toString();
  }
}
