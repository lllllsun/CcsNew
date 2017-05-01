package com.ccs.service;

/**
 * Created by DangH on 2017/5/1.
 */

public class NoticeDetailService {
    public static String getNewsDetailsHtml(String url) {
        String noticeDetailHtml = null;
        try {
            noticeDetailHtml = getOriginalHtmlPage(url);
            if (noticeDetailHtml == null)
                return null;

            String findStart = "<>"
        }
    }
}
