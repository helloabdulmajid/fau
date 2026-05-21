package in.abdulmajid.cardiq.search.service;

import in.abdulmajid.cardiq.offer.repository.OfferRepository;
import in.abdulmajid.cardiq.search.dto.SearchCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final OfferRepository offerRepository;

    public List<SearchCardResponse> search(String keyword) {

        return offerRepository
                .findByMerchant_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(
                        keyword,
                        keyword
                )
                .stream()
                .map(offer -> SearchCardResponse.builder()
                        .cardName(offer.getCard().getName())
                        .bankName(offer.getCard().getBank().getName())
                        .offerTitle(offer.getTitle())
                        .value(offer.getValue())
                        .offerType(offer.getOfferType().name())
                        .merchantName(offer.getMerchant().getName())
                        .categoryName(offer.getCategory().getName())
                        .build())
                .toList();
    }
}