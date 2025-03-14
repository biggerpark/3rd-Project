package com.green.jobdone.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class PicUrlMaker {
    private final MyFileUtils myFileUtils;


    @Value("${file.directory}")
    public static String makeFolder(long businessId){
        return String.format("");
    }

    public static String makePicUrl(long businessId, String picName) {
        return String.format("/pic/business/%d/%s", businessId, picName);
    }
    public static String makePicUrlPortfolio(long businessId, long portfolioId, String picName) {
        return String.format("/pic/business/%d/portfolio/%d/%s", businessId,portfolioId, picName);
    }
    public static String makePicUrlPortfolioThumb(long businessId, long portfolioId, String picName) {
        return String.format("/pic/business/%d/portfolio/%d/thumbnail/%s", businessId,portfolioId, picName);
    }
    public static String makePicUrlBusiness(long businessId, String picName) {
        return String.format("/pic/business/%d/pics/%s", businessId, picName);
    }


    public static String makePicUserUuidUrl(String uuid, String picName) {
        return String.format("/pic/user/%s/%s", uuid,picName);
    }

    public static String makePicUserUrl(long userId, String picName) {
        return String.format("/pic/user/%d/%s", userId, picName);
    }

    public static String makePicUrlLogo(long businessId, String picName) {
        return String.format("/pic/business/%d/logo/%s", businessId, picName);
    } //D:\pjh\3rd-Project\jobdone\business\196\logo
    public static String makePicUrlPaper(long businessId, String picName) {
        return String.format("/pic/business/%d/paper/%s", businessId, picName);
    }

    public static String makePicUrlWithUrl(String url){
        return String.format("/pic/%s", url);
    }


    public static String makePicUrlChat(long roomId, long chatId, String picName ) {
        return String.format("/pic/room/%d/chat/%d/%s", roomId, chatId, picName);
    }

    public static String makePicUrlReview(long reviewId, String picName) {
        return String.format("/pic/review/%d/%s", reviewId, picName);
    }

    public static String makePicQa(long qaId, String picName) {
        return String.format("/pic/qa/%d/%s", qaId, picName);
    }

}
