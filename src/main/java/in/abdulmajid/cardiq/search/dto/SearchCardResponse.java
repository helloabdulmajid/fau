package in.abdulmajid.cardiq.search.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchCardResponse {

    private String cardName;

    private String bankName;

    private String offerTitle;

    private Double value;

    private String offerType;

    private String merchantName;

    private String categoryName;
}