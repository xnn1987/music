/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 请按照下面检查:
	// 1、请确保自己的商户私钥和商户公钥正确；
	// 2、上传商户公钥，地址：b.alipay.com（我的商家服务—查询PID和KEY点击进去）；
	// 3、demo里面使用商户私钥和支付宝公钥，下载最新demo里面有支付宝公钥，统一使用那个；

	// 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String DEFAULT_PARTNER = "2088011751067022";
	// 商户收款的支付宝账号
	public static final String DEFAULT_SELLER = "ak885@ak885.com";
	// 商户（RSA）私钥
	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOGy7WvuI9qhb50Q5iOopwlXnhZwZ9fksCVqTMgQPGCquG5ExM918sKYJUE+hX+TOCSpGdbHa8vI5nYXUQf/BQfUvrFTOuf+P+86oB/l2s8ct1oxgyZYqmMY4pj7y4uy+5DbL6v3LtjJjZ1UYWgruJLNbA/fUqPrQKeTZ7UB5JZ3AgMBAAECgYADY4RdEn5HUhUy7oYGEwovAbnF7Hoiq7RZW3c7qcK+UNHkFfQ+iS7T7STiDeZDPBqjskZJC0V7EdEkBMWasSGKLVa25meoIPPMng7RLfa0eoN9LYYzjNbLw7JwZNMq07Dd/Fs4stqIXh2DaOaByJ5zgXcz9638Zz/58vwAqwH1gQJBAPhXvQSDpETDlGxdYQ/VYznJIWlUkAKEUR4xruMPVTcSwrdUMuL00aiqtcuInRBOjoEreqsGB7+Y1rS+hNQexLkCQQDoqHQEKh4aSPRWu6jHe2uRxK0SOsuoylYDe9FQAPyBMTbtbe4h9IHBsqNDqk502VV9f7CNSXdtHiNZ/ZJB5vyvAkB/E25OJApngWiudq3XC0rl48xr+HBkSU6005l46HyjXL80TipnxfyOgSPbTUGJeZkj8ZqobsKCQPQEssSHQmNJAkA0z2fmOhkzJjQwK9CX1zAHmnkeljH8UlFL4SMV4eNu/9jT9AwFsRZy/TWJ+nJsm5iIMB/HMlHldt3XY3N7eg8JAkEA8lS6sOwONi6JQlnTxHXT98/dEPlv5/tnr82dk3g9IOHclQE3KmRb9vCU3g8lqo5FCum4W1tvNtUBaRLEoJ4wXw==";
	// 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDhsu1r7iPaoW+dEOYjqKcJV54WcGfX5LAlakzIEDxgqrhuRMTPdfLCmCVBPoV/kzgkqRnWx2vLyOZ2F1EH/wUH1L6xUzrn/j/vOqAf5drPHLdaMYMmWKpjGOKY+8uLsvuQ2y+r9y7YyY2dVGFoK7iSzWwP31Kj60Cnk2e1AeSWdwIDAQAB";
}
