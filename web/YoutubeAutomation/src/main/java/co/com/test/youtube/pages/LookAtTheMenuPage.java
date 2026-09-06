package co.com.test.youtube.pages;

import net.serenitybdd.screenplay.targets.Target;

public class LookAtTheMenuPage {

    public static final Target LIVE = Target.the("Button menu").locatedBy("//div[@id='guide-inner-content']/ytd-guide-renderer/div[1]/ytd-guide-section-renderer[3]/div/ytd-guide-entry-renderer//yt-formatted-string[.='Live' or .='En vivo']");
    public static final Target TEXT_LIVE = Target.the("Button menu").locatedBy("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-browse[2]/div[3]/ytd-tabbed-page-header/div/div/yt-page-header-renderer/yt-page-header-view-model/div/div[1]/div/yt-dynamic-text-view-model/h1/span");

}
