package com.molloc.app.web.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.google.common.collect.Maps;
import com.molloc.app.web.utils.ConfigHelper;

/**
 * 极光推送工具类
 * 
 * @author admin
 *
 */
public class JpushUtils {

	private static final Logger LOG = LoggerFactory.getLogger(JpushUtils.class);
	private static final String APPKEY = ConfigHelper.getInstance().getConfig("jpush.appKey");
	private static final String MASTERSECRET = ConfigHelper.getInstance().getConfig("jpush.masterSecret");

	public static final String TITLE = ConfigHelper.getInstance().getConfig("jpush.default.title");
	public static final String TAG = ConfigHelper.getInstance().getConfig("jpush.default.tag");
	public static final String ANDROID_NEW_VERSION_TAG = ConfigHelper.getInstance().getConfig(
			"jpush.android.new.version.tag");
	public static final String IOS_NEW_VERSION_TAG = ConfigHelper.getInstance().getConfig("jpush.ios.new.version.tag");

	public static final String MSG_CONTENT = "爷爷在2014-07-04 18时餐中服用 3支康泰克,请按时服用.";
	public static final String REGISTRATION_ID = "0900e8d85ef";

	public static final String TAKE_MEDICINE_REMINDER_ALERT = "健康服药提醒";
	public static final String HEALTH_INFOMATION_ALERT = "健康咨询消息";
	public static final String SYS_NOTICE_NEW_VERSION_ALERT = "版本更新提醒";
	public static final String SYS_NOTICE_ALERT = "系统消息";

	public static final int PUSH_IOS = 1;
	public static final int PUSH_ANDROID = 0;
	public static final int PUSH_ANDROID_AND_IOS = 3;// 推送系统消息,基于标签推送

	// extras中常用的key
	public static final String EXTRA_KEY_NOTICE = "notice";
	public static final String EXTRA_KEY_TYPE = "type";
	public static final String EXTRA_KEY_MSGID = "msg_id";

	public static final int MAX_RETRY_TIMES = NumberUtils.toInt(ConfigHelper.getInstance().getConfig(
			"jpush.default.max.retry.time"));

	/**
	 * 按标签推送系统消息
	 * 
	 * @param alert 通知内容
	 * @param pushType 0-android, 1-ios
	 * @param tag 标签
	 * @param extras 向客户端发送额外的业务数据
	 */
	public static void sendTagPush(String alert, int pushType, String tag, Map<String, String> extras) {
		sendPush(alert, null, null, tag, pushType, extras, true);
	}

	/**
	 * @param alert 通知内容
	 * @param title 通知标题
	 * @param alias 别名
	 * @param pushType 0-android, 1-ios
	 * @param extras 额外的业务数据
	 */
	public static void sendAliasPush(String alert, String title, String alias, int pushType, Map<String, String> extras) {
		sendPush(alert, title, alias, null, pushType, extras, false);
	}

	/**
	 * 极光推送统一入口,按别名推送
	 * 
	 * @param alert 要推送的内容
	 * @param title 推送内容的标题(只适用于Android)
	 * @param alias 别名
	 * @param tag 标签
	 * @param pushType 0-android, 1-ios
	 * @param extras 额外的业务数据
	 * @param tagFlag true-按标签推送, false-按别名推送
	 */
	private static void sendPush(String alert, String title, String alias, String tag, int pushType,
			Map<String, String> extras, boolean tagFlag) {
		JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY, MAX_RETRY_TIMES);

		PushPayload payload = null;

		// For push, all you need do is to build PushPayload object.
		if (tagFlag) {
			payload = buildTagPushPayload(alert, tag, pushType, extras);
		} else {
			payload = buildAliasPushPayload(alert, title, alias, pushType, extras);
		}

