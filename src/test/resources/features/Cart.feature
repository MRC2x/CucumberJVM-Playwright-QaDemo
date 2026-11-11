@Cart
Feature: Cart

  @tmsLink=CART-1
  @CART-1
  Scenario: CART-1: Signed-out user can add available items to the cart
    Given a signed-out user on the "/catalog" page
    When user randomly adds 3 available item from catalog
    Then cart icon shows 3 items in the cart
    When user clicks on the cart icon on the Catalog page
    Then user is on the "/cart" page
    And added items are present in the cart


  @tmsLink=CART-2
  @CART-2
  Scenario: CART-2: User can remove the item from the cart from the item card
    Given a signed-out user on the "/catalog" page
    When user randomly adds 1 available item from catalog
    Then cart icon shows 1 items in the cart
    And X button is present and active on the card of the added item
    When user clicks on the X button on the added item card
    Then cart icon shows 0 items in the cart
    When user clicks on the cart icon on the Catalog page
    Then user is on the "/cart" page
    And the cart is empty