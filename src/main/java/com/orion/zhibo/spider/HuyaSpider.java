/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.utils.Utils;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月6日
 */
@Component
public class HuyaSpider extends AbstractSpider {
    
    Pattern yyid = Pattern.compile("YYID = '(\\d+)';");
    Pattern uid = Pattern.compile("l_p = '(\\d+)';");
    Pattern chtopidP = Pattern.compile("CHTOPID = '(\\d+)';");
    Pattern subchidP = Pattern.compile("SUBCHID = '(\\d+)';");
    
    String liveInfoUrl = "http://phone.huya.com/api/liveinfo?sid=%s&from=android&client=3.2.3&version=1&subsid=%s";

    @Override
    protected String customPlatform() {
        return "huya";
    }

    @Override
    public void parse(Actor actor) {
        Document document = Jsoup.parse(HttpUtils.get(actor.getLiveUrl(), header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        
        String chtopid, subchid;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("CHTOPID")) {
                Matcher chtopidmatcher = chtopidP.matcher(script);
                Matcher subchidmatcher = subchidP.matcher(script);
                chtopid = chtopidmatcher.find() ? chtopidmatcher.group(1) : "";
                subchid = subchidmatcher.find() ? subchidmatcher.group(1) : "";
                if (StringUtils.isBlank(subchid) || chtopid.equals("0")) {
                    if (liveRoom != null) {
                        liveRoom.setStatus(LiveStatus.CLOSE);
                        liveRoom.setNumber(0);
                        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
                    }
                    break;
                }
                JSONObject liveInfo = JSON.parseObject(HttpUtils.get(String.format(liveInfoUrl, chtopid, subchid)));
                JSONObject info = liveInfo.getJSONObject("info");
                if (info == null || info.isEmpty()) {
                    break;
                }
                // 一般来说不变的信息
                if (liveRoom == null) {
                    liveRoom = new LiveRoom();
                    liveRoom.setActor(actor);
                }
                liveRoom.setUid(info.getString("yyid"));
                liveRoom.setRoomId(info.getString("liveUid"));
                liveRoom.setFlashUrl(document.select("#flash-link").attr("value")); 
                liveRoom.setName(info.getString("liveNick"));
                liveRoom.setTitle(info.getString("liveName"));
                liveRoom.setDescription("");
                liveRoom.setThumbnail(info.getString("snapshot"));
                liveRoom.setAvatar(info.getString("livePortait"));
                liveRoom.setNumber(info.getIntValue("users"));
                liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
                liveRoom.setStatus(LiveStatus.LIVING);
                break;
            }
        }
        if (liveRoom != null) {
            upsertLiveRoom(liveRoom);
        }
    }

}
