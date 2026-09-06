package com.qvision.certificacion.userinterfaces;

import org.openqa.selenium.WebElement;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;

public class TarifaNetaPages extends PageObject{
	
	@FindBy(xpath="//div[@class='sum']/h4")
    WebElement tarifa;
	public String  tarifaNeta(){
		return tarifa.getText().replaceAll("\\$|\\.00", "");
	}
}
