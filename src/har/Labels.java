package har;

public enum Labels {
    BOXING("boxing", "boxing/", 100), HANDCLAPPING("handclapping",
            "handclapping/", 99), HANDWAVING("handwaving", "handwaving/", 100), RUNNING(
            "running", "running/", 100), JOGGING("jogging", "jogging/", 100), WALKING(
            "walking", "walking/", 100);

    private String name;
    private String address;
    private int numberOfVideos;

    Labels(String name, String address, int numberOfVideos) {
        this.name = name;
        this.address = address;
        this.numberOfVideos = numberOfVideos;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public int getNumberOfVideos() {
        return this.numberOfVideos;
    }

    public static void main(String[] args) {
        for (Labels c : Labels.values()) {
            System.out.println(c);
            System.out.println(c.ordinal());
        }
    }
}
