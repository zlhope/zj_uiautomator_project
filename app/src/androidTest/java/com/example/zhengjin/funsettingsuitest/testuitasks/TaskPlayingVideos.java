package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhengjin.funsettingsuitest.utils.HttpUtils;
import com.squareup.okhttp.Request;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.zhengjin.funsettingsuitest.utils.ShellCmdUtils.TAG;

/**
 * Created by zhengjin on 2016/11/24.
 * <p>
 * Contains the UI selectors and tasks for playing film and TV.
 */

@SuppressWarnings("deprecation")
public final class TaskPlayingVideos {

    private static TaskPlayingVideos instance = null;
//    private UiDevice device;
//    private UiActionsManager action;

    private TaskPlayingVideos() {
//        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskPlayingVideos getInstance() {
        if (instance == null) {
            instance = new TaskPlayingVideos();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    @Nullable
    public TvInfo getTvInfoByName(String tvName) {
        final int limit = 20;
        return getTvInfoByName(tvName, limit);
    }

    @Nullable
    public TvInfo getTvInfoByName(String tvName, int limit) {
        JSONObject respObj = JSON.parseObject(
                this.doSendRequestAndRetResponse(this.buildTvSearchGetRequest(limit)));
        if (!this.isResponseOk(respObj)) {
            return null;
        }

        JSONArray dataTvs = respObj.getJSONArray("data");
        for (int idx = 0, size = dataTvs.size(); idx < size; idx++) {
            JSONObject tv = dataTvs.getJSONObject(idx);
            if (tvName.equals(tv.getString("name"))) {
                return new TvInfo(
                        tv.getIntValue("media_id"), tv.getString("name"),
                        tv.getIntValue("total_num"), tv.getBooleanValue("is_end"),
                        tv.getString("vip_type"));
            }
        }

        return null;
    }

    public int getLatestTvTotalNumByName(String tvName) {
        TvInfo tvInfo = this.getTvInfoByName(tvName);
        if (tvInfo == null) {
            return -1;
        }

        JSONObject respObj = JSON.parseObject(this.doSendRequestAndRetResponse(
                this.buildTvDetailsGetRequest(tvInfo.getMediaId())));
        if (!this.isResponseOk(respObj)) {
            return -1;
        }

        return respObj.getJSONObject("data").getJSONArray("episodes").size();
    }

    private boolean isResponseOk(JSONObject response) {
        String retCode = response.getString("retCode");
        String retMsg = response.getString("retMsg");
        if ("200".equals(retCode) && "ok".equals(retMsg)) {
            return true;
        } else {
            Log.e(TAG, String.format(
                    "Error, response ret code: %s, ret message: %s", retCode, retMsg));
            return false;
        }
    }

    private List<BasicNameValuePair> buildTvSearchGetRequestParams(int limit) {
        List<BasicNameValuePair> URL_PARAMS = new ArrayList<>(20);
        URL_PARAMS.add(new BasicNameValuePair("mtype", "2"));
        URL_PARAMS.add(new BasicNameValuePair("area", "0"));
        URL_PARAMS.add(new BasicNameValuePair("cate", "0"));
        URL_PARAMS.add(new BasicNameValuePair("year", "1900_2100"));
        URL_PARAMS.add(new BasicNameValuePair("order", "2"));
        URL_PARAMS.add(new BasicNameValuePair("pg", "1"));
        URL_PARAMS.add(new BasicNameValuePair("pz", String.valueOf(limit)));
        URL_PARAMS.add(new BasicNameValuePair("pv", "0"));
        URL_PARAMS.add(new BasicNameValuePair("version", "2.10.0.7_s"));
        URL_PARAMS.add(new BasicNameValuePair("sid", "FD5551A-SU"));
        URL_PARAMS.add(new BasicNameValuePair("mac", "28:76:CD:01:96:F6"));
        URL_PARAMS.add(new BasicNameValuePair("chiptype", "638"));

        return URL_PARAMS;
    }

    private List<BasicNameValuePair> buildTvDetailsGetRequestParams(int tvId) {
        List<BasicNameValuePair> URL_PARAMS = new ArrayList<>(20);
        URL_PARAMS.add(new BasicNameValuePair("id", String.valueOf(tvId)));
        URL_PARAMS.add(new BasicNameValuePair("account_id", "203186836"));
        URL_PARAMS.add(new BasicNameValuePair("token", "u9YuGT9-L5BCLqIPdaRQlV_Qop5FGdiS1ei" +
                "P7vnT0ijo43tKEEZ6CvI8SxiSTMnms4x45Wx4jhpYDJKBvgUGLAMrqPNHMwPfYEBgfj3kbiY"));
        URL_PARAMS.add(new BasicNameValuePair("version", "2.10.0.7_s"));
        URL_PARAMS.add(new BasicNameValuePair("sid", "FD5551A-SU"));
        URL_PARAMS.add(new BasicNameValuePair("mac", "28:76:CD:01:96:F6"));
        URL_PARAMS.add(new BasicNameValuePair("chiptype", "638"));

        return URL_PARAMS;
    }

    private Request buildTvSearchGetRequest(int limit) {
        final String tvAllUrl = "http://js.funtv.bestv.com.cn/search/mretrieve/v2";
        String url = HttpUtils.attachHttpGetParams(
                tvAllUrl, this.buildTvSearchGetRequestParams(limit));
        return HttpUtils.buildRequest(url);
    }

    private Request buildTvDetailsGetRequest(int tvId) {
        final String tvDetailsUrl = "http://jm.funtv.bestv.com.cn/media/episode/v2";
        String url = HttpUtils.attachHttpGetParams(
                tvDetailsUrl, this.buildTvDetailsGetRequestParams(tvId));
        return HttpUtils.buildRequest(url);
    }

    private String doSendRequestAndRetResponse(Request request) {
        String response;
        try {
            response = HttpUtils.getStringFromServer(request);
        } catch (IOException e) {
            response = String.format("{\"retCode\": \"-1\", \"retMsg\": \"%s\"}", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    public static class TvInfo {
        private int mediaId;
        private String tvName;
        private int totalNum;
        private boolean isEnd;
        private String isVip;

        public TvInfo(int mediaId, String tvName, int totalNum, boolean isEnd, String isVip) {
            this.mediaId = mediaId;
            this.tvName = tvName;
            this.totalNum = totalNum;
            this.isEnd = isEnd;
            this.isVip = isVip;
        }

        public int getMediaId() {
            return this.mediaId;
        }

        public String getTvName() {
            return this.tvName;
        }

        public int getTotalNum() {
            return this.totalNum;
        }

        public boolean getIsEnd() {
            return this.isEnd;
        }

        public String getIsVip() {
            return this.isVip;
        }
    }

}