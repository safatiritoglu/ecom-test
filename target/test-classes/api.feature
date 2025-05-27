Feature: Invoice API Operations

  Scenario: Get token from login endpoint
    Given The client sends credentials to the /token endpoint
    Then A token should be returned and saved to token.txt

  Scenario: Retrieve invoice with barcode
    Given A valid barcode "123456789"
    When The client requests the invoice with this barcode
    Then The invoice data should be saved to viewInvoice.txt

  Scenario: Send invoice data
    Given A valid token and barcode "123456789"
    When The client sends the invoice to the API
    Then The response should be saved to sendInvoice.txt