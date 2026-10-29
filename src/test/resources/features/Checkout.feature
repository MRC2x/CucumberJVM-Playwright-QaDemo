@Checkout
Feature: : Checkout

  @tmsLink=TST-1
  @TST-1
  Scenario: TST 1: 'Proceed to checkout' button click requires guest user to signed in
    Given a signed-out user on the "/" page
    When user clicks on "Browse Products" button on the Home page
    Then user is on the "/catalog" page
    When user selects any available item from catalog
    Then user is on the product page of the selected item
    And user clicks on "Add to Cart" button on the Product page
    And user clicks on "View Cart" button on the Product page
    When selected item is added to the cart
    Then user clicks on "Proceed to Checkout" button on the Cart page
    Then user is on the "/login" page


  @tmsLink=TST-2
  @TST-2
  Scenario: TST 2: Signed-in user can get to 'Checkout' page
    Given a signed-in user on the "/" page
    When user clicks on "Browse Products" button on the Home page
    Then user is on the "/catalog" page
    When user selects any available item from catalog
    Then user is on the product page of the selected item
    And user clicks on "Add to Cart" button on the Product page
    And user clicks on "View Cart" button on the Product page
    When selected item is added to the cart
    Then user clicks on "Proceed to Checkout" button on the Cart page
    Then user is on the "/checkout" page