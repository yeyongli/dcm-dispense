package com.kurumi.dispense.util;

public class Result {
	private static final long serialVersionUID = -6159210228929778190L;

	  public Result() {
	    this(false, "", null, 0);
	  }

	  /**
	   * @param success 是否成功
	   */
	  public Result(boolean success) {
	    this(success, "", null, 0);
	  }

	  /**
	   * @param success 是否成功
	   * @param msg 信息
	   */
	  public Result(boolean success, String msg) {
	    this(success, msg, null, 0);
	  }

	  /**
	   * @param success 是否成功
	   * @param msg 信息
	   * @param data 数据
	   */
	  public Result(boolean success, String msg, Object data) {
	    this(success, msg, data, 0);
	  }
	  /**
	   * @param success 是否成功
	   * @param msg 信息
	   * @param data 数据
	   * @param status 状态码
	   */
	  public Result(boolean success, String msg, Object data, int status) {
	    this.success = success;
	    this.msg = msg;
	    this.data = data;
	    this.status = status;
	  }

	  public String getMsg() {
	    return msg;
	  }

	  public void setMsg(String msg) {
	    this.msg = msg;
	  }

	  public int getStatus() {
	    return status;
	  }

	  public void setStatus(int status) {
	    this.status = status;
	  }

	  public boolean isSuccess() {
	    return success;
	  }

	  public void setSuccess(boolean success) {
	    this.success = success;
	  }

	  public Object getData() {
	    return data;
	  }

	  public void setData(Object data) {
	    this.data = data;
	  }


	  /**
	   * 信息
	   */
	  private String msg;

	  /**
	   * 状态码
	   */
	  private int status;

	  /**
	   * 是否成功
	   */
	  private boolean success;

	  /**
	   * 数据
	   */
	  private Object data;

}
