package it.attsd.deepsky.dto;

public class DeepSkyObjectDto {
    private Long id;
    private String name;
    private ConstellationDto constellation;

    public DeepSkyObjectDto(String name, ConstellationDto constellation) {
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

    public ConstellationDto getConstellation() {
        return constellation;
    }

    public void setConstellation(ConstellationDto constellation) {
        this.constellation = constellation;
    }
}
