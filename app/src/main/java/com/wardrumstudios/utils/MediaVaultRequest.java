package com.wardrumstudios.utils;

public class MediaVaultRequest {
    private long endTime = -1;
    private String ipAddress;
    private String mediaURL;
    private String pageURL;
    private String referrer;
    private long startTime = -1;

    public MediaVaultRequest(String mediaURL2) {
        this.mediaURL = mediaURL2;
    }

    public String getURLParamers() {
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("&p=100");
        if (this.referrer != null) {
            urlParams.append("&ru=1");
        }
        if (this.pageURL != null && this.pageURL.length() > 0) {
            urlParams.append("&pu=1");
        }
        if (this.ipAddress != null && this.ipAddress.length() > 0) {
            urlParams.append("&ip=" + this.ipAddress);
        }
        if (this.startTime != -1) {
            urlParams.append("&s=" + this.startTime);
        }
        if (this.endTime != -1) {
            urlParams.append("&e=" + this.endTime);
        }
        if (urlParams.length() > 0) {
            return urlParams.substring(1);
        }
        return urlParams.toString();
    }

    public String getHashParameters() {
        StringBuilder hash = new StringBuilder();
        if (this.referrer != null) {
            hash.append(this.referrer);
        }
        if (this.pageURL != null) {
            hash.append(this.pageURL);
        }
        return hash.toString();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime2) {
        this.startTime = startTime2;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime2) {
        this.endTime = endTime2;
    }

    public String getIPAddress() {
        return this.ipAddress;
    }

    public void setIPAddress(String ipAddress2) {
        this.ipAddress = ipAddress2;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public void setReferrer(String referrer2) {
        this.referrer = referrer2;
    }

    public String getPageURL() {
        return this.pageURL;
    }

    public void setPageURL(String pageURL2) {
        this.pageURL = pageURL2;
    }

    public String getMediaURL() {
        return this.mediaURL;
    }

    public void setMediaURL(String mediaURL2) {
        this.mediaURL = mediaURL2;
    }
}
