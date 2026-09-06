package co.com.test.youtube.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

@DefaultUrl("https://www.youtube.com/")
public class LookForAVideoPages extends PageObject {

    public static final String LBLSEARCH = "search";
    public static final String BTNSEARCH = "search-icon-legacy";
    public static final Target VIDEOSEARCHED = Target.the("Video searched").locatedBy(
            "//ytd-video-renderer/div/div/div/div/h3/a/yt-formatted-string[contains(text(),'{0}')]");

    public static final Target SKIPADDS = Target.the("Skip").locatedBy("//button/*[text()='Omitir' or text()='Skip']");

    public static final Target MUSICPLAYER = Target.the("Music player").locatedBy("//span[.='{0}']");

}
