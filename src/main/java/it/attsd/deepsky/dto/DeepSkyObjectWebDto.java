package it.attsd.deepsky.dto;

public class DeepSkyObjectWebDto {
    private Long id;
    private String name;
    private Long constellationId;

    public DeepSkyObjectWebDto() {

    }

    public DeepSkyObjectWebDto(String name, Long constellationId) {
        this.name = name;
        this.constellationId = constellationId;
    }

    public DeepSkyObjectWebDto(Long id, String name, Long constellationId) {
        this.id = id;
        this.name = name;
        this.constellationId = constellationId;
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

    public Long getConstellationId() {
        return constellationId;
    }

    public void setConstellationId(Long constellationId) {
        this.constellationId = constellationId;
    }
}
