Feature: Reverse Words in a String
  In order to read backwards
  readers must have the words in their text reveresed

  Scenario: Empty String Reversal
    Given a String Reverser
    When I reverse the string ""
    Then the result is ""

  Scenario: Single Character Reversal
    Given a String Reverser
    When I reverse the string "A"
    Then the result is "A"

  Scenario: Multicharacter Word Reversal
    Given a String Reverser
    When I reverse the string "Bacon"
    Then the result is "Bacon"

  Scenario: Multiword String Reversal
    Given a String Reverser
    When I reverse the string "Bacon is the life blood of Agile Software Development"
    Then the result is "Development Software Agile of blood life the is Bacon"

  Scenario: Palindrome String Reversal
    Given a String Reverser
    When I reverse the string "Rats Live on no Evil Star"
    Then the result is "Star Evil no on Live Rats"

  Scenario: Consolidated Table Example
    Given a String Reverser
    When I reverse these strings:
      |                                                                |
      | A                                                              |
      | Bacon                                                          |
      | Bacon is the life blood of Agile Software Development          |
      | Rats Live on no Evil Star                                      |
    Then the results are:
      |                                                                |
      | A                                                              |
      | Bacon                                                          |
      | Development Software Agile of blood life the is Bacon          |
      | Star Evil no on Live Rats                                      |

  Scenario Outline: Outline Example
    Given a String Reverser
    When I reverse the string "<a string>"
    Then the result is "<reversed string>"

  Examples:
    | a string                                              | reversed string                                       |
    |                                                       |                                                       |
    | A                                                     | A                                                     |
    | bacon                                                 | bacon                                                 |
    | Bacon is the life blood of Agile Software Development | Development Software Agile of blood life the is Bacon |
    | Rats Live on no Evil Star                             | Star Evil no on Live Rats                             |