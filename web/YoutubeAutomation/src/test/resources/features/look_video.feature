#encoding: iso-8859-1
#Author: juanfranciscobumo@gmail.com
@Issue:SPRIN-1
Feature: Look a video in YouTube
  How an user of YouTube
  I want to look for a video
  To check that the search is successful

  Scenario Outline: Look a video
    Given that 'Juan' is on YouTube
    When Juan looks for the song '<Song>'
    And Juan plays video of '<autor>'
    Then Juan checks that the video has duration of '<time>'

    Examples: Data
      | Song                        | autor                                      | time |
      | need you now                | Lady Antebellum - Need You Now             | 4:31 |
    #  | Always Remember Us This Way | Lady Gaga - Always Remember Us This Way (f | 4:01 |