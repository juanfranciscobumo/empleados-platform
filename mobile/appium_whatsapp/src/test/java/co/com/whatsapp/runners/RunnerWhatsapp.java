package co.com.whatsapp.runners;

import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "src/test/resources/features/whatsapp.feature"
, glue = "co/com/whatsapp/stepdefinitions"
, snippets = CucumberOptions.SnippetType.CAMELCASE)
public class RunnerWhatsapp {
}
