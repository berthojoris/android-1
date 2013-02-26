package com.twofours.surespot.chat;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.twofours.surespot.common.SurespotLog;

/**
 * @author adam
 * 
 */
public class SurespotMessage implements Comparable<SurespotMessage> {
	private static final String TAG = "SurespotMessage";
	private String mType;
	private String mSubtype;
	private String mFrom;
	private String mTo;
	private String mIv;
	private String mCipherData;
	private String mPlainData;
	private Integer mId;
	private Integer mResendId;
	private String mMimeType;
	private int mHeight;
	private Date mDateTime;
	private String mToVersion;

	private String mFromVersion;

	private boolean mLoading;

	public String getSubType() {
		return mSubtype;
	}

	public void setSubType(String subtype) {
		mSubtype = subtype;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getFrom() {
		return mFrom;
	}

	public void setFrom(String from) {
		mFrom = from;
	}

	public String getTo() {
		return mTo;
	}

	public void setTo(String to) {
		mTo = to;
	}

	public String getCipherData() {
		return mCipherData;
	}

	public void setCipherData(String cipherText) {
		mCipherData = cipherText;
	}

	public String getPlainData() {
		return mPlainData;
	}

	public void setPlainData(String plainText) {
		mPlainData = plainText;
	}

	public Integer getId() {
		return mId;
	}

	public void setId(Integer id) {
		mId = id;
	}

	public Integer getResendId() {
		return mResendId;
	}

	public void setResendId(Integer resendId) {
		this.mResendId = resendId;
	}

	public String getOtherUser() {
		return ChatUtils.getOtherUser(this.mFrom, this.mTo);
	}

	public String getTheirVersion() {
		String otherUser = ChatUtils.getOtherUser(this.mFrom, this.mTo);
		if (mFrom.equals(otherUser)) {
			return getFromVersion();
		}
		else {
			return getToVersion();
		}
	}

	public String getOurVersion() {
		String otherUser = ChatUtils.getOtherUser(this.mFrom, this.mTo);
		if (mFrom.equals(otherUser)) {
			return getToVersion();
		}
		else {
			return getFromVersion();
		}
	}

	public static SurespotMessage toSurespotMessage(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			return toSurespotMessage(jsonObject);
		}
		catch (JSONException e) {
			SurespotLog.w(TAG, "toSurespotMessage", e);
		}

		return null;

	}

	/**
	 * @param jsonMessage
	 * @return SurespotMessage
	 * @throws JSONException
	 */
	public static SurespotMessage toSurespotMessage(JSONObject jsonMessage) throws JSONException {

		SurespotMessage chatMessage = new SurespotMessage();

		Integer id = jsonMessage.optInt("id");
		if (id != null) {
			chatMessage.setId(id);
		}
		chatMessage.setType(jsonMessage.getString("type"));
		chatMessage.setFrom(jsonMessage.getString("from"));
		chatMessage.setTo(jsonMessage.getString("to"));
		
		chatMessage.setCipherData(jsonMessage.optString("data", null));
		chatMessage.setSubType(jsonMessage.optString("subtype"));
		chatMessage.setMimeType(jsonMessage.optString("mimeType"));
		chatMessage.setIv(jsonMessage.optString("iv"));
		chatMessage.setHeight(jsonMessage.optInt("height"));
		chatMessage.setToVersion(jsonMessage.optString("toVersion"));
		chatMessage.setFromVersion(jsonMessage.optString("fromVersion"));
		Integer resendId = jsonMessage.optInt("resendId");
		if (resendId != null) {
			chatMessage.setResendId(resendId);
		}

		long datetime = jsonMessage.optLong("datetime");
		if (datetime > 0) {
			chatMessage.setDateTime(new Date(datetime));
		}

		return chatMessage;
	}

	public JSONObject toJSONObject() {
		JSONObject message = new JSONObject();

		try {
			message.put("type", this.getType());
			message.put("to", this.getTo());
			message.put("from", this.getFrom());

			if (this.getId() != null) {
				message.put("id", this.getId());
			}

			if (this.getSubType() != null) {
				message.put("subtype", this.getSubType());
			}
			if (this.getCipherData() != null) {
				message.put("data", this.getCipherData());
			}

			if (this.getResendId() != null) {
				message.put("resendId", this.getResendId());
			}
			if (this.getMimeType() != null) {
				message.put("mimeType", this.getMimeType());
			}
			if (this.getIv() != null) {
				message.put("iv", this.getIv());
			}

			if (this.getToVersion() != null) {
				message.put("toVersion", this.getToVersion());
			}
			if (this.getFromVersion() != null) {
				message.put("fromVersion", this.getFromVersion());
			}
			if (this.getDateTime() != null) {
				message.put("datetime", this.getDateTime().getTime());
			}
			if (this.getHeight() > 0) {

				message.put("height", this.getHeight());
			}

			return message;
		}
		catch (JSONException e) {
			SurespotLog.w(TAG, "toJSONObject", e);
		}
		return null;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		SurespotMessage rhs = (SurespotMessage) obj;

		if (this.getId() != null && rhs.getId() != null && this.getId().equals(rhs.getId())) {
			return true;
		}
		else {
			// iv should be unique across all messages
			return (this.getIv().equals(rhs.getIv()));
		}
	}

	public int hashCode() {
		return this.getCipherData().hashCode() + this.getFrom().hashCode() + this.getTo().hashCode();
	}

	public String getMimeType() {
		return mMimeType;
	}

	public void setMimeType(String mMimeType) {
		this.mMimeType = mMimeType;
	}

	public String getIv() {
		return mIv;
	}

	public void setIv(String mIv) {
		this.mIv = mIv;
	}

	public boolean isLoading() {
		return mLoading;
	}

	public void setLoading(boolean mLoading) {
		this.mLoading = mLoading;
	}

	public Integer getHeight() {
		return mHeight;
	}

	public void setHeight(Integer mHeight) {
		this.mHeight = mHeight;
	}

	public Date getDateTime() {
		return mDateTime;
	}

	public void setDateTime(Date mDateTime) {
		this.mDateTime = mDateTime;
	}

	public String getToVersion() {
		return mToVersion;
	}

	public void setToVersion(String toVersion) {
		mToVersion = toVersion;
	}

	public String getFromVersion() {
		return mFromVersion;
	}

	public void setFromVersion(String fromVersion) {
		mFromVersion = fromVersion;
	}

	@Override
	public int compareTo(SurespotMessage another) {

		Integer thisId = this.getId();
		Integer rhsId = another.getId();

		if (thisId == rhsId)
			return 0;

		// if we're null we want to be at the bottom of list
		if (thisId == null && rhsId != null) {
			return 1;
		}

		if (rhsId == null && thisId != null) {
			// should never be true
			return -1;
		}

		return thisId.compareTo(rhsId);
	}

}
