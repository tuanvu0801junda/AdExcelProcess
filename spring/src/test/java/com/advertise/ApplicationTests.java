package com.advertise;

import com.advertise.entity.Advertisement;
import com.advertise.entity.Campaign;
import com.advertise.entity.ErrorExcel;
import com.advertise.repository.AdvertisementRepo;
import com.advertise.service.AdExcelService;
import com.advertise.service.CampaignExcelService;
import com.advertise.service.ErrorExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@EnableJpaRepositories
class ApplicationTests {

	@Autowired
	private static AdvertisementRepo advertisementRepo;

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
		String filename = "Advertising_Template_1.xlsx";
		AdExcelService adHandler = new AdExcelService();
		List<Advertisement> ads = adHandler.readAdFromExcel(filename);
		List<ErrorExcel> errAd = adHandler.getErrList();

		CampaignExcelService campaignHandler = new CampaignExcelService();
		List<Campaign> campaigns = campaignHandler.readCampaignFromExcel(filename);
		ArrayList<ErrorExcel> errCampaign = campaignHandler.getErrList();

		System.out.println("campaigns.size = "+campaigns.size());
		System.out.println("errCampaign.size = "+errCampaign.size());
		System.out.println("ads.size = "+ads.size());
		System.out.println("errAd.size = "+errAd.size());

		System.out.println("\n");

		ArrayList<ErrorExcel> both = new ArrayList<>();
		if (errCampaign.size() == 0) printCampaign(campaigns);
		else {
			printErrList(errCampaign);
			both.addAll(errCampaign);
		}
		if (errAd.size() == 0) printAds(ads);
		else {
			printErrList(errAd);
			both.addAll(errAd);
		}
		ErrorExcelService err = new ErrorExcelService();
		err.writeErrorExcel(both);

		ConfigurableApplicationContext context = SpringApplication.run(ApplicationTests.class, args);
		AdvertisementRepo advertisementRepo = context.getBean(AdvertisementRepo.class);
		Advertisement newAd = advertisementRepo.save(ads.get(0));
		System.out.print(newAd.getAdID());
		System.out.print("-" + newAd.getAdName());
		System.out.print("-" + newAd.getAdStatus());
		System.out.print("-" + newAd.getAdType());
		System.out.println("-" + newAd.getBigModifier() + "\n");
	}
}
