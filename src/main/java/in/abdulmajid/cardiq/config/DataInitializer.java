package in.abdulmajid.cardiq.config;

import in.abdulmajid.cardiq.bank.entity.Bank;
import in.abdulmajid.cardiq.bank.repository.BankRepository;
import in.abdulmajid.cardiq.card.entity.Card;
import in.abdulmajid.cardiq.card.enums.*;
import in.abdulmajid.cardiq.card.repository.CardRepository;
import in.abdulmajid.cardiq.category.entity.Category;
import in.abdulmajid.cardiq.category.repository.CategoryRepository;
import in.abdulmajid.cardiq.merchant.entity.Merchant;
import in.abdulmajid.cardiq.merchant.repository.MerchantRepository;
import in.abdulmajid.cardiq.offer.entity.Offer;
import in.abdulmajid.cardiq.offer.enums.OfferType;
import in.abdulmajid.cardiq.offer.repository.OfferRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

//@Configuration
public class DataInitializer {

 //   @Bean
    CommandLineRunner initData(
            BankRepository bankRepository,
            CardRepository cardRepository,
            CategoryRepository categoryRepository,
            MerchantRepository merchantRepository,
            OfferRepository offerRepository
    ) {

        return args -> {

            if (bankRepository.count() > 0) {
                return;
            }

            Bank sbi = new Bank();
            sbi.setName("SBI");

            bankRepository.save(sbi);

            Bank axis = new Bank();
            axis.setName("Axis");

            bankRepository.save(axis);

            Category shopping = new Category();
            shopping.setName("Shopping");

            categoryRepository.save(shopping);

            Merchant amazon = new Merchant();
            amazon.setName("Amazon");

            merchantRepository.save(amazon);

            Card cashbackCard = new Card();

            cashbackCard.setName("SBI Cashback Card");
            cashbackCard.setJoiningFee(999.0);
            cashbackCard.setAnnualFee(999.0);

            cashbackCard.setCardType(CardType.CREDIT);
            cashbackCard.setNetwork(CardNetwork.VISA);
            cashbackCard.setRewardType(RewardType.CASHBACK);
            cashbackCard.setCardLevel(CardLevel.ENTRY);

            cashbackCard.setBank(sbi);

            cardRepository.save(cashbackCard);

            Card axisAce = new Card();

            axisAce.setName("Axis Ace");

            axisAce.setJoiningFee(499.0);
            axisAce.setAnnualFee(499.0);

            axisAce.setCardType(CardType.CREDIT);
            axisAce.setNetwork(CardNetwork.MASTERCARD);
            axisAce.setRewardType(RewardType.CASHBACK);
            axisAce.setCardLevel(CardLevel.ENTRY);

            axisAce.setBank(axis);

            cardRepository.save(axisAce);

            Offer axisOffer = new Offer();

            axisOffer.setTitle("2% Cashback on Amazon");

            axisOffer.setDescription("Get 2% cashback on Amazon purchases.");

            axisOffer.setOfferType(OfferType.CASHBACK);

            axisOffer.setValue(2.0);

            axisOffer.setMaxBenefit(1000.0);

            axisOffer.setMinimumSpend(500.0);

            axisOffer.setStartDate(LocalDate.now());

            axisOffer.setEndDate(LocalDate.now().plusMonths(3));

            axisOffer.setVerifiedAt(LocalDate.now());

            axisOffer.setCard(axisAce);

            axisOffer.setMerchant(amazon);

            axisOffer.setCategory(shopping);

            offerRepository.save(axisOffer);

            Offer amazonOffer = new Offer();

            amazonOffer.setTitle("5% Cashback on Amazon");
            amazonOffer.setDescription("Get 5% cashback on Amazon purchases.");

            amazonOffer.setOfferType(OfferType.CASHBACK);

            amazonOffer.setValue(5.0);
            amazonOffer.setMaxBenefit(5000.0);
            amazonOffer.setMinimumSpend(1000.0);

            amazonOffer.setStartDate(LocalDate.now());
            amazonOffer.setEndDate(LocalDate.now().plusMonths(6));

            amazonOffer.setVerifiedAt(LocalDate.now());

            amazonOffer.setCard(cashbackCard);
            amazonOffer.setMerchant(amazon);
            amazonOffer.setCategory(shopping);

            offerRepository.save(amazonOffer);

            System.out.println("Sample data initialized.");
        };
    }
}