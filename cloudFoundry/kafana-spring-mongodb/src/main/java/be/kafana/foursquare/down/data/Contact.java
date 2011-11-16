package be.kafana.foursquare.down.data;


public class Contact implements IFourSquareNode {

  private String phone;
  private String formattedPhone;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFormattedPhone() {
    return formattedPhone;
  }

  public void setFormattedPhone(String formattedPhone) {
    this.formattedPhone = formattedPhone;
  }

  @Override
  public String toString() {
    return String.format("Contact [phone=%s, formattedPhone=%s]", phone, formattedPhone);
  }


}
