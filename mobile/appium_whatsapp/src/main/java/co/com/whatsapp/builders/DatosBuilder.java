package co.com.whatsapp.builders;

import co.com.whatsapp.interfaces.DatosInterface;
import co.com.whatsapp.model.TestData;

public class DatosBuilder implements DatosInterface {
	private String contact;
	private String message;

	public DatosBuilder(String contact) {
		this.contact = contact;
	}

	public static DatosBuilder contact(String contact) {
		return new DatosBuilder(contact);
	}

	public TestData andSendMessage(String message) {
		this.message = message;
		return this.build();
	}

	@Override
	public TestData build() {
		TestData data = new TestData();
		data.setContact(this.contact);
		data.setMessage(this.message);
		return data;
	}

}