		try {
			if (null == payload) {
				LOG.error("构建JpushPayload 失败, 构建参数为:[alert:{},title:{},alias:{},pushType:{}]", alert, title, alias,
						pushType);
				throw new IllegalArgumentException("pyload must be not null.");
			}
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - {}", result);

		} catch (APIConnectionException e) {
			LOG.error("tag=[{}], alias=[{}]", tag, alias);
			LOG.error("Connection error. Should retry later. ", e);

		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: {}", e.getStatus());
			LOG.info("Error Code: {}", e.getErrorCode());
			LOG.info("Error Message: {}", e.getErrorMessage());
			LOG.info("Msg ID: {}", e.getMsgId());
		}
	}

	/**
	 * 根据推送的设备类型构建基于别名PushPayload
	 * 
	 * @param alert 通知内容
	 * @param title 通知标题
	 * @param alias 别名
	 * @param pushType 0-android, 1-ios
	 * @return
	 */
	private static PushPayload buildAliasPushPayload(String alert, String title, String alias, int pushType,
			Map<String, String> extras) {
		PushPayload payload = null;

		switch (pushType) {
		case PUSH_ANDROID:
			if (StringUtils.isBlank(title)) {
				payload = buildPushObject_android_alias_alert(alert, alias, extras);
			} else {
				payload = buildPushObject_android_alias_alertWithTitle(alert, title, alias, extras);
			}
			break;
		case PUSH_IOS:
			payload = buildPushObject_ios_alias(alert, alias, extras);
			break;
		default:
			break;
		}
		return payload;
	}

	/**
	 * 根据推送的设备类型构建基于标签PushPayload
	 * 
	 * @param alert 通知内容
	 * @param title 通知标题
	 * @param extras 发给客户端额外的参数,用于业务处理
	 * @return
	 */
	private static PushPayload buildTagPushPayload(String alert, String tag, int pushType, Map<String, String> extras) {
		PushPayload payload = null;

		switch (pushType) {
		case PUSH_ANDROID:
			payload = buildPushObject_tag_android(alert, tag, extras);
			break;
		case PUSH_IOS:
			payload = buildPushObject_tag_ios(alert, tag, extras);
			break;
		default:
			break;
		}
		return payload;
	}

	/**
	 * just for test.
	 * 
	 * @return
	 */
	public static PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll("alert");
	}

	/**
	 * 
	 * 按别名推送(不带通知标题)
	 * 
	 * @param alert 要推送的内容
	 * @param alias 按别名推送
	 * @param extras 发给客户端额外的参数,用于业务处理
	 * @return
	 */
	public static PushPayload buildPushObject_android_alias_alert(String alert, String alias, Map<String, String> extras) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.alias(alias))
				.setNotification(
						Notification.newBuilder().setAlert(alert)
								.addPlatformNotification(AndroidNotification.newBuilder().addExtras(extras).build())
								.build()).build();
	}

	/**
	 * 
	 * 按别名推送(有通知标题)
	 * 
	 * @param alert 要推送的内容
	 * @param title 推送消息的标题
	 * @param alias 按别名推送
	 * @param extras 发给客户端额外的参数,用于业务处理
	 * @return
	 */
	public static PushPayload buildPushObject_android_alias_alertWithTitle(String alert, String title, String alias,
			Map<String, String> extras) {
		title = StringUtils.defaultString(title, TITLE);

		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.alias(alias))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(alert)
								.addPlatformNotification(
										AndroidNotification.newBuilder().setTitle(title).addExtras(extras).build())
								.build()).build();

	}

	/**
	 * 推送至IOS,按别名推送
	 * 
	 * @param alert 要推送的内容
	 * @param alias 按别名推送
	 * @param extras 发给客户端额外的参数,用于业务处理
	 * @return
	 */
	public static PushPayload buildPushObject_ios_alias(String alert, String alias, Map<String, String> extras) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.alias(alias))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(alert)
								.addPlatformNotification(
										IosNotification.newBuilder().incrBadge(1).addExtras(extras).build()).build())
				.build();
	}

	/**
	 * 构建基于Tag的android的pushPayload
	 * 
	 * @param alert 通知内容
	 * @param tag 标签
	 * @param extras 发给客户端额外的参数,用于业务处理
	 * @return
	 */
	public static PushPayload buildPushObject_tag_android(final String alert, String tag,
			final Map<String, String> extras) {
		tag = StringUtils.defaultString(tag, TAG);
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.tag(tag))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(alert)
								.addPlatformNotification(
										AndroidNotification.newBuilder().setTitle(TITLE).addExtras(extras).build())
								.build()).build();
	}

	/**
	 * 构建基于Tag的Ios和Android的pushPayload
	 * 
	 * @param alert 通知内容
	 * @param tag 标签
	 * @param extra 发给客户端额外的参数,用于业务处理
	 * @return
	 */
	public static PushPayload buildPushObject_tag_android_ios(final String alert, String tag,
			final Map<String, String> extras) {
		tag = StringUtils.defaultString(tag, TAG);
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag(tag))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(alert)
								.addPlatformNotification(
										AndroidNotification.newBuilder().setTitle(TITLE).addExtras(extras).build())
								.addPlatformNotification(
										IosNotification.newBuilder().incrBadge(1).addExtras(extras).build()).build())
				.build();
	}

	/**
	 * 根据tag推送消息
	 * 
	 * @param alert 通知内容
	 * @param tag 标签
	 * @param extra 额外的业务数据
	 * @return
	 */
	public static PushPayload buildPushObject_tag_ios(final String alert, String tag, final Map<String, String> extras) {
		tag = StringUtils.defaultString(tag, TAG);
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.tag(tag))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(alert)
								.addPlatformNotification(
										IosNotification.newBuilder().incrBadge(1).addExtras(extras).build()).build())
				.build();
	}

	public static void main(String[] args) {
		Map<String, String> extras = Maps.newHashMap();
		extras.put("notice", "版本升级啦.");
		extras.put("type", String.valueOf(1L));
		sendTagPush(JpushUtils.SYS_NOTICE_NEW_VERSION_ALERT, JpushUtils.PUSH_ANDROID, JpushUtils.TAG, extras);
	}

}
