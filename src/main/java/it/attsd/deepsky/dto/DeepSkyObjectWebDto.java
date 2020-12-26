package it.attsd.deepsky.dto;

public class DeepSkyObjectWebDto {
    private Long id;
    private String name;
    private Long constellation;

    public DeepSkyObjectWebDto() {

    }

    public DeepSkyObjectWebDto(String name, Long constellation) {
        this.name = name;
        this.constellation = constellation;
    }

    public DeepSkyObjectWebDto(Long id, String name, Long constellation) {
        this.id = id;
        this.name = name;
        this.constellation = constellation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getConstellation() {
        return constellation;
    }

    public void setConstellation(Long constellation) {
        this.constellation = constellation;
    }
}
