
Feature: Adding a product to the cart on Trendyol


  Scenario: User logs in and adds a product from the lowest-rated seller to the cart
    Given The user visits the Trendyol website
    And The user logs in with valid credentials
    When The user searches for "cep telefonu"
    And The user filters the price between 15000 and 20000 TL
    And The user selects a random product from the bottom of the list
    And The user adds the product from the lowest-rated seller to the cart
    Then The product should be visible in the cart
