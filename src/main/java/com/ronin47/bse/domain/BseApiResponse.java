package com.ronin47.bse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BseApiResponse {
//    "NEWSID": "f670dd31-76fb-4d0b-81ce-c41e606a8d8d",
//            "SCRIP_CD": 532286,
//            "XML_NAME": "ANN_532286_{F670DD31-76FB-4D0B-81CE-C41E606A8D8D}",
//            "NEWSSUB": "JINDAL STEEL & POWER LTD. - 532286 - Shareholding for the Period Ended June 30, 2019",
//            "DT_TM": "2019-07-21T13:43:30",
//            "NEWS_DT": "2019-07-21T13:43:30",
//            "CRITICALNEWS": 0,
//            "ANNOUNCEMENT_TYPE": "A",
//            "QUARTER_ID": null,
//            "FILESTATUS": "N    ",
//            "ATTACHMENTNAME": "",
//            "MORE": "",
//            "HEADLINE": "Jindal Steel & Power Ltd has submitted to BSE the Shareholding Pattern for the Period Ended June 30, 2019. For more details, kindly <a href=\"http://www.bseindia.com/corporates/shpSecurities.aspx?scripcd=532286&qtrid=102.00\">Click here</a>",
//            "CATEGORYNAME": "Company Update",
//            "OLD": 1,
//            "RN": 1,
//            "PDFFLAG": 0,
//            "NSURL": "http://www.bseindia.com/stock-share-price/jindal-steel--power-ltd/jindalstel/532286/",
//            "SLONGNAME": "JINDAL STEEL & POWER LTD.",
//            "AGENDA_ID": 65,

    @JsonProperty("NEWSID")
    @SerializedName("NEWSID")
    private String newsId;

    @JsonProperty("SCRIP_CD")
    @SerializedName("SCRIP_CD")
    private Long scriptId;

    @JsonProperty("XML_NAME")
    @SerializedName("XML_NAME")
    private String xmlName;

    @JsonProperty("NEWSSUB")
    @SerializedName("NEWSSUB")
    private String newsSub;

    @JsonProperty("DT_TM")
    @SerializedName("DT_TM")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;

    @JsonProperty("NEWS_DT")
    @SerializedName("NEWS_DT")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime newsDateTime;

    @JsonProperty("CRITICALNEWS")
    @SerializedName("CRITICALNEWS")
    private Integer criticalNews;

    @JsonProperty("ANNOUNCEMENT_TYPE")
    @SerializedName("ANNOUNCEMENT_TYPE")
    private String announcementType;

    @JsonProperty("QUARTER_ID")
    @SerializedName("QUARTER_ID")
    private String quarterId;

    @JsonProperty("FILESTATUS")
    @SerializedName("FILESTATUS")
    private String fileStatus;

    @JsonProperty("ATTACHMENTNAME")
    @SerializedName("ATTACHMENTNAME")
    private String attachmentName;

    @JsonProperty("HEADLINE")
    @SerializedName("HEADLINE")
    private String headline;

    @JsonProperty("CATEGORYNAME")
    @SerializedName("CATEGORYNAME")
    private String categoryName;

    @JsonProperty("OLD")
    @SerializedName("OLD")
    private Integer old;

    @JsonProperty("RN")
    @SerializedName("RN")
    private Integer rn;

    @JsonProperty("PDFFLAG")
    @SerializedName("PDFFLAG")
    private Integer pdfFlag;

    @JsonProperty("NSURL")
    @SerializedName("NSURL")
    private String nsUrl;

    @JsonProperty("SLONGNAME")
    @SerializedName("SLONGNAME")
    private String slongName;

    @JsonProperty("AGENDA_ID")
    @SerializedName("AGENDA_ID")
    private Integer agendaId;
}
