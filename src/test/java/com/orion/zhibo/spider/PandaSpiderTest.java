/**
 * Copyright 2016 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import com.orion.core.utils.HttpUtils;

/**
 * description here
 *
 * @author yezi
 * @since 2016年4月4日
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
public class PandaSpiderTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    @Test
    public void testThumbnail() {
        Map<String, String> header = new HashMap<>();
        header.put(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.13 Safari/537.36");
        String url = "http://www.panda.tv/cate/dota2";
        Document document = Jsoup.parse(HttpUtils.get(url, header, "UTF-8"));
        Elements elements = document.select("#sortdetail-container li a");
        System.out.println(elements.size());
        for (Element element : elements) {
            String roomId = element.attr("data-id");
            Element thumbnail = element.select(".video-cover img").first();
            System.out.println(thumbnail);
            System.out.println(roomId + " = " + thumbnail.attr("data-original"));
        }
    }
}
