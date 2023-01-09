package ke.co.freddylyric.bloodysamaritan.models;

public class User {

    String name, bloodGroup, email, phoneNumber, id, search, type, profilepictureurl, idnumber, region;

    public User() {
    }

    public User(String name, String bloodGroup, String region, String email, String phoneNumber, String id, String search, String type, String profilepictureurl, String idnumber) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this. region = region;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.search = search;
        this.type = type;
        this.profilepictureurl = profilepictureurl;
        this.idnumber = idnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }
}
