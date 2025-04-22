package model;

/**
 * 地址类。
 */
public class Address {
    private String province;    // 省份
    private String city;        // 城市
    private String street;      // 街道
    private String houseNumber; // 门牌号

    /**
     * 构造函数。
     * @param province 省份
     * @param city 城市
     * @param street 街道
     * @param houseNumber 门牌号
     */
    public Address(String province, String city, String street, String houseNumber) {
        this.province = province;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    // Getters
    public String getProvince() { return province; }
    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }

    // Setters
    public void setProvince(String province) { this.province = province; }
    public void setCity(String city) { this.city = city; }
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    /**
     * 返回地址的字符串表示。
     * @return 格式化的地址字符串
     */
    @Override
    public String toString() {
        return String.format("%s%s%s%s",
                province != null ? province : "",
                city != null ? city : "",
                street != null ? street : "",
                houseNumber != null ? houseNumber : "");
    }
}
