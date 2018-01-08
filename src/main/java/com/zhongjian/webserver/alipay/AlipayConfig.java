package com.zhongjian.webserver.alipay;

public class AlipayConfig {
	// 1.商户appid
	public static String APPID = "2016082100305178";

	// 1.商家id
	public static String BusinessId = "2016082100305178";

	// 2.私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCVyt48ZvDf483Og9xhJGXK40q6p/dZ4TwMub9AEp8GN6mJBzXb0vxuMIGENTXNxFFvoQXPsyrgpprcVi3Zqvoy9QWdITRKD5iudv79Y9pqEtxHSBM8ArcWeNhUY9JN9NWnCeFuxdst7bXAqcZZoRyz+aa8sYMa7JYr8SkpYmPIeWpyRxoBm6dHrjCy7QnJ4wKBhG/hZeR33OPMvHRGD6EyP+es2fHVkcw5VBj62Njrd617h5/M9RbQZUfIdgds3t03BxOs0GDMPnS+G+U4H+G/Rhs5HkdO7AFyFLmEMF58611WWAhypCKOQNIlNk+hNWaU1cIwQcH4qO7yr9vC4DpRAgMBAAECggEBAI//Al68csg3/MuPFRbJX5WHpHoFKBJeJFTzZvfYLslSTwbsSIk2rPXcaVVHtZ5GICcYezasPQbTAkHYEqGR7I1539iqZ9LeZV8qA3xhNkEMOnfHwL3Kumi+6O9POoc4Wv8Fz5GmYEopnrREQOo+qBIXZ4Mzfh4u2zoIzhH7hILdRMUzqsXdtHcYziVFQwcWpTNyGHRGtEj94f0t4ZaEDXwDZKog95q2eByhsLDNEFYaLtl6Kivlhhl2JEPwKXmSJGQdhdPwo/BMDK8SNzEvKy9G2SfAFbw5njbM5Zkd2aM80R1rqh8ycb/jtYll7l9pGVtAXJFAQTzJKE8W91rHHnECgYEAxXNH+33h64Rd0PZNIDlpOkGz6a67/wiknHVmhrabrcj+PeQV7oKsB9ibG9eR6DOOWp6bzkwWc53r0eTPpIovGC5/YHCwxGEOR9UtWmRFit7iQjojYzeqyEmGNP+Wn9JiHbXlVs/Id8Gs7CJK0FMg9+6hMIktWVfBaenanTV3XI0CgYEAwjXQgfoaeWRIDyijFKOrKfQ6wkUlxYeXcsM/pi46kCMliI4zdB0Z/KbdxhKCIsE5Afg6MwJYCmAzAHS0VqGbuZTfgtNLE/cKCk0alKWw3dRLBsa2eRuPgeqdBcBMcnwSs19dcKFIgZ1sgYGTNk3bxMfBEA0b6nqwt0De33P1XdUCgYEAkeqP15s/yf0YF8TtWlMTIKpNevRWmjKY88cO7Z8ljVhsCvD0BD8tQt8gQYMdoKv5F57/JYW1Udvy2jpt+Prlsskp+OtOttHC6/LugU541hfAvZCZdkk9PKaZN7FqbtHbEHnL8ku7qkTGtfrhrvK/+nmvRV8a1Fu147+ef29ztkECgYAtZJKlrqV9dhj5og/sBQ0+sx0Ax1S6xKR7aHN6DhgWFIDzV/lizGKEkSQI5c6Q/QSsFy7TOVsGodGJ670y+ncBe3B6jZRgyD8KhVcPI2MhljsrDEseJ3j1brbg3z9/Csz8K/AVIT1nPjpcUsLr0GrrpZzUeZvqxF6Flp4BMINLOQKBgQC6tOh6MEyNja4sRl8AX65GE/W38acRDafR7BGKf3Bxa8CU8AMuhEfTtcYUcFV6NM41Wfy75UFmjWkvNxvJmaL1iMpDZV8KHmYSBnP16sBil3W44gbckFVSHil8QbYif/viXCAjoXTWzTx+TacQaTpijrt51ZZ49PeavCDQjriJ5w==";

	// 3.支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw8MsBC9NDUAObiJRrN2ltNi07stC7AKhxUm2lS5t6vpW1zUOR9MWadI4zJ5EK56ByvMpuq8QRv4OGrCuANHE2hqJPZlGfyhKki4dOLJ2VqaKD45za+dCvUECilpmxlZ857LEVVqfHCY1nt+k6hZW5rJTpbmcjGbufkCCGK5Fro21ZnFR9Q4RYR+nOzdIlLcMxZVZ3oVVx3XaDencbyL8a5kJeGBTXIHRp7w9X6kufGO32WzUp9DhV2zZJcYO2LAJ4idahSKn1gGxBIFfaouodW1ahNUCV+BuZD+CBc1WhpaT3ueakkNAevUXm3M6hoJNVruhIjZEuJS0+Ruvk9ra0QIDAQAB";

	// 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://e2fmrv.natappfree.cc/alipaytest/notify_url";

	// 5.页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 商户可以自定义同步跳转地址
	public static String return_url = "http://e2fmrv.natappfree.cc/alipaytest/return_url";

	// 6.请求网关地址
	public static String URL = "https://openapi.alipaydev.com/gateway.do";

	// 7.编码
	public static String CHARSET = "UTF-8";

	// 8.返回格式
	public static String FORMAT = "json";

	// 9.加密类型
	public static String SIGNTYPE = "RSA2";

}