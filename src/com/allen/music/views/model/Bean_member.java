package com.allen.music.views.model;

import com.allen.music.views.base.BaseBean;

/**
 * 用户信息
 * 
 * @version 1.0
 * @created 2013-11-9
 */
public class Bean_member extends BaseBean {

	private static final long serialVersionUID = 1L;
	// --------------用户信息--------------
	public int uid;// 系统编号
	public String admin;// 账号
	public String is_v;// 1普通会员2琴行
	public String open;// 是否开放
	public String v_open;// 琴行审核 y已审核 n未审核
	public String upgrade;// 专家会员1审核2允许3拒绝
	public String identity;// "身份： 1乐童 2习乐人 3乐迷 4琴童家长 5专业人士 6名人名家"
	public String points;// 积分
	public String email;// 邮箱 不可重复
	public String password;// 密码
	public String salt;//
	public String username;// 昵称 不可重复
	public String dpmain;// 个性化名次 不可重复
	public String local;// 我所在
	public String blogtag;// 博客标签
	public String sign;// 个性签名
	public String num;// 发布数量
	public String flow;// 关注我的
	public String likenum;// 我喜欢的
	public String qq;// qq
	public String moboile;// 手机
	public String regtime;// 创建时间
	public String logtime;// 登陆时间
	public String regip;// 创建ip
	public String logip;// 登陆ip
	public String m_rep;// 回复通知
	public String m_flow;// 关注通知
	public String m_pm;// 私信通知
	public String contacter;// 琴行主要负责人
	public String sex;// 性别
	public String age;// 年龄
	public String telephone;// 电话
	public String profession;// 职业
	public String interest;// 兴趣爱好
	public String musicstyle;// 喜欢音乐风格
	public String detailedAddress;// 详细地址
	public String province;// 省代号
	public String city;// 市代号
	public String area;// 县代号
	public String ismail;// 是否接受优惠信息
	public String zip;// 邮编
	public String fax;// 公司传真
	public String classify;// 分类
	public String region;// 区域
	public String updataline;// 更新时间
	public String rolesid;// 角色id
	public String rolesTime;// 设置时间
	public String userId;// 新浪微博uid
	public String openid;// QQ

	public static Bean_member parse(String username, String pwd) {
		Bean_member user = new Bean_member();

		// 登录信息
		user.username = username;
		user.password = pwd;
		return user;
	}

}
