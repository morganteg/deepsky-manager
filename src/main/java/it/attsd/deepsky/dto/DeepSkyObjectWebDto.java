package it.attsd.deepsky.dto;

public class DeepSkyObjectWebDto {
    private Long id;
    private String name;
    private Long constellation;

    public DeepSkyObjectWebDto(Long id, String name, Long constellation) {
        this.id = id;
        this.name = name;
        this.constellation = constellation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getConstellation() {
        return constellation;
    }

}
