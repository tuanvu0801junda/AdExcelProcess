package com.advertise;

import com.advertise.entity.Advertisement;
import com.advertise.entity.Campaign;
import com.advertise.entity.ErrorExcel;
import com.advertise.service.AdExcelHandler;
import com.advertise.service.CampaignExcelHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class ApplicationTests {

	public static void printAds(List<Advertisement> ads){
		for (Advertisement ad: ads){
			System.out.print(ad.getAdID());
			System.out.print("-"+ad.getAdName());
			System.out.print("-"+ad.getAdStatus());
			System.out.print("-"+ad.getAdType());
			System.out.println("-"+ad.getBigModifier()+"\n");
		}
	}

	public static void printCampaign(List<Campaign> campaigns){
		for (Campaign campaign: campaigns){
			System.out.print(campaign.getCampaignID());
			System.out.print("-"+campaign.getCampaignName());
			System.out.print("-"+campaign.getCampaignStatus());
			System.out.print("-"+campaign.getStartDate()+" - "+campaign.getEndDate());
			System.out.println("-"+campaign.getBudget()+"\n");
		}
	}

	public static void printErrList(List<ErrorExcel> errList){
		for (ErrorExcel err: errList){
			System.out.print(err.getSheetName());
			System.out.print("-"+err.getHeaderName());
			System.out.print("-"+err.getRowNum());
			System.out.print("-"+err.getErrMessage()+"\n");
		}
	}

	@Test
	public static void main(String[] args) throws IOException {
		String filename = "Ad_Template.xlsx";
		AdExcelHandler adHandler = new AdExcelHandler();
		List<Advertisement> ads = adHandler.readAdFromExcel(filename);
		List<ErrorExcel> errAd = adHandler.getErrList();

		CampaignExcelHandler campaignHandler = new CampaignExcelHandler();
		List<Campaign> campaigns = campaignHandler.readCampaignFromExcel(filename);
		List<ErrorExcel> errCampaign = campaignHandler.getErrList();

		System.out.println("campaigns.size = "+campaigns.size());
		System.out.println("errCampaign.size = "+errCampaign.size());
		System.out.println("ads.size = "+ads.size());
		System.out.println("errAd.size = "+errAd.size());

		System.out.println("\n");

		printCampaign(campaigns);
		printErrList(errCampaign);
		printAds(ads);
		printErrList(errAd);
	}
}
