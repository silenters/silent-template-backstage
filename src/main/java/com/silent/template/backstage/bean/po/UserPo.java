package com.silent.template.backstage.bean.po;


import javax.validation.constraints.NotBlank;

import com.silent.framework.base.utils.UserUtils;
import com.silent.template.backstage.constants.AuthConstants;

/**
 * 用户数据表映射类
 * @author TanJin
 * @date 2018年3月5日
 */
public class UserPo {

	private String id;				// 唯一主键
	@NotBlank
	private String username;		// 用户名
	@NotBlank
	private String password;		// 密码
	@NotBlank
	private String realname;		// 真实姓名
	@NotBlank
	private String company;			// 公司
	@NotBlank
	private String job;				// 工作职位
	
	private String salt;			// 密码加密盐
	private int status;				// 状态
	private long time;				// 更新时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * 校验密码
	 * @param pwd 用户输入密码
	 * @return
	 * @date 2017年8月17日
	 */
	public boolean checkPassword(String pwd) {
		String enPass = UserUtils.encodePassword(pwd, this.getSalt());
		return enPass.equals(this.password);
	}
	
	/**
	 * 重置密码
	 * @param inputPwd 新密码
	 * @return
	 */
	public void resetPassword(String inputPwd){
		this.password = UserUtils.encodePassword(inputPwd, this.getSalt());
	}
	
	/**
	 * 判断该用户当前是否可用
	 * @param 
	 * @return
	 * @date 2017年8月21日
	 */
	public boolean isEnabled() {
		return this.getStatus() == AuthConstants.STATUS_ENABLED;
	}
	
	/**
	 * 修改密码
	 * @param 
	 * @return
	 */
	public void changePassword(String password) {
		setSalt(UserUtils.createRandSalt());
		setPassword(UserUtils.encodePassword(password, this.getSalt()));
	}
	
	@Override
	public String toString() {
		return "UserPo [id=" + id + ", username=" + username + ", password=" + password + ", realname=" + realname
				+ ", company=" + company + ", job=" + job + ", salt=" + salt + ", status=" + status + ", time=" + time
				+ "]";
	}
	
}
