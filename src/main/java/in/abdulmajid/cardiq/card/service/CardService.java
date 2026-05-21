package in.abdulmajid.cardiq.card.service;

import in.abdulmajid.cardiq.bank.entity.Bank;
import in.abdulmajid.cardiq.bank.repository.BankRepository;
import in.abdulmajid.cardiq.card.dto.CardResponse;
import in.abdulmajid.cardiq.card.dto.CreateCardRequest;
import in.abdulmajid.cardiq.card.entity.Card;
import in.abdulmajid.cardiq.card.repository.CardRepository;
import in.abdulmajid.cardiq.exception.DuplicateResourceException;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;
import in.abdulmajid.cardiq.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final BankRepository bankRepository;
    private final OfferRepository offerRepository;

    public CardResponse createCard(
            CreateCardRequest request
    ) {

        if (cardRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Card already exists");
        }

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bank not found")
                );

        Card card = new Card();

        mapRequestToEntity(card, request);

        card.setBank(bank);

        Card savedCard = cardRepository.save(card);

        return mapToResponse(savedCard);
    }

    public List<CardResponse> getAllCards() {

        return cardRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CardResponse getCardById(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found")
                );

        return mapToResponse(card);
    }

    public CardResponse updateCard(
            Long id,
            CreateCardRequest request
    ) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found")
                );

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bank not found")
                );

        mapRequestToEntity(card, request);

        card.setBank(bank);

        Card updatedCard = cardRepository.save(card);

        return mapToResponse(updatedCard);
    }

    public void deleteCard(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found")
                );

        if (offerRepository.existsByCard_Id(id)) {
            throw new DuplicateResourceException(
                    "Cannot delete card because offers are associated with it"
            );
        }

        cardRepository.delete(card);
    }

    private void mapRequestToEntity(
            Card card,
            CreateCardRequest request
    ) {

        card.setName(request.getName());

        card.setJoiningFee(request.getJoiningFee());

        card.setAnnualFee(request.getAnnualFee());

        card.setNetwork(request.getNetwork());

        card.setCardType(request.getCardType());

        card.setRewardType(request.getRewardType());

        card.setCardLevel(request.getCardLevel());

        card.setLtf(request.getLtf());

        card.setAirportLoungeAccess(
                request.getAirportLoungeAccess()
        );

        card.setRailwayLoungeAccess(
                request.getRailwayLoungeAccess()
        );

        card.setFuelSurchargeWaiver(
                request.getFuelSurchargeWaiver()
        );

        card.setCoBranded(
                request.getCoBranded()
        );

        card.setEmiAvailable(
                request.getEmiAvailable()
        );

        card.setRewardRate(
                request.getRewardRate()
        );

        card.setDescription(
                request.getDescription()
        );
        card.setActive(
                request.getActive()
        );

        card.setCoBrandPartner(
                request.getCoBrandPartner()
        );

        card.setImageUrl(
                request.getImageUrl()
        );
    }

    private CardResponse mapToResponse(
            Card card
    ) {

        return CardResponse.builder()
                .id(card.getId())
                .name(card.getName())
                .bankName(card.getBank().getName())
                .network(card.getNetwork())
                .cardType(card.getCardType())
                .rewardType(card.getRewardType())
                .cardLevel(card.getCardLevel())
                .joiningFee(card.getJoiningFee())
                .annualFee(card.getAnnualFee())
                .ltf(card.getLtf())
                .airportLoungeAccess(
                        card.getAirportLoungeAccess()
                )
                .railwayLoungeAccess(
                        card.getRailwayLoungeAccess()
                )
                .fuelSurchargeWaiver(
                        card.getFuelSurchargeWaiver()
                )
                .coBranded(card.getCoBranded())
                .emiAvailable(card.getEmiAvailable())
                .rewardRate(card.getRewardRate())
                .description(card.getDescription())
                .active(card.getActive())

                .coBrandPartner(
                        card.getCoBrandPartner()
                )

                .imageUrl(
                        card.getImageUrl()
                )

                .build();
    }
}