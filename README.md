# Cardiq

Cardiq is a credit card recommendation and offer discovery platform built with Spring Boot.

The goal of Cardiq is to help users find the best credit card for a merchant, category, spending pattern, or benefit requirement by using structured card, offer, merchant, category, and benefit data.

---

# Features

## Card Management

- Create card
- Update card
- Delete card
- Get card by ID
- Get card by Slug
- Get all cards
- Filter cards

Supports:

- Joining Fee
- Annual Fee
- Lifetime Free (LTF)
- Forex Markup
- Reward Type
- Reward Conversion
- Reward Expiry
- Lounge Access
- Railway Lounge
- Fuel Waiver
- EMI Availability
- Co-Branded Cards
- Minimum Income Requirement
- Card Network
- Card Level

---

## Bank Management

Manage issuing banks.

Examples:

- HDFC Bank
- SBI Card
- ICICI Bank
- Axis Bank
- Kotak Mahindra Bank
- IndusInd Bank

Features:

- Create Bank
- Update Bank
- Delete Bank
- Get Bank
- List Banks

---

## Merchant Management

Manage merchant information.

Examples:

- Amazon
- Flipkart
- Swiggy
- Zomato
- Myntra
- Uber
- MakeMyTrip

Features:

- Create Merchant
- Update Merchant
- Delete Merchant
- Get Merchant
- List Merchants

Supports:

- Merchant Logo
- Website
- Description
- Merchant Type
- Slug

---

## Category Management

Manage spending categories.

Examples:

- Shopping
- Travel
- Food
- Fuel
- Entertainment
- Utility Bills

Features:

- Create Category
- Update Category
- Delete Category
- Get Category
- List Categories

Supports:

- Icon
- Slug
- Description

---

## Benefit Rule Management

Stores how rewards are credited.

Examples:

- Statement Credit
- Wallet Cashback
- Reward Points
- Airline Miles
- Hotel Points
- Voucher
- Edge Rewards
- CashPoints

Supports:

- Reward Conversion
- Redemption Fee
- Expiry Rules
- Minimum Redemption Points
- Notes

---

## Offer Management

Stores card offers.

Examples:

- 5% Cashback on Amazon
- 10% Instant Discount on Flipkart
- 5X Reward Points on Travel
- Airport Lounge Access
- Fuel Surcharge Waiver

Supports:

- Cashback
- Instant Discount
- Reward Points
- Miles
- EMI Benefits

Additional Rules:

- Minimum Spend
- Maximum Benefit
- Offer Priority
- Permanent Offer
- Limited Time Offer
- Benefit Period
- Platform Restriction
- Network Restriction

---

## Search Engine

Supports searching by:

- Merchant
- Category
- Card Name
- Bank Name
- Offer Name

Examples:

- Amazon
- Flipkart
- Travel
- Fuel
- Shopping

Returns:

- Best Matching Cards
- Estimated Savings
- Benefit Summary
- Match Percentage
- Matched Keywords
- Suggested Keywords

---

# Project Structure

```text
src/main/java/in/abdulmajid/cardiq

├── bank
├── benefit
├── card
├── category
├── merchant
├── offer
├── recommendation
├── search
├── common
├── config
└── exception
```

---

# Core Modules

## Bank

Stores issuing bank information.

## Card

Stores permanent card information.

Examples:

- Fees
- Reward System
- Lounge Benefits
- Eligibility
- Network

## Merchant

Stores merchant information.

Examples:

- Amazon
- Swiggy
- Flipkart

## Category

Stores spending categories.

Examples:

- Shopping
- Fuel
- Travel

## Benefit Rule

Stores reward crediting logic.

Examples:

- Statement Credit
- Reward Points
- Wallet Cashback

## Offer

Stores merchant/category specific offers.

Examples:

- Amazon Cashback
- Fuel Cashback
- Travel Rewards

## Search

Card recommendation engine.

---

# Database Relationships

```text
Bank
  |
  └── Card

Card
  |
  └── Offer

Merchant
  |
  └── Offer

Category
  |
  └── Offer

BenefitRule
  |
  └── Offer
```

---

# API Base URL

```http
/api/v1
```

---

# Available APIs

## Banks

```http
GET    /banks
GET    /banks/{id}
POST   /banks
PUT    /banks/{id}
DELETE /banks/{id}
```

---

## Cards

```http
GET    /cards
GET    /cards/{id}
GET    /cards/slug/{slug}
POST   /cards
PUT    /cards/{id}
DELETE /cards/{id}

POST   /cards/filter
```

---

## Merchants

```http
GET    /merchants
GET    /merchants/{id}
POST   /merchants
PUT    /merchants/{id}
DELETE /merchants/{id}
```

---

## Categories

```http
GET    /categories
GET    /categories/{id}
POST   /categories
PUT    /categories/{id}
DELETE /categories/{id}
```

---

## Benefit Rules

```http
GET    /benefits
GET    /benefits/{id}
POST   /benefits
PUT    /benefits/{id}
DELETE /benefits/{id}
```

---

## Offers

```http
GET    /offers
GET    /offers/{id}
POST   /offers
PUT    /offers/{id}
DELETE /offers/{id}
```

---

## Search

```http
GET /search?keyword=amazon

GET /search?keyword=travel

GET /search?keyword=swiggy&amount=5000
```

---

# Technology Stack

## Backend

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate

## Database

- MySQL

## Build Tool

- Maven

## Utility Libraries

- Lombok
- Jakarta Validation

---

# Current Status

### Version 1

Completed:

- Bank Module
- Card Module
- Merchant Module
- Category Module
- Benefit Rule Module
- Offer Module
- Search Module
- Dynamic Card Filtering
- Slug Based URLs
- Real Credit Card Data Support

---

# Planned Version 2

- User Module
- Saved Cards
- Owned Cards
- Personalized Recommendations
- Offer Tracking
- Offer Expiry Alerts
- Cashback Calculator
- Reward Point Calculator
- Admin Dashboard
- Analytics Dashboard
- Recommendation Engine Improvements

---

# Author

Abdul Majid

Cardiq
Credit Card Recommendation & Offer Discovery Platform