Feature: Load My Data From a CSV File

  Scenario: Nightly Orders Load
  	Given a nightly orders load file
  	When the file arrives on the landing area
  	Then Nightly Orders are loaded into the ODS
  	
  Scenario: Empty Orders File
    Given an empty nightly orders load file
    When the file arrives on the landing area
    Then no changes occur in the ODS
    And an empty file notification is logged
    
  Scenario: Corrupt Orders File
    Given a corrupt nightly orders load file
    When the file arrives on the landing area
    Then no changes occur in the ODS
    And a corrupt file notification is logged
    
  Scenario: Partially Corrupt Orders File
    Given a partially corrupt nightly orders load file
    When the file arrives on the landing area
    Then no changes occur in the ODS
    And a corrupt file notification is logged